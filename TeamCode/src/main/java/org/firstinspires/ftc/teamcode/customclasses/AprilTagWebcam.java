package org.firstinspires.ftc.teamcode.customclasses;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;

import java.util.ArrayList;

public class AprilTagWebcam
{
    public OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;
    public AprilTagDetection detectedTag;

    static final double FEET_PER_METER = 3.28084;

    double fx = 835.64;
    double fy = 835.64;
    double cx = 459.22;
    double cy = 261.933;

    //Focals (pixels) - Fx: 835.64 Fy: 835.64
    //Optical center - Cx: 459.22 Cy: 261.933

    // UNITS ARE METERS
    double tagsize = 0.17145; //6.75 inches for the page sized ones


    int numFramesWithoutDetection = 0;

    final float DECIMATION_HIGH = 3;
    final float DECIMATION_LOW = 2;
    final float THRESHOLD_HIGH_DECIMATION_RANGE_METERS = 1.0f;
    final int THRESHOLD_NUM_FRAMES_NO_DETECTION_BEFORE_LOW_DECIMATION = 4;

    Telemetry telemetryClass;


    public AprilTagWebcam(OpenCvCamera passedCamera, Telemetry passedTelemetry) { initialize(passedCamera, passedTelemetry);}

    private void initialize(OpenCvCamera passedCamera, Telemetry passedTelemetry)
    {
        camera = passedCamera;

        telemetryClass = passedTelemetry;

        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
    }


    @SuppressLint("DefaultLocale")
    public void DetectTags()
    {
        ArrayList<AprilTagDetection> detections = aprilTagDetectionPipeline.getDetectionsUpdate();

        // If there's been a new frame...
        if(detections != null)
        {
            telemetryClass.addData("FPS", camera.getFps());
            telemetryClass.addData("Overhead ms", camera.getOverheadTimeMs());
            telemetryClass.addData("Pipeline ms", camera.getPipelineTimeMs());

            detectedTag = null;

            // If we don't see any tags
            if(detections.size() == 0)
            {
                numFramesWithoutDetection++;

                // If we haven't seen a tag for a few frames, lower the decimation
                // so we can hopefully pick one up if we're e.g. far back
                if(numFramesWithoutDetection >= THRESHOLD_NUM_FRAMES_NO_DETECTION_BEFORE_LOW_DECIMATION)
                {
                    aprilTagDetectionPipeline.setDecimation(DECIMATION_LOW);
                }
            }
            // We do see tags!
            else
            {
                numFramesWithoutDetection = 0;

                // If the target is within 1 meter, turn on high decimation to
                // increase the frame rate
                if(detections.get(0).pose.z < THRESHOLD_HIGH_DECIMATION_RANGE_METERS)
                {
                    aprilTagDetectionPipeline.setDecimation(DECIMATION_HIGH);
                }

                for(AprilTagDetection detection : detections)
                {
                    detectedTag = detection;

                    Orientation rot = Orientation.getOrientation(detection.pose.R, AxesReference.INTRINSIC, AxesOrder.YXZ, AngleUnit.DEGREES);

                    telemetryClass.addLine(String.format("\nDetected tag ID=%d", detection.id));
                    telemetryClass.addLine(String.format("Translation X: %.2f feet", detection.pose.x*FEET_PER_METER));
                    telemetryClass.addLine(String.format("Translation Y: %.2f feet", detection.pose.y*FEET_PER_METER));
                    telemetryClass.addLine(String.format("Translation Z: %.2f feet", detection.pose.z*FEET_PER_METER));
                    telemetryClass.addLine(String.format("Rotation Yaw: %.2f degrees", rot.firstAngle));
                    telemetryClass.addLine(String.format("Rotation Pitch: %.2f degrees", rot.secondAngle));
                    telemetryClass.addLine(String.format("Rotation Roll: %.2f degrees", rot.thirdAngle));
                }
            }

            telemetryClass.update();
        }
    }
}
