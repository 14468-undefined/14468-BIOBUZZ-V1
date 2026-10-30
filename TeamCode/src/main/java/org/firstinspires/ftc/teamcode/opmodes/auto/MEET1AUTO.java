package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.util.ElapsedTime;

import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;
import org.firstinspires.ftc.teamcode.subsystem.BaseRobot;

@NextAutonomous(name = "Forward Back Auto")
public class MEET1AUTO extends NextOpMode {
    private static final double POWER = 0.2;
    private static final double MOVE_SECONDS = 2.0;

    private final BaseRobot robot;
    private final ElapsedTime timer = new ElapsedTime();

    public MEET1AUTO(BaseRobot robot) {
        super(robot);
        this.robot = robot;
    }


    @Override
    public void start() {
        timer.reset();
    }

    @Override
    public void periodic() {
        if (timer.seconds() < MOVE_SECONDS) {
            robot.drive.setPower(-POWER);
        } else {
            robot.drive.setPower(0);
        }
    }
}