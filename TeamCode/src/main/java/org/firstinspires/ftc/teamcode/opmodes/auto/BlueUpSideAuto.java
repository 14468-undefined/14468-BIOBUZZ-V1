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

@NextAutonomous(name = "blueup")//rename
public class BlueUpSideAuto extends NextOpMode {//TODO: rename

    private final Follower follower;
    private final PoseFactory p = PoseFactory.degrees();

    private final Pose startPose = p.of(24, 24, 0);//TODO
    private final Pose park = p.of(48, 48, 90);//
    private final Pose controlPose = p.of(36, 60, 45);



    public BlueUpSideAuto(BaseRobot robot) {
        super(robot, BulkReadHook.INSTANCE);

        Scheduler.reset(); //resets from previous op mode
        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
    }

    @Override
    public void start() {
        schedule(_____);
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

        //TODO: store pose for teleop
        // OpModeStorage.autonomousEndPose = follower.pose();
    }
}