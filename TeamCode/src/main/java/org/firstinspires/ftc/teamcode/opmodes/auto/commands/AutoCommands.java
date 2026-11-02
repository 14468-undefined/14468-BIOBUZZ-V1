package org.firstinspires.ftc.teamcode.opmodes.auto.commands;


import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.opmodes.auto.paths.PathsAndPosesPark;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.PathsAndPosesUpSide2TipSolo;
import org.firstinspires.ftc.teamcode.subsystem.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystem.BaseRobot;

public class AutoCommands {
    private final Follower follower;
    private final PathsAndPosesPark paths;

    //private final PathsAndPosesUpSide2TipSolo paths2;


    private final BaseRobot robot;

    public AutoCommands(BaseRobot robot, Follower follower, PathsAndPosesUpSide2TipSolo paths){
        this.robot = robot;
        this.follower = follower;
        this.paths = paths;
    }

    public CommandBuilder runPath(Path path){
        return follow(follower, path);
    }

    public CommandBuilder intakePath(Path path){//intake while moving
        return parallel(runPath(path), robot.getIntake().setSpeed(IntakeSubsystem.IntakeState.FORWARD));
    }
}