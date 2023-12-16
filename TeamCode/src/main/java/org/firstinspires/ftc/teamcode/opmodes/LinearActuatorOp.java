package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.customclasses.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.TeamPropDetection;
import org.firstinspires.ftc.teamcode.customclasses.unused.TestRRMechanism;

import java.util.ArrayList;
import java.util.Arrays;

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
