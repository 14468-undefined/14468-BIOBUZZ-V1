
package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.bylazar.field.FieldManager;
import com.bylazar.field.PanelsField;
import com.pedropathing.math.Pose;

public class FieldDrawer {

    private final FieldManager panelsField = PanelsField.INSTANCE.getField();

    public FieldDrawer() {
        panelsField.setOffsets(
                PanelsField.INSTANCE.getPresets().getPEDRO_PATHING()
        );
    }

    public void update(Pose pose) {
        double x = pose.x();
        double y = pose.y();
        double heading = pose.heading();

        // Draw robot
        panelsField.setStyle(
                "blue",
                "white",
                2.0
        );

        panelsField.moveCursor(x, y);
        panelsField.circle(9.0);

        // Draw heading indicator
        panelsField.setStyle(
                "none",
                "green",
                2.0
        );

        double headingLength = 12.0;

        panelsField.moveCursor(x, y);
        panelsField.line(
                x + Math.cos(heading) * headingLength,
                y + Math.sin(heading) * headingLength
        );

        // Send drawing to Panels
        panelsField.update();
    }
}
