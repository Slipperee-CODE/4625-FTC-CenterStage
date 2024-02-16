package org.firstinspires.ftc.teamcode.customclasses.preILT;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.VisibleTagsStorage;
import org.firstinspires.ftc.teamcode.opmodes.ILT.testing.BlueContourVisionProcessor;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

public class ContourAndAprilTagWebcam {
    private final BlueContourVisionProcessor blueContourProcessor;
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
        blueContourProcessor = new BlueContourVisionProcessor();
        aprilTagProcessor = new AprilTagProcessor.Builder()
                .setLensIntrinsics(LensIntrinsics.fx, LensIntrinsics.fy, LensIntrinsics.cx, LensIntrinsics.cy)
                .setOutputUnits(DistanceUnit.METER, AngleUnit.RADIANS)
                .build();

        visionPortal = new VisionPortal.Builder().setCamera(hardwareMap.get(WebcamName.class, "webcam"))

                .setCameraResolution(new Size(960, 544))
                .addProcessors(blueContourProcessor,aprilTagProcessor)
                .build();
        setActiveProcessor(Processor.NONE);

    }
    public void setActiveProcessor(Processor processor) {
        activeProcessor = processor;
        switch (processor) {
            case APRIL_TAG:
                visionPortal.setProcessorEnabled(blueContourProcessor,true);
                visionPortal.setProcessorEnabled(aprilTagProcessor,false);
                break;
            case CONTOUR:
                visionPortal.setProcessorEnabled(blueContourProcessor,false);
                visionPortal.setProcessorEnabled(aprilTagProcessor,true);
                break;
            case NONE:
                visionPortal.setProcessorEnabled(blueContourProcessor,false);
                visionPortal.setProcessorEnabled(aprilTagProcessor,false);
                break;
        }
    }
    public BlueContourVisionProcessor.TeamPropState getTeamPropPosition() {
        return blueContourProcessor.teamPropState;
    }
    public void update() {
        switch (activeProcessor) {
            case APRIL_TAG:
                //blueContourProcessor
                break;
            case CONTOUR:
                VisibleTagsStorage.stored = aprilTagProcessor.getDetections();
                break;
            case NONE:
                visionPortal.setProcessorEnabled(blueContourProcessor,false);
                visionPortal.setProcessorEnabled(aprilTagProcessor,false);
                break;
        }
    }


}
