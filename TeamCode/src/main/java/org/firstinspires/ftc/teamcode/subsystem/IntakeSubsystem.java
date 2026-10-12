package org.firstinspires.ftc.teamcode.subsystem;

import com.pedropathing.ivy.Command;
import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.units.measuretypes.AngularVelocity;
import org.firstinspires.ftc.teamcode.util.Config;


public class IntakeSubsystem implements Mechanism {
    private final NextMotor intakeMotor = new NextMotor(RobotController.expansionHub(), Config.intake);

    private IntakeState intakeState;
    public enum IntakeState {
        FORWARD,
        REVERSE,
        SHOOT,
        OFF,
        CUSTOM
    }

    private final double intakeNormal = 0.5;
    private final double intakeShoot = 1.0;
    private final double reverse = -1.0;
    private final double off = 0.0;
    private double custom = 0.5;

    public IntakeSubsystem(){
        intakeState = IntakeState.OFF;//initialize off

        intakeMotor.setDirection(NextMotor.Direction.FORWARD);//TODO:check
    }



    private void setState(IntakeState intakeState) {
        this.intakeState = intakeState;
    }
    public Command setSpeed(IntakeState s) {
        return instant(() -> this.setState(s));
    }

    public Command customSpeed(double p) {
        return instant(() -> this.setCustomSpeed(p));
    }
    public void setCustomSpeed(double p){
        custom = p;
        setState(IntakeState.CUSTOM);

    }


    public double getSpeed(){
        return intakeMotor.getThrottle();
    }


    @Override
    public void periodic() {
        switch (intakeState) {
            case FORWARD:
                intakeMotor.setThrottle(intakeNormal);
                break;
            case REVERSE:
                intakeMotor.setThrottle(reverse);
                break;
            case SHOOT:
                intakeMotor.setThrottle(intakeShoot);
                break;
            case OFF:
                intakeMotor.setThrottle(off);
                break;


            case CUSTOM:
                intakeMotor.setThrottle(custom);
                break;
        }
    }

}
