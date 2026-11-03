package org.firstinspires.ftc.teamcode.opmodes.auto.setup;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;


public class PathsAndPoses {

	private final PoseFactory factory;

	public final LeavePoses leavePoses;
	public final SquarePoses squarePoses;
	public final OneTipPoses oneTipPoses;
	public PathsAndPoses() {
		factory = new PoseFactory(PoseFactory.Operation.IDENTITY, true);

		squarePoses = new SquarePoses();
		oneTipPoses = new OneTipPoses();
		leavePoses = new LeavePoses();

	}

	public static Path createLinearPath(Pose p1, Pose p2) {
		return Paths.line(p1, p2).linear(p1, p2);
	}

	public static Path createCurvyPath(Pose p1, Pose control, Pose p2) {
		return Paths.curve(p1, control, p2).linear(p1, p2);
	}

	public class SquarePoses {
		public final Pose corner1 = factory.of(0, 0, 0);
		public final Pose corner2 = factory.of(10, 0, 0);
		public final Pose corner3 = factory.of(10, 10, 0);
		public final Pose corner4 = factory.of(0, 10, 0);

		public final Path path1 = createLinearPath(corner1, corner2);
		public final Path path2 = createLinearPath(corner2, corner3);
		public final Path path3 = createLinearPath(corner3, corner4);
		public final Path path4 = createLinearPath(corner4, corner1);

	}

	public class OneTipPoses {

		public final Pose start = factory.of(56.2221, 9.1107, 90);
		public final Pose point1 = factory.of(14.6609, 46.7946, -179.9842);
		public final Pose point1Control1 = factory.of(56.2106, 24.9549, 0);
		public final Pose point1Control2 = factory.of(47.892, 46.8038, 0);
		public final Pose point2 = factory.of(56.6374, 9.3461, 89.7477);
		public final Pose point2Control1 = factory.of(48.3487, 47.5487, 0);
		public final Pose point2Control2 = factory.of(56.7023, 24.0776, 0);
		public final Pose point3 = factory.of(15.3276, 93.648, 90);
		public final Pose point3Control1 = factory.of(25.7373, 19.6649, 0);

		public Path path1() {
			return Paths.curve(start, point1Control1, point1Control2, point1).tangent();
		}

		public Path path2() {
			return Paths.curve(point1, point2Control1, point2Control2, point2).reverseTangent();
		}

		public Path path3() {
			return Paths.curve(point2, point3Control1, point3).constant(point3);
		}
	}


	public class LeavePoses {
		public final Pose startingPose = factory.of(30.43, 7.48, 180);
		public final Pose midPose = factory.of(9.24, 7.30, 180);
		public final Pose control = factory.of(25.96, 56.11, 180);
		public final Pose frontLeavePose = factory.of(10.34, 117.81, 180);
		public final Pose backLeavePose = factory.of(10.11, 95.7, 90);

		public final Path path1 = createLinearPath(startingPose, frontLeavePose);
		public final Path path2 = createLinearPath(startingPose, backLeavePose);
		public final Path path3 = createLinearPath(startingPose, midPose);
		public final Path path4FrontLeavePose = createCurvyPath(midPose, control, frontLeavePose);
		public final Path path4BackLeavePose = createCurvyPath(midPose, control, backLeavePose);
	}
}