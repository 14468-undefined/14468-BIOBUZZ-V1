package org.firstinspires.ftc.teamcode.opmodes.auto;


/**
 * next FTC
 */
import dev.nextftc.robot.opmode.BulkReadHook;
import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;
import org.firstinspires.ftc.teamcode.subsystem.BaseRobot;

/**
 * pedro pathing
 */
import com.pedropathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import static com.pedropathing.api.Paths.*;
import com.pedropathing.paths.Path;

/**
 *
 */
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;//why error





@NextAutonomous(name = "Auto")//@NextAutonomous(name, group, preselectTeleop)

public class SampleAuto extends NextOpMode {


    /**
     * pedro stuff
     */
    private Follower follower; //PEDRO follower
    private final PoseFactory p = PoseFactory.degrees(); // add this
    // our poses
    private boolean isFollowerInitialized = false;//so only initializes once


    /**
     * poses - leave out here so can use anywhere
     */
    private final Pose startPose = p.of(24, 24, 0);
    private final Pose park = p.of(48, 48, 90);
    private final Pose controlPose = p.of(36, 60, 45);


    /**
     * paths
     */

    private Path park() {
        return line(startPose, park).linear(startPose, park);//straight path from start pose to park
    }
    private Path curvyPark() {
        return curve(startPose, controlPose, park).linear(startPose, park);//adds control pose in middle and curves
        //first and last points = start and end - the middle points it goes through
    }

    public SampleAuto(BaseRobot robot) {
        super(robot, BulkReadHook.INSTANCE);



    }

    @Override //ON INIT - Repeatedly
    public void disabledPeriodic(){

        if(!isFollowerInitialized) {
            Scheduler.reset();//ivy
            follower = Constants.create(hardwareMap);
            follower.setPose(startPose);

            isFollowerInitialized = true;

        }


    }
    @Override //ONCE - right after start button
    public void start(){

        schedule(follow(follower, park()));

        //driver1.a().onTrue(());

    }

    @Override //REPEATEDLY - while OpMode running
    public void periodic() {


    }

    @Override //ONCE - when OpMode ends
    public void end(){

    }
}