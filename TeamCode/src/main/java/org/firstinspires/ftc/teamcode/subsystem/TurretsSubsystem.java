package org.firstinspires.ftc.teamcode.subsystem;


import com.pedropathing.ivy.Command;
import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;
import org.firstinspires.ftc.teamcode.util.Config;
import org.firstinspires.ftc.teamcode.util.FieldState;

import static org.firstinspires.ftc.teamcode.util.Constants.TurretConstants.*;

/**
 * Turrets driven in POSITION (servo) mode. No encoder / PID: the Axons are set to
 * servo mode and we just command a 0..1 position.
 *
 * Needs these in Constants.TurretConstants (all public static, non-final so Panels can edit):
 *   SERVO_RANGE_DEG          (double) degrees the servo travels from position 0 to 1 (~355 for Axon)
 *   POLLEN_CENTER_POS        (double) servo position where the pollen turret points straight ahead (0.5 to start)
 *   NECTAR_CENTER_POS        (double) same for nectar
 *   TUNING_POLLEN_POSITION   (double) raw 0..1 position used in TUNING state
 *   TUNING_NECTAR_POSITION   (double) raw 0..1 position used in TUNING state
 * Already existing and still used:
 *   POLLEN_GEAR_RATIO / NECTAR_GEAR_RATIO   (turretDeg = servoDeg * ratio)
 *   POLLEN_ANGLE_SIGN / NECTAR_ANGLE_SIGN   (+1 or -1)
 *   POLLEN_TURRET_FORWARD_OFFSET / LATERAL_OFFSET (and NECTAR_*)
 */
public class TurretsSubsystem implements Mechanism {

    private TurretState turretState = TurretState.OFF;
    public enum TurretState {
        OFF,     // stop sending position commands (servos hold whatever they were last told)
        AUTO,    // aim at the goal using fieldState
        MANUAL,  // aim at manualDegrees (turret degrees)
        TUNING   // raw 0..1 servo positions from TUNING_*_POSITION
    }

    private int manualDegrees = 0;

    private final NextServo pollenTurret1 = new NextServo(RobotController.expansionHub(), Config.pollenTurret1);
    private final NextServo pollenTurret2 = new NextServo(RobotController.expansionHub(), Config.pollenTurret2);
    private final NextServo nectarTurret1 = new NextServo(RobotController.expansionHub(), Config.nectarTurret1);
    private final NextServo nectarTurret2 = new NextServo(RobotController.expansionHub(), Config.nectarTurret2);

    private static final double TURRET_LIMIT_DEG = 180; // travel 180 degrees max in each direction
    private static final double LIMIT_MARGIN_DEG = 5;
    private static final double SOFT_LIMIT_DEG = TURRET_LIMIT_DEG - LIMIT_MARGIN_DEG;

    // Bearing has to get this far past +-180 before the turret is allowed to swap sides
    private static final double SIDE_HYSTERESIS_DEG = 10;

    // Remembers which side (+1 / -1) of the wire limit each turret is on. 0 = not decided yet
    private static class SideTracker {
        int side = 0;
    }

    private final SideTracker pollenSide = new SideTracker();
    private final SideTracker nectarSide = new SideTracker();

    // Last turret angle (deg) we commanded. Used as the "where are we" reference for side picking
    // and for telemetry, since there's no encoder feedback in position mode.
    private double pollenCommandedDeg = 0;
    private double nectarCommandedDeg = 0;

    // Last raw servo position commanded, for telemetry
    private double pollenPosition = 0.5;
    private double nectarPosition = 0.5;

    private final FieldState fieldState;

    private FieldState.GoalSide goalSide = FieldState.GoalSide.GARDEN;
    private FieldState.Alliance alliance = FieldState.Alliance.RED;


    public TurretsSubsystem(FieldState fieldState) {
        this.fieldState = fieldState;
    }


    public void setTurretState(TurretState turretState) {
        this.turretState = turretState;
    }
    public Command setState(TurretState s) {
        return instant(() -> setTurretState(s));
    }

    public void setCustomDeg(int deg) {
        manualDegrees = deg;
    }
    public Command custom(int deg) {
        return instant(() -> this.setCustomDeg(deg));
    }


    // ---- Targets / telemetry getters ----

