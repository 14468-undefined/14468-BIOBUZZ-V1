package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.math.Pose;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.triggers.Trigger;
import org.firstinspires.ftc.teamcode.subsystem.*;
import org.firstinspires.ftc.teamcode.util.AutoToTeleopPoseStorage;

@NextTeleop(name = "FieldSim")
public class FieldSim extends NextOpMode {

    private final BaseRobot robot;


    private final TelemetryManager panelsTelemetry =
            PanelsTelemetry.INSTANCE.getTelemetry();

    // Panels field drawing helper
























































































    private final FieldDrawer fieldDrawer = new FieldDrawer();

    private final PoseFactory factory;

    public FieldSim(BaseRobot robot) {
        super(robot);
        this.robot = robot;
        factory = new PoseFactory(PoseFactory.Operation.IDENTITY, true);


        robot.getFollower().stop();
    }

    @Override
    public void start() {
        Pose startPose = factory.of(72, 72, 0);

        robot.getFollower().setPose(startPose);
        robot.getFollower().stop();
        Trigger.Companion.getDefaultEventLoop().clear();//ADDED 11/1


        robot.init().schedule();
    }

    @Override
    public void periodic() {
        ManualDrive.driveOrHold(
                robot.getFollower(),
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x
        );

        robot.getFollower().update();
        //ManualDrive.driveOrHold(robot.getFollower(), powers);

        // Get current robot pose
        Pose pose = robot.getFollower().pose();

        // Update Panels field drawing
        fieldDrawer.update(pose);

        // Update telemetry
        panelsTelemetry.addData("X", pose.x());
        panelsTelemetry.addData("Y", pose.y());
        panelsTelemetry.addData("Heading", pose.heading());
        panelsTelemetry.update(telemetry);
    }

    @Override
    public void end() {
        AutoToTeleopPoseStorage.clear();
    }
}