package org.firstinspires.ftc.teamcode.opmodes.auto.commands;


import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.subsystem.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.PathsAndPosesUpSide;
import org.firstinspires.ftc.teamcode.subsystem.BaseRobot;

public class AutoCommands {
    private final Follower follower;
    private final PathsAndPosesUpSide paths;
    private final BaseRobot robot = new BaseRobot();

    public AutoCommands(Follower follower, PathsAndPosesUpSide paths){
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