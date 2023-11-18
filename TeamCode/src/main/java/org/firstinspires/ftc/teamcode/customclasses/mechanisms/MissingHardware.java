package org.firstinspires.ftc.teamcode.customclasses.mechanisms;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;

public class MissingHardware {
    private static final ArrayList<String> MissingHardware = new ArrayList<>();

    private MissingHardware() {} // makes this class un-instantiable

    public static void addMissingHardware(String hardware) {
        MissingHardware.add(hardware);
    }
    public static ArrayList<String> getMissingHardware() {
        return MissingHardware;

    }
    public static boolean printMissing(Telemetry telemetry) {
        boolean nothingIsMissing = !MissingHardware.isEmpty();
        for (String name: getMissingHardware()) {

            telemetry.addLine(name);
        }
        if (nothingIsMissing) {
            telemetry.addLine("No Missing Hardware");
        }
        telemetry.update(); // update the telemetry
        return nothingIsMissing;
    }
}
