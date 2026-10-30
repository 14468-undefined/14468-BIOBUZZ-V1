package org.firstinspires.ftc.teamcode.subsystem;


import androidx.annotation.NonNull;
import com.pedropathing.drivetrain.DrivePowers;
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

import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
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
    public final FlowerSubsystem flower = new FlowerSubsystem();

    public final ShootersSubsystem shooters = new ShootersSubsystem(fieldState);
    public final TurretsSubsystem turrets = new TurretsSubsystem(fieldState);
    public final TransferSubsystem transfer = new TransferSubsystem();
    public final DriveSubsystem drive = new DriveSubsystem();

    public BaseRobot(){
        fieldState.setPoseSupplier(() -> getFollower().pose());
    }




    public Follower getFollower() {
        if (follower == null) {
            follower = Constants.create(RobotController.hardwareMap());
        }

        return follower;
    }
    public void startDriveRobotCentric(Gamepad gamepad1) {
        if(follower == null){
            getFollower();
        }
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
                () -> getFollower().pose().heading()
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
                gates.setPose(GatesSubsystem.GateState.CLOSED),
                shooters.setState(ShootersSubsystem.ShooterState.OFF),
                intake.setSpeed(IntakeSubsystem.IntakeState.OFF),
                transfer.setSpeed(TransferSubsystem.TransferState.OFF),
                turrets.setState(TurretsSubsystem.TurretState.OFF),
                flower.setPose(FlowerSubsystem.FlowerState.UP)

        );
    }

    public Command launchBoth(){
        return sequential(
                intake.setSpeed(IntakeSubsystem.IntakeState.OFF)
        );
    }

    public Command teleSpinUpShooters() {
        return sequential(
                transfer.setSpeed(TransferSubsystem.TransferState.OFF),
                intake.setSpeed(IntakeSubsystem.IntakeState.OFF),
                gates.setPose(GatesSubsystem.GateState.CLOSED),
                shooters.setState(ShootersSubsystem.ShooterState.AUTO)
        );
    }
    public Command launchPollen(){
        return sequential(
                //PREPARE
                intake.setSpeed(IntakeSubsystem.IntakeState.OFF),//INTAKE OFF
                gates.setPose(GatesSubsystem.GateState.CLOSED),//GATES CLOSED
                shooters.setState(ShootersSubsystem.ShooterState.AUTO),//SHOOTERS ON //TODO: make a second state setter for each individual shooter
                //waitMs(100),//TODO: if periodic is too fast, add a wait here to let the shooter set the speed before checking if at speed
                shooters.waitTilAtSpeed(),//WAIT

                //SHOOT
                gates.setPose(GatesSubsystem.GateState.OPEN),//GATES OPEN
                intake.setSpeed(IntakeSubsystem.IntakeState.SHOOT),//INTAKE ON
                transfer.setSpeed(TransferSubsystem.TransferState.SHOOT),//TRANSFER ON
                waitMs(2000),//WAIT 2 sec

                //EVERYTHING OFF
                transfer.setSpeed(TransferSubsystem.TransferState.OFF),
                intake.setSpeed(IntakeSubsystem.IntakeState.OFF),
                gates.setPose(GatesSubsystem.GateState.CLOSED),
                shooters.setState(ShootersSubsystem.ShooterState.OFF)
        );
    }
    public Command launchNectar(){
        return sequential(
                intake.setSpeed(IntakeSubsystem.IntakeState.OFF)
        );


    }


    public Command teleIntake() {
        return Command.build()
                .setStart(() -> {
                    gates.setPose(GatesSubsystem.GateState.CLOSED).schedule();
                    intake.setSpeed(IntakeSubsystem.IntakeState.FORWARD).schedule();
                    transfer.setSpeed(TransferSubsystem.TransferState.FORWARD).schedule();
                })
                .setDone(() -> false)
                .setEnd(endCondition -> {
                    intake.setSpeed(IntakeSubsystem.IntakeState.OFF).schedule();
                    transfer.setSpeed(TransferSubsystem.TransferState.OFF).schedule();
                });
    }

    public Command telePurgeIntake() {
        return Command.build()
                .setStart(() -> {
                    gates.setPose(GatesSubsystem.GateState.CLOSED).schedule();
                    intake.setSpeed(IntakeSubsystem.IntakeState.REVERSE).schedule();
                    transfer.setSpeed(TransferSubsystem.TransferState.REVERSE).schedule();
                })
                .setDone(() -> false)
                .setEnd(endCondition -> {
                    intake.setSpeed(IntakeSubsystem.IntakeState.OFF).schedule();
                    transfer.setSpeed(TransferSubsystem.TransferState.OFF).schedule();
                });
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
                drive,
                flower

        );
    }

    //TODO - put pose and field logic in here so that i can get distance in hood and shooter and turret class without recoding it all

}