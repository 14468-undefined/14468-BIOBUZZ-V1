package org.firstinspires.ftc.teamcode.opmodes.auto.executable;


import dev.nextftc.robot.opmode.NextAutonomous;
import org.firstinspires.ftc.teamcode.opmodes.auto.setup.BaseAuto;
import org.firstinspires.ftc.teamcode.subsystem.BaseRobot;

@NextAutonomous(name = "ShootPark")
public class ShootParkAuto extends BaseAuto {
    public ShootParkAuto(BaseRobot robot) {
        super(robot);
        setAutoCommand(runnables.ShootParkAuto());
    }

}