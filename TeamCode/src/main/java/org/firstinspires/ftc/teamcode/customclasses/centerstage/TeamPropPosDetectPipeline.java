package org.firstinspires.ftc.teamcode.customclasses.centerstage;

import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvPipeline;

public class TeamPropPosDetectPipeline extends OpenCvPipeline
{
    @Override
    public Mat processFrame(Mat input)
    {
        return input;
    }

    public int ReturnCurrentTeamPropPos()
    {
        int pos = 0;
        return pos;
    }
}
