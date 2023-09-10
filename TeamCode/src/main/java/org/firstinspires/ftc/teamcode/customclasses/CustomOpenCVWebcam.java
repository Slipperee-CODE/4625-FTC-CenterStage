package org.firstinspires.ftc.teamcode.customclasses;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.openftc.easyopencv.OpenCvCamera;

public class CustomOpenCVWebcam
{
    public OpenCvCamera camera;
    private CustomPipeline pipeline;

    Telemetry telemetryClass;


    public CustomOpenCVWebcam(OpenCvCamera passedCamera, Telemetry passedTelemetry) { initialize(passedCamera, passedTelemetry);}

    private void initialize(OpenCvCamera passedCamera, Telemetry passedTelemetry)
    {
        camera = passedCamera;

        telemetryClass = passedTelemetry;

        pipeline = new CustomPipeline();
        camera.setPipeline(pipeline);
    }

    public void PrintData()
    {
        telemetryClass.addData("Data", pipeline.ReturnData());
    }
}
