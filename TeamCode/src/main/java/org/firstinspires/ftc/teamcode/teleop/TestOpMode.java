package org.firstinspires.ftc.teamcode.teleop;


import dev.nextftc.robot.opmode.BulkReadHook;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;
import org.firstinspires.ftc.teamcode.subsystem.BaseRobot;

@NextTeleop(name = "TeleOp")//@NextAutonomous(name, group, preselectTeleop)
public class TestOpMode extends NextOpMode {

    CommandGamepad driver1 = new CommandGamepad(gamepad1);
    CommandGamepad driver2 = new CommandGamepad(gamepad2);

    public TestOpMode(BaseRobot robot) {
        super(robot, BulkReadHook.INSTANCE);



    }

    @Override //ON INIT - Repeatedly
    public void disabledPeriodic(){

    }
    @Override //ONCE - right after start button
    public void start(){

        //driver1.a().onTrue(());

    }

    @Override //REPEATEDLY - while OpMode running
    public void periodic() {


    }

    @Override //ONCE - when OpMode ends
    public void end(){

    }
}