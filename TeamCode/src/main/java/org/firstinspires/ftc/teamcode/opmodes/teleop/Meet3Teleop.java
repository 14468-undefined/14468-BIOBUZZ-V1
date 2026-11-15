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

@NextTeleop(name = "MEET 3 TELEOP")
public class Meet3Teleop extends NextOpMode {

    private final BaseRobot robot;
    private  int pollenVel = 3000;
    private int nectarVel = 1350;
    private double pollenTurretPos = .57;
    // for manual hardcoding



    public Meet3Teleop(BaseRobot robot) {
        super(robot);//        super(robot, BulkReadHook.INSTANCE);
        this.robot = robot;
        Scheduler.reset();
    }
    private Command increaseNectarVel(int increment) {
        return instant(() -> nectarVel += increment);
    }
    private Command decreaseNectarVel(int increment) {
        return instant(() -> nectarVel -= increment);
    }

    private Command increasePollenVel(int increment) {
        return instant(() -> pollenVel += increment);
    }
    private Command decreasePollenVel(int increment) {
        return instant(() -> pollenVel -= increment);
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



    @Override
    public void start() {
        Trigger.Companion.getDefaultEventLoop().clear();//ADDED 11/1


        CommandGamepad g1 = new CommandGamepad(gamepad1);
        CommandGamepad g2 = new CommandGamepad(gamepad2);

        robot.init().schedule();
        //robot.startDriveFieldCentric(gamepad1);//TODO: Anthony swap with field centric if you'd like but wait til we test this first
        robot.startDriveRobotCentric(gamepad1);
        robot.shooters.setState(ShootersSubsystem.ShooterState.MANUAL).schedule();
        //robot.getFollower().setPose(AutoToTeleopPoseStorage.getEndPoseOr(new Pose(0, 0, 0)));

        robot.turrets.setState(TurretsSubsystem.TurretState.MANUAL2).schedule();
        //intake
        g1.rightBumper().onTrue(robot.intake.setSpeed(IntakeSubsystem.IntakeState.FORWARD));
        g1.rightBumper().onTrue(robot.transfer.setSpeed(TransferSubsystem.TransferState.REVERSE));

        g1.rightBumper().onFalse(robot.intake.setSpeed(IntakeSubsystem.IntakeState.OFF));
        g1.rightBumper().onFalse(robot.transfer.setSpeed(TransferSubsystem.TransferState.OFF));

        //purge intake
        g1.leftBumper().onTrue(robot.intake.setSpeed(IntakeSubsystem.IntakeState.REVERSE));
        g1.leftBumper().onTrue(robot.transfer.setSpeed(TransferSubsystem.TransferState.REVERSE));
        g1.leftBumper().onFalse(robot.intake.setSpeed(IntakeSubsystem.IntakeState.OFF));
        g1.leftBumper().onFalse(robot.transfer.setSpeed(TransferSubsystem.TransferState.OFF));

        g1.y().onTrue(instant(() -> {
            pollenVel = 2700;
            nectarVel = 1215;
        }));

        g1.x().onTrue(instant(() -> {
            pollenVel = 3000;
            nectarVel = 1350;
        }));




        g1.leftTrigger().isOver(0.1).onTrue(robot.shooters.spinNectar(nectarVel));
        g1.leftTrigger().isOver(0.1).onTrue(robot.shooters.spinPollen(pollenVel));
        g1.leftTrigger().isOver(0.1).onFalse(robot.shooters.stopBoth());


        g1.dpadLeft().onTrue(rotateTurretRight(0.01));

        g1.dpadRight().onTrue(rotateTurretLeft(.01));




        //shoot
        g1.rightTrigger().isOver(0.1).onTrue(robot.intake.setSpeed(IntakeSubsystem.IntakeState.SHOOT));
        g1.rightTrigger().isOver(0.1).onTrue(robot.transfer.setSpeed(TransferSubsystem.TransferState.SHOOT));

        g1.rightTrigger().isOver(0.1).onFalse(robot.intake.setSpeed(IntakeSubsystem.IntakeState.OFF));
        g1.rightTrigger().isOver(0.1).onFalse(robot.transfer.setSpeed(TransferSubsystem.TransferState.OFF));

        //flower
        g1.a().onTrue(robot.flower.setPose(FlowerSubsystem.FlowerState.DOWN));
        g1.b().onTrue(robot.flower.setPose(FlowerSubsystem.FlowerState.UP));


        //manual changing shooter power
        g1.dpadUp().onTrue(instant(() -> {
            pollenVel += 25;
            nectarVel = (int) (pollenVel * 0.45);
        }));

        g1.dpadDown().onTrue(instant(() -> {
            pollenVel -= 25;
            nectarVel = (int) (pollenVel * 0.45);
        }));

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
       // telemetry.addData("Target Velocity", velocity);
        // telemetry.addData("Turret position", robot.get)
        telemetry.addData("Pollen Motor Velocity", robot.getShooter().getPollenShooter().getEncoderVelocity().into(RotationsPerMinute));

    }

    @Override
    public void end(){
        AutoToTeleopPoseStorage.clear();//so it doesnt keep between runs

    }

}