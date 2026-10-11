package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.pedropathing.paths.Path;
import dev.nextftc.robot.opmode.BulkReadHook;
import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;
import org.firstinspires.ftc.teamcode.subsystem.BaseRobot;

import com.pedropathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;

import com.pedropathing.ivy.Scheduler;
import org.firstinspires.ftc.teamcode.util.AutoToTeleopPoseStorage;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;

@NextAutonomous(name = "BSS2AUTO")//rename
public class BSS2AUTO extends NextOpMode {//TODO: rename

    private final Follower follower;
    private final PoseFactory p = PoseFactory.degrees();

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




    public BSS2AUTO(BaseRobot robot) {
        super(robot, BulkReadHook.INSTANCE);

        Scheduler.reset(); //resets from previous op mode
        follower = Constants.create(hardwareMap);
        //TODO: follower.setPose();//starting pose
    }

    @Override
    public void start() {
        //TODO: schedule(_____);
    }

    @Override
    public void periodic() {
        follower.update(); //lets path advance

        telemetry.addData("x: ", follower.pose().x());
        telemetry.addData("y: ", follower.pose().y());
        telemetry.addData("heading: ", Math.toDegrees(follower.pose().heading()));
    }

    @Override
    public void end() {

        //store pose for teleop
        AutoToTeleopPoseStorage.autonomousEndPose = follower.pose();
    }
}