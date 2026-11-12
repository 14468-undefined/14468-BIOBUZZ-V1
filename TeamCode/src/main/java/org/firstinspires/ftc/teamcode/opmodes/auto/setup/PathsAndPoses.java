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

		public final Pose start = factory.of(56, 9.1107, 90);//start
		private final Pose path1 = factory.of(58, 15.75, 90);//move forward slightly
		private final Pose point2 = factory.of(33.5, 48, 180);//flower intake
		private final Pose point2Modified = factory.of(50, 48, 180);//flower intake
		private final Pose point3Modified = factory.of(28, 36, 270);//flower intake


		private final Pose lineUpCornerIntake = factory.of(28, 28, 270);
		private final Pose cornerIntake = factory.of(20, 17.5, 270);
		private final Pose cornerIntakeTurn = factory.of(18.09, 15.23, 285);
		private final Pose backUpFromCorner = factory.of(40, 40, 285);
		private final Pose point2ButTurned = factory.of(40, 25, 270);


		/*
		flower intake stuff
		line up behind corner intake - 15.97, 23.05
		corner intake - 21.44, 13.33, 5.1464 radians
		turn away - 18.09, 15.23, 5.4237 rad
		 */

		//34, 107
		//57, 122
		///remove this
		public final Pose MidPointToFarSide = factory.of(48,107,180);//control point
		public final Pose farSideShot = factory.of(80,145,270);//shot on down side
		public final Pose farFlowerLineUp = factory.of(50,110,90);//shot on down side

		public final Pose farFlower = factory.of(47,128,90);//
		public final Pose park = factory.of(30, 95, 270);//park


		private final Pose point3 = factory.of(55.75, 15.75, 90);//shoot
		private final Pose point4 = factory.of(55.75, 8.75, 90);
		private final Pose point5 = factory.of(10.75, 97.75, 90);
		private final Pose point5Control1 = factory.of(22.75, 25.75, 0);

		//private final Pose flowerPoint1 = factory.of(56.1111, 22.4517, 90);
		private final Pose flowerPoint1mid = factory.of(10, 22.4517, 270);

		private final Pose flowerPoint2 = factory.of(9.885, 14.883, 270);
		private final Pose flowerPoint3 = factory.of(9.9961, 8.9965, 270);
		private final Pose flowerPoint4 = factory.of(9.3297, 10.3293, 275);

		public final Pose flowerMid = factory.of(194, -16.8893,90);
		public final Pose shootFar = factory.of(194, -16.8893,270);

		public Path point2Turn(){
			return Paths.line(path1, point2ButTurned).linear(path1, point2ButTurned);
		}

		public Path toCornerLineUp(){
			return Paths.line(point2ButTurned, lineUpCornerIntake).linear(point2ButTurned, lineUpCornerIntake);
		}
		public Path toCornerActual(){
			return Paths.line(lineUpCornerIntake, cornerIntake).linear(lineUpCornerIntake, cornerIntake);
		}

		public Path cornerTurn(){
			return Paths.line(cornerIntake, cornerIntakeTurn).linear(cornerIntake, cornerIntakeTurn);
		}

		public Path backUpFromCorner(){
			return Paths.line(lineUpCornerIntake, backUpFromCorner).linear(lineUpCornerIntake, backUpFromCorner);
		}
		public Path flowerToMid(){
			return Paths.line(point2, MidPointToFarSide).constant(point2);
		}
		public Path toFarShot(){
			return Paths.line(MidPointToFarSide, farSideShot).linear(MidPointToFarSide, farSideShot);
		}
		public Path toFarFlowerLineUp(){
			return Paths.line(farSideShot, farFlowerLineUp).linear(farSideShot, farFlowerLineUp);
		}

		public Path toFarFlower(){
			return Paths.line(farFlowerLineUp, farFlower).linear(farFlowerLineUp, farFlower);
		}
		public Path farFlowerToShot(){
			return Paths.line(farFlower, shootFar).linear(farFlower, shootFar);
		}

		public Path park(){
			return Paths.line(farSideShot, park).constant(farSideShot);
		}
		public Path park2(){
			return Paths.line(point2, park).constant(point2);
		}


		public Path cornerToMid(){
			return Paths.line(backUpFromCorner, MidPointToFarSide).linear(backUpFromCorner, MidPointToFarSide);
		}

		public Path cornerToShot() {
			return Paths.line(backUpFromCorner, path1).linear(backUpFromCorner, path1);
		}




		//0,0,0 start
		//138, -26 (shoot)
		//115, 5(line up for flower)
		//120, 5 (flower)
		//96, 21 (mid point to head to shot)

		/*public Path path1flower() {
			return Paths.line(start, flowerPoint1).constant(flowerPoint1);
		}

		public Path path12flower() {
			return Paths.line(flowerPoint1, flowerPoint1mid).constant(flowerPoint1mid);
		}

		public Path path2Flower() {
			return Paths.line(flowerPoint1, flowerPoint2).constant(flowerPoint2);
		}

		public Path path3Flower() {
			return Paths.line(flowerPoint2, flowerPoint3).constant(flowerPoint3);
		}

		public Path path4Flower() {
			return Paths.line(flowerPoint3, flowerPoint4).constant(flowerPoint4);
		}

		 */
		public Path path1() {
			return Paths.line(start, path1).constant(path1);
		}

		public Path path2() {
			return Paths.line(path1, point2).linear(path1, point2);
		}

		public Path path2Modified() {
			return Paths.line(path1, point2Modified).linear(path1, point2Modified);
		}
		public Path path3Modified1() {
			return Paths.line(point2Modified, point3Modified).linear(point2Modified, point3Modified);
		}

		public Path path3Modified() {
			return Paths.line(point3Modified, lineUpCornerIntake).linear(point3Modified, lineUpCornerIntake);
		}

		public Path path3() {
			return Paths.line(point2, point3).linear(point2, point3);
		}

		public Path path4() {
			return Paths.line(point3, point4).constant(point4);
		}

		public Path path5() {
			return Paths.curve(point4, point5Control1, point5).constant(point5);
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