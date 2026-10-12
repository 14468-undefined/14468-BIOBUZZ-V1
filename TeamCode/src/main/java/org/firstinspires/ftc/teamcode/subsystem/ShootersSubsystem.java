package org.firstinspires.ftc.teamcode.subsystem;

import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static dev.nextftc.units.Units.RotationsPerMinute;
import static org.firstinspires.ftc.teamcode.util.Constants.ShooterConstants.*;

import com.pedropathing.ivy.Command;
import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import org.firstinspires.ftc.teamcode.util.Config;
import org.firstinspires.ftc.teamcode.util.Constants;
import org.firstinspires.ftc.teamcode.util.FieldState;



public class ShootersSubsystem implements Mechanism {

    private ShooterState shooterState = ShooterState.OFF;
    public enum ShooterState {
        AUTO,
        MANUAL,
        OFF,
        TUNING
    }

    private void stateSetter(ShooterState shooterState) {
        this.shooterState = shooterState;
    }
    public Command setState(ShooterState s) {
        return instant(() -> this.stateSetter(s));
    }

    private static final double RPM_DEADBAND = 25; // ignore setpoint changes smaller than this in auto mode

    private final NextMotor nectarShooter = new NextMotor(RobotController.expansionHub(), Config.nectarShooter);
    private final NextMotor pollenShooter = new NextMotor(RobotController.expansionHub(), Config.pollenShooter);

    private double nectarTargetRpm = 0;
    private double pollenTargetRpm = 0;

    private double manualNectarRpm = 0;
    private double manualPollenRpm = 0;


    private final FieldState fieldState;

    // Both shooters fire at the same goal
    private FieldState.GoalSide goalSide = FieldState.GoalSide.GARDEN;



    public ShootersSubsystem(FieldState fieldState) {
        this.fieldState = fieldState;

        pollenShooter.setZeroPowerBehavior(NextMotor.ZeroPowerBehavior.FLOAT);
        nectarShooter.setZeroPowerBehavior(NextMotor.ZeroPowerBehavior.FLOAT);

        // Separate gains per shooter. F = kV
        setNectarPIDF(S_NECTAR_KP, S_POLLEN_KI, S_POLLEN_KD, S_NECTAR_KF);
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
        nectarTargetRpm = 0;
        pollenTargetRpm = 0;
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

    // TODO: LUT (distance -> RPM) in its own class, ideally one table per goalSide
    private double rpmForDistance(double distance) {
        return 0;
    }

    // ---- Commands (mode switches / one-shots) ----
    public Command enableAutoRpm()  { return instant(() -> shooterState = ShooterState.AUTO); }

    // Keeps the shooters at whatever auto last set
    public Command disableAutoRpm() {
        return instant(() -> setManualRpm(nectarTargetRpm, pollenTargetRpm));
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
    public double getNectarTargetRpm() { return nectarTargetRpm; }
    public double getPollenTargetRpm() { return pollenTargetRpm; }
    public double getManualNectarRpm() { return manualNectarRpm; }
    public double getManualPollenRpm() { return manualPollenRpm; }

    public double getNectarCurrentRPM() {
        return nectarShooter.getEncoderVelocity().into(RotationsPerMinute);
    }
    public double getPollenCurrentRPM() {
        return pollenShooter.getEncoderVelocity().into(RotationsPerMinute);
    }
    public boolean isAutoRpm() { return shooterState == ShooterState.AUTO; }

    public boolean isSpinning() { return nectarTargetRpm != 0 || pollenTargetRpm != 0; }

    public Command waitTilAtSpeed() {
        return waitUntil(this::isAtTargetRPM);
    }
    public boolean isAtTargetRPM(){
        return Math.abs(pollenTargetRpm - getPollenCurrentRPM()) < 40 && Math.abs(nectarTargetRpm - getNectarCurrentRPM()) < 40;
    }

    @Override
    public void periodic() {
        switch (shooterState) {
            case AUTO:
                double nectarAuto = rpmForDistance(getNectarShooterDistanceToGoal());
                double pollenAuto = rpmForDistance(getPollenShooterDistanceToGoal());

                // Deadband so pose jitter doesn't constantly rewrite the setpoint
                if (Math.abs(nectarAuto - nectarTargetRpm) > RPM_DEADBAND) nectarTargetRpm = nectarAuto;
                if (Math.abs(pollenAuto - pollenTargetRpm) > RPM_DEADBAND) pollenTargetRpm = pollenAuto;
                break;
            case MANUAL:
                nectarTargetRpm = manualNectarRpm;
                pollenTargetRpm = manualPollenRpm;
                break;
            case TUNING:
                nectarTargetRpm = NECTAR_TUNING_RPM;
                pollenTargetRpm = POLLEN_TUNING_RPM;
                break;
            case OFF:
                nectarTargetRpm = 0;
                pollenTargetRpm = 0;
                break;

        }

        applyRpm(nectarShooter, nectarTargetRpm);
        applyRpm(pollenShooter, pollenTargetRpm);
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