package org.firstinspires.ftc.teamcode.subsystem;


import dev.nextftc.robot.NextRobot;
import dev.nextftc.robot.Mechanism;


import java.util.Set;

public class BaseRobot implements NextRobot {

    public final BrakesSubsystem brakes = new BrakesSubsystem();
    public final FlowerScorerSubsystem flowerScorer = new FlowerScorerSubsystem();
    public final GatesSubsystem gates = new GatesSubsystem();
    public final IntakeSubsystem intake = new IntakeSubsystem();
    public final LimelightSubsystem limelight = new LimelightSubsystem();
    public final SensorsSubsystem sensors = new SensorsSubsystem();

    public final ShootersSubsystem shooters = new ShootersSubsystem();
    public final TurretsSubsystem turrets = new TurretsSubsystem();


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
}