package org.firstinspires.ftc.teamcode.subsystem;

import com.pedropathing.ivy.Command;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;


public class IntakeSubsystem implements Mechanism {
    private final NextMotor motor = new NextMotor("intake");

    @Override
    public void periodic() {
    }

    public Command intake() {
        return instant(() -> motor.setThrottle(1.0));
    }

    public Command intakeReverse() {
        return instant(() -> motor.setThrottle(-1.0));
    }

    public Command stop() {
        return instant(() -> motor.setThrottle(0.0));
    }
}
