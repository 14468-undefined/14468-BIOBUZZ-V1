package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import dev.nextftc.robot.opmode.BulkReadHook;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.triggers.CommandGamepad;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystem.BaseRobot;

import static com.pedropathing.ivy.commands.Commands.*;

@NextTeleop(name = "Shooter Test")
public class ShooterTest extends NextOpMode {

    private final BaseRobot robot;
    private final Follower follower;

    private final CommandGamepad driver1 = new CommandGamepad(gamepad1);

    private double testRpm = 2000; // starting RPM

    public ShooterTest(BaseRobot robot) {
        super(robot, BulkReadHook.INSTANCE);
        this.robot = robot;

        follower = Constants.create(hardwareMap);

        // A: toggle shooters on/off at the current test RPM
        driver1.a().onTrue(toggleShooters());

        // Dpad: adjust test RPM (applies live if already spinning)
        driver1.dpadUp().onTrue(adjustRpm(100));
        driver1.dpadRight().onTrue(adjustRpm(50));
        driver1.dpadDown().onTrue(adjustRpm(-100));
        driver1.dpadLeft().onTrue(adjustRpm(-50));
    }

    private Command toggleShooters() {
        return instant(() -> {
            if (robot.shooters.isSpinning()) robot.shooters.setBothRpm(0);
            else robot.shooters.setBothRpm(testRpm);
        });
    }

    private Command adjustRpm(double delta) {
        return instant(() -> {
            testRpm = Math.max(0, testRpm + delta);
            if (robot.shooters.isSpinning()) robot.shooters.setBothRpm(testRpm);
        });
    }

    @Override
    public void periodic() {
        follower.manual(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        follower.update();

        telemetry.addData("Test RPM", testRpm);
        telemetry.addData("Spinning", robot.shooters.isSpinning());
        telemetry.addData("Nectar target", robot.shooters.getNectarTargetRpm());
        telemetry.addData("Pollen target", robot.shooters.getPollenTargetRpm());
        telemetry.addData("Nectar dist", robot.shooters.getNectarShooterDistanceToGoal());
        telemetry.addData("Pollen dist", robot.shooters.getPollenShooterDistanceToGoal());
        telemetry.update();
    }
}