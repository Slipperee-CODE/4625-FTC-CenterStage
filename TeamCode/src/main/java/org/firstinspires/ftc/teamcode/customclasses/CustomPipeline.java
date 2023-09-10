package org.firstinspires.ftc.teamcode.customclasses;

import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvPipeline;

public class CustomPipeline extends OpenCvPipeline //CHANGE THIS SCRIPT TO ALTER THE PIPELINES FUNCTION
{
    @Override
    public Mat processFrame(Mat input)
    {
        return input;
    }

    public int ReturnData()
    {
        int data = 0;
        return data;
    }
}
