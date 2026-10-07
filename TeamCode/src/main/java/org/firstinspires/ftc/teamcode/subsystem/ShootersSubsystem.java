package org.firstinspires.ftc.teamcode.subsystem;

import static dev.nextftc.units.Units.RotationsPerMinute;

import com.pedropathing.ivy.Command;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import org.firstinspires.ftc.teamcode.util.Constants;
import org.firstinspires.ftc.teamcode.util.FieldState;

public class ShootersSubsystem implements Mechanism {

    private static final double RPM_DEADBAND = 25; // ignore setpoint changes smaller than this in auto mode

    private final NextMotor nectarShooter = new NextMotor("nectar_shooter");//TODO: float mode
    private final NextMotor pollenShooter = new NextMotor("pollen_shooter");//TODO: float mode

    private double nectarTargetRpm = 0;
    private double pollenTargetRpm = 0;

    private boolean autoRpm = false; // when true, periodic() tracks distance -> RPM

    private final FieldState fieldState;

    // Both shooters fire at the same goal
    private FieldState.GoalSide goalSide = FieldState.GoalSide.GARDEN;



    public ShootersSubsystem(FieldState fieldState) {
        this.fieldState = fieldState;


        // Separate gains per shooter. F = kV
        setNectarPIDF(0.0, 0.0, 0.0, 0.0);
        setPollenPIDF(0.0, 0.0, 0.0, 0.0);
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

    // ---- Low-level setters (only place that talks to the motors) ----
    private void setNectarRpm(double rpm) {
        nectarTargetRpm = rpm;
        if (rpm == 0) nectarShooter.setThrottle(0);
        else nectarShooter.setVelocitySetpoint(RotationsPerMinute.of(rpm));
    }

    private void setPollenRpm(double rpm) {
        pollenTargetRpm = rpm;
        if (rpm == 0) pollenShooter.setThrottle(0);
        else pollenShooter.setVelocitySetpoint(RotationsPerMinute.of(rpm));
    }

    // Direct (non-command) setter so an opmode can adjust RPM live
    public void setBothRpm(double rpm) {
        autoRpm = false;
        setNectarRpm(rpm);
        setPollenRpm(rpm);
    }

    // TODO: LUT (distance -> RPM) in its own class, ideally one table per goalSide
    private double rpmForDistance(double distance) {
        return 0;
    }

    // ---- Commands (mode switches / one-shots) ----
    public Command enableAutoRpm()  { return instant(() -> autoRpm = true); }
    public Command disableAutoRpm() { return instant(() -> autoRpm = false); }

    // Teleop: press to spin both at a fixed RPM, press again to stop
    public Command toggleSpin(double rpm) {
        return instant(() -> {
            boolean spinning = isSpinning();
            autoRpm = false;
            setNectarRpm(spinning ? 0 : rpm);
            setPollenRpm(spinning ? 0 : rpm);
        });
    }

    // Teleop: press to track distance -> RPM, press again to stop
    public Command toggleAutoSpin() {
        return instant(() -> {
            if (autoRpm) {
                autoRpm = false;
                setNectarRpm(0);
                setPollenRpm(0);
            } else {
                autoRpm = true;
            }
        });
    }

    // Manual commands turn auto off so periodic() doesn't overwrite them
    public Command spinNectar(double rpm) {
        return instant(() -> {
            autoRpm = false;
            setNectarRpm(rpm);
        });
    }

    public Command spinPollen(double rpm) {
        return instant(() -> {
            autoRpm = false;
            setPollenRpm(rpm);
        });
    }

    public Command spinBoth(double rpm) {
        return instant(() -> {
            autoRpm = false;
            setNectarRpm(rpm);
            setPollenRpm(rpm);
        });
    }

    public Command stopNectar() {
        return instant(() -> {
            autoRpm = false;
            setNectarRpm(0);
        });
    }

    public Command stopPollen() {
        return instant(() -> {
            autoRpm = false;
            setPollenRpm(0);
        });
    }

    public Command stopBoth() {
        return instant(() -> {
            autoRpm = false;
            setNectarRpm(0);
            setPollenRpm(0);
        });
    }

    // ---- State ----
    public double getNectarTargetRpm() { return nectarTargetRpm; }
    public double getPollenTargetRpm() { return pollenTargetRpm; }

    /** True when distance-based auto RPM mode is on. */
    public boolean isAutoRpm() { return autoRpm; }

    /** True when either shooter has a nonzero target (manual or auto). */
    public boolean isSpinning() { return nectarTargetRpm != 0 || pollenTargetRpm != 0; }

    @Override
    public void periodic() {
        if (!autoRpm) return;

        double nectarTargetRPM = rpmForDistance(getNectarShooterDistanceToGoal());
        double pollenTargetRPM = rpmForDistance(getPollenShooterDistanceToGoal());

        // Deadband so pose jitter doesn't constantly rewrite the setpoint
        if (Math.abs(nectarTargetRPM - nectarTargetRpm) > RPM_DEADBAND) setNectarRpm(nectarTargetRPM);
        if (Math.abs(pollenTargetRPM - pollenTargetRpm) > RPM_DEADBAND) setPollenRpm(pollenTargetRPM);
    }
}