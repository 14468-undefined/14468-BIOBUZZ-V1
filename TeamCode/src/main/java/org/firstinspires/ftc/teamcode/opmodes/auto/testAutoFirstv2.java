package org.firstinspires.ftc.teamcode.opmodes.auto;


import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;
import org.firstinspires.ftc.teamcode.opmodes.auto.commands.AutoCommands;
import org.firstinspires.ftc.teamcode.opmodes.auto.commands.RoutinesUpSide2TipSolo;
import org.firstinspires.ftc.teamcode.opmodes.auto.commands.RoutinesUpSide2TipSoloVersion2;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.PathsAndPosesUpSide2TipSolo;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.PathsAndPosesUpSide2Tipv2;
import org.firstinspires.ftc.teamcode.subsystem.BaseRobot;

import static com.pedropathing.ivy.Scheduler.schedule;

@NextAutonomous(name = "Thursday Auto")
public class testAutoFirstv2 extends NextOpMode {
    private final BaseRobot robot;
    private final RoutinesUpSide2TipSoloVersion2 routines;
    private final AutoCommands commands;
    private final PathsAndPosesUpSide2Tipv2 paths;
    private Command selectedRoutine;

    public testAutoFirstv2(BaseRobot robot) {
        super(robot);
        this.robot = robot;
        Scheduler.reset();

        paths = new PathsAndPosesUpSide2Tipv2();
        commands = new AutoCommands(robot, robot.getFollower(), paths);
        routines = new RoutinesUpSide2TipSoloVersion2(robot, paths, commands);

    }

    @Override
    public void start() {
        robot.getFollower().setPose(paths.start);
        schedule(routines.justPaths());
    }

    @Override
    public void periodic() {
        robot.getFollower().update();
        Scheduler.execute();
    }
}