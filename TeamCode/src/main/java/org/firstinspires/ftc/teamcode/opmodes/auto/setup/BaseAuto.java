package org.firstinspires.ftc.teamcode.opmodes.auto.setup;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;


import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;
import org.firstinspires.ftc.teamcode.subsystem.BaseRobot;
import org.firstinspires.ftc.teamcode.subsystem.FlowerSubsystem;

@NextAutonomous(name = "Base Auto")
@Disabled
public class BaseAuto extends NextOpMode {
	protected final BaseRobot robot;
	protected Runnables runnables;
	protected Pose startingPose;
	protected Command autoCommand;

	protected BaseAuto(BaseRobot robot) {
		super(robot);
		this.robot = robot;

		this.runnables = new Runnables(robot);
		this.startingPose = runnables.paths.oneTipPoses.start;
		Scheduler.reset();
	}

	public void setAutoCommand(Command command) {
		this.autoCommand = command;
	}

	@Override
	public void start() {
		robot.flower.setPose(FlowerSubsystem.FlowerState.UP).schedule();
		robot.getFollower().setPose(startingPose);
		if (autoCommand != null) {
			autoCommand.schedule();
		}
	}

	@Override
	public void periodic() {
		robot.getFollower().update();
		telemetry.addData("POSE", robot.getFollower().pose());
		//Scheduler.execute();//11/12/26

	}
}