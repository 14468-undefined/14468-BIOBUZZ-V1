package org.firstinspires.ftc.teamcode.subsystem;


import androidx.annotation.NonNull;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.Gamepad;
import dev.nextftc.hardware.RobotController;
import dev.nextftc.robot.NextRobot;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.drive.DriveCommands;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystem.old.FlowerScorerSubsystem;
import org.firstinspires.ftc.teamcode.util.FieldState;


import java.util.Set;

import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;

public class BaseRobot implements NextRobot {
    private Follower follower;

    public final FieldState fieldState = new FieldState();

    //public final BrakesSubsystem brakes = new BrakesSubsystem();
    public final FlowerScorerSubsystem flowerScorer = new FlowerScorerSubsystem();
    public final GatesSubsystem gates = new GatesSubsystem();
    public final IntakeSubsystem intake = new IntakeSubsystem();
    public final LimelightSubsystem limelight = new LimelightSubsystem();
    public final SensorsSubsystem sensors = new SensorsSubsystem();

    public final ShootersSubsystem shooters = new ShootersSubsystem(fieldState);
    public final TurretsSubsystem turrets = new TurretsSubsystem(fieldState);
    public final TransferSubsystem transfer = new TransferSubsystem();
    public final DriveSubsystem drive = new DriveSubsystem();

    public BaseRobot(){}




    public Follower getFollower() {//TODO: actually integrate this follower into opmod3es
        if (follower == null) {
            follower = Constants.create(RobotController.hardwareMap());
        }

        return follower;
    }
    public void startDriveRobotCentric(Gamepad gamepad1) {
        DriveCommands.mecanumDrive(
                drive.frontLeft,
                drive.frontRight,
                drive.backLeft,
                drive.backRight,
                gamepad1
        ).schedule();
    }

    public void startDriveFieldCentric(Gamepad gamepad1) {
        DriveCommands.mecanumDriveFieldCentric(
                drive.frontLeft,
                drive.frontRight,
                drive.backLeft,
                drive.backRight,
                gamepad1,
                () -> follower.pose().heading()
        ).schedule();
    }



    public IntakeSubsystem getIntake() {
        return intake;
    }
    public ShootersSubsystem getShooter() {
        return shooters;
    }

    public Command init(){
        return parallel(
                //DO THINGS HERE FOR INITIALIZATION
        );
    }

    public Command launchBoth(){
        return sequential(
                intake.setSpeed(IntakeSubsystem.IntakeState.OFF)
        );
    }

    public Command launchPollen(){
        return sequential(
                intake.setSpeed(IntakeSubsystem.IntakeState.OFF)
        );
    }
    public Command launchNectar(){
        return sequential(
                intake.setSpeed(IntakeSubsystem.IntakeState.OFF)
        );


    }

    @NonNull
    @Override
    public Set<Mechanism> getMechanisms() {
        return Set.of(
               //brakes,
                flowerScorer,
                gates,
                intake,
                limelight,
                sensors,
                shooters,
                turrets,
                transfer,
                drive

        );
    }

    //TODO - put pose and field logic in here so that i can get distance in hood and shooter and turret class without recoding it all
}