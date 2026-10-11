package org.firstinspires.ftc.teamcode.subsystem;

import com.pedropathing.ivy.Command;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;


public class IntakeSubsystem implements Mechanism {
    private final NextMotor motor = new NextMotor("intake");

    @Override
    public void periodic() {
    }

    public Command run(double power) {
        return Command.build()
                .setStart(() -> motor.setThrottle(power))
                .setDone(() -> false)
                .setEnd(endCondition -> motor.setThrottle(0.0))
                .requiring(this);
    }

    public Command runReverse(double power) {
        return Command.build()
                .setStart(() -> motor.setThrottle(-power))
                .setDone(() -> false)
                .setEnd(endCondition -> motor.setThrottle(0.0))
                .requiring(this);
    }

    public Command stop() {
        return instant(() -> motor.setThrottle(0.0));
    }
}
