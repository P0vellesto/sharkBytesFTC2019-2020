package org.firstinspires.ftc.teamcode;

import android.graphics.NinePatch;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.ColorSensor;
import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import java.util.ArrayList;

@Autonomous(name="Crater Unhook Odometry", group="Odometry")
public class crater_facing_auto extends LinearOpMode {
    /* Declare OpMode members. */
    private DcMotor mDrv_l0 = null;
    private DcMotor mDrv_r0 = null;
    private DcMotor mDrv_l1 = null;
    private DcMotor mDrv_r1 = null;
    private DcMotor mPin = null;
    private DcMotor mArm = null;
    private DcMotor mWch = null;
    private Servo sBox = null;
    private ColorSensor sensorRGB;

    private ElapsedTime runtime = new ElapsedTime();
    // TODO: explanation for all these box variables // these doubles I think aren't used
    public final static double BOX_HOME = 0.0;
    public final static double BOX_MIN_HOME = 0.0;
    public final static double BOX_MAX_HOME = 1.0;
    public double boxPosition = BOX_HOME;
    // used for sampling
    boolean sampled = false;
    // these variables are calibrated to the robot drive train for easier and more readable odometry
    double NINETY_DEG = 0.585;
    double ONE_FOOT = 0.4;
    double ARM_UP_NINETY = 1.5;
    double ARM_DOWN_NINETY = 0.5;
    //ColorSensor colorSensor; // not used

    public void stopMoving()
    {
        mDrv_l0.setPower(0);
        mDrv_l1.setPower(0);
        mDrv_r0.setPower(0);
        mDrv_r1.setPower(0);
        mArm.setPower(0);
        mWch.setPower(0);
        double sBoxStop = sBox.getPosition();
        sBox.setPosition(sBoxStop);
    }
    public void pauseDrive(double holdTime)
    {
        ElapsedTime holdTimer = new ElapsedTime();
        holdTimer.reset();
        while (opModeIsActive() && holdTimer.time() < holdTime)
        {
            mDrv_l0.setPower(0);
            mDrv_r0.setPower(0);
            mDrv_l1.setPower(0);
            mDrv_r1.setPower(0);
        }
    }

    /* attempt to make a method that takes an array of motors and runs each motor for some amount of time... but java syntax sux2sux so re.
    public void runMotor(ArrayList motors, double holdTime, double power)
    {
        ElapsedTime holdTimer = new ElapsedTime();
        holdTimer.reset();
        while (opModeIsActive() && holdTimer.time() < holdTime)
        {
            for (DcMotor motor : motors) {
                motor.setPower(power);
            }
        }
        stopMoving();
    }*/

    public void turnLeft(double holdTime, double power)
    {
        ElapsedTime holdTimer = new ElapsedTime();
        holdTimer.reset();
        while (opModeIsActive() && holdTimer.time() < holdTime)
        {
            mDrv_l0.setPower(-power);
            mDrv_r0.setPower(power);
            mDrv_l1.setPower(-power);
            mDrv_r1.setPower(power);
        }
        stopMoving();
    }

    public void turnRight(double holdTime, double power)
    {
        ElapsedTime holdTimer = new ElapsedTime();
        holdTimer.reset();
        while (opModeIsActive() && holdTimer.time() < holdTime)
        {
            mDrv_l0.setPower(power);
            mDrv_r0.setPower(-power);
            mDrv_l1.setPower(power);
            mDrv_r1.setPower(-power);
        }
        stopMoving();
    }

    public void onlyLeft(double holdTime, double power)
    {
        ElapsedTime holdTimer = new ElapsedTime();
        holdTimer.reset();
        mDrv_l0.setPower(power);
        mDrv_l1.setPower(power);
        while (opModeIsActive() && holdTimer.time() < holdTime) {}
        stopMoving();
    }

    public void onlyRight(double holdTime, double power)
    {
        ElapsedTime holdTimer = new ElapsedTime();
        holdTimer.reset();
        mDrv_r0.setPower(power);
        mDrv_r1.setPower(power);
        while (opModeIsActive() && holdTimer.time() < holdTime) {}
        stopMoving();
    }

    public void forward (double holdTime, double power)
    {
        ElapsedTime holdTimer = new ElapsedTime();
        holdTimer.reset();
        while (opModeIsActive() && holdTimer.time() < holdTime)
        {
            mDrv_l0.setPower(power);
            mDrv_r0.setPower(power);
            mDrv_l1.setPower(power);
            mDrv_r1.setPower(power);
        }
        stopMoving();
    }

