package org.firstinspires.ftc.teamcode.subsystem;

import com.pedropathing.ivy.Command;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import org.firstinspires.ftc.teamcode.util.Constants;
import org.firstinspires.ftc.teamcode.util.FieldState;

public class ShootersSubsystem implements Mechanism {

    private final NextMotor nectarShooter = new NextMotor("nectar_shooter");
    private final NextMotor pollenShooter = new NextMotor("pollen_shooter");


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
    //add PIDF and distance getter method to determine RPM
    //LUT as well - maybe make it in its own class

    public Command spinNectar() {
        return instant(() -> nectarShooter.setThrottle(.5));
    }
    public Command spinPollen() {
        return instant(() -> pollenShooter.setThrottle(.5));
    }

    public Command stopPollen(){
        return instant(() -> pollenShooter.setThrottle(0));
    }
    public Command stopNectar(){
        return instant(() -> nectarShooter.setThrottle(0));
    }




    @Override
    public void periodic() {
    }


}