package org.firstinspires.ftc.teamcode.subsystem;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;

public class ShootersSubsystem implements Mechanism {

    private final NextMotor nectarShooter = new NextMotor("nectarShooter");
    private final NextMotor pollenShooter = new NextMotor("pollenShooter");


    //TODO
    //add PID and distance getter method to determine RPM
    //LUT as well - maybe make it in its own class
    @Override
    public void periodic() {
    }
}