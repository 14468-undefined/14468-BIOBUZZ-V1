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

public class RSS3PATH {

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(58.8131, 133.1839, 90);
    private final Pose path1 = poseFactory.of(47, 127, 90);
    private final Pose path1Control1 = poseFactory.of(56.6859, 113.6302, 0);
    private final Pose point2 = poseFactory.of(47, 133, 90);
    private final Pose point3 = poseFactory.of(58.5, 124, 270);
    private final Pose point4 = poseFactory.of(58.5, 29, 270);
    private final Pose point5 = poseFactory.of(8.9612, 8.5795, 270);
    private final Pose point5Control1 = poseFactory.of(11.2714, 38.8012, 0);
    private final Pose point6 = poseFactory.of(57.4004, 29.1971, 270);
    private final Pose point6Control1 = poseFactory.of(23.1133, 27.8103, 0);
    private final Pose point7 = poseFactory.of(9.923, 46.6769, 180);
    private final Pose point7Control1 = poseFactory.of(30.4006, 44.6203, 0);
    private final Pose point8 = poseFactory.of(16.7781, 64.9641, 180);
    private final Pose point9 = poseFactory.of(16, 89, 0);

    public Path path1() {
        return curve(start, path1Control1, path1).constant(path1);
    }

    public Path path2() {
        return line(path1, point2).constant(point2);
    }

    public Path path3() {
        return line(point2, point3).linear(point2, point3);
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