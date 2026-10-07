package org.firstinspires.ftc.teamcode.subsystem;

import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;

public class HoodsSubsystem implements Mechanism {

    private final NextServo nectarHood = new NextServo("nectar_hood");
    private final NextServo pollenHood = new NextServo("pollen_hood");


    //TODO:
    //add look-up table and interpolation
    //add distance from goal getter method
    //add method to set angle of hood (in relation to bottom position ig, so not angle but servo position)
    @Override
    public void periodic() {

    }
}