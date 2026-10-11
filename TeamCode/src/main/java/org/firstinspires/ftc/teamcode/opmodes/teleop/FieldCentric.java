package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import dev.nextftc.robot.opmode.BulkReadHook;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.triggers.CommandGamepad;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystem.BaseRobot;

import static com.pedropathing.ivy.commands.Commands.*;

@NextTeleop(name = "Field Centric Meet1")
public class FieldCentric extends NextOpMode {

    private final BaseRobot robot;
    private final Follower follower;

    private final CommandGamepad driver1 = new CommandGamepad(gamepad1);
    private final CommandGamepad driver2 = new CommandGamepad(gamepad2);

    // Heading (radians, from the localizer/Pinpoint) that counts as "facing away from the driver".
    // 0 = use the localizer heading as-is (what the Pedro guide does). Y re-captures it.
    private double headingOffset = 0;

    public FieldCentric(BaseRobot robot) {
        super(robot, BulkReadHook.INSTANCE);
        this.robot = robot;

        // Constructor = old init(). Runs once, on INIT press.
        follower = Constants.create(hardwareMap);

        //driver1.rightTrigger().whileTrue(robot.intake.intake());
        //driver1.leftBumper().whileTrue(robot.intake.intakeReverse());


        //swtich to triggers
        while (driver1.rightTrigger().getValue() > 0.1) {
            robot.intake.intake();
        }
        while (driver1.leftTrigger().getValue() > 0.1) {
            robot.intake.intakeReverse();
        }

        // should make it customizable
        //if (driver1.rightTrigger().getValue() > 0.1) {
      //      robot.intake.setIntakePower(driver1.rightTrigger().getValue());
      //  }
        //else if (driver1.leftTrigger().getValue() > 0.1) {
        //    robot.intake.setIntakePower(-driver1.leftTrigger().getValue());
        //}

        // whileTrue -> runs while held, auto-cancels (and your default
        // command, if you add one, resumes) on release.

        driver1.a().onTrue(robot.shooters.toggleSpin(3000));//spins both shooters at 3k rpm
        driver1.a().onTrue(robot.shooters.toggleAutoSpin());//makes the shooters spin using odometry position and LUT

        driver1.y().onTrue(resetHeading()); // face robot away from you, press Y (keeps X/Y)
        driver1.start().onTrue(relocalize());
    }

    private Command relocalize() {
        return instant(() -> {
            Pose cornerPose = new Pose(10.5, 10.5, Math.toRadians(90));
            follower.setPose(cornerPose);
            headingOffset = 0;
        });
    }

    private Command resetHeading() {
        // Current heading becomes the new "forward". Odometry X/Y is untouched.
        return instant(() -> headingOffset = follower.pose().heading());
    }

    @Override
    public void start() {
        // pass pose from auto to teleop here
        // follower.setPose(pose);
    }

    @Override
    public void periodic() {
        DrivePowers powers = ManualDrive.fieldCentric(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x,
                follower.pose().heading() - headingOffset
        );
        follower.manual(powers);
        follower.update();

        Pose robotPose = follower.pose();
        telemetry.addData("Robot X", robotPose.x());
        telemetry.addData("Robot Y", robotPose.y());
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
        telemetry.addData("Heading Offset", Math.toDegrees(headingOffset));
    }
}