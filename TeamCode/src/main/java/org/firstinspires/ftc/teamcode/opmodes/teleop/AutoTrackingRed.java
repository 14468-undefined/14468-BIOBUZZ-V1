package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.pedropathing.follower.ManualDrive;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;

import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

import org.firstinspires.ftc.teamcode.subsystem.*;
import org.firstinspires.ftc.teamcode.util.AutoToTeleopPoseStorage;
import org.firstinspires.ftc.teamcode.util.FieldState;

import static com.pedropathing.ivy.commands.Commands.*;
import static dev.nextftc.units.Units.RotationsPerMinute;

@NextTeleop(name = "Grayson  RED")
public class AutoTrackingRed extends NextOpMode {

    private final BaseRobot robot;

    public double shooterMultiplier = 1.0;

    private double x = 0;
    private double y = 0;
    private double heading = 0;

    public AutoTrackingRed(BaseRobot robot) {
        super(robot);
        this.robot = robot;
    }

    // Relocalize robot
    private Command relocalize() {
        return instant(() -> {

            robot.getFollower().stop();

            Pose cornerPose = new Pose(
                    24.5,
                    150,
                    Math.toRadians(270)
            );

            x = 24.5;
            y = 150;
            heading = Math.toRadians(270);

            robot.getFollower().setPose(cornerPose);
        });
    }

    @Override
    public void start() {

        // Get initial pose
        x = robot.getFollower().pose().x();
        y = robot.getFollower().pose().y();
        heading = robot.getFollower().pose().heading();

        // Set alliance
        robot.fieldState.setAlliance(FieldState.Alliance.RED);

        robot.getFollower().stop();

        robot.flower.setPose(FlowerSubsystem.FlowerState.UP);

        Trigger.Companion.getDefaultEventLoop().clear();

        CommandGamepad g1 = new CommandGamepad(gamepad1);

        robot.init().schedule();

        // Restore autonomous ending pose
        robot.getFollower().setPose(
                AutoToTeleopPoseStorage.getEndPoseOr(
                        new Pose(x, y, heading)
                )
        );

        robot.turrets.setState(
                TurretsSubsystem.TurretState.MANUAL2
        ).schedule();

        // -------------------------
        // INTAKE
        // -------------------------

        g1.rightBumper().onTrue(
                robot.intake.setSpeed(IntakeSubsystem.IntakeState.FORWARD)
        );

        g1.rightBumper().onTrue(
                robot.transfer.setSpeed(TransferSubsystem.TransferState.REVERSE)
        );

        g1.rightBumper().onFalse(
                robot.intake.setSpeed(IntakeSubsystem.IntakeState.OFF)
        );

        g1.rightBumper().onFalse(
                robot.transfer.setSpeed(TransferSubsystem.TransferState.OFF)
        );

        g1.leftBumper().onTrue(
                robot.intake.setSpeed(IntakeSubsystem.IntakeState.REVERSE)
        );

        g1.leftBumper().onTrue(
                robot.transfer.setSpeed(TransferSubsystem.TransferState.REVERSE)
        );

        g1.leftBumper().onFalse(
                robot.intake.setSpeed(IntakeSubsystem.IntakeState.OFF)
        );

        g1.leftBumper().onFalse(
                robot.transfer.setSpeed(TransferSubsystem.TransferState.OFF)
        );

        // -------------------------
        // SHOOTER VELOCITY ADJUSTMENT
        // -------------------------

        g1.dpadUp().onTrue(
                instant(() -> shooterMultiplier += 0.03)
        );

        g1.dpadDown().onTrue(
                instant(() -> shooterMultiplier -= 0.03)
        );

        g1.dpadRight().onTrue(
                instant(() -> shooterMultiplier = 1.0)
        );

        g1.dpadLeft().onTrue(
                instant(() -> shooterMultiplier = 1.0)
        );

        // -------------------------
        // FLOWER
        // -------------------------

        g1.a().onTrue(
                robot.flower.setPose(FlowerSubsystem.FlowerState.DOWN)
        );

        g1.y().onTrue(
                robot.flower.setPose(FlowerSubsystem.FlowerState.UP)
        );

        // -------------------------
        // RELOCALIZATION
        // -------------------------

        g1.b().onTrue(relocalize());

        // -------------------------
        // AUTOMATIC TURRET TRACKING
        // -------------------------

        g1.leftTrigger().isOver(0.1).onTrue(
                robot.turrets.setState(TurretsSubsystem.TurretState.AUTO)
        );

        g1.leftTrigger().isOver(0.2).onFalse(
                robot.turrets.setState(TurretsSubsystem.TurretState.OFF)
        );

        // -------------------------
        // SHOOTING
        // -------------------------

        g1.rightTrigger().isOver(0.2).onTrue(
                robot.intake.setSpeed(IntakeSubsystem.IntakeState.SHOOT)
        );

        g1.rightTrigger().isOver(0.2).onTrue(
                robot.transfer.setSpeed(TransferSubsystem.TransferState.SHOOT)
        );

        g1.leftTrigger().isOver(0.2).onTrue(
                robot.shooters.setState(ShootersSubsystem.ShooterState.CLOSE)
        );

        g1.leftTrigger().isOver(0.2).onFalse(
                robot.shooters.setState(ShootersSubsystem.ShooterState.OFF)
        );

        g1.rightTrigger().isOver(0.2).onFalse(
                robot.intake.setSpeed(IntakeSubsystem.IntakeState.OFF)
        );

        g1.rightTrigger().isOver(0.2).onFalse(
                robot.transfer.setSpeed(TransferSubsystem.TransferState.OFF)
        );
    }

