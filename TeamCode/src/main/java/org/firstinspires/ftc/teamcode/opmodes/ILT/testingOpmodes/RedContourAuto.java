package org.firstinspires.ftc.teamcode.opmodes.ILT.testingOpmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomOpMode;

@Autonomous(name="RedContourAuto")
public class RedContourAuto extends CustomOpMode {
    private RedContourVisionPortalWebcam redContourVisionPortalWebcam;

    @Override
    public void init() {
        redContourVisionPortalWebcam = new RedContourVisionPortalWebcam(hardwareMap);
    }

    @Override
    public void init_loop(){
        telemetry.addData("TEAM PROP STATE",redContourVisionPortalWebcam.GetTeamPropState());
        telemetry.update();
    }

    @Override
    public void start(){
        redContourVisionPortalWebcam.visionPortal.stopStreaming();
    }

    @Override
    public void mainLoop() {

    }
}
