package org.firstinspires.ftc.teamcode.opmodes.auto.commands;

import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.opmodes.auto.paths.PathsAndPosesUpSide;
import org.firstinspires.ftc.teamcode.subsystem.BaseRobot;

public class RoutinesUpSide {
    private BaseRobot robot;
    private final Follower follower;
    private final PathsAndPosesUpSide paths;
    private final AutoCommands commands;

    public RoutinesUpSide(BaseRobot robot, PathsAndPosesUpSide paths, AutoCommands commands){
        this.robot = robot;
        follower = robot.getFollower();
        this.paths = paths;
        this.commands = commands;
    }

    public Command leaveAuto() {
        return sequential(
                commands.runPath(paths.startPos_to_leavePos())
        );
    }

    public Command preloadAuto() {
        return sequential(
                robot.launch(),
                commands.intakePath(paths.startPos_to_leavePos())
        );
    }


    public Command fullCycleAuto() {
        return sequential(
                preloadAuto(),
                commands.runPath(paths.leavePos_to_startPos()),
                robot.launch(),
                commands.runPath(paths.startPos_to_leavePos())
        );
    }


}