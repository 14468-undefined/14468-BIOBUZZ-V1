package org.firstinspires.ftc.teamcode.opmodes.auto.paths;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import static com.pedropathing.api.Paths.*;
import static com.pedropathing.api.Paths.curve;


public class PathsAndPosesUpSide2TipSolo {
    private final PoseFactory poseFactory = PoseFactory.degrees();

    public PoseFactory getPoseFactory() {
        return poseFactory;
    }


    private final Pose start_pose = poseFactory.of(82, 133, 90);
    private final Pose garden = poseFactory.of(133, 133, 90);
    private final Pose intake_ctrl_pt_1 = poseFactory.of(128.6392, 103.676, 0);
    private final Pose out_of_garden = poseFactory.of(132.1382, 130, 90);
    private final Pose under_hive_up_side = poseFactory.of(82.2606, 109.5764, -90);
    private final Pose under_hive_down_side = poseFactory.of(82, 20, -90);
    private final Pose lineup_flower_intake = poseFactory.of(94.5, 13, -90);
    private final Pose flower_intake = poseFactory.of(94.5, 9, -90);
    private final Pose park_pose = poseFactory.of(9.1057, 93.0516, 0);
    private final Pose park_path_ctrl_pt_1 = poseFactory.of(31.9919, 68.3327, 0);

    public Path startPose_to_garden() {
        return curve(start_pose, intake_ctrl_pt_1, garden).constant(garden);
    }

    public Path out_of_garden() {
        return line(garden, out_of_garden).constant(out_of_garden);
    }

    public Path garden_to_hive() {
        return line(out_of_garden, under_hive_up_side).linear(out_of_garden, under_hive_up_side);
    }

    public Path under_through_hive() {
        return line(under_hive_up_side, under_hive_down_side).tangent();
    }

    public Path line_up_for_flower_intake() {
        return line(under_hive_down_side, lineup_flower_intake).linear(under_hive_down_side, lineup_flower_intake);
    }

    public Path flower_intake() {
        return line(lineup_flower_intake, flower_intake).tangent();
    }

    public Path park_avoid_other_robot() {
        return curve(flower_intake, park_path_ctrl_pt_1, park_pose).linear(flower_intake, park_pose);
    }






    //public Path leavePos_to_startPos(){
        //return line(leavePos, startPos).constant(leavePos);
    //}
}