package org.firstinspires.ftc.teamcode.opmodes.auto.executable;

import com.qualcomm.robotcore.util.ElapsedTime;

import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;
import org.firstinspires.ftc.teamcode.subsystem.BaseRobot;

@NextAutonomous(name = "PARK (RUN) Auto")
public class park extends NextOpMode {
    private static final double POWER = 0.4;
    private static final double MOVE_SECONDS = 5.0;

    private final BaseRobot robot;
    private final ElapsedTime timer = new ElapsedTime();

    public park(BaseRobot robot) {
        super(robot);
        this.robot = robot;
    }

    @Override
    public void start() {
        timer.reset();
    }

    @Override
    public void periodic() {
        double t = timer.seconds();

        if (t < MOVE_SECONDS) {
            robot.drive.setPower(POWER);
        }
        else {
            robot.drive.setPower(0);
        }
    }
}