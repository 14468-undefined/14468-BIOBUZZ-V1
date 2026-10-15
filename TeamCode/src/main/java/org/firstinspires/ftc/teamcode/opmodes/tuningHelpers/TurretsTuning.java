package org.firstinspires.ftc.teamcode.opmodes.tuningHelpers;

import com.pedropathing.ivy.Scheduler;

import org.firstinspires.ftc.teamcode.subsystem.BaseRobot;
import org.firstinspires.ftc.teamcode.subsystem.TurretsSubsystem;
import org.firstinspires.ftc.teamcode.util.Constants;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.Trigger;


@NextTeleop(name = "Turret Tuning", group = "1")
public class TurretsTuning extends NextOpMode {
    private final BaseRobot robot;

    private static final double SWEEP_SECONDS = 5.0;

    private long startNanos;

    public TurretsTuning(BaseRobot robot) {
        super(robot);
        this.robot = robot;
        Trigger.Companion.getDefaultEventLoop().clear();
    }

    @Override
    public void start() {
        startNanos = System.nanoTime();
        robot.turrets.setTurretState(TurretsSubsystem.TurretState.TUNING);
    }

    @Override
    public void periodic() {
        double t = (System.nanoTime() - startNanos) / 1e9;
        double phase = (t % (2 * SWEEP_SECONDS)) / SWEEP_SECONDS;
        double position = phase <= 1 ? phase : 2 - phase;

        Constants.TurretConstants.TUNING_POLLEN_POSITION = position;
        Constants.TurretConstants.TUNING_NECTAR_POSITION = position;

        telemetry.addData("Sweep position", position);
        telemetry.addData("Pollen commanded", robot.turrets.getPollenPosition());
        telemetry.addData("Nectar commanded", robot.turrets.getNectarPosition());
        telemetry.update();
    }

    @Override
    public void end() {
        Scheduler.reset();
    }
}