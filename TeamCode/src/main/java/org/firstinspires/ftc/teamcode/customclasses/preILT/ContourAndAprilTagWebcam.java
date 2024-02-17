package org.firstinspires.ftc.teamcode.customclasses.preILT;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.VisibleTagsStorage;
import org.firstinspires.ftc.teamcode.opmodes.ILT.testing.BlueContourVisionProcessor;
import org.firstinspires.ftc.teamcode.opmodes.ILT.testing.ContourVisionProcessor;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.concurrent.TimeUnit;

public class ContourAndAprilTagWebcam {
    private final ContourVisionProcessor contourProcessor;
    private final AprilTagProcessor aprilTagProcessor;

    private final  VisionPortal visionPortal;

    private static class LensIntrinsics {
        static double fx = 835.64;
        static double fy = 835.64;
        static double cx = 459.22;
        static double cy = 261.933;

    }

    public enum Processor {
        APRIL_TAG,
        CONTOUR,
        NONE
    }
    private Processor activeProcessor;


    public ContourAndAprilTagWebcam (HardwareMap hardwareMap) {
        contourProcessor = new ContourVisionProcessor();
        aprilTagProcessor = new AprilTagProcessor.Builder()
                .setLensIntrinsics(LensIntrinsics.fx, LensIntrinsics.fy, LensIntrinsics.cx, LensIntrinsics.cy)
                .setOutputUnits(DistanceUnit.METER, AngleUnit.RADIANS)
                .build();

        visionPortal = new VisionPortal.Builder().setCamera(hardwareMap.get(WebcamName.class, "webcam"))
                .setCameraResolution(new Size(960, 544))
                .addProcessors(contourProcessor,aprilTagProcessor)
                .build();

        setActiveProcessor(Processor.NONE);
        while (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            CustomOpMode.sleep(1);
        }
        visionPortal.getCameraControl(ExposureControl.class).setMode(ExposureControl.Mode.Manual);
    }
    public void SetContourColor(ContourVisionProcessor.Color color) {
        contourProcessor.setColor(color);
    }
    public void setExposure(int millis) {
         visionPortal.getCameraControl(ExposureControl.class).setExposure(millis, TimeUnit.MILLISECONDS);
    }
    public void setGain(int grain) {
        visionPortal.getCameraControl(GainControl.class).setGain(grain);
    }
    public void setActiveProcessor(Processor processor) {
        activeProcessor = processor;
        switch (processor) {
            case APRIL_TAG:
                visionPortal.setProcessorEnabled(contourProcessor,false);
                visionPortal.setProcessorEnabled(aprilTagProcessor,true);
                break;
            case CONTOUR:
                visionPortal.setProcessorEnabled(contourProcessor,true);
                visionPortal.setProcessorEnabled(aprilTagProcessor,false);
                break;
            case NONE:
                visionPortal.setProcessorEnabled(contourProcessor,false);
                visionPortal.setProcessorEnabled(aprilTagProcessor,false);
                break;
        }
    }
    public ContourVisionProcessor.TeamPropState getTeamPropPosition() {
        return contourProcessor.teamPropState;
    }
    public void update() {
        switch (activeProcessor) {
            case APRIL_TAG:
                VisibleTagsStorage.stored = aprilTagProcessor.getDetections();
                //blueContourProcessor
                break;
            case CONTOUR:
            case NONE:
                break;
        }
    }


}
