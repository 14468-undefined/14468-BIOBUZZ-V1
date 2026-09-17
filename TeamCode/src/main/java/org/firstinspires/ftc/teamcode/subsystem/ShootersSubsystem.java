package org.firstinspires.ftc.teamcode.subsystem;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import org.firstinspires.ftc.teamcode.util.Constants;
import org.firstinspires.ftc.teamcode.util.FieldState;

public class ShootersSubsystem implements Mechanism {

    private final NextMotor nectarShooter = new NextMotor("nectarShooter");
    private final NextMotor pollenShooter = new NextMotor("pollenShooter");


    private final FieldState fieldState;

    private FieldState.GoalSide pollenSide = FieldState.GoalSide.GARDEN;
    private FieldState.GoalSide nectarSide = FieldState.GoalSide.LOADING_ZONE;

    public ShootersSubsystem(FieldState fieldState) {
        this.fieldState = fieldState;
    }

    public void setPollenGoalSide(FieldState.GoalSide side) {
        this.pollenSide = side;
    }

    public void setNectarGoalSide(FieldState.GoalSide side) {
        this.nectarSide = side;
    }

    public double getPollenShooterDistanceToGoal() {
        return fieldState.distanceToGoal(Constants.TurretConstants.POLLEN_TURRET_FORWARD_OFFSET, Constants.TurretConstants.POLLEN_TURRET_LATERAL_OFFSET);
    }

    public double getNectarShooterDistanceToGoal() {
        return fieldState.distanceToGoal(Constants.TurretConstants.NECTAR_TURRET_FORWARD_OFFSET, Constants.TurretConstants.NECTAR_TURRET_LATERAL_OFFSET);
    }

    //TODO
    //add PID and distance getter method to determine RPM
    //LUT as well - maybe make it in its own class



    @Override
    public void periodic() {
    }


}