    public void backward(double holdTime, double power)
    { // not used
        ElapsedTime holdTimer = new ElapsedTime();
        holdTimer.reset();
        while (opModeIsActive() && holdTimer.time() < holdTime)
        {
            mDrv_l0.setPower(-power);
            mDrv_r0.setPower(-power);
            mDrv_l1.setPower(-power);
            mDrv_r1.setPower(-power);
        }
        stopMoving();
    }

    public void turnArm(double holdTime, double power)
    {
        ElapsedTime holdTimer = new ElapsedTime();
        holdTimer.reset();
        while (opModeIsActive() && holdTimer.time() < holdTime)
        {
            mArm.setPower(power/3);
        }
    }

    public void turnBoxServo(double holdTime, double turnAmount)
    {
        ElapsedTime holdTimer = new ElapsedTime();
        holdTimer.reset();
        while (opModeIsActive() && holdTimer.time() < holdTime)
        {
            boxPosition += turnAmount; // Adds the amount you want to turn the box to the current position.
            sBox.setPosition(boxPosition); // Sets the new position of the box
        }
    }
    public void setBoxPosition(double position)
    {
        while (opModeIsActive())
        {
            sBox.setPosition(position);
        }
    }

    public void raiseRackPinionMotor(double holdTime, double power)
    {
        ElapsedTime holdTimer = new ElapsedTime();
        holdTimer.reset();
        while (opModeIsActive() && holdTimer.time() < holdTime) {
            mPin.setPower(-power);
        }
        mPin.setPower(0);
    }

    public void lowerRackPinionMotor(double holdTime, double power)
    {
        ElapsedTime holdTimer = new ElapsedTime();
        holdTimer.reset();
        while (opModeIsActive() && holdTimer.time() < holdTime)
        {
            mPin.setPower(power);
        }
        mPin.setPower(0);
    }

    public void extendArm(double holdTime, double power)
    { // not used
        ElapsedTime holdTimer = new ElapsedTime();
        holdTimer.reset();
        while (opModeIsActive() && holdTimer.time() < holdTime)
        {
            mWch.setPower(power);
        }
    }

    public void extendRaise(double holdTime, double power)
    {
        ElapsedTime holdTimer = new ElapsedTime();
        holdTimer.reset();
        while (opModeIsActive() && holdTimer.time() < holdTime)
        {
            mArm.setPower(power/3);
            mWch.setPower(power);
        }
    }
    public void sampling()
    {
        /*if (!sampled)
        {
            // hsvValues is an array that will hold the hue, saturation, and value information.
            float hsvValues[] = {0F,0F,0F};
            // values is a reference to the hsvValues array.
            final float values[] = hsvValues;
            Color.RGBToHSV((sensorRGB.red() * 255) / 800, (sensorRGB.green() * 255) / 800, (sensorRGB.blue() * 255) / 800, hsvValues);
            telemetry.addData("Clear", sensorRGB.alpha());
            telemetry.addData("Red  ", sensorRGB.red());
            telemetry.addData("Green", sensorRGB.green());
            telemetry.addData("Blue ", sensorRGB.blue());
            telemetry.addData("Hue", hsvValues[0]);
            telemetry.update();
            boolean isGold = true; // this would be instead a line of code that checks the color
            if (isGold)
            {

                mArm.setPower(.33); // keeps arm in the air
                sampled = true; // say we already sampled
                onlyRight(0.5 * NINETY_DEG, 1); // push the gold thingy over
                onlyLeft(0.45 * NINETY_DEG, 1); // get back on track
                mArm.setPower(.33); // keeps arm in the air
            }
        }*/
        if (!sampled)
        {
            sampled = true;
            onlyRight(NINETY_DEG, 1); // push the gold thingy over
            onlyLeft(NINETY_DEG, 1); // get back on track

        }
    }

    /* code for sampling with color sensor, not used for this competition.
        public void jewel(double holdTime)
        {
            ElapsedTime holdTimer = new ElapsedTime();
            holdTimer.reset();
            //TODO: Figure out what colors we need to look for, and then assign what to do when we see them
            while (opModeIsActive() && holdTimer.time() < holdTime)
            {
                if (colorSensor.blue() > colorSensor.red()) //TODO: We need to get the rough RBG of each mineral
                {
                    turnRight(1, 1);
                    forward(0.5, 1);
                    telemetry.addData("BLUE", "%s visible");
                    //robot.armServo.setPosition(0.0);
                } else {
                    forward(0.5, 1);
                    turnLeft(1, 1);
                    //Assuming that when you turn right for 1 sec,
                    //and you turn back left for 1 you will be in the original position
                    telemetry.addData("RED", "%s visible");
                    //sArm.setPosition(ARM_HOME);
                }

            }
        }
    */

