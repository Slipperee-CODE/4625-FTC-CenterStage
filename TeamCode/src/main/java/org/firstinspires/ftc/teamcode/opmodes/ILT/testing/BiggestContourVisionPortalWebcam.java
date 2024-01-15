package org.firstinspires.ftc.teamcode.opmodes.ILT.testing;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;

public class BiggestContourVisionPortalWebcam {
    public VisionPortal visionPortal;
    private BiggestContourVisionProcessor biggestContourVisionProcessor;

    public BiggestContourVisionPortalWebcam(HardwareMap hardwareMap){
        biggestContourVisionProcessor = new BiggestContourVisionProcessor();
        visionPortal = new VisionPortal.Builder().setCamera(hardwareMap.get(WebcamName.class, "webcam"))
                .setCameraResolution(new Size(960, 544))
                .addProcessor(biggestContourVisionProcessor)
                .build();
        while (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
        }
    }
}
