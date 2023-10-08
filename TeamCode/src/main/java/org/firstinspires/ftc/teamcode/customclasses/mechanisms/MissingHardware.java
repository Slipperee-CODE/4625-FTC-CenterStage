package org.firstinspires.ftc.teamcode.customclasses.mechanisms;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;

public class MissingHardware {
    private static ArrayList<String> MissingHardware = new ArrayList<>();

    private MissingHardware() {} // makes this class un-instantiable

    public static void addMissingHardware(String hardware) {
        MissingHardware.add(hardware);
    }
    public static ArrayList<String> getMissingHardware() {
        return MissingHardware;

    }
    public static void printMissing(Telemetry telemetry) {
        boolean somethingIsMissing = false;
        for (String name: getMissingHardware()) {
            somethingIsMissing = true;
            telemetry.addLine(name);
        }
        if (somethingIsMissing) {
            telemetry.addLine("No Missing Hardware");
        }
        telemetry.update();
    }
}
