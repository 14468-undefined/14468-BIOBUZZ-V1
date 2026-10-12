package org.firstinspires.ftc.teamcode.opmodes.auto.paths;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.api.Paths.through;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;


public class PathsAndPosesUpSide {
    private final PoseFactory poseFactory = PoseFactory.degrees();

    public PoseFactory getPoseFactory() {
        return poseFactory;
    }


    // MAKE POSES HERE
    public final Pose startPos = poseFactory.of(0, 0, 0);//heading should be toward garden
    public final Pose shotPose = poseFactory.of(0, 0, 0);
    public final Pose gardenPose = poseFactory.of(0, 0, 0);

    // MAKE PATHS HERE

    public Path startPose_to_shotPose() {
        return through(startPos, shotPose).constant(startPos);
    }

    public Path shotPose_to_Garden() {
        return through(shotPose, gardenPose);
    }

    public Path gardenPose_to_shotPose() {
        return through(gardenPose, shotPose);
    }





    //public Path leavePos_to_startPos(){
        //return line(leavePos, startPos).constant(leavePos);
    //}
}