    public double getPollenTurretTargetAngle() {
        return fieldState.bearingToGoalDeg(POLLEN_TURRET_FORWARD_OFFSET, POLLEN_TURRET_LATERAL_OFFSET);
    }

    public double getNectarTurretTargetAngle() {
        return fieldState.bearingToGoalDeg(NECTAR_TURRET_FORWARD_OFFSET, NECTAR_TURRET_LATERAL_OFFSET);
    }

    /** Turret angle we last told the pollen turret to go to (not a measured value). */
    public double getPollenCommandedAngle() { return pollenCommandedDeg; }
    public double getNectarCommandedAngle() { return nectarCommandedDeg; }

    /** Last raw 0..1 servo position sent. */
    public double getPollenPosition() { return pollenPosition; }
    public double getNectarPosition() { return nectarPosition; }


    // ---- Math ----

    // Wraps any angle into [-180, 180)
    private static double normalizeDeg(double deg) {
        return ((deg + 180) % 360 + 360) % 360 - 180;
    }

    private static double clampToTravel(double deg) {
        return Math.max(-SOFT_LIMIT_DEG, Math.min(SOFT_LIMIT_DEG, deg));
    }

    private static double clamp01(double v) {
        return Math.max(0.0, Math.min(1.0, v));
    }

    // Turns a bearing into a target inside the travel range. Near +-180 the bearing is
    // ambiguous (it can be reached from either side), so stay on the current side until
    // the bearing has moved well clear of the boundary.
    private double resolveTarget(double bearingDeg, double referenceDeg, SideTracker tracker) {
        double b = normalizeDeg(bearingDeg);

        if (Math.abs(b) <= 180 - SIDE_HYSTERESIS_DEG) {
            if (b != 0) tracker.side = b > 0 ? 1 : -1;
            return clampToTravel(b);
        }

        if (tracker.side == 0) tracker.side = referenceDeg >= 0 ? 1 : -1;

        double onSide;
        if (tracker.side > 0) onSide = b > 0 ? b : b + 360;
        else onSide = b < 0 ? b : b - 360;
        return clampToTravel(onSide);
    }

    // turret degrees -> servo position (0..1)
    // turretDeg = servoDeg * gearRatio * sign  =>  servoDeg = turretDeg / (gearRatio * sign)
    private static double degToPosition(double turretDeg, double centerPos, double gearRatio, double sign) {
        if (gearRatio == 0 || SERVO_RANGE_DEG == 0) return clamp01(centerPos);
        double servoDeg = turretDeg / gearRatio * sign;
        return clamp01(centerPos + servoDeg / SERVO_RANGE_DEG);
    }


    // ---- Output ----

    private void commandPollenAngle(double bearingDeg) {
        double target = resolveTarget(bearingDeg, pollenCommandedDeg, pollenSide);
        pollenCommandedDeg = target;
        setPollenPosition(degToPosition(target, POLLEN_CENTER_POS, POLLEN_GEAR_RATIO, POLLEN_ANGLE_SIGN));
    }

    private void commandNectarAngle(double bearingDeg) {
        double target = resolveTarget(bearingDeg, nectarCommandedDeg, nectarSide);
        nectarCommandedDeg = target;
        setNectarPosition(degToPosition(target, NECTAR_CENTER_POS, NECTAR_GEAR_RATIO, NECTAR_ANGLE_SIGN));
    }

    private void setPollenPosition(double pos) {
        pollenPosition = clamp01(pos);
        pollenTurret1.setPosition(pollenPosition);
        pollenTurret2.setPosition(pollenPosition);
    }

    private void setNectarPosition(double pos) {
        nectarPosition = clamp01(pos);
        nectarTurret1.setPosition(nectarPosition);
        nectarTurret2.setPosition(nectarPosition);
    }


    @Override
    public void periodic() {
        switch (turretState) {
            case AUTO:
                commandNectarAngle(getNectarTurretTargetAngle());
                commandPollenAngle(getPollenTurretTargetAngle());
                break;

            case MANUAL:
                commandNectarAngle(manualDegrees);
                commandPollenAngle(manualDegrees);
                break;

            case TUNING:
                // Raw positions straight from Panels, no gear ratio / limits applied.
                setPollenPosition(TUNING_POLLEN_POSITION);
                setNectarPosition(TUNING_NECTAR_POSITION);
                break;

            case OFF:
            default:
                // Position servos have no "off". Just stop updating them.
                break;
        }
    }
}