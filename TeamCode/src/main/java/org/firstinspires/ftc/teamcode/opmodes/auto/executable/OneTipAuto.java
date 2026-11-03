package org.firstinspires.ftc.teamcode.opmodes.auto.executable;


import dev.nextftc.robot.opmode.NextAutonomous;
import org.firstinspires.ftc.teamcode.opmodes.auto.setup.BaseAuto;
import org.firstinspires.ftc.teamcode.subsystem.BaseRobot;

@NextAutonomous(name = "OneTipAuto")
public class OneTipAuto extends BaseAuto {
    public OneTipAuto(BaseRobot robot) {
        super(robot);
        setAutoCommand(runnables.OneTipAuto());
    }

}