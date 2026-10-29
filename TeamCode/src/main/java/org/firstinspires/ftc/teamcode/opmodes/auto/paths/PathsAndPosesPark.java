package org.firstinspires.ftc.teamcode.opmodes.auto.paths;
import static com.pedropathing.api.Paths.*;
import com.pedropathing.api.Paths;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
public class PathsAndPosesPark {


        private final PoseFactory poseFactory = PoseFactory.degrees();

    public PoseFactory getPoseFactory(){
        return poseFactory;
    }

        public final Pose start = poseFactory.of(14, 133, 90);
        private final Pose path1 = poseFactory.of(14, 120, 90);

        public Path park() {
            return Paths.line(start, path1).constant(path1);
        }
    }


