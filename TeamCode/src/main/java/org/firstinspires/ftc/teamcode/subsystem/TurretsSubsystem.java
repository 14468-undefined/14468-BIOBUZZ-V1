package org.firstinspires.ftc.teamcode.subsystem;

import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import dev.nextftc.hardware.actuators.NextCRServo;
import dev.nextftc.hardware.actuators.NextFeedbackCRServo;

import dev.nextftc.hardware.sensors.NextAnalogInput;
import dev.nextftc.hardware.util.AnalogFeedback;
import dev.nextftc.robot.Mechanism;

import java.util.function.Supplier;

public class TurretsSubsystem implements Mechanism {

    //TODO - fix this for actual feedback servo initialization - see nextFTC stuff
    //can also do this by port but configuration name makes most sense imo
    private final NextFeedbackCRServo pollenTurret1 = new NextFeedbackCRServo("pollenTurret1", new NextAnalogInput("pollenTurret"));
    private final NextCRServo pollenTurret2 = new NextCRServo("pollenTurret2");
    private final NextFeedbackCRServo nectarTurret1 = new NextFeedbackCRServo("nectarTurret1", new NextAnalogInput("nectarTurret"));
    private final NextCRServo nectarTurret2 = new NextCRServo("nectarTurret2");


    //GOAL COORDINATES
    private static final double BLUE_GOAL_X = 84;
    private static final double RED_GOAL_X = 58;
    private static final double RED_GARDEN_SIDE_GOAL_Y = 55; // bottom left in Pedro
    private static final double RED_LOADING_ZONE_SIDE_GOAL_Y = 86; // top left in Pedro
    private static final double BLUE_GARDEN_SIDE_GOAL_Y = 86; // top right in Pedro
    private static final double BLUE_LOADING_ZONE_SIDE_GOAL_Y = 55; // bottom right in Pedro

    //TODO - measure these
    // Mounting offsets, in inches, relative to center of robot
    // +forward = ahead of center, -forward = behind center.
    // +lateral = left of center,  -lateral = right of center.
    private static final double POLLEN_TURRET_FORWARD_OFFSET = -0.0; // TODO: measure
    private static final double POLLEN_TURRET_LATERAL_OFFSET = 0.0; // TODO: measure
    private static final double NECTAR_TURRET_FORWARD_OFFSET = -0.0; // TODO: measure
    private static final double NECTAR_TURRET_LATERAL_OFFSET = -0.0; // TODO: measure

    public enum Alliance { RED, BLUE }
    public enum GoalSide { GARDEN, LOADING_ZONE }

    private Supplier<Pose> robotPose = () -> new Pose(0, 0, 0);
    private Alliance alliance = Alliance.BLUE;
    private GoalSide POLLEN_SIDE = GoalSide.GARDEN;
    private GoalSide NECTAR_SIDE = GoalSide.LOADING_ZONE;

    public void setPoseSupplier(Supplier<Pose> poseSupplier) {
        this.robotPose = poseSupplier;
    }

    public void setAlliance(Alliance alliance) {
        this.alliance = alliance;
    }

    public void setPollenGoalSide(GoalSide side) {
        this.POLLEN_SIDE = side;
    }

    public void setNectarGoalSide(GoalSide side) {
        this.NECTAR_SIDE = side;
    }

    private double goalX() {
        if (alliance == Alliance.BLUE) {
            return BLUE_GOAL_X;
        } else {
            return RED_GOAL_X;
        }
    }


    private double goalY(GoalSide side) {
        if (alliance == Alliance.BLUE) {
            if (side == GoalSide.GARDEN) {
                return BLUE_GARDEN_SIDE_GOAL_Y;
            }
            else {
                return BLUE_LOADING_ZONE_SIDE_GOAL_Y;
            }
        } else {
            if (side == GoalSide.GARDEN) {
                return RED_GARDEN_SIDE_GOAL_Y;
            }
            else {
                return RED_LOADING_ZONE_SIDE_GOAL_Y;
            }
        }
    }

    public double getPollenTargetAngle() {
        return targetAngleFor(goalX(), goalY(POLLEN_SIDE), POLLEN_TURRET_FORWARD_OFFSET, POLLEN_TURRET_LATERAL_OFFSET);
    }

    public double getNectarTargetAngle() {
        return targetAngleFor(goalX(), goalY(NECTAR_SIDE), NECTAR_TURRET_FORWARD_OFFSET, NECTAR_TURRET_LATERAL_OFFSET);
    }

    /**
     * Calculates the target angle relative to the robot for each turret.
     *
     * @param goalX - X coord of goal
     * @param goalY - Y coord of goal
     * @param forwardOffset - of turret relative to robot center
     * @param lateralOffset - of turret relative to robot center
     * @return the angle in degrees, relative to robot-front (0 = straight ahead).
     * NOTE: POSITIVE = CCW, NEGATIVE = CW (like unit circle)
     */
    private double targetAngleFor(double goalX, double goalY, double forwardOffset, double lateralOffset) {
        Pose pose = robotPose.get();
        double headingRad = pose.heading(); // Math.cos/sin below need radians

        double turretFieldX = pose.x() + forwardOffset * Math.cos(headingRad) - lateralOffset * Math.sin(headingRad);
        double turretFieldY = pose.y() + forwardOffset * Math.sin(headingRad) + lateralOffset * Math.cos(headingRad);

        double dx = goalX - turretFieldX;
        double dy = goalY - turretFieldY;

        double fieldAngleToGoalDeg = Math.toDegrees(Math.atan2(dy, dx));
        double headingDeg = Math.toDegrees(headingRad);

        return normalizeDegrees(fieldAngleToGoalDeg - headingDeg);
    }

    /**
     * Wraps an angle in degrees to the range (-180, 180]
     */
    private double normalizeDegrees(double angleDeg) {
        angleDeg %= 360;
        if (angleDeg > 180) {
            angleDeg -= 360;
        }
        if (angleDeg < -180) {
            angleDeg += 360;
        }
        return angleDeg;
    }

    /**
     * ANGLE CALCULATION MATH STOPS, TURRET MOTOR CONTROL STARTS HERE
     * --------------------------------------------------------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------------------------------------------------------
     */

    /*
    need the following:
    - set power command
    - PID for the power using the analog input
    - stop method
     */


    @Override
    public void periodic() {
    }
}