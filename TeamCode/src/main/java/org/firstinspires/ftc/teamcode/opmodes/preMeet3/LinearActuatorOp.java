package org.firstinspires.ftc.teamcode.opmodes.preMeet3;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.TeamPropDetection;

@Disabled
@TeleOp(name="Distance Test")
public class LinearActuatorOp extends CustomOpMode {
    private TeamPropDetection distances;


    public void init() {
        super.init();
        distances = new TeamPropDetection(hardwareMap);
    }
    public void initLoop(){}
    protected boolean handleState(RobotState state) {
        return true;
    }
    public void start() {

    }
    protected void onMainLoop() {
        telemetry.addData("LeftDistance: ",distances.leftSensor.getDistance(DistanceUnit.METER));
        telemetry.addData("RightDistance: ",distances.rightSensor.getDistance(DistanceUnit.METER));
    }
    protected void onNextLoop() {

    }

    protected void onIdleLoop() {

    }


}
