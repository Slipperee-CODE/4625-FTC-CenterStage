package org.firstinspires.ftc.teamcode.centerstage;



import org.firstinspires.ftc.robotcore.external.Telemetry;

public interface OpenCVPipeline {

    //Mat processFrame(Mat input);
    //Object getUserContextForDrawHook();
    //void requestViewportDrawHook(Object userContext);
    //void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext);
    int ReturnCurrentTeamPropPos();
    void PrintTelemetry(Telemetry telemetry);
    boolean tuneBias();
    void manualTuneBias(double left, double center, double right) ;
    void setCameraResolution(int width, int height);
    void setOptimization(int level);
    void setDebug(boolean debug);
}
