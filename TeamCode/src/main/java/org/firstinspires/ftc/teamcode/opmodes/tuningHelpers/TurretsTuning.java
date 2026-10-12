package org.firstinspires.ftc.teamcode.opmodes.tuningHelpers;

import com.pedropathing.ivy.Scheduler;

import org.firstinspires.ftc.teamcode.subsystem.BaseRobot;
import org.firstinspires.ftc.teamcode.subsystem.TurretsSubsystem;
import org.firstinspires.ftc.teamcode.util.Constants;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

@NextTeleop(name = "Turret Tuning", group = "1")
public class TurretsTuning extends NextOpMode {
    private final BaseRobot robot;

    public TurretsTuning(BaseRobot robot) {
        super(robot);
        this.robot = robot;
        Trigger.Companion.getDefaultEventLoop().clear();
    }

    @Override
    public void start() {
        CommandGamepad g1 = new CommandGamepad(gamepad1);

        // TUNING state reads TUNING_POLLEN_ANGLE / TUNING_NECTAR_ANGLE every loop,
        // so edit them live in Panels and watch the turret chase them.
        robot.turrets.setState(TurretsSubsystem.TurretState.TUNING);

        // Quick kill switch / re-enable

    }

    @Override
    public void periodic() {
        double pollenTarget = Constants.TurretConstants.TUNING_POLLEN_ANGLE;
        double nectarTarget = Constants.TurretConstants.TUNING_NECTAR_ANGLE;
        double pollenCurrent = robot.turrets.getPollenTurretCurrentAngle();
        double nectarCurrent = robot.turrets.getNectarTurretCurrentAngle();

        telemetry.addData("Pollen target (deg)", pollenTarget);
        telemetry.addData("Pollen current (deg)", pollenCurrent);
        telemetry.addData("Pollen error (deg)", pollenTarget - pollenCurrent);

        telemetry.addData("Nectar target (deg)", nectarTarget);
        telemetry.addData("Nectar current (deg)", nectarCurrent);
        telemetry.addData("Nectar error (deg)", nectarTarget - nectarCurrent);

        telemetry.addData("Pollen servo raw (deg)", robot.turrets.getPollenServoAngleDeg());
        telemetry.addData("Nectar servo raw (deg)", robot.turrets.getNectarServoAngleDeg());

        telemetry.addLine("A = OFF, B = TUNING");
        telemetry.update();
    }

    @Override
    public void end() {
        Scheduler.reset();
    }
}