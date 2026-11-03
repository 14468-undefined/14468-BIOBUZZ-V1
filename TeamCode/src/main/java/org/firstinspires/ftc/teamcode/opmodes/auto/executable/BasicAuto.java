package org.firstinspires.ftc.teamcode.opmodes.auto.executable;


import dev.nextftc.robot.opmode.NextAutonomous;
import org.firstinspires.ftc.teamcode.opmodes.auto.setup.BaseAuto;
import org.firstinspires.ftc.teamcode.subsystem.BaseRobot;

@NextAutonomous(name = "Leave Auto")
public class BasicAuto extends BaseAuto {
	public BasicAuto(BaseRobot robot) {
		super(robot);
		setAutoCommand(runnables.leaveAuto(false));
	}

}