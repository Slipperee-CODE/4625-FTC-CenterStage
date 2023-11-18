package org.firstinspires.ftc.teamcode.customclasses.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;

public abstract class MechanismBase implements Mechanism {
    public MechanismState state;
    CustomGamepad gamepad;

    public abstract void update();

    public void update(Telemetry telemetry){
        update();
    }

    public void setState(MechanismState state) {
        this.state = state;
    }

    public <T> T getHardware(Class<? extends T> classOrInterface, String deviceName, HardwareMap hardwareMap) {
        T hw = hardwareMap.tryGet(classOrInterface, deviceName.trim());
        if (hw == null) {
            MissingHardware.addMissingHardware(deviceName);
            if (classOrInterface == Servo.class)
                return (T) new EmptyServo(); //
            else if (classOrInterface == DcMotor.class)
                return (T) new EmptyDcMotor();
        }
        return hw;
    }
    private class EmptyServo implements Servo {
        @Override
        public ServoController getController() {
            return null;
        }

        @Override
        public int getPortNumber() {
            return 0;
        }

        @Override
        public void setDirection(Servo.Direction direction) {

        }


        @Override
        public Servo.Direction getDirection() {
            return null;
        }

        @Override
        public void setPosition(double position) {

        }

        @Override
        public double getPosition() {
            return 0;
        }

        @Override
        public void scaleRange(double min, double max) {

        }

        @Override
        public Manufacturer getManufacturer() {
            return null;
        }

        @Override
        public String getDeviceName() {
            return null;
        }

        @Override
        public String getConnectionInfo() {
            return null;
        }

        @Override
        public int getVersion() {
            return 0;
        }

        @Override
        public void resetDeviceConfigurationForOpMode() {

        }

        @Override
        public void close() {

        }
    }
    private class EmptyDcMotor implements DcMotor {
        @Override
        public MotorConfigurationType getMotorType() {return null;}
        @Override
        public void setMotorType(MotorConfigurationType motorType) {}
        @Override
        public DcMotorController getController() {return null;}
        @Override
        public int getPortNumber() {return 0;}
        @Override
        public void setZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {}
        @Override
        public ZeroPowerBehavior getZeroPowerBehavior() {return null;}
        @Override
        public void setPowerFloat() {}
        @Override
        public boolean getPowerFloat() {return false;}
        @Override
        public void setTargetPosition(int position) {}
        @Override
        public int getTargetPosition() {return 0;}
        @Override
        public boolean isBusy() {return false;}
        @Override
        public int getCurrentPosition() {return 0;}
        @Override
        public void setMode(RunMode mode) {}
        @Override
        public RunMode getMode() {return null;}
        @Override
        public void setDirection(Direction direction) {}
        @Override
        public Direction getDirection() {return null;}
        @Override
        public void setPower(double power) {}
        @Override
        public double getPower() {return 0;}
        @Override
        public Manufacturer getManufacturer() {return null;}
        @Override
        public String getDeviceName() {return null;}
        @Override
        public String getConnectionInfo() {return null;}
        @Override
        public int getVersion() {return 0;}
        @Override
        public void resetDeviceConfigurationForOpMode() {}
        @Override
        public void close() {}
    }
}
