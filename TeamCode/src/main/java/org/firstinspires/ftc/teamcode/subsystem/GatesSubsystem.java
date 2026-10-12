package org.firstinspires.ftc.teamcode.subsystem;

import com.pedropathing.ivy.Command;
import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;
import org.firstinspires.ftc.teamcode.util.Config;
import org.firstinspires.ftc.teamcode.util.Constants;

public class GatesSubsystem implements Mechanism {

    private GateState gateState = GateState.CLOSED;
    public enum GateState {
        CLOSED,
        OPEN,
        TUNING
    }


    private void setState(GateState gateState) {
        this.gateState = gateState;
    }
    public Command setPose(GateState s) {
        return instant(() -> this.setState(s));
    }

    public GateState getGateState(){
        return gateState;
    }
    private final double nectar_closed = 0.5;
    private final double pollen_closed = 0.5;

    private final double nectar_open = 1.0;
    private final double pollen_open = 1.0;
    private final double nectar_tuning_manual = 0;//TODO
    private final double pollen_tuning_manual = 0;//TODO



    private final NextServo nectarGate = new NextServo(RobotController.expansionHub(), Config.nectarGateServo);
    private final NextServo pollenGate = new NextServo(RobotController.controlHub(), Config.pollenGateServo);


    public GatesSubsystem(){
        gateState = GateState.CLOSED;//initialize off

    }
    @Override
    public void periodic() {

        switch (gateState) {
            case CLOSED:
                nectarGate.setPosition(nectar_closed);
                pollenGate.setPosition(pollen_closed);
                break;
            case OPEN:
                nectarGate.setPosition(nectar_open);
                pollenGate.setPosition(pollen_open);
                break;
            case TUNING:
                nectarGate.setPosition(nectar_tuning_manual);
                pollenGate.setPosition(pollen_tuning_manual);
                break;
        }
    }
}