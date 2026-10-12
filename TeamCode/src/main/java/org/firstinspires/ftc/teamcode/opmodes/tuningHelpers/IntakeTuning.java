package org.firstinspires.ftc.teamcode.opmodes.tuningHelpers;

import com.pedropathing.ivy.Scheduler;

import org.firstinspires.ftc.teamcode.subsystem.BaseRobot;
import org.firstinspires.ftc.teamcode.subsystem.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystem.TransferSubsystem;
import org.firstinspires.ftc.teamcode.subsystem.TurretsSubsystem;
import org.firstinspires.ftc.teamcode.util.Constants;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

@NextTeleop(name = "Intake Tuning", group = "1")
public class IntakeTuning extends NextOpMode {
    private final BaseRobot robot;

    public IntakeTuning(BaseRobot robot) {
        super(robot);
        this.robot = robot;
        Trigger.Companion.getDefaultEventLoop().clear();
    }

    @Override
    public void start() {
        CommandGamepad g1 = new CommandGamepad(gamepad1);
        robot.startDriveRobotCentric(gamepad1);//TODO: anthony swap with field centric if you'd like but wait til we test this first

        // TUNING state reads TUNING_POLLEN_ANGLE / TUNING_NECTAR_ANGLE every loop,
        // so edit them live in Panels and watch the turret chase them.
        g1.rightTrigger().isOver(.1).onTrue(robot.intake.setSpeed(IntakeSubsystem.IntakeState.FORWARD));
        g1.rightTrigger().isOver(.1).onFalse(robot.intake.setSpeed(IntakeSubsystem.IntakeState.TUNING));
        g1.rightBumper().onTrue(robot.intake.setSpeed(IntakeSubsystem.IntakeState.FORWARD));
        g1.rightBumper().onFalse(robot.intake.setSpeed(IntakeSubsystem.IntakeState.TUNING));

        g1.leftTrigger().isOver(.1).onTrue(robot.transfer.setSpeed(TransferSubsystem.TransferState.FORWARD));
        g1.leftTrigger().isOver(.1).onFalse(robot.transfer.setSpeed(TransferSubsystem.TransferState.TUNING));
        g1.leftBumper().onTrue(robot.transfer.setSpeed(TransferSubsystem.TransferState.FORWARD));
        g1.leftBumper().onFalse(robot.transfer.setSpeed(TransferSubsystem.TransferState.TUNING));



    }

    @Override
    public void periodic() {

        telemetry.addData("Intake Power", Constants.IntakeConstants.INTAKE_TUNING_POWER);        telemetry.addData("Intake Power", Constants.IntakeConstants.INTAKE_TUNING_POWER);
        telemetry.addData("Transfer Power", Constants.IntakeConstants.TRANSFER_TUNING_POWER);


        telemetry.update();
    }

    @Override
    public void end() {
        Scheduler.reset();
    }
}