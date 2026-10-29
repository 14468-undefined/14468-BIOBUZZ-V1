package org.firstinspires.ftc.teamcode.opmodes.auto.paths;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

public class PathsAndPosesUpSide2Tipv2 {
    private final PoseFactory poseFactory = PoseFactory.degrees();

    public PoseFactory getPoseFactory() {
        return poseFactory;
    }

    public final Pose start = poseFactory.of(0, 0, 0);

    private final Pose turnPoint   = poseFactory.of(17.6, 0, 180);
    private final Pose lineUp      = poseFactory.of(27, -33, 200.5);   // 3.5 rad
    private final Pose intake1     = poseFactory.of(21.7, -24.88, 237.8); // 4.15 rad
    private final Pose intake2     = poseFactory.of(14, -21, 256.1);   // 4.47 rad
    private final Pose intake3     = poseFactory.of(10, -20.5, 270.4); // 4.72 rad
    private final Pose moveOut     = poseFactory.of(4.03, -14.64, 270.4); // 4.72 rad
    private final Pose turnAround  = poseFactory.of(-6.2, -24.22, 0);

    private Path lineLinear(Pose from, Pose to) {
        return Paths.line(from, to).linear(from, to);
    }

    public Path path1() { return lineLinear(start, turnPoint); }
    public Path path2() { return lineLinear(turnPoint, lineUp); }
    public Path path3() { return lineLinear(lineUp, intake1); }
    public Path path4() { return lineLinear(intake1, intake2); }
    public Path path5() { return lineLinear(intake2, intake3); }
    public Path path6() { return lineLinear(intake3, moveOut); }
    public Path path7() { return lineLinear(moveOut, turnAround); }
}