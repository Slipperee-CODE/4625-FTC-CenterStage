package org.firstinspires.ftc.teamcode.opmodes.ILT.testingOpmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomOpMode;

@Autonomous(name="BlueContourAuto")
public class BlueContourAuto extends CustomOpMode {
    private BlueContourVisionPortalWebcam blueContourVisionPortalWebcam;

    @Override
    public void init() {
        blueContourVisionPortalWebcam = new BlueContourVisionPortalWebcam(hardwareMap);
    }

    @Override
    public void init_loop(){
        telemetry.addData("TEAM PROP STATE",blueContourVisionPortalWebcam.GetTeamPropState());
        telemetry.update();
    }

    @Override
    public void start(){
        blueContourVisionPortalWebcam.visionPortal.stopStreaming();
    }

    @Override
    public void mainLoop() {

    }
}
