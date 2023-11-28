package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.customclasses.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.unused.TestRRMechanism;

import java.util.ArrayList;
import java.util.Arrays;

@TeleOp(name="LinearActuator")
public class LinearActuatorOp extends CustomOpMode {
    private DcMotor actuatorMotor;


    public void init() {
        super.init();
        actuatorMotor = hardwareMap.get(DcMotor.class,"LinearActuator");
    }
    public void initLoop(){}
    protected boolean handleState(RobotState state) {
        return true;
    }
    public void start() {

    }
    protected void onMainLoop() {
      actuatorMotor.setPower(gamepad1.right_trigger - gamepad1.left_trigger);
    }
    protected void onNextLoop() {

    }

    protected void onIdleLoop() {

    }


}
