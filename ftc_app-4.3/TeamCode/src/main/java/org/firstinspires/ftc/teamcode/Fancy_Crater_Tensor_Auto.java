
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
// motor imports
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


import java.util.List;

@Disabled
@Autonomous(name="Fancy Crater Unhook w/ Tensor called", group="Final")
public class Fancy_Crater_Tensor_Auto extends LinearOpMode
{
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private static final String VUFORIA_KEY = "AXintg3/////AAABmfNtfFit30hUjY6OwyDJ+ApUdprK4NLN3D7Fo0PdokHRKNToq2vdRLN6q9SaOptQwr7stamCCbjslhfHa4xoVwA9S5CcAC6JzJZryjK5Epvtv/r9ifHBmEvr4Fe88f1jEye7/vHmfNwiM6sY9BTb59KyeNLxqkCCVKefNfwqGzl7GpGUiZQYbZhRYgxgnA6KbcYa55gQu9Lu0HbuxYgmmeqVUFDhGp6OMAxExRVQ7/jvsx4EXsuc1XAPjzTECzw5RyzrAzfuInK5dNAGVmIzzwucIBxq9y9TLiWKSh2KmVIp/54/Vh6YVD9pGrmRCWUo+pFpcS9m7J9DV4u13BkYqOWFenYmfI/2NymnyI80H50u";
    double ARM_UP_NINETY = 1.5;
    double ARM_DOWN_NINETY = 0.5;


    // from here on out all imports and stuff are for motor control

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor mDrv_l0 = null;
    private DcMotor mDrv_l1 = null;
    private DcMotor mDrv_r0 = null;
    private DcMotor mDrv_r1 = null;

    private DcMotor mArm = null;
    private DcMotor mWch = null;
    private DcMotor mPin = null;
    private Servo sBox = null;
    //private Servo sBoxFloppy = null;

    DcMotor[] mDrive = new DcMotor[]{ mDrv_l0, mDrv_l1, mDrv_r0, mDrv_r1 };

    //DcMotor[] mDrv_l = new DcMotor[]{ mDrv_l0, mDrv_l1 };
    //DcMotor[] mDrv_r = new DcMotor[]{ mDrv_r0, mDrv_r1 };

    public void runMotors(DcMotor[] motors, double[] power, double holdTime)
    { // take an array of motors and powers and drive those motors at that power for holdTime
        telemetry.update();

        for (int i = 0; i < motors.length; i++) { motors[i].setPower(power[(i+1)%(power.length)]); } // set power to all motors

        ElapsedTime holdTimer = new ElapsedTime(); // make a timer
        holdTimer.reset(); // set to 0
        while (opModeIsActive() && holdTimer.time() < holdTime) {} // wait for timer to end

        for (int i = 0; i < motors.length; i++) { motors[i].setPower(0); } // stop all motors
    }

    // turn
    public void turnRight(double power, double holdTime)
    { // turn right for holdTime at power
        telemetry.addData("Turn Right",  "pow:%3f hold:%3f", power, holdTime);
        runMotors(new DcMotor[]{ mDrv_l0, mDrv_r0, mDrv_l1, mDrv_r1 }, new double[]{ power, -power }, holdTime); // run the motors
    }
    public void turnLeft(double power, double holdTime)
    { // turn left for holdTime at power
        telemetry.addData("Turn Left",  "pow:%3f hold:%3f", power, holdTime);
        runMotors(new DcMotor[]{ mDrv_l0, mDrv_r0, mDrv_l1, mDrv_r1 }, new double[]{ -power, power }, holdTime); // run the motors
    }

    // lattice shifting
    public void shift_f(double power, double holdTime)
    { // move forward for holdTime at power
        telemetry.addData("Shift Forwards",  "pow:%3f hold:%3f", power, holdTime);
        runMotors(new DcMotor[]{ mDrv_l0, mDrv_r0, mDrv_l1, mDrv_r1 }, new double[]{ power }, holdTime); // run the motors
    }
    public void shift_b(double power, double holdTime)
    { // move backward for holdTime at power
        telemetry.addData("Shift Backwards",  "pow:%3f hold:%3f", power, holdTime);
        runMotors(new DcMotor[]{ mDrv_l0, mDrv_r0, mDrv_l1, mDrv_r1 }, new double[]{ -power }, holdTime); // run the motors
    }
    public void shift_l(double power, double holdTime)
    { // shift left for holdTime at power
        telemetry.addData("Shift Left",  "pow:%3f hold:%3f", power, holdTime);
        runMotors(new DcMotor[]{ mDrv_r0, mDrv_l0, mDrv_l1, mDrv_r1 }, new double[]{ power, -power }, holdTime); // run the motors
    }
    public void shift_r(double power, double holdTime)
    { // shift right for holdTime at power
        telemetry.addData("Shift Right",  "pow:%3f hold:%3f", power, holdTime);
        runMotors(new DcMotor[]{ mDrv_r0, mDrv_l0, mDrv_l1, mDrv_r1 }, new double[]{ -power, power }, holdTime); // run the motors
    }

