/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Mecanum_Tele", group="Linear Opmode")
//@Disabled
public class Mecanum_TeleOp extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    // Declare the drive motors with our naming conventions
    private DcMotor mDrv_r0 = null; // right front
    private DcMotor mDrv_r1 = null; // right back
    private DcMotor mDrv_l0 = null; // left front
    private DcMotor mDrv_l1 = null; // left back

    private DcMotor mPin = null; // rack and pinion motor

    private DcMotor mArm = null; // arm base servo
    private DcMotor mWch = null; // arm extending motor
    private Servo sBox = null; // servo that turns the box on the arm
    private Servo sFlp = null; // servo that turns the ziplocks in the box

    boolean aPressed = false; // stops the box servo
    //boolean sensitivity = false; // if true, the drive train is half speed
    int sensitivityValue = 1; // is used to go through power settings
    boolean sensitivitySwitch = false; // used to limit the switching of sensitivity to once per second
    String telementryDirection; // used to display the direction in the telemetry
    boolean joystick = false; // used to toggle between using and not using the joystick for power
    boolean driveMode = true; // used to toggle between using mec easy drive and tank drive

    /* Albert's functions (deprecated)
    public void powerRight(double power) { // set power to the right motors
        mDrv_r0.setPower(power);
        mDrv_r1.setPower(power);
    }

    public void powerLeft(double power) { // set power to the left motors
        mDrv_l0.setPower(power);
        mDrv_l1.setPower(power);
    }
*/
    protected void mecDrive(double leftJoystickY, double rightJoystickY, double leftJoystickX, double rightJoystickX)
    {
        // if statement for going forward or backward
        if ((leftJoystickY >= 0.5 && rightJoystickY >= 0.5) || (leftJoystickY <= -0.5 && rightJoystickY <= -0.5))
        {
            mDrv_l0.setPower(leftJoystickY / sensitivityValue);
            mDrv_l1.setPower(leftJoystickY / sensitivityValue);
            mDrv_r0.setPower(rightJoystickY / sensitivityValue);
            mDrv_r1.setPower(rightJoystickY / sensitivityValue);
        }
        // if statement for going to the left or right
        if ((leftJoystickX <= -0.5 && rightJoystickX <= -0.5) || (leftJoystickX >= 0.5 && rightJoystickX >= 0.5))
        {
            mDrv_l0.setPower(leftJoystickX / sensitivityValue);
            mDrv_l1.setPower(-leftJoystickX / sensitivityValue);
            mDrv_r0.setPower(-rightJoystickX / sensitivityValue);
            mDrv_r1.setPower(rightJoystickX / sensitivityValue);
        }
        // if statement for turning left or right
        if ((leftJoystickY >= 0.5 && rightJoystickY <= -0.5) || (leftJoystickY <= -0.5 && rightJoystickY >= 0.5))
        {
            mDrv_l0.setPower(leftJoystickX / sensitivityValue);
            mDrv_l1.setPower(leftJoystickX / sensitivityValue);
            mDrv_r0.setPower(rightJoystickX / sensitivityValue);
            mDrv_r1.setPower(rightJoystickX / sensitivityValue);
        }
        // if statement for going left forward diagonal
        if ((leftJoystickY >= 0.5 && gamepad1.right_bumper) || (leftJoystickY >= 0.5 && gamepad1.left_bumper))
        {
            mDrv_l1.setPower(leftJoystickY / sensitivityValue);
            mDrv_r0.setPower(leftJoystickY / sensitivityValue);
        }
        // if statement for going left backward diagonal
        if ((leftJoystickY <= -0.5 && gamepad1.right_bumper) || (leftJoystickY <= -0.5 && gamepad1.left_bumper))
        {
            mDrv_l0.setPower(leftJoystickY / sensitivityValue);
            mDrv_r1.setPower(leftJoystickY / sensitivityValue);
        }
        // if statement for going right forward diagonal
        if ((rightJoystickY >= 0.5 && gamepad1.right_bumper) || (rightJoystickY >= 0.5 && gamepad1.left_bumper))
        {
            mDrv_l0.setPower(leftJoystickY / sensitivityValue);
            mDrv_r1.setPower(leftJoystickY / sensitivityValue);
        }
        // if statement for going right backward diagonal
        if ((rightJoystickY <= -0.5 && gamepad1.right_bumper) || (rightJoystickY <= -0.5 && gamepad1.left_bumper))
        {
            mDrv_l1.setPower(leftJoystickY / sensitivityValue);
            mDrv_r0.setPower(leftJoystickY / sensitivityValue);
        }
    }
    protected void doNotChangeSensitivity(double holdTime)
    {
        ElapsedTime holdTimer = new ElapsedTime();
        while (opModeIsActive() && holdTimer.time() < holdTime)
        {
            sensitivitySwitch = true;
        }
        sensitivitySwitch = false;
    }
    protected void armPower(double pinionPower, double boxPower, double armPower, double wchPower)
    {
        boolean check = false;
        mPin.setPower(pinionPower);
        mWch.setPower(wchPower);
        //I dunno if this works. sBox.getPowerFloat(); is something you can do, but i dunno how to work that.
        aPressed = false;

        if (sBox != null)
        {
            if (gamepad2.a)
            {
                aPressed = true;
                double currentLocation = sBox.getPosition();
                sBox.setPosition(currentLocation);
            }
            else if (boxPower > 0.25)
            {
                sBox.setPosition(boxPower);
                check = true;
            }
            else if (boxPower < -0.25)
            {
                sBox.setPosition(boxPower);
                check = true;
            }
        }

        if (sFlp != null)
        {
            if (gamepad2.left_bumper)
            {
                sFlp.setPosition(-1);
            }
            else if (gamepad2.right_bumper)
            {
                sFlp.setPosition(1);
            }
            else
            {
                sFlp.setPosition(0.515);
            }
        }

        // idk why this is in the arm method, should be in mec power easy and tank drive
        if (gamepad1.a)
        {
            double holdTime = 0.25;
            sensitivityValue = 2;
            doNotChangeSensitivity(holdTime);
        }
        else if (gamepad1.b)
        {
            double holdTime = 0.25;
            sensitivityValue = 10; doNotChangeSensitivity(holdTime);
        }
        else if (gamepad1.x)
        {
            double holdTime = 0.25;
            sensitivityValue = 1; doNotChangeSensitivity(holdTime);
        }
        else if (gamepad1.y)
        {
            joystick = !joystick;
        }

        if (armPower > 0.1)
        {
            mArm.setPower(armPower);
            if (!check)
            {
                if (sBox != null)
                {
                    sBox.setPosition(-armPower);
                }
            }
        }
        else if (armPower < -0.1)
        {
            mArm.setPower(armPower);
            if (sBox != null)
            {
                sBox.setPosition(-armPower);
            }
        }
    }
    protected void mecPowerEasy(double dirX, double dirY, double power)
    {
        boolean turning = false;
        if (gamepad1.right_bumper)
        {
            turning = true;
            mDrv_l0.setPower(power / sensitivityValue);
            mDrv_l1.setPower(power / sensitivityValue);
            mDrv_r0.setPower(-power / sensitivityValue);
            mDrv_r1.setPower(-power / sensitivityValue);
            telementryDirection = "turningRight";
        }
        if (gamepad1.left_bumper)
        {
            turning = true;
            mDrv_l0.setPower(-power / sensitivityValue);
            mDrv_l1.setPower(-power / sensitivityValue);
            mDrv_r0.setPower(power / sensitivityValue);
            mDrv_r1.setPower(power / sensitivityValue);
            telementryDirection = "turningLeft";
        }
        if (!turning)
        {
            String direction = evaluateDirection(dirX, dirY);
            if (direction == "forward")
            {
                mDrv_l0.setPower(power / sensitivityValue);
                mDrv_l1.setPower(power / sensitivityValue);
                mDrv_r0.setPower(power / sensitivityValue);
                mDrv_r1.setPower(power / sensitivityValue);
            }
            else if (direction == "backward")
            {
                mDrv_l0.setPower(-power / sensitivityValue);
                mDrv_l1.setPower(-power / sensitivityValue);
                mDrv_r0.setPower(-power / sensitivityValue);
                mDrv_r1.setPower(-power / sensitivityValue);
            }
            else if (direction == "left")
            {
                mDrv_l0.setPower(-power / sensitivityValue);
                mDrv_l1.setPower(power / sensitivityValue);
                mDrv_r0.setPower(power / sensitivityValue);
                mDrv_r1.setPower(-power / sensitivityValue);
            }
            else if (direction == "right")
            {
                mDrv_l0.setPower(power / sensitivityValue);
                mDrv_l1.setPower(-power / sensitivityValue);
                mDrv_r0.setPower(-power / sensitivityValue);
                mDrv_r1.setPower(power / sensitivityValue);
            }
            else if (direction == "left-forward")
            {
                mDrv_l0.setPower(0);
                mDrv_l1.setPower(power / sensitivityValue);
                mDrv_r0.setPower(power / sensitivityValue);
                mDrv_r1.setPower(0);
            }
            else if (direction == "left-backward")
            {
                mDrv_l0.setPower(-power / sensitivityValue);
                mDrv_l1.setPower(0);
                mDrv_r0.setPower(0);
                mDrv_r1.setPower(-power / sensitivityValue);
            }
            else if (direction == "right-forward")
            {
                mDrv_l0.setPower(power / sensitivityValue);
                mDrv_r1.setPower(power / sensitivityValue);
                mDrv_l1.setPower(0);
                mDrv_r0.setPower(0);
            }
            else if (direction == "right-backward")
            {
                mDrv_l0.setPower(0);
                mDrv_l1.setPower(-power / sensitivityValue);
                mDrv_r0.setPower(-power / sensitivityValue);
                mDrv_r1.setPower(0);
            }
            else if (direction == "unknown")
            {
                mDrv_l0.setPower(0);
                mDrv_l1.setPower(0);
                mDrv_r0.setPower(0);
                mDrv_r1.setPower(0);
            }
            else
            {
                mDrv_l0.setPower(0);
                mDrv_l1.setPower(0);
                mDrv_r0.setPower(0);
                mDrv_r1.setPower(0);
            }
        }
        if (power == 0)
        {
            mDrv_l0.setPower(0);
            mDrv_l1.setPower(0);
            mDrv_r0.setPower(0);
            mDrv_r1.setPower(0);
        }
    }
    protected void tankDrive(double joystickOneY, double joystickTwoY)
    {
        mDrv_l0.setPower(joystickOneY / sensitivityValue);
        mDrv_l1.setPower(joystickOneY / sensitivityValue);
        mDrv_r0.setPower(joystickTwoY / sensitivityValue);
        mDrv_r1.setPower(joystickTwoY / sensitivityValue);
    }
    protected String evaluateDirection(double dirX, double dirY)
    {
        String direction = "unknown";
        double threshold = 0.25;
        boolean notForOrBack = true;
        if (dirY <= 0.5 && dirY >= -0.5)
        {
            notForOrBack = true;
        }
        else if (dirY >= 0.5 )
        {
            notForOrBack = false;
            direction = "backward";
        }
        else if (dirY <= -0.5)
        {
            notForOrBack = false;
            direction = "forward";
        }
        if (dirX <= -threshold)
        {
            if (notForOrBack)
            {
                direction = "left";
            }
            else if (direction == "forward")
            {
                direction = "left-forward";
            }
            else if (direction == "backward")
            {
                direction = "left-backward";
            }
        }
        else if (dirX >= threshold)
        {
            if (notForOrBack)
            {
                direction = "right";
            }
            else if (direction == "forward")
            {
                direction = "right-forward";
            }
            else if (direction == "backward")
            {
                direction = "right-backward";
            }
        }
        telementryDirection = direction;
        return direction;
    }

    @Override
    public void runOpMode()
    {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        // drive motors
        mDrv_r0  = hardwareMap.get(DcMotor.class, "mDrv_r0");
        mDrv_r1  = hardwareMap.get(DcMotor.class, "mDrv_r1");
        mDrv_l0  = hardwareMap.get(DcMotor.class, "mDrv_l0");
        mDrv_l1  = hardwareMap.get(DcMotor.class, "mDrv_l1");
        // rack and pinion, and intake
        mPin = hardwareMap.get(DcMotor.class, "mPin"); // rack and pinion motor
        // arm motors and servos
        mArm = hardwareMap.get(DcMotor.class, "mArm"); // arm base motor
        mWch = hardwareMap.get(DcMotor.class, "mWch"); // arm extending motor
        if (hardwareMap.get(Servo.class, "sBox") != null)
        {
            sBox = hardwareMap.get(Servo.class, "sBox"); // box that holds commodities
        }
        if (hardwareMap.get(Servo.class, "sBoxFloppy") != null)
        {
            sFlp = hardwareMap.get(Servo.class, "sBoxFloppy"); // servo that spins the floppy tubes in the box
        }

        mDrv_l0.setDirection(DcMotor.Direction.FORWARD);
        mDrv_l1.setDirection(DcMotor.Direction.FORWARD);
        mDrv_r0.setDirection(DcMotor.Direction.REVERSE);
        mDrv_r1.setDirection(DcMotor.Direction.REVERSE);

        mWch.setDirection(DcMotor.Direction.FORWARD);

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        //leftDrive.setDirection(DcMotor.Direction.FORWARD);
        //rightDrive.setDirection(DcMotor.Direction.REVERSE);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive())
        {

            // Setup a variable for each drive wheel to save power level for telemetry
            // these lines should be reversed because we always have right above left but sike its not so sux2sux
            // these are use
            double leftX; // used for complicated mec
            double leftY; // used for complicated mec
            double rightX; // used for complicated mec
            double rightY; // used for complicated mec
            double dirX; // used for simple mec
            double dirY; // used for simple mec
            double power; // used to control how much power

            double pinPower; // power for the pinion motor

            double boxPower; // for the box servo
            double flpPower; // for the floppy tube intake

            double armPower; // for the servo at the base of the arm
            double wchPower; //for the motor that extends the arm

            rightY = gamepad1.right_stick_y;
            leftY  = gamepad1.left_stick_y;
            rightX = gamepad1.right_stick_x;
            leftX = gamepad1.left_stick_x;

            dirX = gamepad1.left_stick_x;
            dirY = gamepad1.left_stick_y;
            if (joystick)
            {
                power = gamepad1.right_stick_y;
                if (power < 0)
                {
                    power = -power;
                }
            }
            else
            {
                power = 1;
            }

            pinPower = -gamepad2.left_stick_y;

            boxPower = gamepad2.left_stick_x;

            wchPower = -gamepad2.right_stick_x;
            armPower = -gamepad2.right_stick_y/1.5;


            // Send calculated power to wheels
            /* alberts functions
            powerRight(rightPower);
            powerLeft(leftPower);
            */
            // nicassa's function -- this sets power to the motors
            armPower(pinPower, boxPower, armPower, wchPower); // used to control arm
            if (driveMode)
            {
                mecPowerEasy(dirX, dirY, power); // used to control drive train
            }
            else
            {
                tankDrive(-gamepad1.left_stick_y, -gamepad1.right_stick_y);
            }

            if (gamepad1.dpad_up)
            {
                driveMode = false;
            }
            else if (gamepad1.dpad_down)
            {
                driveMode = true;
            }

            // Show the elapsed game time and wheel power.
            // telemetry is that little black console below all the controls that appears while the robot is running
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            //telemetry.addData("Drive", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.addData("Rack and Pinion", "(%.2f)", pinPower);
            telemetry.addData("arm", "Arm (%.2f), Winch (%.2f), Box (%.2f)", armPower, wchPower, boxPower);
            if (driveMode)
            {
                telemetry.addData("power", "JoystickPower (%.2f)", power);
            }
            else
            {
                telemetry.addLine("Left joystick: " + -gamepad1.left_stick_y + ". Right joystick: " + -gamepad1.right_stick_y + ".");
            }
            /*if (aPressed)
            {
                telemetry.addLine("A is pressed");
            }
            else
            {
                telemetry.addLine("A is not pressed");
            }*/
            if (sensitivityValue == 1)
            {
                telemetry.addLine("100% Power");
            }
            else if (sensitivityValue == 2)
            {
                telemetry.addLine("50% Power");
            }
            else if (sensitivityValue == 4)
            {
                telemetry.addLine("25% Power");
            }
            else if (sensitivityValue == 10)
            {
                telemetry.addLine("10% Power");
            }
            if (joystick)
            {
                telemetry.addLine("Joystick is being used for power");
            }
            else
            {
                telemetry.addLine("Joystick is not being used for power");
            }
            telemetry.addLine("Direction is " + telementryDirection);
            if (driveMode)
            {
                telemetry.addLine("Currently using Mecanum Drive. To switch to the special, magical, fagnificent, easter egg of Tank Drive, press dpad up.");
            }
            else
            {
                telemetry.addLine("Currently using Tank Drive, to switch back, press dpad down.");
            }
            telemetry.update();
        }
    }
}
