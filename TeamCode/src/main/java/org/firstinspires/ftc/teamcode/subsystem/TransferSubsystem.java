package org.firstinspires.ftc.teamcode.subsystem;

import com.pedropathing.ivy.Command;
import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.units.measuretypes.AngularVelocity;
import org.firstinspires.ftc.teamcode.util.Config;
import org.firstinspires.ftc.teamcode.util.Constants;


public class TransferSubsystem implements Mechanism {
    private final NextMotor transferMotor = new NextMotor(RobotController.expansionHub(), Config.transfer);

    private TransferState transferState;
    public enum TransferState {
        FORWARD,
        REVERSE,
        SHOOT,
        OFF,
        CUSTOM,
        TUNING
    }

    private final double transferNormal = 0.5;
    private final double transferShoot = 1.0;
    private final double reverse = -1;
    private final double off = 0.0;
    private double custom = 0.5;

    public TransferSubsystem(){
        transferState = TransferState.OFF;//initialize off

        transferMotor.setDirection(NextMotor.Direction.FORWARD);//TODO:check
    }



    public void setState(TransferState transferState) {
        this.transferState = transferState;
    }
    public Command setSpeed(TransferState s) {
        return instant(() -> this.setState(s));
    }

    public Command customSpeed(double p) {
        return instant(() -> this.setCustomSpeed(p));
    }
    public void setCustomSpeed(double p){
        custom = p;
        setState(TransferState.CUSTOM);

    }


    public double getSpeed(){
        return transferMotor.getThrottle();
    }


    @Override
    public void periodic() {
        switch (transferState) {
            case FORWARD:
                transferMotor.setThrottle(transferNormal);
                break;
            case REVERSE:
                transferMotor.setThrottle(reverse);
                break;
            case SHOOT:
                transferMotor.setThrottle(transferShoot);
                break;
            case OFF:
                transferMotor.setThrottle(off);
                break;
            case TUNING:
                transferMotor.setThrottle(Constants.IntakeConstants.TRANSFER_TUNING_POWER);
                break;


            case CUSTOM:
                transferMotor.setThrottle(custom);
                break;
        }
    }

}
