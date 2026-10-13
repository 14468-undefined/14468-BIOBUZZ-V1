package org.firstinspires.ftc.teamcode.subsystem;


import com.pedropathing.ivy.Command;
import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.control.feedback.PIDController;
import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextCRServo;
import dev.nextftc.hardware.actuators.NextFeedbackCRServo;
import dev.nextftc.hardware.sensors.NextAnalogInput;
import dev.nextftc.robot.Mechanism;
import org.firstinspires.ftc.teamcode.util.Config;
import org.firstinspires.ftc.teamcode.util.Constants;
import org.firstinspires.ftc.teamcode.util.FieldState;

import static dev.nextftc.units.Units.Degrees;
import static org.firstinspires.ftc.teamcode.util.Constants.TurretConstants.*;

public class TurretsSubsystem implements Mechanism {


    private TurretState turretState = TurretState.OFF;
    public enum TurretState {
        OFF,
        AUTO,
        MANUAL,
        TUNING
    }

    private int manualDegrees = 0;

    private final NextFeedbackCRServo pollenTurret1 = new NextFeedbackCRServo(RobotController.expansionHub(), Config.pollenTurret1, new NextAnalogInput("pollen"));
    private final NextCRServo pollenTurret2 = new NextCRServo(RobotController.expansionHub(), Config.pollenTurret2, 0);
    private final NextFeedbackCRServo nectarTurret1 = new NextFeedbackCRServo(RobotController.expansionHub(), Config.nectarTurret1, new NextAnalogInput(RobotController.expansionHub(), Config.nectarAnalogEncoder));
    private final NextCRServo nectarTurret2 = new NextCRServo(RobotController.expansionHub(), Config.nectarTurret2);

    private PIDController pollenPID = new PIDController(new PIDCoefficients(Constants.TurretConstants.T_POLLEN_KP, Constants.TurretConstants.T_POLLEN_KI, Constants.TurretConstants.T_POLLEN_KD));
    private PIDController nectarPID = new PIDController(new PIDCoefficients(Constants.TurretConstants.T_NECTAR_KP, Constants.TurretConstants.T_NECTAR_KI, Constants.TurretConstants.T_NECTAR_KD));

    private double lastPollenKP = Constants.TurretConstants.T_POLLEN_KP;
    private double lastPollenKI = Constants.TurretConstants.T_POLLEN_KI;
    private double lastPollenKD = Constants.TurretConstants.T_POLLEN_KD;
    private double lastNectarKP = Constants.TurretConstants.T_NECTAR_KP;
    private double lastNectarKI = Constants.TurretConstants.T_NECTAR_KI;
    private double lastNectarKD = Constants.TurretConstants.T_NECTAR_KD;

    private static final double TURRET_LIMIT_DEG = 180;//travel 180 degrees max in each direction
    private static final double LIMIT_MARGIN_DEG = 5;
    private static final double SOFT_LIMIT_DEG = TURRET_LIMIT_DEG - LIMIT_MARGIN_DEG;
    private static final double ANGLE_TOLERANCE_DEG = 1;

    // Bearing has to get this far past +-180 before the turret is allowed to swap sides
    private static final double SIDE_HYSTERESIS_DEG = 10;

    // Remembers which side (+1 / -1) of the wire limit each turret is on. 0 = not decided yet
    private static class SideTracker {
        int side = 0;
    }

    private final SideTracker pollenSide = new SideTracker();
    private final SideTracker nectarSide = new SideTracker();


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


    public double getPollenTurretTargetAngle() {
        return fieldState.bearingToGoalDeg(Constants.TurretConstants.POLLEN_TURRET_FORWARD_OFFSET, Constants.TurretConstants.POLLEN_TURRET_LATERAL_OFFSET);
    }

    public double getNectarTurretTargetAngle() {
        return fieldState.bearingToGoalDeg(Constants.TurretConstants.NECTAR_TURRET_FORWARD_OFFSET, Constants.TurretConstants.NECTAR_TURRET_LATERAL_OFFSET);
    }



    public double getNectarTurretCurrentAngle(){
        return servoToTurretDeg(nectarTurret1.getAngle().into(Degrees), Constants.TurretConstants.NECTAR_ZERO_DEG, Constants.TurretConstants.NECTAR_GEAR_RATIO, Constants.TurretConstants.NECTAR_ANGLE_SIGN);
    }
    public double getPollenTurretCurrentAngle(){
        return servoToTurretDeg(pollenTurret1.getAngle().into(Degrees), Constants.TurretConstants.POLLEN_ZERO_DEG, Constants.TurretConstants.POLLEN_GEAR_RATIO, Constants.TurretConstants.POLLEN_ANGLE_SIGN);
    }

    public double getNectarServoAngleDeg(){
        return nectarTurret1.getAngle().into(Degrees);
    }
    public double getPollenServoAngleDeg(){
        return pollenTurret1.getAngle().into(Degrees);
    }

    private static double servoToTurretDeg(double servoDeg, double zeroDeg, double gearRatio, double sign) {
        return normalizeDeg(servoDeg - zeroDeg) * gearRatio * sign;
    }

    //TODO: idle state or just set to 0 when don't want PID active
    public double pollenTurretTargetPowerAUTO(){
        return turretPower(pollenPID, pollenSide, getPollenTurretTargetAngle(), getPollenTurretCurrentAngle(), T_POLLEN_KF);
    }
    public double pollenTurretTargetPowerMANUAL(int deg){
        return turretPower(pollenPID, pollenSide, deg,  getPollenTurretCurrentAngle(), T_POLLEN_KF);
    }
    public double nectarTurretTargetPowerAUTO(){
        return turretPower(nectarPID, nectarSide, getNectarTurretTargetAngle(), getNectarTurretCurrentAngle(), T_NECTAR_KF);
    }
    public double nectarTurretTargetPowerMANUAL(int deg){
        return turretPower(nectarPID, nectarSide, deg, getNectarTurretCurrentAngle(), T_NECTAR_KF);
    }

