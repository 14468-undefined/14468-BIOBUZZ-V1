package org.firstinspires.ftc.teamcode.opmodes.auto;

import static com.pedropathing.ivy.Scheduler.schedule;

import com.pedropathing.ivy.Scheduler;

import org.firstinspires.ftc.teamcode.opmodes.auto.commands.AutoCommands;
import org.firstinspires.ftc.teamcode.opmodes.auto.commands.RoutinesUpSide2TipSolo;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.PathsAndPosesUpSide2TipSolo;
import org.firstinspires.ftc.teamcode.subsystem.BaseRobot;

import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

@NextAutonomous(name = "Test Auto First")
public class testAutoFirst extends NextOpMode {
    private final BaseRobot robot;
    private final RoutinesUpSide2TipSolo routines;
    private final AutoCommands commands;
    private final PathsAndPosesUpSide2TipSolo paths;

    public testAutoFirst(BaseRobot robot) {
        super(robot);
        this.robot = robot;

        // Init-equivalent: runs every time INIT is pressed
        Scheduler.reset();

        paths = new PathsAndPosesUpSide2TipSolo();
        commands = new AutoCommands(robot, robot.getFollower(), paths);
        routines = new RoutinesUpSide2TipSolo(robot, paths, commands);

        robot.getFollower().setPose(paths.start_pose);//MOVED 11/1
        robot.getFollower().update();
    }

    @Override
    public void start() {
        schedule(routines.justPaths());
    }

    @Override
    public void periodic() {
        robot.getFollower().update();
        Scheduler.execute();
    }

    @Override
    public void end() {
        Scheduler.reset();//ADDED 11/1

    }
}