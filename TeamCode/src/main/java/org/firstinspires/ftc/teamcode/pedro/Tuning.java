package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.tuning.autotune.Procedure;
import com.pedropathing.tuning.autotune.Tuner;
import org.firstinspires.ftc.teamcode.pedro.procedures.MecanumTuner;

public class Tuning {
    // Tuners go here

    /**
     * added 10/7/26
     */
    @Tuner
    public static Procedure mecanumTuner() {
        return new MecanumTuner();
    }
}