    public double pollenTurretTargetPowerTUNING(){
        return turretPower(pollenPID, pollenSide, Constants.TurretConstants.TUNING_POLLEN_ANGLE, getPollenTurretCurrentAngle(), Constants.TurretConstants.T_POLLEN_KF);
    }
    public double nectarTurretTargetPowerTUNING(){
        return turretPower(nectarPID, nectarSide, Constants.TurretConstants.TUNING_NECTAR_ANGLE, getNectarTurretCurrentAngle(), Constants.TurretConstants.T_NECTAR_KF);
    }

    public void setCustomDeg(int deg){
        manualDegrees = deg;
    }
    public Command custom(int deg) {
        return instant(() -> this.setCustomDeg(deg));
    }


    // Wraps any angle into [-180, 180)
    private static double normalizeDeg(double deg) {
        return ((deg + 180) % 360 + 360) % 360 - 180;
    }

    private static double clampToTravel(double deg) {
        return Math.max(-SOFT_LIMIT_DEG, Math.min(SOFT_LIMIT_DEG, deg));
    }

    // Turns a bearing into a target inside the travel range. Near +-180 the bearing is
    // ambiguous (it can be reached from either side), so stay on the current side until
    // the bearing has moved well clear of the boundary.
    private double resolveTarget(double bearingDeg, double currentDeg, SideTracker tracker) {
        double b = normalizeDeg(bearingDeg);

        if (Math.abs(b) <= 180 - SIDE_HYSTERESIS_DEG) {
            if (b != 0) tracker.side = b > 0 ? 1 : -1;
            return clampToTravel(b);
        }

        if (tracker.side == 0) tracker.side = currentDeg >= 0 ? 1 : -1;

        double onSide;
        if (tracker.side > 0) onSide = b > 0 ? b : b + 360;
        else onSide = b < 0 ? b : b - 360;
        return clampToTravel(onSide);
    }

    private double turretPower(PIDController pid, SideTracker tracker, double bearingDeg, double currentDeg, double kF) {

        double target = resolveTarget(bearingDeg, currentDeg, tracker);
        double error = target - currentDeg;

        double output = pid.calculate(error);

        // Static friction
        if (Math.abs(error) > ANGLE_TOLERANCE_DEG) {
            output += kF * Math.signum(error);
        }

        // Hard cutoff
        if (currentDeg >= SOFT_LIMIT_DEG && output > 0) output = 0;
        if (currentDeg <= -SOFT_LIMIT_DEG && output < 0) output = 0;

        return Math.max(-1.0, Math.min(1.0, output));
    }


    @Override
    public void periodic() {
        if (Constants.TurretConstants.T_POLLEN_KP != lastPollenKP || Constants.TurretConstants.T_POLLEN_KI != lastPollenKI || Constants.TurretConstants.T_POLLEN_KD != lastPollenKD) {
            lastPollenKP = Constants.TurretConstants.T_POLLEN_KP;
            lastPollenKI = Constants.TurretConstants.T_POLLEN_KI;
            lastPollenKD = Constants.TurretConstants.T_POLLEN_KD;
            pollenPID = new PIDController(new PIDCoefficients(lastPollenKP, lastPollenKI, lastPollenKD));
        }
        if (Constants.TurretConstants.T_NECTAR_KP != lastNectarKP || Constants.TurretConstants.T_NECTAR_KI != lastNectarKI || Constants.TurretConstants.T_NECTAR_KD != lastNectarKD) {
            lastNectarKP = Constants.TurretConstants.T_NECTAR_KP;
            lastNectarKI = Constants.TurretConstants.T_NECTAR_KI;
            lastNectarKD = Constants.TurretConstants.T_NECTAR_KD;
            nectarPID = new PIDController(new PIDCoefficients(lastNectarKP, lastNectarKI, lastNectarKD));
        }

        switch (turretState) {
            case AUTO:
                double nectarPower = nectarTurretTargetPowerAUTO();
                nectarTurret1.setPower(nectarPower);
                nectarTurret2.setPower(nectarPower);

                double pollenPower = pollenTurretTargetPowerAUTO();
                pollenTurret1.setPower(pollenPower);
                pollenTurret2.setPower(pollenPower);
                break;

            case OFF:
                pollenTurret1.setPower(0);
                pollenTurret2.setPower(0);
                nectarTurret1.setPower(0);
                nectarTurret2.setPower(0);
                break;


            case MANUAL:
                double nectarManualPower = nectarTurretTargetPowerMANUAL(manualDegrees);
                nectarTurret1.setPower(nectarManualPower);
                nectarTurret2.setPower(nectarManualPower);

                double pollenManualPower = pollenTurretTargetPowerMANUAL(manualDegrees);
                pollenTurret1.setPower(pollenManualPower);
                pollenTurret2.setPower(pollenManualPower);

                break;

            case TUNING:
                if(Constants.TurretConstants.TUNING_NECTAR_POWER == 0) {
                    double nectarTunePower = nectarTurretTargetPowerTUNING();
                    nectarTurret1.setPower(nectarTunePower);
                    nectarTurret2.setPower(nectarTunePower);

                    double pollenTunePower = pollenTurretTargetPowerTUNING();
                    pollenTurret1.setPower(pollenTunePower);
                    pollenTurret2.setPower(pollenTunePower);

                }
                else {
                    nectarTurret1.setPower(TUNING_NECTAR_POWER);
                    nectarTurret2.setPower(TUNING_NECTAR_POWER);
                }
                break;
        }


    }
}