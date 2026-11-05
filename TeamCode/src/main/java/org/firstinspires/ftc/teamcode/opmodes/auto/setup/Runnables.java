package org.firstinspires.ftc.teamcode.opmodes.auto.setup;

import static com.pedropathing.ivy.commands.Commands.conditional;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.ivy.Command;
import com.pedropathing.paths.Path;
import org.firstinspires.ftc.teamcode.subsystem.*;


public class Runnables {

	private final BaseRobot robot;
	public final PathsAndPoses paths;

	public Command decidePath(boolean condition, Path option1, Path option2) {
		return conditional(() -> condition, robot.moveToPoint(option1), robot.moveToPoint(option2));
	}

	public Runnables(BaseRobot robot) {
		this.robot = robot;
		this.paths = new PathsAndPoses();
	}

	/* FRONT AUTOS (We are shooting first) */

	public Command leaveAuto(boolean isFront) {
		return decidePath(isFront, paths.leavePoses.path1, paths.leavePoses.path2);
	}

	public Command preloadAuto(boolean isFront) {
		return sequential(
				robot.moveToPoint(paths.leavePoses.path3),
				waitMs(1000),
				decidePath(isFront, paths.leavePoses.path4FrontLeavePose, paths.leavePoses.path4BackLeavePose)
		);
	}


	public Command SquareAuto(){
		return sequential(
				robot.moveToPoint(paths.squarePoses.path1),
				robot.moveToPoint(paths.squarePoses.path2),

				robot.moveToPoint(paths.squarePoses.path3),
				robot.moveToPoint(paths.squarePoses.path4)
		);
	}

	public Command OneTipAuto(){
		return sequential(
				/*robot.moveToPoint(paths.oneTipPoses.path1flower()),
				robot.moveToPoint(paths.oneTipPoses.path12flower()),


				robot.moveToPoint(paths.oneTipPoses.path2Flower()),
				robot.intake.setSpeed(IntakeSubsystem.IntakeState.FORWARD),
				robot.moveToPoint(paths.oneTipPoses.path3Flower()),
				robot.moveToPoint(paths.oneTipPoses.path4Flower()),
				robot.intake.setSpeed(IntakeSubsystem.IntakeState.OFF),

				 */
                //SPIN UP SHOOTERS
				robot.shooters.setState(ShootersSubsystem.ShooterState.CLOSE),

                //DRIVE FORWARD AND SHOOT
				robot.moveToPoint(paths.oneTipPoses.path1()),

				robot.transfer.setSpeed(TransferSubsystem.TransferState.SHOOT),
				robot.intake.setSpeed(IntakeSubsystem.IntakeState.SHOOT),
				waitMs(3000),
				robot.transfer.setSpeed(TransferSubsystem.TransferState.OFF),
				robot.intake.setSpeed(IntakeSubsystem.IntakeState.OFF),

                //GO TO FLOWER AND INTAKE
				robot.flower.setPose(FlowerSubsystem.FlowerState.DOWN),
				robot.moveToPoint(paths.oneTipPoses.path2()),
				robot.intake.setSpeed(IntakeSubsystem.IntakeState.FORWARD),
				waitMs(4000),
				robot.intake.setSpeed(IntakeSubsystem.IntakeState.OFF),

                //GO TO FAR SIDE AND SHOOT
				robot.moveToPoint(paths.oneTipPoses.flowerToMid()),
				robot.moveToPoint(paths.oneTipPoses.toFarShot()),

                robot.transfer.setSpeed(TransferSubsystem.TransferState.SHOOT),
                robot.intake.setSpeed(IntakeSubsystem.IntakeState.SHOOT),
                waitMs(3000),
                robot.transfer.setSpeed(TransferSubsystem.TransferState.OFF),
                robot.intake.setSpeed(IntakeSubsystem.IntakeState.OFF),

                //GO TO FAR FLOWER AND INTAKE
                robot.flower.setPose(FlowerSubsystem.FlowerState.DOWN),
                robot.moveToPoint(paths.oneTipPoses.toFarFlower()),
                robot.intake.setSpeed(IntakeSubsystem.IntakeState.FORWARD),
                waitMs(4000),
                robot.intake.setSpeed(IntakeSubsystem.IntakeState.OFF),

                //SHOOT AGAIN
                robot.moveToPoint(paths.oneTipPoses.farFlowerToShot()),
                robot.flower.setPose(FlowerSubsystem.FlowerState.UP),
                robot.moveToPoint(paths.oneTipPoses.farFlowerToShot()),

                robot.transfer.setSpeed(TransferSubsystem.TransferState.SHOOT),
                robot.intake.setSpeed(IntakeSubsystem.IntakeState.SHOOT),
                waitMs(3000),
                robot.transfer.setSpeed(TransferSubsystem.TransferState.OFF),
                robot.intake.setSpeed(IntakeSubsystem.IntakeState.OFF),

                //PARK
                robot.moveToPoint(paths.oneTipPoses.park())


				);
	}


}