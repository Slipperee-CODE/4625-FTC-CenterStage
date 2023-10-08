package org.firstinspires.ftc.teamcode.customclasses.unused;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.customclasses.webcam.OpenCVPipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

public class Webcam {
    public OpenCvCamera camera;
    private final int WEBCAM_WIDTH = 960;
    private final int WEBCAM_HEIGHT = 544;
    HardwareMap hardwareMap = null;
    public OpenCVPipeline pipeline = null;


    public Webcam(HardwareMap hwMap) { initialize(hwMap);}

    private void initialize(HardwareMap hwMap){
        hardwareMap = hwMap;

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "webcam"), cameraMonitorViewId);

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(WEBCAM_WIDTH,WEBCAM_HEIGHT, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });
    }

    public void UseCustomPipeline(OpenCvPipeline pipeline)
    {
        this.pipeline = (OpenCVPipeline) pipeline;
        this.pipeline.setCameraResolution(WEBCAM_WIDTH,WEBCAM_HEIGHT);
        camera.setPipeline(pipeline);
    }
}
