package org.firstinspires.ftc.teamcode.opmodes.auto;

import dev.nextftc.robot.opmode.BulkReadHook;
import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.N5extOpMode;
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
@NextAutonomous(name = "BSS2PATH")
public class BSS2PATH extends NextOpMode {

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(82, 133, 90);
    private final Pose point1 = poseFactory.of(82, 133, 90);
    private final Pose path1 = poseFactory.of(133, 133, 90);
    private final Pose path1Control1 = poseFactory.of(128.6392, 103.676, 0);
    private final Pose point3 = poseFactory.of(132.1382, 130, 90);
    private final Pose point4 = poseFactory.of(82.2606, 109.5764, -90);
    private final Pose point5 = poseFactory.of(82, 20, -90.1667);
    private final Pose point6Start = poseFactory.of(82, 20, -90);
    private final Pose point6 = poseFactory.of(94.5, 13, -90);
    private final Pose point7 = poseFactory.of(94.5, 9, -90);
    private final Pose point8 = poseFactory.of(82, 23.5, 270);
    private final Pose point9 = poseFactory.of(106.5, 38.25, 31.0497);
    private final Pose point10Start = poseFactory.of(106.5, 38.25, 270);
    private final Pose point10 = poseFactory.of(131, 53, 180);
    private final Pose point10Control1 = poseFactory.of(111.4737, 55.4883, 0);

    public Path path1() {
        return line(start, point1).constant(point1);
    }

    public Path path1_2() {
        return curve(point1, path1Control1, path1).constant(path1);
    }

    public Path path3() {
        return line(path1, point3).constant(point3);
    }

    public Path path4() {
        return line(point3, point4).linear(point3, point4);
    }

    public Path path5() {
        return line(point4, point5).tangent();
    }

    public Path path6() {
        return line(point6Start, point6).linear(point6Start, point6);
    }

    public Path path7() {
        return line(point6, point7).tangent();
    }

    public Path path8() {
        return line(point7, point8).constant(point8);
    }

    public Path path9() {
        return line(point8, point9).tangent();
    }

    public Path path10() {
        return curve(point10Start, point10Control1, point10).linear(point10Start, point10);
    }
}