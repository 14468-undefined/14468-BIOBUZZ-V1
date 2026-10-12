package org.firstinspires.ftc.teamcode.util;

import com.pedropathing.math.Pose;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Shared field/pose state - alliance, goal coordinates, and the
 * geometry helpers any mechanism needs to reason about where it
 * is on the field relative to a goal. Not a Mechanism itself;
 * just a data/geometry holder other subsystems reference.
 */
public class FieldState {

    // GOAL COORDINATES
    private static final double BLUE_GOAL_X = 84;
    private static final double RED_GOAL_X = 58;
    private static final double RED_GARDEN_SIDE_GOAL_Y = 55; // bottom left in Pedro
    private static final double RED_LOADING_ZONE_SIDE_GOAL_Y = 86; // top left in Pedro
    private static final double BLUE_GARDEN_SIDE_GOAL_Y = 86; // top right in Pedro
    private static final double BLUE_LOADING_ZONE_SIDE_GOAL_Y = 55; // bottom right in Pedro

    private Supplier<Pose> robotPose;
    public enum Alliance { RED, BLUE }
    public enum GoalSide { GARDEN, LOADING_ZONE }

    public void setPoseSupplier(Supplier<Pose> poseSupplier) {
        this.robotPose = Objects.requireNonNull(poseSupplier);
    }

    private Alliance alliance = Alliance.BLUE;
    private GoalSide goalSide = GoalSide.GARDEN;



    public void setGoalSide(GoalSide side) {
        this.goalSide = side;
    }

    public GoalSide getGoalSide() {
        return goalSide;
    }

    public Alliance getAlliance() {
        return alliance;
    }
    public void setAlliance(Alliance alliance) {
        this.alliance = alliance;
    }

    public Pose getRobotPose() {
        if (robotPose == null) {
            throw new IllegalStateException("Robot pose supplier has not been configured");
        }
        return robotPose.get();
    }

    public double goalX() {
        if (alliance == Alliance.BLUE) {
            return BLUE_GOAL_X;
        } else {
            return RED_GOAL_X;
        }
    }

    public double goalY(GoalSide side) {
        if (alliance == Alliance.BLUE) {
            if (side == GoalSide.GARDEN) {
                return BLUE_GARDEN_SIDE_GOAL_Y;
            } else {
                return BLUE_LOADING_ZONE_SIDE_GOAL_Y;
            }
        } else {
            if (side == GoalSide.GARDEN) {
                return RED_GARDEN_SIDE_GOAL_Y;
            } else {
                return RED_LOADING_ZONE_SIDE_GOAL_Y;
            }
        }
    }

    /**
     * Field position {x, y} of a mechanism mounted off-center from the robot.
     * +forward = ahead of center, +lateral = left of center.
     * in inches
     */
    public double[] mechanismFieldPosition(double forwardOffset, double lateralOffset) {
        Pose pose = getRobotPose();
        double headingRad = pose.heading();

        double x = pose.x() + forwardOffset * Math.cos(headingRad) - lateralOffset * Math.sin(headingRad);
        double y = pose.y() + forwardOffset * Math.sin(headingRad) + lateralOffset * Math.cos(headingRad);
        return new double[] { x, y };
    }

    /**
     * Straight-line distance, in field units, from a mechanism's
     * position to the given goal.
     */
    public double distanceToGoal(double forwardOffset, double lateralOffset) {
        double[] pos = mechanismFieldPosition(forwardOffset, lateralOffset);
        double dx = goalX() - pos[0];
        double dy = goalY(goalSide) - pos[1];
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double bearingToGoalDeg(double forwardOffset, double lateralOffset) {
        Pose pose = getRobotPose();
        double[] pos = mechanismFieldPosition(forwardOffset, lateralOffset);

        double dx = goalX() - pos[0];
        double dy = goalY(goalSide) - pos[1];

        double fieldAngleToGoalDeg = Math.toDegrees(Math.atan2(dy, dx));
        double headingDeg = Math.toDegrees(pose.heading());

        return normalizeDegrees(fieldAngleToGoalDeg - headingDeg);
    }


    /**
     * clamp from (-180, 180]
     * @param angleDeg
     * @return
     */
    public static double normalizeDegrees(double angleDeg) {
        angleDeg %= 360;
        if (angleDeg > 180) {
            angleDeg -= 360;
        }
        if (angleDeg < -180) {
            angleDeg += 360;
        }
        return angleDeg;
    }
}