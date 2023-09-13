package org.firstinspires.ftc.teamcode.customclasses.centerstage;

import org.firstinspires.ftc.robotcore.external.Telemetry;
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
    Scalar rectNormalColor = new Scalar(255.0, 0.0, 0.0);
    Scalar rectFoundColor = new Scalar(0.0, 255.0, 0.0);
    int autoVersion = 0;

    int WEBCAM_HEIGHT = 544;

    int WEBCAM_WIDTH = 960;

    @Override
    public Mat processFrame(Mat input)
    {
        Imgproc.cvtColor(input, YCbCr, Imgproc.COLOR_RGB2YCrCb);

        Rect leftRect = new Rect(1,1,WEBCAM_WIDTH/3 - 1, WEBCAM_HEIGHT - 1);
        Rect centerRect = new Rect(WEBCAM_WIDTH/3,1,WEBCAM_WIDTH/3 - 1, WEBCAM_HEIGHT - 1);
        Rect rightRect = new Rect(2 * WEBCAM_WIDTH/3,1,WEBCAM_WIDTH/3 - 1, WEBCAM_HEIGHT - 1);

        input.copyTo(output);

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
            Imgproc.rectangle(output, leftRect, rectFoundColor, 5);
            Imgproc.rectangle(output, centerRect,rectNormalColor , 5);
            Imgproc.rectangle(output, rightRect, rectNormalColor, 5);
        }

        else if (centerAvgFinal >= rightAvgFinal && centerAvgFinal >= leftAvgFinal)
        {
            autoVersion = 2;
            Imgproc.rectangle(output, leftRect, rectNormalColor, 5);
            Imgproc.rectangle(output, centerRect, rectFoundColor, 5);
            Imgproc.rectangle(output, rightRect, rectNormalColor, 5);
        }

        else if (rightAvgFinal >= centerAvgFinal && rightAvgFinal >= leftAvgFinal)
        {
            autoVersion = 3;
            Imgproc.rectangle(output, leftRect, rectNormalColor, 5);
            Imgproc.rectangle(output, centerRect, rectNormalColor, 5);
            Imgproc.rectangle(output, rightRect, rectFoundColor, 5);
        }
        
        return output;
    }

    public int ReturnCurrentTeamPropPos()
    {
        return autoVersion;
    }


    public void PrintTelemetry(Telemetry telemetry)
    {
        telemetry.addData("Left Avg Red",leftAvgFinal);
        telemetry.addData("Center Avg Red",centerAvgFinal);
        telemetry.addData("Right Avg Red",rightAvgFinal);
    }
}

/*
I followed this tutorial https://www.youtube.com/watch?v=547ZUZiYfQE - Cai
 */