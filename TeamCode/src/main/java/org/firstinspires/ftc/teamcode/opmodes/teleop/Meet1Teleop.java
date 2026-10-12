package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import dev.nextftc.robot.opmode.BulkReadHook;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystem.BaseRobot;
import org.firstinspires.ftc.teamcode.subsystem.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystem.ShootersSubsystem;
import org.firstinspires.ftc.teamcode.util.AutoToTeleopPoseStorage;

import java.text.Normalizer;

import static com.pedropathing.ivy.commands.Commands.*;
import static dev.nextftc.units.Units.RotationsPerMinute;

@NextTeleop(name = "MEET 1 TELEOP")
public class Meet1Teleop extends NextOpMode {

    private final BaseRobot robot;



    public Meet1Teleop(BaseRobot robot) {
        super(robot);//        super(robot, BulkReadHook.INSTANCE);
        this.robot = robot;

    }

    private Command relocalize() {
        return instant(() -> {
            Pose cornerPose = new Pose(10.5, 10.5, Math.toRadians(90));
            robot.getFollower().setPose(cornerPose);
        });
    }

    @Override
    public void start() {
        Trigger.Companion.getDefaultEventLoop().clear();

        CommandGamepad g1 = new CommandGamepad(gamepad1);
        CommandGamepad g2 = new CommandGamepad(gamepad2);

        robot.init().schedule();
        robot.startDriveRobotCentric(gamepad1);//TODO: anthony swap with field centric if you'd like but wait til we test this first
        robot.getFollower().setPose(AutoToTeleopPoseStorage.getEndPoseOr(new Pose(0, 0, 0)));


        g1.rightTrigger().isOver(.1).whileTrue(robot.teleIntake());
        g1.leftTrigger().isOver(.3).whileTrue(robot.telePurgeIntake());

        g1.b().onTrue(robot.teleSpinUpShooters());
        g1.a().onTrue(robot.shooters.setState(ShootersSubsystem.ShooterState.OFF));//cancel
        g1.x().onTrue(robot.launchBoth());




        g1.start().onTrue(relocalize());


    }

    @Override
    public void periodic() {
        double forward = -gamepad1.left_stick_y;
        double lateral = -gamepad1.left_stick_x;
        double turn = -gamepad1.right_stick_x;

        robot.getFollower().update();

        Pose robotPose = robot.getFollower().pose();
        telemetry.addData("Robot X", robotPose.x());
        telemetry.addData("Robot Y", robotPose.y());
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));


        telemetry.addData("Nectar Motor Velocity", robot.getShooter().getNectarShooter().getEncoderVelocity().into(RotationsPerMinute));
        telemetry.addData("Pollen Motor Velocity", robot.getShooter().getPollenShooter().getEncoderVelocity().into(RotationsPerMinute));

    }

    @Override
    public void end(){
        AutoToTeleopPoseStorage.clear();//so it doesnt keep between runs

    }

}