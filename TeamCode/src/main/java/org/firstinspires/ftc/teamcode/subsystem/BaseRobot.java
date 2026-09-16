package org.firstinspires.ftc.teamcode.subsystem;

import dev.nextftc.core.subsystems.Mechanism;
import dev.nextftc.core.subsystems.NextRobot;

import java.util.Set;

public class BaseRobot implements NextRobot {



    @Override
    public Set<Mechanism> getMechanisms() {
        return Set.of(
                drive,
                gate,
                hood,
                intake,
                limelight,
                shooter,
                turret,
                led
        );
    }
}