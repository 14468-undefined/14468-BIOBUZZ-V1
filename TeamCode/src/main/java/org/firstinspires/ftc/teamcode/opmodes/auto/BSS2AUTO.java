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
    private final Pose start = poseFactory.of(82, 133, 90);
    //SHOT here
    //private final Pose point1 = poseFactory.of(82, 133, 90);
    private final Pose loadingZone = poseFactory.of(133, 133, 90);
    private final Pose intakeCP = poseFactory.of(128.6392, 103.676, 0);
    private final Pose outOfLZ = poseFactory.of(132.1382, 130, 90);
    private final Pose underHive = poseFactory.of(82.2606, 109.5764, -90);
    private final Pose farSide = poseFactory.of(82, 20, -90);
   // private final Pose point6Start = poseFactory.of(82, 20, -90);
    private final Pose preIntakePos = poseFactory.of(94.5, 13, -90);
    private final Pose intakePos = poseFactory.of(94.5, 9, -90);
    private final Pose shootPos = poseFactory.of(82, 23.5, 270);
    private final Pose avoidOtherRobot = poseFactory.of(106.5, 38.25, 31.0497);
    private final Pose turn = poseFactory.of(106.5, 38.25, 270);
    private final Pose park = poseFactory.of(131, 53, 180);
    private final Pose parkCP = poseFactory.of(111.4737, 55.4883, 0);

   // public Path path1() {
  //      return line(start, point1).constant(point1);
   // }//TODO: anthony - name like moveToShoot

    public Path intakeLZ() {
        return curve(start, intakeCP, loadingZone).constant(loadingZone);
    }

    public Path backOut() {
        return line(loadingZone, outOfLZ).constant(outOfLZ);
    }

    public Path underHive() {
        return line(outOfLZ, underHive).linear(outOfLZ, underHive);
    }

    public Path throughHive() {
        return line(underHive, farSide).tangent();
    }

    public Path shootAndMoveToIntake() {
        return line(farSide, preIntakePos).linear(farSide, preIntakePos);
    }

    public Path intakeFlower() {
        return line(preIntakePos, intakePos).tangent();
    }

    public Path shootFinal() {
        return line(intakePos, shootPos).constant(shootPos);
    }

    public Path curveToPark() {
        return line(shootPos, avoidOtherRobot).tangent();
    }

    public Path goPark() {
        return curve(turn, parkCP, park).linear(turn, park);
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
        robot.getFollower().setPose(start);
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