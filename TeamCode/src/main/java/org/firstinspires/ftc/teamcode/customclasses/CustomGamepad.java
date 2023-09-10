package org.firstinspires.ftc.teamcode.customclasses;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

public class CustomGamepad {
    public final Gamepad gamepad; // has to be public because the Gamepad class is too complicated to fully wrap.
    private OpMode opMode;


    public boolean x,y,a,b,up,down,left,right,xDown,yDown,aDown,bDown,upDown,downDown,leftDown,rightDown;
    private boolean px,py,pa,pb,pup,pdown,pleft,pright;
    public boolean xToggle,yToggle,aToggle,bToggle,upToggle,downToggle,leftToggle,rightToggle;


    public void Update() {
        //Rebinds the gamepads variables to our own use
        x     = gamepad.x;
        y     = gamepad.y;
        a     = gamepad.a;
        b     = gamepad.b;
        up    = gamepad.dpad_up;
        down  = gamepad.dpad_down;
        left  = gamepad.dpad_left;
        right = gamepad.dpad_right;

        xDown = x && !px;
        yDown = y && !py;
        aDown = a && !pa;
        bDown = b && !pb;
        upDown   = up && !pup;
        downDown  = down && !pdown;
        leftDown  = left && !pleft;
        rightDown = right && !pright;

        px = x;
        py = y;
        pa = a;
        pb = b;
        pup =up;
        pdown =down;
        pleft =left;
        pright =right;

        //CAI -> ADDING BUTTON TOGGLE TO THE WRAPPER WITH THE XOR OPERATOR
        xToggle ^= xDown;
        yToggle ^= yDown;
        aToggle ^= aDown;
        bToggle ^= bDown;
        upToggle ^= upDown;
        downToggle ^= downDown;
        leftToggle ^= leftDown;
        rightToggle ^= rightDown;
    }


    public CustomGamepad(OpMode passedOpMode, int gamepadNum){
        opMode = passedOpMode;

        switch (gamepadNum){
            case 1:
                gamepad = opMode.gamepad1;
                break;

            case 2:
                gamepad = opMode.gamepad2;
                break;

            default:
                gamepad = opMode.gamepad1;
                break;
        }
    }
}
