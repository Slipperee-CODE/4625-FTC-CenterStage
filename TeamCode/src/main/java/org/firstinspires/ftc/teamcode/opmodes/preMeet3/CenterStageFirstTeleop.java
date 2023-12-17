package org.firstinspires.ftc.teamcode.opmodes.preMeet3;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.customclasses.preMeet3.CustomOpMode;

@Disabled
@Autonomous(name="BlueCenterStageFirstTeleopButNotActually")
public class CenterStageFirstTeleop extends CustomOpMode
{
    protected void initLoop() {}

    protected void onMainLoop() {
        // Normal Robot Movement :)
        //robot.emulateController(gamepad1.left_stick_y,gamepad1.left_stick_x,gamepad1.right_stick_x);

        // Update mechanisms
        //tagAlignMechanism.update();
    }

    protected void onNextLoop() {}

    protected void onIdleLoop() {}

    protected boolean handleState(RobotState state) { return true; }

    public void start() {

    }

    public void init(){
        super.init();

        //Webcam webcam = new Webcam(hardwareMap, telemetry,true);
        //tagAlignMechanism = new LeosAprilTagFun(telemetry, hardwareMap, robot, webcam,true);   //telemetry.addLine("we have made the fun!");
        //telemetry.update();

        //tagAlignMechanism.setState(MechanismState.ON);
        //telemetry.addLine("init is over bruh");
        //telemetry.update();
    }
}
