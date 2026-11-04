package org.firstinspires.ftc.teamcode.subsystem.old;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;
import org.firstinspires.ftc.teamcode.util.Config;

public class HoodsSubsystem implements Mechanism {

    private final NextServo nectarHood = new NextServo(RobotController.controlHub(), Config.nectarHoodServo);
    private final NextServo pollenHood = new NextServo(RobotController.controlHub(), Config.pollenHoodServo);


    //TODO:
    //add look-up table and interpolation
    //add distance from goal getter method
    //add method to set angle of hood (in relation to bottom position ig, so not angle but servo position)
    @Override
    public void periodic() {

    }
}