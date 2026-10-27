package org.firstinspires.ftc.teamcode.opmodes.auto.commands;

import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.opmodes.auto.paths.PathsAndPosesUpSide2TipSolo;
import org.firstinspires.ftc.teamcode.subsystem.BaseRobot;

public class RoutinesUpSide2TipSolo {
    private BaseRobot robot;
    private final Follower follower;
    private final PathsAndPosesUpSide2TipSolo paths;
    private final AutoCommands commands;

    public RoutinesUpSide2TipSolo(BaseRobot robot, PathsAndPosesUpSide2TipSolo paths, AutoCommands commands){
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
                preloadAuto(),
                commands.intakePath(paths.startPose_to_garden()),
                robot.launchPollen(),
                commands.runPath(paths.out_of_garden()),
                commands.runPath(paths.garden_to_hive()),
                commands.runPath(paths.under_through_hive()),
                robot.launchPollen(),
                commands.runPath(paths.line_up_for_flower_intake()),
                commands.intakePath(paths.flower_intake()).raceWith(),//TODO - add time here so it acc runs intake for long enough
                robot.launchPollen(),
                commands.runPath(paths.park_avoid_other_robot())


        );
    }


    public Command justPaths() {
        return sequential(
                preloadAuto(),
                commands.runPath(paths.startPose_to_garden()),
                commands.runPath(paths.out_of_garden()),
                commands.runPath(paths.garden_to_hive()),
                commands.runPath(paths.under_through_hive()),
                commands.runPath(paths.line_up_for_flower_intake()),
                commands.runPath(paths.flower_intake()).raceWith(),//TODO - add time here so it acc runs intake for long enough
                commands.runPath(paths.park_avoid_other_robot())


        );
    }


}