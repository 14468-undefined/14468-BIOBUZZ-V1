package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import dev.nextftc.robot.opmode.BulkReadHook;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.triggers.CommandGamepad;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystem.BaseRobot;

import static com.pedropathing.ivy.commands.Commands.*;

@NextTeleop(name = "MEET 1 TELEOP")
public class Meet1Teleop extends NextOpMode {

    private final BaseRobot robot;
    private final Follower follower;

    private final CommandGamepad driver1 = new CommandGamepad(gamepad1);
    private final CommandGamepad driver2 = new CommandGamepad(gamepad2);

    public Meet1Teleop(BaseRobot robot) {
        super(robot, BulkReadHook.INSTANCE);
        this.robot = robot;

        // Constructor = old init(). Runs once, on INIT press.
        follower = Constants.create(hardwareMap);

        driver1.rightBumper().whileTrue(robot.intake.intake());
        driver1.leftBumper().whileTrue(robot.intake.intakeReverse());
        // whileTrue -> runs while held, auto-cancels (and your default
        // command, if you add one, resumes) on release.

        driver1.a().onTrue(robot.shooters.toggleSpin(3000));//spins both shooters at 3k rpm
        driver1.a().onTrue(robot.shooters.toggleAutoSpin());//makes the shooters spin using odometry position and LUT



        driver1.start().onTrue(relocalize());
    }

    private Command relocalize() {
        return instant(() -> {
            Pose cornerPose = new Pose(10.5, 10.5, Math.toRadians(90));
            follower.setPose(cornerPose);
        });
    }

    @Override
    public void start() {
        // pass pose from auto to teleop here
        // follower.setPose(pose);
    }

    @Override
    public void periodic() {
        double forward = -gamepad1.left_stick_y;
        double lateral = gamepad1.left_stick_x;
        double turn = gamepad1.right_stick_x;
        follower.manual(forward, lateral, turn);
        follower.update();

        Pose robotPose = follower.pose();
        telemetry.addData("Robot X", robotPose.x());
        telemetry.addData("Robot Y", robotPose.y());
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
    }
}