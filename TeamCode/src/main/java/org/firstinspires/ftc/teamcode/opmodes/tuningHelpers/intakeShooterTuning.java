package org.firstinspires.ftc.teamcode.opmodes.tuningHelpers;




import static dev.nextftc.units.Units.RotationsPerMinute;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.ivy.Scheduler;

import org.firstinspires.ftc.teamcode.subsystem.*;
import org.firstinspires.ftc.teamcode.util.Constants;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

@NextTeleop(name = "Intake Shooter Tuning", group = "1")
public class intakeShooterTuning extends NextOpMode {
    private final BaseRobot robot;
    private final TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

    public intakeShooterTuning(BaseRobot robot) {
        super(robot);
        this.robot = robot;
        Trigger.Companion.getDefaultEventLoop().clear();


    }
    @Override
    public void start(){
        CommandGamepad g1 = new CommandGamepad(gamepad1);


        robot.intake.setState(IntakeSubsystem.IntakeState.TUNING);
        robot.transfer.setState(TransferSubsystem.TransferState.TUNING);
        robot.shooters.stateSetter(ShootersSubsystem.ShooterState.TUNING);
        robot.turrets.setTurretState(TurretsSubsystem.TurretState.TUNING);

        //robot.shooters.getNectarShooter().setThrottle(1);

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