    public void runOpMode()
    {
        /*init color sensor
        colorSensor = hardwareMap.get(ColorSensor.class, "sensor_color");
        colorSensor.enableLed(true);
        */
        // drive motors
        mDrv_l0 = hardwareMap.get(DcMotor.class, "mDrv_l0");
        mDrv_r0 = hardwareMap.get(DcMotor.class, "mDrv_r0");
        mDrv_l1 = hardwareMap.get(DcMotor.class, "mDrv_l1");
        mDrv_r1 = hardwareMap.get(DcMotor.class, "mDrv_r1");
        // other motors
        mPin = hardwareMap.get(DcMotor.class, "mPin");
        mArm = hardwareMap.get(DcMotor.class, "mArm");
        mWch = hardwareMap.get(DcMotor.class, "mWch");
        // servos
        sBox = hardwareMap.get(Servo.class, "sBox");
        // color sensor
        sensorRGB = hardwareMap.colorSensor.get("color_sensor");

        // set motor directions so that positive "power" on motors moves the robot forwards
        mDrv_l0.setDirection(DcMotor.Direction.REVERSE);
        mDrv_l1.setDirection(DcMotor.Direction.REVERSE);

        // hsvValues is an array that will hold the hue, saturation, and value information.
        float hsvValues[] = {0F,0F,0F};

        // values is a reference to the hsvValues array.
        final float values[] = hsvValues;

        // these variables are calibrated to the robot drive train for easier and more readable odometry

        waitForStart();

        // Autonomous event chain:
        // unhook
        raiseRackPinionMotor(4.5, 1); // extend rack and pinion to lower robot
        onlyRight(1 * NINETY_DEG, -1); // unhook but only run right motor so the robot spins away from the lander
        backward(1.3 * ONE_FOOT, 1); // back up to be next to sampling field

        //sample
        onlyRight(1.50 * NINETY_DEG, -1); // spin to be facing perpendicular to the sampling field
        forward(ONE_FOOT / 3.7, 1); // align with the first commodity

        onlyLeft(NINETY_DEG / 4, 1); // align with the first commodity
        // pauseDrive(0.25); // pause the robot for testing
        sampling(); // samples it
        forward(ONE_FOOT * 1.25, 1); // align with the second commodity
        onlyRight(NINETY_DEG/5, 1);
        // pauseDrive(0.25); // pause the robot for testing
        // sampling(); // samples it
        forward(ONE_FOOT * 1.25, 1); // align with the third commodity
        // pauseDrive(0.25); // pause the robot for testing
        // sampling(); // samples it

        // go to depo
        forward(3.70 * ONE_FOOT, 1); // go around the crater
        turnLeft(NINETY_DEG / 2.40, 1); // turn towards depo
        turnRight(NINETY_DEG/3, 1); // turn towards depo
        forward(4.4 * ONE_FOOT, 1); // go forward to the depo

        // drop the marker
        turnArm(ARM_UP_NINETY, -1); // turns the arm to put down the marker
        turnBoxServo(1, 1.0); // turns the box to drop marker
        // the method turnArm might have to be used instead of extendRaise
        turnArm(ARM_DOWN_NINETY, 1); // puts the arm back in the air so that the robot can be driven

        // park
        backward(5 * ONE_FOOT, 1); //go to the crater and park


        // sample (placeholder code
        /*
        boolean sampled = false;
        if (false /* todo: use color sensor to see if we should sample this one * /) // facing lander, if left most is gold
        {
            sampled = true; // say we already sampled
            turnRight(0.25 * NINETY_DEG, 1); // push the gold thingy over
            turnLeft(0.25 * NINETY_DEG, 1); // get back on track
        }
        forward(1.16*ONE_FOOT, 1); // move forward to next possible sample spot
        if (false /* todo: use color sensor to see if we should sample this one * / && !sampled) // facing lander, if middle is gold
        {
            sampled = true; // say that we already sampled
            turnRight(0.25 * NINETY_DEG, 1); // push the gold thingy over
            turnLeft(0.25 * NINETY_DEG, 1); // get back on track
        }
        forward(1.16*ONE_FOOT, 1); // move forward to the next possible sample
        if (! sampled) // if we haven't sampled yet (if we have missed the gold already then we already havent gotten the points so we should just do it here.
        {
            sampled = true; // say that we already sampled
            turnRight(0.25 * NINETY_DEG, 1); // push the gold thingy over
            turnLeft(0.25 * NINETY_DEG, 1); // get back on track
        }*/
    }
}