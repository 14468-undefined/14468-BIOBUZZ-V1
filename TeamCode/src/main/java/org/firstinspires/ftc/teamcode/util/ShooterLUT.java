package org.firstinspires.ftc.teamcode.util;

public class ShooterLUT {

    // ===== EDIT HERE: { distance, rpm } =====
    // Distance units must match fieldState.distanceToGoal(...). Keep sorted by distance.
    // The numbers below are PLACEHOLDERS, so replace them with your tuned values.
    private static final double[][] POINTS = {
            {24, 2800},
            {36, 3000},
            {48, 3200},
            {60, 3450},
            {72, 3700},
            {84, 4000},
            {96, 4300},
    };
    // ========================================

    /** Returns the interpolated RPM for a distance. Clamps outside the table. */
    public static double get(double distance) {
        if (POINTS.length == 0) return 0;

        if (distance <= POINTS[0][0]) return POINTS[0][1];
        if (distance >= POINTS[POINTS.length - 1][0]) return POINTS[POINTS.length - 1][1];

        for (int i = 0; i < POINTS.length - 1; i++) {
            double d0 = POINTS[i][0], r0 = POINTS[i][1];
            double d1 = POINTS[i + 1][0], r1 = POINTS[i + 1][1];
            if (distance >= d0 && distance <= d1) {
                double t = (distance - d0) / (d1 - d0);
                return r0 + t * (r1 - r0);
            }
        }
        return POINTS[POINTS.length - 1][1];
    }
}