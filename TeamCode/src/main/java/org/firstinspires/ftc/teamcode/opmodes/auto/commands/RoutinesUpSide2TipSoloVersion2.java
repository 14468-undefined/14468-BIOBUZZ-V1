package org.firstinspires.ftc.teamcode.opmodes.auto.commands;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.PathsAndPosesUpSide2TipSolo;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.PathsAndPosesUpSide2Tipv2;
import org.firstinspires.ftc.teamcode.subsystem.BaseRobot;
import org.firstinspires.ftc.teamcode.subsystem.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystem.ShootersSubsystem;
import org.firstinspires.ftc.teamcode.subsystem.TransferSubsystem;

import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

public class RoutinesUpSide2TipSoloVersion2 {
    private BaseRobot robot;
    private final Follower follower;
    private final PathsAndPosesUpSide2Tipv2 paths;
    private final AutoCommands commands;

    public RoutinesUpSide2TipSoloVersion2(BaseRobot robot, PathsAndPosesUpSide2Tipv2 paths, AutoCommands commands){
        this.robot = robot;
        follower = robot.getFollower();
        this.paths = paths;
        this.commands = commands;
    }



    public Command preloadAuto() {
        return sequential(
                robot.launchPollen()
        );
    }


    public Command fullCycleAuto() {
        return sequential(
                robot.shooters.setState(ShootersSubsystem.ShooterState.MANUAL),
                robot.shooters.setRpm(3750),
                waitMs(2000),
                robot.intake.setSpeed(IntakeSubsystem.IntakeState.SHOOT),
                robot.transfer.setSpeed(TransferSubsystem.TransferState.SHOOT),
                waitMs(3000),
                robot.intake.setSpeed(IntakeSubsystem.IntakeState.OFF),
                robot.transfer.setSpeed(TransferSubsystem.TransferState.OFF),
                robot.shooters.setRpm(0),
                commands.intakePath(paths.path1()),
                commands.intakePath(paths.path3()),
                commands.runPath(paths.path4()),
                commands.runPath(paths.path5()),

                commands.runPath(paths.path6()),
                robot.shooters.setRpm(3750),
                waitMs(2000),
                robot.intake.setSpeed(IntakeSubsystem.IntakeState.SHOOT),
                robot.transfer.setSpeed(TransferSubsystem.TransferState.SHOOT),
                waitMs(3000),
                robot.intake.setSpeed(IntakeSubsystem.IntakeState.OFF),
                robot.transfer.setSpeed(TransferSubsystem.TransferState.OFF),
                robot.shooters.setRpm(0),
                commands.runPath(paths.path7())


        );
    }

    public Command justPaths() {
        return sequential(
                commands.runPath(paths.path1()),
                commands.runPath(paths.path2()),
                commands.runPath(paths.path3()),
                commands.runPath(paths.path4()),
                commands.runPath(paths.path5()),
                commands.runPath(paths.path6()),
                commands.runPath(paths.path7())
        );
    }


}