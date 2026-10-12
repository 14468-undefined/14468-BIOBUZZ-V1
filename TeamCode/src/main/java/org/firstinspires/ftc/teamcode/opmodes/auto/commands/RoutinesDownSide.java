package org.firstinspires.ftc.teamcode.opmodes.auto.commands;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.PathsAndPosesDownSide;
import org.firstinspires.ftc.teamcode.subsystem.BaseRobot;

import static com.pedropathing.ivy.groups.Groups.sequential;

public class RoutinesDownSide {
    private BaseRobot robot;
    private final Follower follower;
    private final PathsAndPosesDownSide paths;
    private final AutoCommands commands;

    public RoutinesDownSide(BaseRobot robot, PathsAndPosesDownSide paths, AutoCommands commands){
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