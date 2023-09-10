package org.firstinspires.ftc.teamcode.legacy;


public class NonThreadPressButton
{
    private boolean isClicked = false;


    public NonThreadPressButton(){ initialize(); }

    private void initialize() { }


    public boolean Update(boolean buttonState)
    {
        if (buttonState && !isClicked)
        {
            isClicked = true;
        }
        else
        {
            isClicked = false;
        }

        return isClicked;
    }
}
