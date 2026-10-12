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

    private final BaseRobot robot;
    private final Follower follower;
    private final PoseFactory p = PoseFactory.degrees();

    private final PoseFactory poseFactory = PoseFactory.degrees();

    //TODO: anthony - can you rename all of these - have to be like shotPose, moveToShootControlPoint
    private final Pose start_pose = poseFactory.of(82, 133, 90);
    //SHOT here
    //private final Pose point1 = poseFactory.of(82, 133, 90);
    private final Pose garden = poseFactory.of(133, 133, 90);
    private final Pose intake_ctrl_pt_1 = poseFactory.of(128.6392, 103.676, 0);
    private final Pose out_of_garden = poseFactory.of(132.1382, 130, 90);
    private final Pose under_hive_up_side = poseFactory.of(82.2606, 109.5764, -90);
    private final Pose under_hive_down_side = poseFactory.of(82, 20, -90);
   // private final Pose point6Start = poseFactory.of(82, 20, -90);
    private final Pose lineup_flower_intake = poseFactory.of(94.5, 13, -90);
    private final Pose flower_intake = poseFactory.of(94.5, 9, -90);
    private final Pose park_pose = poseFactory.of(9.1057, 93.0516, 0);
    private final Pose park_path_ctrl_pt_1 = poseFactory.of(31.9919, 68.3327, 0);
   // public Path path1() {
  //      return line(start, point1).constant(point1);
   // }//TODO: anthony - name like moveToShoot

    public Path intakeGarden() {
        return curve(start_pose, intake_ctrl_pt_1, garden).constant(garden);
    }

    public Path backOut() {
        return line(garden, out_of_garden).constant(out_of_garden);
    }

    public Path garden_to_hive() {
        return line(out_of_garden, under_hive_up_side).linear(out_of_garden, under_hive_up_side);
    }

    public Path under_through_hive() {
        return line(under_hive_up_side, under_hive_down_side).tangent();
    }

    public Path line_up_for_flower_intake() {
        return line(under_hive_down_side, lineup_flower_intake).linear(under_hive_down_side, lineup_flower_intake);
    }

    public Path flower_intake() {
        return line(lineup_flower_intake, flower_intake).tangent();
    }

    public Path park_avoid_other_robot() {
        return curve(flower_intake, park_path_ctrl_pt_1, park_pose).linear(flower_intake, park_pose);
    }



    public BSS2AUTO(BaseRobot robot) {
        super(robot);
        this.robot = robot;
        Scheduler.reset(); //resets from previous op mode

        //commands = new AutoCommands(robot.getFollower(), paths);
        follower = robot.getFollower();
    }

    @Override
    public void start() {
        robot.getFollower().setPose(start_pose);
        //TODO: schedule(_____);

    }

    @Override
    public void periodic() {
        robot.getFollower().update(); //lets path advance
        Scheduler.execute();

        telemetry.addData("x: ", follower.pose().x());
        telemetry.addData("y: ", follower.pose().y());
        telemetry.addData("heading: ", Math.toDegrees(follower.pose().heading()));
    }

    @Override
    public void end() {

        //store pose for teleop
        AutoToTeleopPoseStorage.autonomousEndPose = robot.getFollower().pose();
    }
}