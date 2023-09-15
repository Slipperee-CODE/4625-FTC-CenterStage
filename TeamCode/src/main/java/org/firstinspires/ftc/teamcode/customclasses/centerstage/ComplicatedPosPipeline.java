package org.firstinspires.ftc.teamcode.customclasses.centerstage;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class ComplicatedPosPipeline extends OpenCvPipeline
{
    Mat leftCrop, centerCrop, rightCrop = new Mat();
    double leftRedPercent,centerRedPercent,rightRedPercent;
    Mat output = new Mat();
    Scalar rectNormalColor = new Scalar(255.0, 0.0, 0.0);
    Scalar rectFoundColor = new Scalar(0.0, 255.0, 0.0);
    int autoVersion = 0;

    int WEBCAM_HEIGHT = 544;

    int WEBCAM_WIDTH = 960;
    int heightOffset = WEBCAM_HEIGHT - WEBCAM_HEIGHT/2;
    int widthOffset = 50;

    Rect leftRect = new Rect(1+widthOffset, heightOffset,WEBCAM_WIDTH/5 - 1, WEBCAM_HEIGHT/4);
    Rect centerRect = new Rect(WEBCAM_WIDTH/3+widthOffset, heightOffset,WEBCAM_WIDTH/5 - 1, WEBCAM_HEIGHT/4);
    Rect rightRect = new Rect(2 * WEBCAM_WIDTH/3+widthOffset, heightOffset,WEBCAM_WIDTH/5 - 1, WEBCAM_HEIGHT/4);

    private double scalarSum(Scalar scalar) {
        return scalar.val[0] + scalar.val[1] + scalar.val[2] + scalar.val[3];
    }
    private int indexOfLargest(double[] arr) {
        double largest = Double.NEGATIVE_INFINITY;
        int index = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > largest) {
                largest = arr[i];
                index = i;
            }
        }
        return index;
    }



    @Override
    public Mat processFrame(Mat input)
    {

        //Imgproc.cvtColor(input, YCbCr, Imgproc.COLOR_RGB2YCrCb);

        input.copyTo(output);

        leftCrop = input.submat(leftRect);
        centerCrop = input.submat(centerRect);
        rightCrop = input.submat(rightRect);

        Scalar leftsum = Core.sumElems(leftCrop);
        Scalar centersum = Core.sumElems(centerCrop);
        Scalar  rightsum = Core.sumElems(rightCrop);
        leftRedPercent = leftsum.val[0] / scalarSum(leftsum);
        centerRedPercent = centersum.val[0] / scalarSum(centersum);
        rightRedPercent = rightsum.val[0] / scalarSum(rightsum);

        double[] arr = new double[] {leftRedPercent,centerRedPercent,rightRedPercent};

        int screen_side = indexOfLargest(arr);


        switch (screen_side) {
            case 0:
                autoVersion = 1;
                Imgproc.rectangle(output, leftRect, rectFoundColor, 5);
                Imgproc.rectangle(output, centerRect,rectNormalColor , 5);
                Imgproc.rectangle(output, rightRect, rectNormalColor, 5);
                break;
            case 1:
                autoVersion = 2;
                Imgproc.rectangle(output, leftRect, rectNormalColor, 5);
                Imgproc.rectangle(output, centerRect, rectFoundColor, 5);
                Imgproc.rectangle(output, rightRect, rectNormalColor, 5);
                break;
            case 2:
                autoVersion = 3;
                Imgproc.rectangle(output, leftRect, rectNormalColor, 5);
                Imgproc.rectangle(output, centerRect, rectNormalColor, 5);
                Imgproc.rectangle(output, rightRect, rectFoundColor, 5);
                break;

        }

        return output;
    }

    public int ReturnCurrentTeamPropPos()
    {
        return autoVersion;
    }


    public void PrintTelemetry(Telemetry telemetry)
    {
        telemetry.addData("Left Red",leftRedPercent);
        telemetry.addData("Center Red",centerRedPercent);
        telemetry.addData("Right Red",rightRedPercent);
    }
}

/*
I followed this tutorial https://www.youtube.com/watch?v=547ZUZiYfQE - Cai
 */