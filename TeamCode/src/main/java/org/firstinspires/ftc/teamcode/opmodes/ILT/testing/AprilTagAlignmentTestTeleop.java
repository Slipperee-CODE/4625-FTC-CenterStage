package org.firstinspires.ftc.teamcode.opmodes.ILT.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.Mechanism;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.MechanismState;
import org.firstinspires.ftc.teamcode.customclasses.webcam.AprilTagVisionPortalWebcam;
import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.AprilTagAlign;

@Autonomous(name="AprilTagAlignmentTest")
public class AprilTagAlignmentTestTeleop extends CustomOpMode {
    private AprilTagAlign aprilTagAlign;// change!!!
    private AprilTagVisionPortalWebcam webcam;

    protected CustomGamepad gamepad1;
    protected CustomGamepad gamepad2;


    public void init() {
        super.init();
        webcam = new AprilTagVisionPortalWebcam(telemetry,hardwareMap);

        gamepad1 = new CustomGamepad(this, 1);
        gamepad2 = new CustomGamepad(this, 2);
        aprilTagAlign = new AprilTagAlign(hardwareMap, telemetry, gamepad1,robotDrivetrain);
        aprilTagAlign.setState(MechanismState.ON);
    }
    public void initLoop(){}
    public void start() {

    }

    @Override
    public void mainLoop() {
        double speed = robotDrivetrain.getSpeedConstant();
        robotDrivetrain.setSpeedConstant(Range.clip(speed + (gamepad1.gamepad.right_trigger - gamepad1.gamepad.left_trigger) * 0.0010, 0,1));
        telemetry.addData("Speed Constant: ",robotDrivetrain.getSpeedConstant());
        gamepad1.update();
        gamepad2.update();
        webcam.GetDetections();
        aprilTagAlign.update();

    }

    protected void onMainLoop() {

    }

}
