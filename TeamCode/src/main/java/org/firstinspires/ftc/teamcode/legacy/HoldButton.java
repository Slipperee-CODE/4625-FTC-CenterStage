package org.firstinspires.ftc.teamcode.legacy;


import com.qualcomm.robotcore.hardware.Gamepad;

public class HoldButton
{
    private Gamepad overrideGamepad;
    private boolean overridable;
    private Runnable function;


    public HoldButton(Runnable passedFunction, Gamepad passedGamepad){ initialize(passedFunction, passedGamepad); }

    public HoldButton(Runnable passedFunction){ initialize(passedFunction); }

    private void initialize(Runnable passedFunction, Gamepad passedGamepad)
    {
        function = passedFunction;
        overrideGamepad = passedGamepad;
        overridable = true;
    }

    private void initialize(Runnable passedFunction)
    {
        overridable = false;
    }


    public void Update(boolean buttonState)
    {
        if (buttonState)
        {
            if (overridable)
            {
                if (!OverrideCheck())
                {
                    function.run();
                }
            }
            else
            {
                function.run();
            }
        }
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
