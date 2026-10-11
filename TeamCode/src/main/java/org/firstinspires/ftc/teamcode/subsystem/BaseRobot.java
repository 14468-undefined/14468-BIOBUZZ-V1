package org.firstinspires.ftc.teamcode.subsystem;


import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import dev.nextftc.robot.NextRobot;
import dev.nextftc.robot.Mechanism;
import org.firstinspires.ftc.teamcode.util.FieldState;


import java.util.Set;

import static com.pedropathing.ivy.groups.Groups.sequential;

public class BaseRobot implements NextRobot {
    private Follower follower;

    public final FieldState fieldState = new FieldState();

    public final BrakesSubsystem brakes = new BrakesSubsystem();
    public final FlowerScorerSubsystem flowerScorer = new FlowerScorerSubsystem();
    public final GatesSubsystem gates = new GatesSubsystem();
    public final IntakeSubsystem intake = new IntakeSubsystem();
    public final LimelightSubsystem limelight = new LimelightSubsystem();
    public final SensorsSubsystem sensors = new SensorsSubsystem();

    public final ShootersSubsystem shooters = new ShootersSubsystem(fieldState);
    public final TurretsSubsystem turrets = new TurretsSubsystem(fieldState);

    public BaseRobot(){}



    public Command launch(){
        return sequential(
                intake.setSpeed(IntakeSubsystem.IntakeState.OFF),
                shooters.auto();//auto spinup
        )


    }

    @Override
    public Set<Mechanism> getMechanisms() {
        return Set.of(
               brakes,
                flowerScorer,
                gates,
                intake,
                limelight,
                sensors,
                shooters,
                turrets

        );
    }

    //TODO - put pose and field logic in here so that i can get distance in hood and shooter and turret class without recoding it all
}