    // diagonal shifting
    public void shift_fl(double power, double holdTime)
    { // diagonal shift left and forwards
        telemetry.addData("Shift Front Left",  "pow:%3f hold:%3f", power, holdTime);
        runMotors(new DcMotor[]{ mDrv_r0, mDrv_l1 }, new double[]{ power }, holdTime); // run the motors
    }
    public void shift_fr(double power, double holdTime)
    { // diagonal shift right and forwards
        telemetry.addData("Shift Front Right",  "pow:%3f hold:%3f", power, holdTime);
        runMotors(new DcMotor[]{ mDrv_r1, mDrv_l0 }, new double[]{ power }, holdTime); // run the motors
    }
    public void shift_bl(double power, double holdTime)
    { // diagonal shift left and backwards
        telemetry.addData("Shift Back Left",  "pow:%3f hold:%3f", power, holdTime);
        runMotors(new DcMotor[]{ mDrv_r1, mDrv_l0 }, new double[]{ -power }, holdTime); // run the motors
    }
    public void shift_br(double power, double holdTime)
    { // diagonal shift right and backwards
        telemetry.addData("Shift Back Right",  "pow:%3f hold:%3f", power, holdTime);
        runMotors(new DcMotor[]{ mDrv_r0, mDrv_l1 }, new double[]{ -power }, holdTime); // run the motors
    }

    // control
    public void pause(double holdTime)
    {
        telemetry.addData("holding for", "time: %3f", holdTime);
        telemetry.update();
        ElapsedTime holdTimer = new ElapsedTime(); // make a timer
        holdTimer.reset(); // set to 0
        while (opModeIsActive() && holdTimer.time() < holdTime) {} // wait for timer to end
    }
    public void stop_drive()
    {
        telemetry.addLine("Stopped");
        telemetry.update();
        for (int i=0; i<mDrive.length; i++)
        {
            mDrive[i].setPower(0);
        }
    }

    // arm stuff & and pinion


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
    { // not used
        ElapsedTime holdTimer = new ElapsedTime();
        holdTimer.reset();
        //double boxPosition
        while (opModeIsActive() && holdTimer.time() < holdTime)
        {
            //boxPosition += turnAmount; // Adds the amount you want to turn the box to the current position.
            sBox.setPosition(turnAmount); // Sets the new position of the box
        }
        sBox.setPosition(0.5);
    }
    public void stopBoxServo()
    {
        sBox.setPosition(0.5);
    }
    /*public void turnFloppy(double holdTime, double turnAmount)
    {
        ElapsedTime holdTimer = new ElapsedTime();
        holdTimer.reset();
        while (opModeIsActive() && holdTimer.time() < holdTime)
        {
            sBoxFloppy.setPosition(turnAmount);
        }
        sBoxFloppy.setPosition(0.5);
    }
    public void stopFloppy()
    {
        sBoxFloppy.setPosition(0.5);
    }*/
    public void setBoxPosition(double position)
    { // not used
        if (opModeIsActive())
        {
            sBox.setPosition(position);
        }
    }

    public void raiseRackPinionMotor(double holdTime, double power)
    {
        telemetry.addLine("Rack and pinion is being extended for " + holdTime + "seconds.");
        ElapsedTime holdTimer = new ElapsedTime();
        holdTimer.reset();
        while (opModeIsActive() && holdTimer.time() < holdTime)
        {
            mPin.setPower(-power);
            mArm.setPower(.33);
        }
        mPin.setPower(0);
    }

