package org.firstinspires.ftc.teamcode.customclasses.centerstage;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class TeamPropPosDetectPipeline extends OpenCvPipeline
{
    Mat YCbCr = new Mat();
    Mat leftCrop, centerCrop, rightCrop;
    Scalar leftAvg, centerAvg, rightAvg;
    double leftAvgFinal, centerAvgFinal, rightAvgFinal;
    Mat output = new Mat();
    Scalar rectColor = new Scalar(255.0, 0.0, 0.0);
    int autoVersion = 0;

    int WEBCAM_HEIGHT = 544;

    int WEBCAM_WIDTH = 960;

    @Override
    public Mat processFrame(Mat input)
    {
        Imgproc.cvtColor(input, YCbCr, Imgproc.COLOR_RGB2YCrCb);

        Rect leftRect = new Rect(1,1,WEBCAM_WIDTH/3, WEBCAM_HEIGHT);
        Rect centerRect = new Rect(WEBCAM_WIDTH/3,1,WEBCAM_WIDTH/3, WEBCAM_HEIGHT);
        Rect rightRect = new Rect(2 * WEBCAM_WIDTH/3,1,WEBCAM_WIDTH/3, WEBCAM_HEIGHT);

        input.copyTo(output);

        Imgproc.rectangle(output, leftRect, rectColor, 2);
        Imgproc.rectangle(output, centerRect, rectColor, 2);
        Imgproc.rectangle(output, rightRect, rectColor, 2);

        leftCrop = YCbCr.submat(leftRect);
        centerCrop = YCbCr.submat(centerRect);
        rightCrop = YCbCr.submat(rightRect);

        Core.extractChannel(leftCrop, leftCrop, 2);
        Core.extractChannel(centerCrop, centerCrop, 2);
        Core.extractChannel(rightCrop, rightCrop, 2);

        leftAvg = Core.mean(leftCrop);
        centerAvg = Core.mean(centerCrop);
        rightAvg = Core.mean(rightCrop);

        leftAvgFinal = leftAvg.val[0];
        centerAvgFinal = centerAvg.val[0];
        rightAvgFinal = rightAvg.val[0];

        if (leftAvgFinal >= centerAvgFinal && leftAvgFinal >= rightAvgFinal)
        {
            autoVersion = 1;
        }

        if (rightAvgFinal >= centerAvgFinal && rightAvgFinal >= leftAvgFinal)
        {
            autoVersion = 2;
        }

        if (centerAvgFinal >= rightAvgFinal && centerAvgFinal >= leftAvgFinal)
        {
            autoVersion = 3;
        }

        return output;
    }

    public int ReturnCurrentTeamPropPos()
    {
        return autoVersion;
    }
}

/*
I followed this tutorial https://www.youtube.com/watch?v=547ZUZiYfQE - Cai
 */