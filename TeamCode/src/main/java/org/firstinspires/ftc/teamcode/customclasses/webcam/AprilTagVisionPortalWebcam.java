package org.firstinspires.ftc.teamcode.customclasses.webcam;


import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.VisibleTagsStorage;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import android.util.Size;

import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor.Builder;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class AprilTagVisionPortalWebcam
{
    private Telemetry telemetry = null;

    /**
     * The variable to store our instance of the AprilTag processor.
     */
    private AprilTagProcessor aprilTag;
    double fx = 835.64;
    double fy = 835.64;
    double cx = 459.22;
    double cy = 261.933;
    /**
     * The variable to store our instance of the vision portal.
     */
    private VisionPortal visionPortal;
    private HardwareMap hardwareMap;
    private ExposureControl exposureControl;

    public AprilTagVisionPortalWebcam(Telemetry telemetry,HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        initAprilTag();


        // Push telemetry to the Driver Station.
        telemetry.update();



        // Save more CPU resources when camera is no longer needed.

    }   // end method runOpMode()

    /**
     * Initialize the AprilTag processor.
     */
    private void initAprilTag() {

        // Create the AprilTag processor the easy way.
        aprilTag = new Builder().setLensIntrinsics(fx, fy, cx, cy).setOutputUnits(DistanceUnit.METER, AngleUnit.RADIANS).build();

            // Create the vision portal the easy way.
            visionPortal = new VisionPortal.Builder().setCamera(hardwareMap.get(WebcamName.class, "webcam"))
                    .setCameraResolution(new Size(960, 544))
                    .addProcessor(aprilTag)
                    .build();

        while (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            CustomOpMode.sleep(1);
        }
        exposureControl = visionPortal.getCameraControl(ExposureControl.class);
        exposureControl.setMode(ExposureControl.Mode.Manual);



        GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
        gainControl.setGain(1000);


        //BARRETT TOLD ME DECIMATION IS THE KEY TO FIXING IT NOT SERING IT AT LONG RANGES


    }   // end method initAprilTag()

    /**
     * Add telemetry about AprilTag detections.
     */
    public List<AprilTagDetection> GetDetections()
    {
        VisibleTagsStorage.stored = aprilTag.getDetections();
        return VisibleTagsStorage.stored;
    }
    public void Update() {
        VisibleTagsStorage.stored = aprilTag.getDetections();
    }
    public long GetExposure() {
        return exposureControl.getExposure(TimeUnit.MILLISECONDS);
    }
    public void SetExposure(long milli) {
        exposureControl.setExposure(milli,TimeUnit.MILLISECONDS);
    }
    public void SetGain(int grain) {
        visionPortal.getCameraControl(GainControl.class).setGain(grain);
    }
    public int GetGain() {
        return visionPortal.getCameraControl(GainControl.class).getGain();
    }
    public void stop() {
        visionPortal.stopStreaming();
    }
    public void resume() {
        visionPortal.resumeStreaming();
        SetExposure(6);
    }
    private void telemetryAprilTag() {

        List<org.firstinspires.ftc.vision.apriltag.AprilTagDetection> currentDetections = aprilTag.getDetections();
        telemetry.addData("# AprilTags Detected", currentDetections.size());

        // Step through the list of detections and display info for each one.
        //for (AprilTagDetection detection : currentDetections) {
        //    if (detection.metadata != null) {
        //        telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
        //        telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
        //        telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
        //        telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
        //    } else {
        //        telemetry.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
        //        telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
        //    }
        //}   // end for() loop

        // Add "key" information to telemetry
        //telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
        //telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
        //telemetry.addLine("RBE = Range, Bearing & Elevation");

    }   // end method telemetryAprilTag()

}

