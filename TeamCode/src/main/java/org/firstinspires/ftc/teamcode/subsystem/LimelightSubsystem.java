package org.firstinspires.ftc.teamcode.subsystem;

import com.pedropathing.ivy.Command;
import dev.nextftc.hardware.webcams.NextLimelight;
import dev.nextftc.robot.Mechanism;

public class LimelightSubsystem implements Mechanism {

    private final NextLimelight limelight = new NextLimelight("limelight");

    private static final int DEFAULT_PIPELINE = 0;
    private static final int POLL_RATE_HZ = 100;

    public void initialize() {
        limelight.startReading(DEFAULT_PIPELINE, POLL_RATE_HZ);
    }

    public Command initializeLimelight() {
        return instant(() -> limelight.startReading(
                DEFAULT_PIPELINE,
                POLL_RATE_HZ
        ));
    }

    public Command setPipeline(int pipeline) {
        return instant(() -> limelight.getCamera().pipelineSwitch(pipeline));
    }

    public Command stopLimelight() {
        return instant(() -> limelight.getCamera().stop());
    }

    public double getTy() {
        return limelight.getTY();
    }

    public double getTx() {
        return limelight.getTX();
    }

    public boolean hasTarget() {
        return limelight.getLatestResult().isValid();//TODO: idk if this is right
    }

    @Override
    public void periodic() {
        // NextLimelight handles reading the camera.
    }
}