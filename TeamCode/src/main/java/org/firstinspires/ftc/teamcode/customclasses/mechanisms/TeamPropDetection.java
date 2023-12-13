package org.firstinspires.ftc.teamcode.customclasses.mechanisms;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class TeamPropDetection extends MechanismBase implements Mechanism{
    private DistanceSensor leftSensor;
    private DistanceSensor rightSensor;

    public static final MechanismState DEFAULT = MechanismState.FORWARD;
    public TeamPropDetection(HardwareMap hardwareMap) {
        leftSensor = getHardware(DistanceSensor.class,"LeftSensor",hardwareMap);
        rightSensor = getHardware(DistanceSensor.class, "RightSensor",hardwareMap);
    }
    @Override
    public void update(Telemetry telemetry) {
        final double ldist = leftSensor.getDistance(DistanceUnit.METER);
        final double rdist = rightSensor.getDistance(DistanceUnit.METER);

        boolean onLeft = ldist < 0.10;
        boolean onRight = rdist < 0.10;
        if (onRight && onLeft) {
            //GOOFY SOmething wrong went wrong where the team prop is detected on both sides of the robot which is not physically possible ahhhhh!
            this.state = DEFAULT;
        } else if (onRight) {
            this.state = MechanismState.RIGHT;
        } else if (onLeft) {
            this.state = MechanismState.LEFT;
        } else {
            // we dont detect it to our sides therefore it must be in the front or we just knocked it out.
            // we take the more optimistic path and just say that its the front
            this.state = MechanismState.FORWARD;
        }
        if (telemetry != null) {
            telemetry.addData("Left Distance: ",ldist);
            telemetry.addData("Right Distance",rdist);
        }
    }
    @Override
    public void update() {
        this.update(null);
    }

}
