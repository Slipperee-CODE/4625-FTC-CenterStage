package org.firstinspires.ftc.teamcode.opmodes.ILT.testingOpmodes;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.opmodes.ILT.testing.BlueContourVisionProcessor;
import org.firstinspires.ftc.teamcode.opmodes.ILT.testing.RedContourVisionProcessor;
import org.firstinspires.ftc.vision.VisionPortal;

public class BlueContourVisionPortalWebcam {
    public VisionPortal visionPortal;
    private BlueContourVisionProcessor blueContourVisionProcessor;

    public BlueContourVisionPortalWebcam(HardwareMap hardwareMap){
        blueContourVisionProcessor = new BlueContourVisionProcessor();
        visionPortal = new VisionPortal.Builder().setCamera(hardwareMap.get(WebcamName.class, "webcam"))
                .setCameraResolution(new Size(960, 544))
                .addProcessor(blueContourVisionProcessor)
                .build();
        while (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
        }
    }

    public BlueContourVisionProcessor.TeamPropState GetTeamPropState() {
        return blueContourVisionProcessor.teamPropState;
    }
    public void close() {
        visionPortal.close();
    }
}
