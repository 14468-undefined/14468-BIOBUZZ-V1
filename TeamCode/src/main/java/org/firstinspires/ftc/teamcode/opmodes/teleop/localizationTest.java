package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;
import org.firstinspires.ftc.teamcode.subsystem.*;
import org.firstinspires.ftc.teamcode.util.AutoToTeleopPoseStorage;

import static com.pedropathing.ivy.commands.Commands.instant;
import static dev.nextftc.units.Units.RotationsPerMinute;

@NextTeleop(name = "Localization Test")
public class localizationTest extends NextOpMode {

    private final BaseRobot robot;
    // for manual hardcoding



    public localizationTest(BaseRobot robot) {
        super(robot);//        super(robot, BulkReadHook.INSTANCE);
        this.robot = robot;

    }



    @Override
    public void start() {
        robot.getFollower().setPose(new Pose(56, 9.1107, 90));//start

    }

    @Override
    public void periodic() {
        robot.getFollower().update();


        telemetry.addData("Robot X", robot.getCurrentPose().x());
        telemetry.addData("Robot Y", robot.getCurrentPose().y());
        telemetry.addData("Robot Heading", robot.getCurrentPose().heading());
        telemetry.update();

    }

    @Override
    public void end(){

    }

}