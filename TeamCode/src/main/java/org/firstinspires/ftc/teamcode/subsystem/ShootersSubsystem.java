package org.firstinspires.ftc.teamcode.subsystem;

import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static dev.nextftc.units.Units.Degrees;
import static dev.nextftc.units.Units.RotationsPerMinute;
import static org.firstinspires.ftc.teamcode.util.Constants.ShooterConstants.*;

import com.pedropathing.ivy.Command;
import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import org.firstinspires.ftc.teamcode.util.*;


public class ShootersSubsystem implements Mechanism {

    private ShooterState shooterState = ShooterState.OFF;
    public enum ShooterState {
        AUTO,
        MANUAL,
        OFF,
        TUNING
    }

    public void stateSetter(ShooterState shooterState) {
        this.shooterState = shooterState;
    }
    public Command setState(ShooterState s) {
        return instant(() -> this.stateSetter(s));
    }

    private static final double RPM_DEADBAND = 25; // ignore setpoint changes smaller than this in auto mode

    private final NextMotor nectarShooter = new NextMotor(RobotController.expansionHub(), Config.nectarShooter);
    private final NextMotor pollenShooter = new NextMotor(RobotController.expansionHub(), Config.pollenShooter);

    private double nectarTar = 0;
    private double pollenTar = 0;

    private double manualNectarRpm = 0;
    private double manualPollenRpm = 0;

    private double lastNectarKP = S_NECTAR_KP;
    private double lastNectarKI = S_NECTAR_KI;
    private double lastNectarKD = S_NECTAR_KD;
    private double lastNectarKF = S_NECTAR_KF;
    private double lastPollenKP = S_POLLEN_KP;
    private double lastPollenKI = S_POLLEN_KI;
    private double lastPollenKD = S_POLLEN_KD;
    private double lastPollenKF = S_POLLEN_KF;


    private final FieldState fieldState;

    // Both shooters fire at the same goal
    private FieldState.GoalSide goalSide = FieldState.GoalSide.GARDEN;



    public ShootersSubsystem(FieldState fieldState) {
        this.fieldState = fieldState;

        nectarShooter.setDirection(NextMotor.Direction.REVERSE);
        pollenShooter.setAnglePerCount(Degrees.of(360.0 / 28.0));

        pollenShooter.setZeroPowerBehavior(NextMotor.ZeroPowerBehavior.FLOAT);
        nectarShooter.setZeroPowerBehavior(NextMotor.ZeroPowerBehavior.FLOAT);

        // Separate gains per shooter. F = kV
        setNectarPIDF(S_NECTAR_KP, S_NECTAR_KI, S_NECTAR_KD, S_NECTAR_KF);
        setPollenPIDF(S_POLLEN_KP, S_POLLEN_KI, S_POLLEN_KD, S_POLLEN_KF);
    }

    // ---- PIDF tuning ----
    public void setNectarPIDF(double p, double i, double d, double f) {
        nectarShooter.getVelocityConstants().withP(p).withI(i).withD(d).withV(f);
    }

    public void setPollenPIDF(double p, double i, double d, double f) {
        pollenShooter.getVelocityConstants().withP(p).withI(i).withD(d).withV(f);
    }

    // ---- Goal side (shared) ----
    public void setGoalSide(FieldState.GoalSide side) { this.goalSide = side; }
    public FieldState.GoalSide getGoalSide() { return goalSide; }

    public Command toggleGoalSide() {
        return instant(() -> goalSide = (goalSide == FieldState.GoalSide.GARDEN)
                ? FieldState.GoalSide.LOADING_ZONE
                : FieldState.GoalSide.GARDEN);
    }


    // ---- Distance (separate: turrets sit at different offsets) ----
    public double getPollenShooterDistanceToGoal() {
        return fieldState.distanceToGoal(Constants.TurretConstants.POLLEN_TURRET_FORWARD_OFFSET, Constants.TurretConstants.POLLEN_TURRET_LATERAL_OFFSET);
    }

    public double getNectarShooterDistanceToGoal() {
        return fieldState.distanceToGoal(Constants.TurretConstants.NECTAR_TURRET_FORWARD_OFFSET, Constants.TurretConstants.NECTAR_TURRET_LATERAL_OFFSET);
    }

    private void setOff() {
        shooterState = ShooterState.OFF;
        nectarTar = 0;
        pollenTar = 0;
    }

    // Direct (non-command) setters so an opmode can adjust RPM live.
    // These store the manual RPMs and switch to MANUAL, periodic() applies them.
    public void setManualRpm(double nectarRpm, double pollenRpm) {
        manualNectarRpm = nectarRpm;
        manualPollenRpm = pollenRpm;
        shooterState = ShooterState.MANUAL;
    }

    public void setBothRpm(double rpm) {
        setManualRpm(rpm, rpm);
    }
    public Command setRpm(double r) {
        return instant(() -> this.setBothRpm(r));
    }


    // TODO: LUT (distance -> RPM) in its own class, ideally one table per goalSide
    private double nectarRpmForDistance(double distance) {
        return NectarShooterLUT.get(distance);
    }
    private double pollenRpmForDistance(double distance) {
        return PollenShooterLUT.get(distance);
    }


