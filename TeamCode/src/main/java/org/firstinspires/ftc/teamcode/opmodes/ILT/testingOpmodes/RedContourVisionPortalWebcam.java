package org.firstinspires.ftc.teamcode.opmodes.ILT.testingOpmodes;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.opmodes.ILT.testing.RedContourVisionProcessor;
import org.firstinspires.ftc.vision.VisionPortal;

public class RedContourVisionPortalWebcam {
    public VisionPortal visionPortal;
    private RedContourVisionProcessor redContourVisionProcessor;

    public RedContourVisionPortalWebcam(HardwareMap hardwareMap){
        redContourVisionProcessor = new RedContourVisionProcessor();
        visionPortal = new VisionPortal.Builder().setCamera(hardwareMap.get(WebcamName.class, "webcam"))
                .setCameraResolution(new Size(960, 544))
                .addProcessor(redContourVisionProcessor)
                .build();
        while (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
        }
    }

    public RedContourVisionProcessor.TeamPropState GetTeamPropState() {
        return redContourVisionProcessor.teamPropState;
    }
}
