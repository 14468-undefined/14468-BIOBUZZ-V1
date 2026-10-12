package org.firstinspires.ftc.teamcode.opmodes.tuningHelpers;




import static dev.nextftc.units.Units.RotationsPerMinute;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.ivy.Scheduler;

import org.firstinspires.ftc.teamcode.subsystem.BaseRobot;
import org.firstinspires.ftc.teamcode.util.Constants;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;
import org.firstinspires.ftc.teamcode.subsystem.ShootersSubsystem;

@NextTeleop(name = "Shooter Tuning", group = "1")
public class ShooterTuning extends NextOpMode {
    private final BaseRobot robot;
    private final TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

    public ShooterTuning(BaseRobot robot) {
        super(robot);
        this.robot = robot;
        Trigger.Companion.getDefaultEventLoop().clear();


    }
    @Override
    public void start(){
        CommandGamepad g1 = new CommandGamepad(gamepad1);


        robot.shooters.setState(ShootersSubsystem.ShooterState.TUNING);

    }


    @Override
    public void periodic() {
        double pollenRpm = robot.getShooter().getPollenShooter().getEncoderVelocity().into(RotationsPerMinute);
        double nectarRpm = robot.getShooter().getNectarShooter().getEncoderVelocity().into(RotationsPerMinute);
        double pollenTarget = Constants.ShooterConstants.POLLEN_TUNING_RPM;
        double nectarTarget = Constants.ShooterConstants.NECTAR_TUNING_RPM;

        telemetry.addData("Pollen (RPM)", pollenRpm);
        telemetry.addData("Nectar (RPM)", nectarRpm);

        panelsTelemetry.addData("pollenTarget", pollenTarget);
        panelsTelemetry.addData("pollenActual", pollenRpm);
        panelsTelemetry.addData("nectarTarget", nectarTarget);
        panelsTelemetry.addData("nectarActual", nectarRpm);

        panelsTelemetry.update(telemetry);
    }

    @Override
    public void end() {
        Scheduler.reset();
    }

}