    public void lowerRackPinionMotor(double holdTime, double power)
    {
        telemetry.addLine("Rack and pinion is being unextended for " + holdTime + "seconds.");
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

    // others
    public void dropMarker()
    {
        telemetry.addLine("Marker is being dropped.");
        turnArm(ARM_UP_NINETY, -1); // turns the arm to put down the marker
        turnBoxServo(1, 1); // turns the box to drop marker
        //turnFloppy(1, 1); // turns the floppy tube to make sure that the marker drops
        turnArm(ARM_DOWN_NINETY, 1); // puts the arm back in the air so that the robot can be driven
    }

    @Override
    public void runOpMode()
    {
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.


        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        mDrv_l0 = hardwareMap.get(DcMotor.class, "mDrv_l0");
        mDrv_l1 = hardwareMap.get(DcMotor.class, "mDrv_l1");
        mDrv_r0 = hardwareMap.get(DcMotor.class, "mDrv_r0");
        mDrv_r1 = hardwareMap.get(DcMotor.class, "mDrv_r1");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        mDrv_l0.setDirection(DcMotor.Direction.REVERSE);
        mDrv_l1.setDirection(DcMotor.Direction.REVERSE);
        mDrv_r0.setDirection(DcMotor.Direction.FORWARD);
        mDrv_r1.setDirection(DcMotor.Direction.FORWARD);

        mArm = hardwareMap.get(DcMotor.class, "mArm");
        mWch = hardwareMap.get(DcMotor.class, "mWch");
        mPin = hardwareMap.get(DcMotor.class, "mPin");
        sBox = hardwareMap.get(Servo.class, "sBox");
        //sBoxFloppy = hardwareMap.get(Servo.class, "sBoxFloppy");




        int state = 0; // controls what stage of auto we are in:
        /**
         * 0  before start
         * 1  unhooking from lander and moving to sampling field
         * 2  sampling sequence
         * 3  end of sampling, move to the depot
         * 4  drop marker
         * 5  turn around and park to crater
         * 6  end of auto
         */

        // TODO: this
        double ONE_FOOT = 0.4;
        double NINTEY_DEG = 0.585;

        waitForStart();



        state ++;
        /** 1 Unhooking from lander and moving to sampling field */
        raiseRackPinionMotor(4.5, 1); // extend rack
        turnLeft(1, NINTEY_DEG * 2); // turn around to unhook
        shift_bl(1, ONE_FOOT * 3.33); // go to first sample
        //
        /** 2 Sampling sequence */
        int current = 0; // robot's perspective 0 = right, 1 = middle, 2 = left (facing field corner from lander)
        // current is how many times we've checked and the mineral was silver
        // when current is negative, then we have correctly sampled already and don't need to use the camera anymore

        FancyNickTensor fancyNickTensor = new FancyNickTensor();
        telemetry.addLine("Mineral sampling commencing");
        telemetry.update();
        while (state == 2)
        {
            if (fancyNickTensor.getNumberOfRecognitions() == 1)
            {
                if (fancyNickTensor.returnIfGold())
                { // if its a gold mineral
                    telemetry.addLine("Gold Detected!");
                    telemetry.update();
                    pause(0.5);
                    shift_b(1, 0.25); // hit the gold cube
                    shift_f(1, 0.25); // realign
                    for (int i = 0; i < 2 - current; i++)
                    { // depending on how many we passed before sucessful sapmling,
                        shift_r(1, ONE_FOOT);
                    }
                    state++;
                }
                else if (fancyNickTensor.returnIfSilver())
                {
                    telemetry.addLine("Silver Detected!");
                    telemetry.update();
                    current++;
                    pause(0.5);
                    shift_r(1, ONE_FOOT);
                }
            }
            else
            {
                telemetry.addLine("The number of recognized materials (gold cubes and silver balls) is not one, but it needs to be one for the code to work properly. The number is " + fancyNickTensor.getNumberOfRecognitions() + ".");
                telemetry.update();
                current++;
                pause(0.5);
                shift_r(1, ONE_FOOT);
                throw new IllegalArgumentException("The number of recognized materials (gold cubes and silver balls) is not one, but it needs to be one for the code to work properly. The number is " + fancyNickTensor.getNumberOfRecognitions() + ". This is a FATAL ERROR");
            }

            /** 3 End of sampling, move to the depot */
            shift_r(1, ONE_FOOT * 2.08); // shift over until we are at the wall
            turnLeft(1, 0.5 * NINTEY_DEG); // face back to the wall
            shift_r(1, ONE_FOOT * 6.25); // go to depo
            state++;


            /** 4 Drop marker */
            dropMarker(); // drop the marker
            state++;

            /** 5 Turn around and park to crater */

            shift_l(1, ONE_FOOT * 6.66); // go to crater and park
            state++;
        }
    }
}