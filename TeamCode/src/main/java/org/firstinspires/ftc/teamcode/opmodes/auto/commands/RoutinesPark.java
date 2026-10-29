package org.firstinspires.ftc.teamcode.opmodes.auto.commands;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.PathsAndPosesPark;
import org.firstinspires.ftc.teamcode.subsystem.BaseRobot;

import static com.pedropathing.ivy.groups.Groups.sequential;

public class RoutinesPark {
    private BaseRobot robot;
    private final Follower follower;
    private final PathsAndPosesPark paths;
    private final AutoCommands commands;

    public RoutinesPark(BaseRobot robot, PathsAndPosesPark paths, AutoCommands commands){
        this.robot = robot;
        follower = robot.getFollower();
        this.paths = paths;
        this.commands = commands;
    }

    public Command park() {
        return sequential(
                commands.runPath(paths.park())
        );
    }


}
