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

@NextTeleop(name = "Auto Tracking BLUE Tests")
public class AutoTracking extends NextOpMode {

    private final BaseRobot robot;


    public double shooterMultiplier = 1;
    private double x =0 ;
    private double y = 0;
    private double heading =0;


    public AutoTracking(BaseRobot robot) {
        super(robot);//        super(robot, BulkReadHook.INSTANCE);
        this.robot = robot;

    }

    private Command relocalize() {
        return instant(() -> {
            robot.getFollower().stop();
            Pose cornerPose = new Pose(82, 8, Math.toRadians(90));
            x= 82;y = 8;
            heading = Math.toRadians(90);
            robot.getFollower().setPose(cornerPose);
        });
    }



    @Override
    public void start() {
        x = robot.getFollower().pose().x();
        y = robot.getFollower().pose().y();
        heading = robot.getFollower().pose().heading();
        robot.fieldState.setAlliance(FieldState.Alliance.BLUE);



        robot.getFollower().stop();

        robot.flower.setPose(FlowerSubsystem.FlowerState.UP);
        //robot.getFollower().update();
        Trigger.Companion.getDefaultEventLoop().clear();

        CommandGamepad g1 = new CommandGamepad(gamepad1);
        CommandGamepad g2 = new CommandGamepad(gamepad2);

        robot.init().schedule();
        //START continue to track live

        robot.getFollower().setPose(AutoToTeleopPoseStorage.getEndPoseOr(new Pose(x, y, heading)));

        x = robot.getFollower().pose().x();
        y = robot.getFollower().pose().y();
        heading = Math.toDegrees(robot.getFollower().pose().heading());


        robot.turrets.setState(TurretsSubsystem.TurretState.MANUAL2).schedule();
        //intake
        g1.rightBumper().onTrue(robot.intake.setSpeed(IntakeSubsystem.IntakeState.FORWARD));
        g1.rightBumper().onTrue(robot.transfer.setSpeed(TransferSubsystem.TransferState.REVERSE));

        g1.rightBumper().onFalse(robot.intake.setSpeed(IntakeSubsystem.IntakeState.OFF));
        g1.rightBumper().onFalse(robot.transfer.setSpeed(TransferSubsystem.TransferState.OFF));


        g1.leftBumper().onTrue(robot.intake.setSpeed(IntakeSubsystem.IntakeState.REVERSE));
        g1.leftBumper().onTrue(robot.transfer.setSpeed(TransferSubsystem.TransferState.REVERSE));

        g1.leftBumper().onFalse(robot.intake.setSpeed(IntakeSubsystem.IntakeState.OFF));
        g1.leftBumper().onFalse(robot.transfer.setSpeed(TransferSubsystem.TransferState.OFF));



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


        g1.a().onTrue(robot.flower.setPose(FlowerSubsystem.FlowerState.DOWN));
        g1.y().onTrue(robot.flower.setPose(FlowerSubsystem.FlowerState.UP));

        g1.b().onTrue(relocalize());




        g1.leftTrigger().isOver(.1).onTrue(
                robot.turrets.setState(TurretsSubsystem.TurretState.AUTO)
        );
        g1.leftTrigger().isOver(.2).onFalse(robot.turrets.setState(TurretsSubsystem.TurretState.OFF));

        g1.rightTrigger().isOver(0.2).onTrue(robot.intake.setSpeed(IntakeSubsystem.IntakeState.SHOOT));
        g1.rightTrigger().isOver(0.2).onTrue(robot.transfer.setSpeed(TransferSubsystem.TransferState.SHOOT));
        g1.leftTrigger().isOver(0.2).onTrue(robot.shooters.setState(ShootersSubsystem.ShooterState.CLOSE));

        g1.leftTrigger().isOver(0.2).onFalse(robot.shooters.setState(ShootersSubsystem.ShooterState.OFF));

        g1.rightTrigger().isOver(0.2).onFalse(robot.intake.setSpeed(IntakeSubsystem.IntakeState.OFF));
        g1.rightTrigger().isOver(0.2).onFalse(robot.transfer.setSpeed(TransferSubsystem.TransferState.OFF));

        g1.rightTrigger().isOver(0.2).onFalse(robot.intake.setSpeed(IntakeSubsystem.IntakeState.OFF));
        g1.rightTrigger().isOver(0.2).onFalse(robot.transfer.setSpeed(TransferSubsystem.TransferState.OFF));



    }

    @Override
    public void periodic() {

        robot.shooters.setMultiplier(shooterMultiplier);
        ManualDrive.driveOrHold(
                robot.getFollower(),
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x
        );

        robot.getFollower().update();
        //ManualDrive.driveOrHold(robot.getFollower(), powers);
        //robot.getFollower().update();
        double forward = -gamepad1.left_stick_y;
        double lateral = -gamepad1.left_stick_x;
        double turn = -gamepad1.right_stick_x;

        telemetry.addData("SHOOTER MULTIPLER: ", shooterMultiplier);
        //robot.getFollower().update();

        Pose robotPose = robot.getFollower().pose();

        x = robotPose.x();
        y = robotPose.y();
        heading = Math.toDegrees(robotPose.heading());



        if (y>70) {
            robot.fieldState.setGoalSide(FieldState.GoalSide.GARDEN);
        } else {
            robot.fieldState.setGoalSide(FieldState.GoalSide.LOADING_ZONE);
        }

        telemetry.addData("Robot X", x);
        telemetry.addData("Robot Y", y);
        telemetry.addData("scoring side", robot.fieldState.getGoalSide());
        telemetry.addData("color", robot.fieldState.getAlliance());
        telemetry.addData("Robot Heading", heading);
        telemetry.addData("Turret Degrees", robot.turrets.getPollenTurretTargetAngle());

        telemetry.addData("Difference", heading - robot.turrets.getPollenTurretTargetAngle());




        telemetry.addData("Nectar Motor Velocity", robot.getShooter().getNectarShooter().getEncoderVelocity().into(RotationsPerMinute));
        telemetry.addData("Pollen Motor Velocity", robot.getShooter().getPollenShooter().getEncoderVelocity().into(RotationsPerMinute));

        telemetry.update();
    }

    @Override
    public void end(){
        AutoToTeleopPoseStorage.clear();//so it doesnt keep between runs

    }

}