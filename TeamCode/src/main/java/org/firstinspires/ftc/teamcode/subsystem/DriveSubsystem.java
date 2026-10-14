package org.firstinspires.ftc.teamcode.subsystem;


import org.firstinspires.ftc.teamcode.util.Config;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;

public class DriveSubsystem implements Mechanism {
    public DriveSubsystem(){
        frontRight.setDirection(NextMotor.Direction.REVERSE);
        backRight.setDirection(NextMotor.Direction.REVERSE);
    }
    public final NextMotor frontLeft = new NextMotor(RobotController.controlHub(), Config.leftFrontMotor);
    public final NextMotor frontRight = new NextMotor(RobotController.controlHub(), Config.rightFrontMotor);
    public final NextMotor backLeft = new NextMotor(RobotController.controlHub(), Config.leftBackMotor);
    public final NextMotor backRight = new NextMotor(RobotController.controlHub(), Config.rightBackMotor);




}