package org.firstinspires.ftc.teamcode.customclasses;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.centerstage.OpenCVPipeline;
import org.firstinspires.ftc.teamcode.customclasses.centerstage.ComplicatedPosPipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

public class Webcam {
    public OpenCvCamera camera;

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
                camera.startStreaming(960,544, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });
    }

    public void UseCustomPipeline(OpenCvPipeline pipeline)
    {
        if (pipeline == null) {
            pipeline = new ComplicatedPosPipeline();
        }
        this.pipeline = (OpenCVPipeline) pipeline;
        camera.setPipeline(pipeline);
    }
}
