package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.hardware.Servo;

import dev.nextftc.robot.opmode.BulkReadHook;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.triggers.CommandGamepad;

import static com.pedropathing.ivy.commands.Commands.*;

/**
 * Standalone servo tester. No dependence on BaseRobot
 *
 * Controls:
 *   D-pad Up/Down    -> cycle which servo is selected
 *   D-pad Left/Right -> nudge selected servo position by STEP
 *   A / B / Y        -> snap selected servo to 0.0 / 0.5 / 1.0
 */
@NextTeleop(name = "Servo Test")
public class TestServoPositions extends NextOpMode {

    // Add every servo you want to test here, exactly as named in your hardware config.
    private static final String[] SERVO_NAMES = {
            "claw",
            "wrist",
            "arm"
    };

    private static final double STEP = 0.01; // position change per D-pad press

    private final Servo[] servos = new Servo[SERVO_NAMES.length];
    private final double[] positions = new double[SERVO_NAMES.length];
    private int selected = 0;

    private final CommandGamepad driver1 = new CommandGamepad(gamepad1);

    public TestServoPositions() {
        super(BulkReadHook.INSTANCE);

        for (int i = 0; i < SERVO_NAMES.length; i++) {
            servos[i] = hardwareMap.get(Servo.class, SERVO_NAMES[i]);
            positions[i] = servos[i].getPosition();
        }

        // Cycle selection
        driver1.dpadUp().onTrue(instant(() -> {
            selected = (selected + 1) % servos.length;
        }));
        driver1.dpadDown().onTrue(instant(() -> {
            selected = (selected - 1 + servos.length) % servos.length;
        }));

        // Nudge position
        driver1.dpadRight().onTrue(instant(this::increment));
        driver1.dpadLeft().onTrue(instant(this::decrement));

        // Jump to common reference points, handy for finding physical limits fast
        driver1.a().onTrue(instant(() -> setPosition(0.0)));
        driver1.b().onTrue(instant(() -> setPosition(0.5)));
        driver1.y().onTrue(instant(() -> setPosition(1.0)));
    }

    private void increment() {
        setPosition(Math.min(1.0, positions[selected] + STEP));
    }

    private void decrement() {
        setPosition(Math.max(0.0, positions[selected] - STEP));
    }

    private void setPosition(double pos) {
        positions[selected] = pos;
        servos[selected].setPosition(pos);
    }

    @Override
    public void start() {
        // nothing to do here for this opmode
    }

    @Override
    public void periodic() {
        telemetry.addData("Selected", SERVO_NAMES[selected] + "  (" + (selected + 1) + "/" + servos.length + ")");
        telemetry.addData("Position", "%.3f", positions[selected]);
        telemetry.addLine();
        telemetry.addLine("D-pad Up/Down: switch servo");
        telemetry.addLine("D-pad Left/Right: nudge +/- " + STEP);
        telemetry.addLine("A: 0.0   B: 0.5   Y: 1.0");
        telemetry.update();
    }
}