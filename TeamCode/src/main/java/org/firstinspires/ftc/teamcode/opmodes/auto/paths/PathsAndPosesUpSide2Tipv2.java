package org.firstinspires.ftc.teamcode.opmodes.auto.paths;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;


public class PathsAndPosesUpSide2Tipv2 {
    private final PoseFactory poseFactory = PoseFactory.degrees();

    public PoseFactory getPoseFactory() {
        return poseFactory;
    }


        private final Pose start = poseFactory.of(9.2406, 13.7755, 0);
        private final Pose path1 = poseFactory.of(21.458, 11.5651, 200);
        private final Pose path1Control1 = poseFactory.of(36.9834, 25.2765, 0);
        private final Pose point2Start = poseFactory.of(21.458, 11.5651, 200);
        private final Pose point2 = poseFactory.of(8.3301, 8.5522, 180);
        private final Pose point3Start = poseFactory.of(8.3301, 8.5522, 180);
        private final Pose point3 = poseFactory.of(17.3265, 14.4388, 180);
        private final Pose point4Start = poseFactory.of(17.3265, 14.4388, 180);
        private final Pose point4 = poseFactory.of(20.3254, 15.1052, 0);
        private final Pose point5 = poseFactory.of(9.3297, 9.6629, 0);
        private final Pose point6Start = poseFactory.of(9.3297, 9.6629, 0);
        private final Pose point6 = poseFactory.of(10.4403, 94.8516, 0);
        private final Pose point6Control1 = poseFactory.of(51.2187, 55.3402, 0);

        public Path path1() {
            return Paths.curve(start, path1Control1, path1).linear(start, path1);
        }

        public Path path2() {
            return Paths.line(point2Start, point2).linear(point2Start, point2);
        }

        public Path path3() {
            return Paths.line(point3Start, point3).linear(point3Start, point3);
        }

        public Path path4() {
            return Paths.line(point4Start, point4).linear(point4Start, point4);
        }

        public Path path5() {
            return Paths.line(point4, point5).constant(point5);
        }

        public Path path6() {
            return Paths.curve(point6Start, point6Control1, point6).linear(point6Start, point6);
        }


    // Autonomous routine



    //public Path leavePos_to_startPos(){
        //return line(leavePos, startPos).constant(leavePos);
    //}
}