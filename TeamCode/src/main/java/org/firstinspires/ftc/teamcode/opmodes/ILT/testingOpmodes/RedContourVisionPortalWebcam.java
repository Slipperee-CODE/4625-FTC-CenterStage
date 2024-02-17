package org.firstinspires.ftc.teamcode.opmodes.ILT.testingOpmodes;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.opmodes.ILT.testing.ContourVisionProcessor;
import org.firstinspires.ftc.vision.VisionPortal;

public class RedContourVisionPortalWebcam {
    public VisionPortal visionPortal;
    private ContourVisionProcessor redContourVisionProcessor;

    public RedContourVisionPortalWebcam(HardwareMap hardwareMap){
        redContourVisionProcessor = new ContourVisionProcessor();
        visionPortal = new VisionPortal.Builder().setCamera(hardwareMap.get(WebcamName.class, "webcam"))
                .setCameraResolution(new Size(960, 544))
                .addProcessor(redContourVisionProcessor)
                .build();
        while (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
        }
    }

    public ContourVisionProcessor.TeamPropState GetTeamPropState() {
        return redContourVisionProcessor.teamPropState;
    }
}
