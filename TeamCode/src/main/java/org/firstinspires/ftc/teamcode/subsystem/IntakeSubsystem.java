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

<<<<<<< HEAD
    public Command runReverse(double power) {
        return Command.build()
                .setStart(() -> motor.setThrottle(-power))
                .setDone(() -> false)
                .setEnd(endCondition -> motor.setThrottle(0.0))
                .requiring(this);
=======
    public Command setIntakePower(double value) {return instant(() -> motor.setThrottle(value));}


    public Command intakeReverse() {
        return instant(() -> motor.setThrottle(-1.0));
>>>>>>> 1fed42eae6e72d26e8a48a71f1959a9fe81e19b2
    }

    public Command stop() {
        return instant(() -> motor.setThrottle(0.0));
    }
}
