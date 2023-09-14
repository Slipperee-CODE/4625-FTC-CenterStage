package org.firstinspires.ftc.teamcode.customclasses.centerstage;

public class BackdropPosition {
    public BackdropPosition leftBackdropPosition = null;
    public BackdropPosition rightBackdropPosition = null;
    public int selfPosition;
    public int id;

    /*
    public BackdropPosition MoveLeft()
    {
            selfPosition--;
            if (selfPosition < 0){
                if (leftBackdropPosition != null){
                    leftBackdropPosition.selfPosition = 1;
                    return leftBackdropPosition;
                }
                selfPosition++;
            }
            return this;
    }

    public BackdropPosition MoveRight()
    {
        selfPosition++;
        if (selfPosition > 1){
            if (rightBackdropPosition != null){
                rightBackdropPosition.selfPosition = 0;
                return rightBackdropPosition;
            }
            selfPosition--;
        }
        return this;
    }
     */
}
