package org.firstinspires.ftc.teamcode.opmodes.auto.paths;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.api.Paths.through;


public class PathsAndPosesDownSide {
    private final PoseFactory poseFactory = PoseFactory.degrees();
    public PoseFactory getPoseFactory(){
        return poseFactory;
    }



    // MAKE POSES HERE
    public final Pose startPos = poseFactory.of(83, 8.65, 270);
    public final Pose leavePos = poseFactory.of(137.37, 30, 180);
    // MAKE PATHS HERE

    public Path startPos_to_leavePos() {
        return through(startPos, leavePos).constant(startPos);
    }

    public Path leavePos_to_startPos(){
        return line(leavePos, startPos).constant(leavePos);
    }
}