package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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
@NextAutonomous(name = "RA2PATH")
@Disabled

public class RA2PATH {

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(57.669, 7.3669, 90);
    private final Pose path1 = poseFactory.of(10.7087, 21.0905, 270);
    private final Pose path1Control1 = poseFactory.of(46.998, 42.1769, 0);
    private final Pose point2 = poseFactory.of(9.3618, 8.3002, 270);
    private final Pose point3 = poseFactory.of(58.5, 37, 90);
    private final Pose point4 = poseFactory.of(58.5, 120, 90);
    private final Pose point5 = poseFactory.of(47, 120, 90);
    private final Pose point6 = poseFactory.of(47, 131.5, 90);
    private final Pose point7 = poseFactory.of(58.5129, 119.3291, 90);
    private final Pose point8 = poseFactory.of(10.8131, 90.0636, 0);
    private final Pose point8Control1 = poseFactory.of(32.6322, 87.5408, 0);

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
        return line(point3, point4).tangent();
    }

    public Path path5() {
        return line(point4, point5).linear(point4, point5);
    }

    public Path path6() {
        return line(point5, point6).tangent();
    }

    public Path path7() {
        return line(point6, point7).linear(point6, point7);
    }

    public Path path8() {
        return curve(point7, point8Control1, point8).linear(point7, point8);
    }
}