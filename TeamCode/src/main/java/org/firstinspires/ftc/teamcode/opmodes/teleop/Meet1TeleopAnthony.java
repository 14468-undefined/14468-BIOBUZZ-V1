package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;
import org.firstinspires.ftc.teamcode.subsystem.*;
import org.firstinspires.ftc.teamcode.util.AutoToTeleopPoseStorage;

import static com.pedropathing.ivy.commands.Commands.instant;
import static dev.nextftc.units.Units.RotationsPerMinute;

@NextTeleop(name = "MEET 1 TELEOP")
public class Meet1TeleopAnthony extends NextOpMode {

    private final BaseRobot robot;
    private  int velocity = 2075;
    private double pollenTurretPos = .42;
    // for manual hardcoding



    public Meet1TeleopAnthony(BaseRobot robot) {
        super(robot);//        super(robot, BulkReadHook.INSTANCE);
        this.robot = robot;

    }
    private Command increaseVel(int increment) {
        return instant(() -> velocity += increment);
    }
    private Command decreaseVel(int increment) {
        return instant(() -> velocity -= increment);
    }


    private Command rotateTurretRight(double increment) {
        return instant(() -> {
            pollenTurretPos += increment;
            robot.turrets.pollenCustom(pollenTurretPos).schedule();
        });
    }

    private Command rotateTurretLeft(double increment) {
        return instant(() -> {
            pollenTurretPos -= increment;
            robot.turrets.pollenCustom(pollenTurretPos).schedule();
        });
    }
    private Command relocalize() {
        return instant(() -> {
            Pose cornerPose = new Pose(10.5, 10.5, Math.toRadians(90));
            robot.getFollower().setPose(cornerPose);
        });
    }
    //Field Centric resetting, not needed for robot centric
    private Command resetHeading() {

        return instant(() -> {
            Pose robotPose = robot.getFollower().pose();
            Pose newPose = new Pose(robotPose.x(), robotPose.y(), Math.toRadians(180));
            //this is intake facing forward can change later
            robot.getFollower().setPose(newPose);
            robot.startDriveFieldCentric(gamepad1);
        });

    }


    @Override
    public void start() {
        Scheduler.reset();//ADDED 11/1
        Trigger.Companion.getDefaultEventLoop().clear();//ADDED 11/1

        Trigger.Companion.getDefaultEventLoop().clear();

        CommandGamepad g1 = new CommandGamepad(gamepad1);
        CommandGamepad g2 = new CommandGamepad(gamepad2);

        robot.init().schedule();
        //robot.startDriveFieldCentric(gamepad1);//TODO: Anthony swap with field centric if you'd like but wait til we test this first
        robot.startDriveFieldCentric(gamepad1);
        //robot.shooters.setState(ShootersSubsystem.ShooterState.MANUAL).schedule();
        robot.getFollower().setPose(AutoToTeleopPoseStorage.getEndPoseOr(new Pose(0, 0, 0)));

        robot.turrets.setState(TurretsSubsystem.TurretState.MANUAL2).schedule();
        //intake
        g1.rightTrigger().isOver(.1).onTrue(robot.intake.setSpeed(IntakeSubsystem.IntakeState.FORWARD));
        g1.rightTrigger().isOver(.1).onTrue(robot.transfer.setSpeed(TransferSubsystem.TransferState.REVERSE));

        g1.rightTrigger().isOver(.1).onFalse(robot.intake.setSpeed(IntakeSubsystem.IntakeState.OFF));
        g1.rightTrigger().isOver(.1).onFalse(robot.transfer.setSpeed(TransferSubsystem.TransferState.OFF));

        //purge intake
        g1.leftTrigger().isOver(.1).onTrue(robot.intake.setSpeed(IntakeSubsystem.IntakeState.REVERSE));
        g1.leftTrigger().isOver(.1).onTrue(robot.transfer.setSpeed(TransferSubsystem.TransferState.REVERSE));
        g1.leftTrigger().isOver(.1).onFalse(robot.intake.setSpeed(IntakeSubsystem.IntakeState.OFF));
        g1.leftTrigger().isOver(.1).onFalse(robot.transfer.setSpeed(TransferSubsystem.TransferState.OFF));

        //g1.leftBumper().onTrue(robot.shooters.spinBoth(velocity));
        //g1.leftBumper().onFalse(robot.shooters.spinBoth(0));
        g1.leftBumper().onTrue(robot.shooters.setState(ShootersSubsystem.ShooterState.CLOSE));
        g1.leftBumper().onFalse(robot.shooters.setState(ShootersSubsystem.ShooterState.OFF));

        //g1.dpadRight().onTrue(robot.turrets.pollenCustom(.42));
        g1.dpadRight().onTrue(rotateTurretRight(.01));


        //g1.dpadLeft().onTrue(robot.turrets.pollenCustom(.44));
        g1.dpadLeft().onTrue(rotateTurretLeft(.01));


        g1.a().onTrue(instant(() -> {
            pollenTurretPos = .42;
            robot.turrets.pollenCustom(pollenTurretPos).schedule();
        })); //center it




        //shoot
        g1.b().onTrue(robot.intake.setSpeed(IntakeSubsystem.IntakeState.SHOOT));
        g1.b().onTrue(robot.transfer.setSpeed(TransferSubsystem.TransferState.SHOOT));

        g1.b().onFalse(robot.intake.setSpeed(IntakeSubsystem.IntakeState.OFF));
        g1.b().onFalse(robot.transfer.setSpeed(TransferSubsystem.TransferState.OFF));

        //flower
        g1.rightBumper().onTrue(robot.flower.setPose(FlowerSubsystem.FlowerState.DOWN));
        g1.x().onTrue(robot.flower.setPose(FlowerSubsystem.FlowerState.UP));


        g1.y().onTrue(resetHeading());

        //manual changing shooter power
        g1.dpadUp().onTrue(increaseVel(25));
        g1.dpadDown().onTrue(decreaseVel(25));



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
        telemetry.addData("Target Velocity", velocity);
       // telemetry.addData("Turret position", robot.get)
        telemetry.addData("Pollen Motor Velocity", robot.getShooter().getPollenShooter().getEncoderVelocity().into(RotationsPerMinute));

    }

    @Override
    public void end(){
        AutoToTeleopPoseStorage.clear();//so it doesnt keep between runs

    }

}