    // ---- Commands (mode switches / one-shots) ----
    public Command enableAutoRpm()  { return instant(() -> shooterState = ShooterState.AUTO); }

    // Keeps the shooters at whatever auto last set
    public Command disableAutoRpm() {
        return instant(() -> setManualRpm(nectarTar, pollenTar));
    }

    // Teleop: press to spin both at a fixed RPM, press again to stop
    public Command spinAuto(double rpm) {
        return instant(() -> {
            if (isSpinning()) setOff();
            else setBothRpm(rpm);
        });
    }

    // Teleop: press to track distance -> RPM, press again to stop
    public Command toggleAutoSpin() {
        return instant(() -> {
            if (shooterState == ShooterState.AUTO) setOff();
            else shooterState = ShooterState.AUTO;
        });
    }

    public Command spinNectar(double rpm) {
        return instant(() -> setManualRpm(rpm, manualPollenRpm));
    }

    public Command spinPollen(double rpm) {
        return instant(() -> setManualRpm(manualNectarRpm, rpm));
    }

    public Command spinBoth(double rpm) {
        return instant(() -> setBothRpm(rpm));
    }

    public Command stopNectar() {
        return instant(() -> setManualRpm(0, manualPollenRpm));
    }

    public Command stopPollen() {
        return instant(() -> setManualRpm(manualNectarRpm, 0));
    }

    public Command stopBoth() {
        return instant(this::setOff);
    }

    public ShooterState getState() { return shooterState; }
    public double getNectarTar() { return nectarTar; }
    public double getPollenTar() { return pollenTar; }
    public double getManualNectarRpm() { return manualNectarRpm; }
    public double getManualPollenRpm() { return manualPollenRpm; }

    public double getNectarCurrentRPM() {
        return nectarShooter.getEncoderVelocity().into(RotationsPerMinute);
    }
    public double getPollenCurrentRPM() {
        return pollenShooter.getEncoderVelocity().into(RotationsPerMinute);
    }
    public boolean isAutoRpm() { return shooterState == ShooterState.AUTO; }

    public boolean isSpinning() { return nectarTar != 0 || pollenTar != 0; }

    public Command waitTilAtSpeed() {
        return waitUntil(this::isAtTar);
    }
    public boolean isAtTar(){
        return Math.abs(pollenTar - getPollenCurrentRPM()) < 40 && Math.abs(nectarTar - getNectarCurrentRPM()) < 40;
    }

    private void updateGains() {
        if (S_NECTAR_KP != lastNectarKP || S_NECTAR_KI != lastNectarKI || S_NECTAR_KD != lastNectarKD || S_NECTAR_KF != lastNectarKF) {
            lastNectarKP = S_NECTAR_KP;
            lastNectarKI = S_NECTAR_KI;
            lastNectarKD = S_NECTAR_KD;
            lastNectarKF = S_NECTAR_KF;
            setNectarPIDF(lastNectarKP, lastNectarKI, lastNectarKD, lastNectarKF);
        }
        if (S_POLLEN_KP != lastPollenKP || S_POLLEN_KI != lastPollenKI || S_POLLEN_KD != lastPollenKD || S_POLLEN_KF != lastPollenKF) {
            lastPollenKP = S_POLLEN_KP;
            lastPollenKI = S_POLLEN_KI;
            lastPollenKD = S_POLLEN_KD;
            lastPollenKF = S_POLLEN_KF;
            setPollenPIDF(lastPollenKP, lastPollenKI, lastPollenKD, lastPollenKF);
        }
    }

    @Override
    public void periodic() {
        updateGains();

        switch (shooterState) {
            case AUTO:
                double nectarAuto = nectarRpmForDistance(getNectarShooterDistanceToGoal());
                double pollenAuto = pollenRpmForDistance(getPollenShooterDistanceToGoal());

                // Deadband so pose jitter doesn't constantly rewrite the setpoint
                if (Math.abs(nectarAuto - nectarTar) > RPM_DEADBAND) nectarTar = nectarAuto;
                if (Math.abs(pollenAuto - pollenTar) > RPM_DEADBAND) pollenTar = pollenAuto;
                break;
            case MANUAL:
                nectarTar = manualNectarRpm;
                pollenTar = manualPollenRpm;
                break;
            case TUNING:
                nectarTar = NECTAR_TUNING_RPM;
                pollenTar = POLLEN_TUNING_RPM;

                //new
                break;
            case OFF:
                nectarTar = 0;
                pollenTar = 0;
                break;

        }
        //nectarShooter.setThrottle(1);

        applyRpm(nectarShooter, nectarTar);
        applyRpm(pollenShooter, pollenTar);
    }

    private void applyRpm(NextMotor motor, double rpm) {
        if (rpm == 0) motor.setThrottle(0);
        else motor.setVelocitySetpoint(RotationsPerMinute.of(rpm));
    }



    public NextMotor getNectarShooter(){
        return nectarShooter;
    }
    public NextMotor getPollenShooter(){
        return pollenShooter;
    }
}