package org.firstinspires.ftc.teamcode.opmodes.ILT.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomOpMode;

@Autonomous(name="FindBiggestContourThirdAuto")
public class FindBiggestContourThirdAuto extends CustomOpMode {
    private BiggestContourVisionPortalWebcam biggestContourVisionPortalWebcam;

    @Override
    public void init() {
        biggestContourVisionPortalWebcam = new BiggestContourVisionPortalWebcam(hardwareMap);

    }

    @Override
    public void start(){
        biggestContourVisionPortalWebcam.visionPortal.stopStreaming();
    }

    @Override
    public void mainLoop() {

    }
}
