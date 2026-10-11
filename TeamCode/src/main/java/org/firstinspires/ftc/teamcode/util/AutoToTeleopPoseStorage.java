package org.firstinspires.ftc.teamcode.util;

import com.pedropathing.math.Pose;

/**
 * Holds the robot's pose at the end of auto so teleop can pick up from it
 * survives the switch between auto to teleop - if u restart it'll lose it tho
 *
 * Usage:
 *   Auto (at the end, or every loop):
 *       AutoToTeleopPoseStorage.autonomousEndPose = follower.getPose();
 *
 *   Teleop (in init/start):
 *       follower.setStartingPose(AutoToTeleopPoseStorage.getEndPoseOr(new Pose(0, 0, 0)));
 *
 *   Teleop (when done, so a stale pose doesn't leak into the next run):
 *       AutoToTeleopPoseStorage.clear();
 */
public class AutoToTeleopPoseStorage {

    /** Pose the robot ended auto on. Null if auto hasn't run (or was cleared). */
    public static Pose autonomousEndPose = null;

    /** True if auto stored a pose we can use. */
    public static boolean hasPose() {
        return autonomousEndPose != null;
    }

    /** Returns the stored pose, or the fallback if nothing was stored. */
    public static Pose getEndPoseOr(Pose fallback) {
        if (autonomousEndPose != null) {
            return autonomousEndPose;
        }
        else{
            return fallback;
        }
    }

    /** Wipe the stored pose. */
    public static void clear() {
        autonomousEndPose = null;
    }
}