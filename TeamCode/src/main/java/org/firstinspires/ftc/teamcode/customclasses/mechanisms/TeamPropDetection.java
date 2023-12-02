package org.firstinspires.ftc.teamcode.customclasses.mechanisms;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TeamPropDetection extends MechanismBase implements Mechanism{
    DistanceSensor leftSensor;
    DistanceSensor rightSensor;

    public TeamPropDetection(HardwareMap hardwareMap, Telemetry telemetry) {
        leftSensor = getHardware(DistanceSensor.class,"LeftSensor",hardwareMap);
        rightSensor = getHardware(DistanceSensor.class, "RightSensor",hardwareMap);
    }
    @Override
    public void update() {

    }

}
