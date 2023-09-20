package org.firstinspires.ftc.teamcode.centerstage;

import android.graphics.Canvas;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Mat;

public interface OpenCVPipeline {
    Mat processFrame(Mat input);
    Object getUserContextForDrawHook();
    void requestViewportDrawHook(Object userContext);
    void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext);
    int ReturnCurrentTeamPropPos();
    void PrintTelemetry(Telemetry telemetry);
    boolean tuneBias();
}
