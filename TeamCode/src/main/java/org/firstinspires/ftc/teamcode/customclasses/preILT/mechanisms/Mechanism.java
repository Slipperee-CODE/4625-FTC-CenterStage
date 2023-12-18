package org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms.MechanismState;

public interface Mechanism {
    void update(Telemetry telemetry);
    void update();
    void setState(MechanismState state);
    void mechanismTuningUpdate(Telemetry telemetry);
}
