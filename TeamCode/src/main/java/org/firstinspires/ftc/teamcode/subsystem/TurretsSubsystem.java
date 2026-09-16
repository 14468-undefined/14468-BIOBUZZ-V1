package org.firstinspires.ftc.teamcode.subsystem;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;

public class TurretsSubsystem implements Mechanism {

    private final NextMotor pollenTurret = new NextMotor("pollenTurret");
    private final NextMotor nectarTurret = new NextMotor("nectarTurret");

    @Override
    public void periodic() {
    }
}