    @Override
    public void periodic() {

        // -------------------------
        // SHOOTER MULTIPLIER
        // -------------------------

        robot.shooters.setMultiplier(shooterMultiplier);

        // -------------------------
        // DRIVE
        // -------------------------

        ManualDrive.driveOrHold(
                robot.getFollower(),
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x
        );

        robot.getFollower().update();

        // -------------------------
        // ROBOT POSITION
        // -------------------------

        Pose robotPose = robot.getFollower().pose();

        x = robotPose.x();
        y = robotPose.y();
        heading = Math.toDegrees(robotPose.heading());

        // -------------------------
        // AUTOMATIC SCORING SIDE
        // -------------------------

        if (y < 70) {

            robot.fieldState.setGoalSide(
                    FieldState.GoalSide.GARDEN
            );

        } else {

            robot.fieldState.setGoalSide(
                    FieldState.GoalSide.LOADING_ZONE
            );
        }

        // -------------------------
        // TELEMETRY
        // -------------------------

        telemetry.addData("Alliance", robot.fieldState.getAlliance());

        telemetry.addData("Robot X", x);
        telemetry.addData("Robot Y", y);
        telemetry.addData("Robot Heading", heading);

        telemetry.addData(
                "Scoring Side",
                robot.fieldState.getGoalSide()
        );

        telemetry.addData(
                "Shooter Multiplier",
                shooterMultiplier
        );

        telemetry.addData(
                "Turret Degrees",
                robot.turrets.getPollenTurretTargetAngle()
        );

        telemetry.addData(
                "Difference",
                heading - robot.turrets.getPollenTurretTargetAngle()
        );

        telemetry.addData(
                "Nectar Motor Velocity",
                robot.getShooter()
                        .getNectarShooter()
                        .getEncoderVelocity()
                        .into(RotationsPerMinute)
        );

        telemetry.addData(
                "Pollen Motor Velocity",
                robot.getShooter()
                        .getPollenShooter()
                        .getEncoderVelocity()
                        .into(RotationsPerMinute)
        );

        telemetry.update();
    }

    @Override
    public void end() {

        AutoToTeleopPoseStorage.clear();
    }
}