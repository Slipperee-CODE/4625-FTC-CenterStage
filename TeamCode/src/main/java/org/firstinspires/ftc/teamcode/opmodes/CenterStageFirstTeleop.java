package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.customclasses.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.LeosAprilTagFun;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.MechanismState;

@Autonomous(name="BlueCenterStageFirstTeleopButNotActually")
public class CenterStageFirstTeleop extends CustomOpMode
{
    private LeosAprilTagFun tagAlignMechanism = null;
    protected void initLoop() {}

    protected void onMainLoop() {
        // Normal Robot Movement :)
        //robot.emulateController(gamepad1.left_stick_y,gamepad1.left_stick_x,gamepad1.right_stick_x);

        // Update mechanisms
        tagAlignMechanism.update();
    }

    protected void onNextLoop() {}

    protected void onIdleLoop() {}

    protected boolean handleState(RobotState state) { return true; }

    public void start() {

    }

    public void init(){
        super.init();
        tagAlignMechanism = new LeosAprilTagFun(telemetry, hardwareMap, robot);
        tagAlignMechanism.setState(MechanismState.ON);
    }
}
