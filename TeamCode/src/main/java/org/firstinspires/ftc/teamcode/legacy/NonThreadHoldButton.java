package org.firstinspires.ftc.teamcode.legacy;


import com.qualcomm.robotcore.hardware.Gamepad;

public class NonThreadHoldButton
{
    private Gamepad overrideGamepad;
    private boolean overridable;


    public NonThreadHoldButton(Gamepad passedGamepad){ initialize(passedGamepad); }

    public NonThreadHoldButton(){ initialize(); }

    private void initialize(Gamepad passedGamepad)
    {
        overrideGamepad = passedGamepad;
        overridable = true;
    }

    private void initialize()
    {
        overridable = false;
    }


    public boolean Update(boolean buttonState)
    {
        if (buttonState)
        {
            if (overridable)
            {
                if (!OverrideCheck())
                {
                    return true;
                }
            }
            else
            {
                return true;
            }
        }
        return false;
    }


    private boolean OverrideCheck()
    {
        if (!(overrideGamepad.left_stick_x == 0 && overrideGamepad.left_stick_y == 0 && overrideGamepad.right_stick_x == 0 && overrideGamepad.right_stick_y == 0))
        {
            return true;
        }
        return false;
    }
}
