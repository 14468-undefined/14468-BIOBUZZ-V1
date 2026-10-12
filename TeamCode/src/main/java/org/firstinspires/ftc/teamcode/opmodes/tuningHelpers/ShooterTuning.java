package org.firstinspires.ftc.teamcode.opmodes.tuningHelpers;




import static dev.nextftc.units.Units.RotationsPerMinute;

import com.pedropathing.ivy.Scheduler;

import org.firstinspires.ftc.teamcode.subsystem.BaseRobot;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;
import org.firstinspires.ftc.teamcode.subsystem.ShootersSubsystem;

@NextTeleop(name = "Shooter Tuning", group = "1")
public class ShooterTuning extends NextOpMode {
    private final BaseRobot robot;

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
        telemetry.addData("Pollen (RPM)", robot.getShooter().getPollenShooter().getEncoderVelocity().into(RotationsPerMinute));
        telemetry.addData("Nectar (RPM)", robot.getShooter().getNectarShooter().getEncoderVelocity().into(RotationsPerMinute));

        telemetry.update();
    }

    @Override
    public void end() {
        Scheduler.reset();
    }

}