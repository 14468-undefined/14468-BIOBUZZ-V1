package org.firstinspires.ftc.teamcode.opmodes.auto;

import dev.nextftc.robot.opmode.BulkReadHook;
import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;
import org.firstinspires.ftc.teamcode.subsystem.BaseRobot;

import com.pedropathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import static com.pedropathing.api.Paths.*;
import com.pedropathing.paths.Path;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.pedro.PedroCommands.*; // needs com.pedropathing.ivy:pedro:1.1.1

public class BA3PATH {

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(81, 8, 90);
    private final Pose path1 = poseFactory.of(94.5, 12, -90);
    private final Pose path1Control1 = poseFactory.of(88.723, 32.3264, 0);
    private final Pose point2 = poseFactory.of(94.5, 8, -90);
    private final Pose point3 = poseFactory.of(84.5635, 23.4659, -90);
    private final Pose point4 = poseFactory.of(83.5212, 113.7099, 90);
    private final Pose point5 = poseFactory.of(132.2394, 132.8453, 90);
    private final Pose point5Control1 = poseFactory.of(134.3981, 82.3196, 0);
    private final Pose point6 = poseFactory.of(83.5189, 113.4052, 90);
    private final Pose point6Control1 = poseFactory.of(132.9797, 83.837, 0);
    private final Pose point7 = poseFactory.of(128, 94.3648, 0);
    private final Pose point7Control1 = poseFactory.of(114.7477, 119.67, 0);
    private final Pose point8 = poseFactory.of(133, 94.3, 0);
    private final Pose point9 = poseFactory.of(123, 53.5, -180);

    public Path path1() {
        return curve(start, path1Control1, path1).constant(path1);
    }

    public Path path2() {
        return line(path1, point2).constant(point2);
    }

    public Path path3() {
        return line(point2, point3).constant(point3);
    }

    public Path path4() {
        return line(point3, point4).constant(point4);
    }

    public Path path5() {
        return curve(point4, point5Control1, point5).constant(point5);
    }

    public Path path6() {
        return curve(point5, point6Control1, point6).constant(point6);
    }

    public Path path7() {
        return curve(point6, point7Control1, point7).linear(point6, point7);
    }

    public Path path8() {
        return line(point7, point8).constant(point8);
    }

    public Path path9() {
        return line(point8, point9).linear(point8, point9);
    }
}

