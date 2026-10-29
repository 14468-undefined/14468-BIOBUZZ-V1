package org.firstinspires.ftc.teamcode.subsystem;

import com.pedropathing.ivy.Command;
import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;
import org.firstinspires.ftc.teamcode.util.Config;
import org.firstinspires.ftc.teamcode.util.Constants;

import static org.firstinspires.ftc.teamcode.util.Constants.FlowerConstants.TUNING_FLOWER_MECH;

public class FlowerSubsystem implements Mechanism {

    private FlowerState flowerState = FlowerState.UP;
    public enum FlowerState {
        DOWN,
        UP,
        TUNING
    }


    private void setState(FlowerState flowerState) {
        this.flowerState = flowerState;
    }
    public Command setPose(FlowerState s) {
        return instant(() -> this.setState(s));
    }

    public FlowerState getFlowerState(){
        return flowerState;
    }
    private final double tuning_manual = 0;
    private final double DOWN = 0.12;//TODO: Tune
    private final double UP = 0.9;//TODO: Tune



    //private final NextServo nectarGate = new NextServo(RobotController.controlHub(), Config.nectarGateServo);

    //private final NextServo pollenGate = new NextServo(RobotController.controlHub(), Config.pollenGateServo);
    private final NextServo flowerServo = new NextServo(RobotController.controlHub(), Config.flowerMechServo);


    public FlowerSubsystem(){
        flowerState = FlowerState.UP;//initialize off

    }
    @Override
    public void periodic() {

        switch (flowerState) {
            case DOWN:
                flowerServo.setPosition(DOWN);
                break;
            case UP:
                flowerServo.setPosition(UP);
                break;
            case TUNING:
                flowerServo.setPosition(TUNING_FLOWER_MECH);
                break;
        }
    }
}