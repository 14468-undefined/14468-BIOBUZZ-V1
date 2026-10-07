package org.firstinspires.ftc.teamcode.subsystem;

import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.control.feedback.PIDController;
import dev.nextftc.hardware.actuators.NextCRServo;
import dev.nextftc.hardware.actuators.NextFeedbackCRServo;
import dev.nextftc.hardware.sensors.NextAnalogInput;
import dev.nextftc.robot.Mechanism;
import org.firstinspires.ftc.teamcode.util.Constants;
import org.firstinspires.ftc.teamcode.util.FieldState;

import static org.firstinspires.ftc.teamcode.util.Constants.TurretConstants.NECTAR_TURRET_LATERAL_OFFSET;

public class TurretsSubsystem implements Mechanism {

    private final NextFeedbackCRServo pollenTurret1 = new NextFeedbackCRServo("pollen_turret1", new NextAnalogInput("pollenTurret"));
    private final NextCRServo pollenTurret2 = new NextCRServo("pollen_turret2");
    private final NextFeedbackCRServo nectarTurret1 = new NextFeedbackCRServo("nectar_turret1", new NextAnalogInput("nectarTurret"));
    private final NextCRServo nectarTurret2 = new NextCRServo("nectar_turret2");

    private final PIDController pollenPID = new PIDController(new PIDCoefficients(Constants.TurretConstants.POLLEN_KP, Constants.TurretConstants.POLLEN_KI, Constants.TurretConstants.POLLEN_KD));
    private final PIDController nectarPID = new PIDController(new PIDCoefficients(Constants.TurretConstants.NECTAR_KP, Constants.TurretConstants.NECTAR_KI, Constants.TurretConstants.NECTAR_KD));
    //TODO: pidf? or js PID


    private final FieldState fieldState;

    private FieldState.GoalSide goalSide = FieldState.GoalSide.GARDEN;
    private FieldState.Alliance alliance = FieldState.Alliance.RED;


    public TurretsSubsystem(FieldState fieldState) {
        this.fieldState = fieldState;
    }


    public double getPollenTurretTargetAngle() {
        return fieldState.bearingToGoalDeg(Constants.TurretConstants.POLLEN_TURRET_FORWARD_OFFSET, Constants.TurretConstants.POLLEN_TURRET_LATERAL_OFFSET);
    }

    public double getNectarTurretTargetAngle() {
        return fieldState.bearingToGoalDeg(Constants.TurretConstants.NECTAR_TURRET_FORWARD_OFFSET, Constants.TurretConstants.NECTAR_TURRET_LATERAL_OFFSET);
    }



    public double getNectarTurretCurrentAngle(){
        //TODO - make sure it aligns with the unit circle stuff where 0 is straight ahead
        return 0;
    }
    public double getPollenTurretCurrentAngle(){
        //TODO - make sure it aligns with the unit circle stuff where 0 is straight ahead
        return 0;
    }

        //TODO: idle state or just set to 0 when don't want PID active -
        // also something to not let it wrap around TOO MANY TIMES AND COOK WIRES
    public double pollenTurretTargetPower(){
        double output = pollenPID.calculate(getPollenTurretTargetAngle() - getPollenTurretCurrentAngle());
        return output;
    }
    public double nectarTurretTargetPower(){
        double output = nectarPID.calculate(getNectarTurretTargetAngle() - getNectarTurretCurrentAngle());
        return output;
    }


    @Override
    public void periodic() {
        nectarTurret1.setPower(nectarTurretTargetPower());
        nectarTurret2.setPower(nectarTurretTargetPower());

        pollenTurret1.setPower(pollenTurretTargetPower());
        pollenTurret2.setPower(pollenTurretTargetPower());


    }
}