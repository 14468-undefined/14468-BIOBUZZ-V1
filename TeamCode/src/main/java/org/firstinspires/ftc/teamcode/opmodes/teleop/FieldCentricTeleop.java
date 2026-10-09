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

@NextTeleop(name = "Field Centric")
public class FieldCentricTeleop extends NextOpMode {

    private final BaseRobot robot;
    private final Follower follower;

    private final CommandGamepad driver1 = new CommandGamepad(gamepad1);
    private final CommandGamepad driver2 = new CommandGamepad(gamepad2);

    // The Pinpoint heading (radians) at which the robot is facing away from the driver.
    // Y re-captures this from the Pinpoint without touching X/Y odometry.
    private static final double CORNER_HEADING = Math.toRadians(90);
    private double driverForwardHeading = CORNER_HEADING;

    public FieldCentricTeleop(BaseRobot robot) {
        super(robot, BulkReadHook.INSTANCE);
        this.robot = robot;

        // Constructor = old init(). Runs once, on INIT press.
        follower = Constants.create(hardwareMap);

        driver1.rightBumper().whileTrue(robot.intake.intake());
        driver1.leftBumper().whileTrue(robot.intake.intakeReverse());
        // whileTrue -> runs while held, auto-cancels (and your default
        // command, if you add one, resumes) on release.

        driver1.a().onTrue(robot.shooters.toggleSpin(3000)); // spins both shooters at 3k rpm
        driver1.a().onTrue(robot.shooters.toggleAutoSpin()); // makes the shooters spin using odometry position and LUT

        driver1.y().onTrue(resetHeading()); // line the robot up facing away from you, then press
        driver1.start().onTrue(relocalize()); // full pose reset to the corner
    }

    private Command relocalize() {
        return instant(() -> {
            Pose cornerPose = new Pose(10.5, 10.5, CORNER_HEADING);
            follower.setPose(cornerPose);
            driverForwardHeading = CORNER_HEADING;
        });
    }

    private Command resetHeading() {
        return instant(() -> driverForwardHeading = follower.pose().heading());
    }

    @Override
    public void start() {
        // pass pose from auto to teleop here
        // follower.setPose(pose);
    }

    @Override
    public void periodic() {
        follower.update();
        Pose robotPose = follower.pose();

        // Stick inputs in the driver's frame (up = away from driver)
        double fieldForward = -gamepad1.left_stick_y;
        double fieldLeft    = -gamepad1.left_stick_x;   // left-positive, Pedro convention
        double turn         = -gamepad1.right_stick_x;  // CCW-positive, Pedro convention

        // Robot heading relative to "facing away from the driver"
        double heading = robotPose.heading() - driverForwardHeading;
        double cos = Math.cos(heading);
        double sin = Math.sin(heading);

        // Rotate driver-frame input into the robot's frame
        double robotForward = fieldForward * cos + fieldLeft * sin;
        double robotLeft    = -fieldForward * sin + fieldLeft * cos;

        follower.manual(robotForward, robotLeft, turn);

        telemetry.addData("Robot X", robotPose.x());
        telemetry.addData("Robot Y", robotPose.y());
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
        telemetry.addData("Driver Forward Heading", Math.toDegrees(driverForwardHeading));
    }
}