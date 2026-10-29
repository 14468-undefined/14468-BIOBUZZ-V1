package org.firstinspires.ftc.teamcode.opmodes.auto;


import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;
import org.firstinspires.ftc.teamcode.opmodes.auto.commands.AutoCommands;
import org.firstinspires.ftc.teamcode.opmodes.auto.commands.RoutinesPark;
import org.firstinspires.ftc.teamcode.opmodes.auto.commands.RoutinesUpSide2TipSolo;
import org.firstinspires.ftc.teamcode.opmodes.auto.commands.RoutinesUpSide2TipSoloVersion2;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.PathsAndPosesPark;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.PathsAndPosesUpSide2TipSolo;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.PathsAndPosesUpSide2Tipv2;
import org.firstinspires.ftc.teamcode.subsystem.BaseRobot;

import static com.pedropathing.ivy.Scheduler.schedule;

@NextAutonomous(name = "Park")

public class ParkAuto extends NextOpMode {
    private final BaseRobot robot;
    private final RoutinesPark routines;
    private final AutoCommands commands;
    private final PathsAndPosesPark paths;
    private Command selectedRoutine;

    public ParkAuto(BaseRobot robot) {
        super(robot);
        this.robot = robot;
        Scheduler.reset();

        paths = new PathsAndPosesPark();
        commands = new AutoCommands(robot, robot.getFollower(), paths);
        routines = new RoutinesPark(robot, paths, commands);

    }

    @Override
    public void start() {
        robot.getFollower().setPose(paths.start);
        schedule(routines.park());
    }

    @Override
    public void periodic() {
        robot.getFollower().update();
        Scheduler.execute();
    }
}