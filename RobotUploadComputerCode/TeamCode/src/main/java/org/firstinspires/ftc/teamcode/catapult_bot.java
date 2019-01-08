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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


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

@TeleOp(name="catapult bot", group="Linear Opmode")
//@Disabled
public class catapult_bot extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    // Declare the drive motors with our naming conventions
    private DcMotor mDrv_r0 = null; // left front
    private DcMotor mDrv_r1 = null; // left back
    private DcMotor mDrv_l0 = null; // right front
    private DcMotor mDrv_l1 = null; // right back

    private DcMotor mArm = null; // rack and pinion motor
    private DcMotor mItk = null; // intake motor


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
    public void power(double leftPower, double rightPower, double intakePower)
    {
        //robot.leftDrive.setPower(leftPower);
        //robot.rightDrive.setPower(rightPower);


        //This int check is declared and is used the check whether the box is manually turned or not. If it is manually turned, it = 1.
        //Otherwise, it = 0.
        int check;
        check = 0;
        mDrv_r0.setPower(rightPower);
        mDrv_r1.setPower(rightPower);
        mDrv_l0.setPower(leftPower);
        mDrv_l1.setPower(leftPower);
        //mArm.setPower(pinionPower); // using the x button to trigger catapult now
        mItk.setPower(intakePower);
        //I dunno if this works. sBox.getPowerFloat(); is something you can do, but i dunno how to work that.
    }

    @Override
    public void runOpMode() {
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
        mArm = hardwareMap.get(DcMotor.class, "mPin"); // rack and pinion motor
        mItk = hardwareMap.get(DcMotor.class, "mItk"); // intake motor

        mDrv_l0.setDirection(DcMotor.Direction.FORWARD);
        mDrv_l1.setDirection(DcMotor.Direction.FORWARD);
        mDrv_r0.setDirection(DcMotor.Direction.REVERSE);
        mDrv_r1.setDirection(DcMotor.Direction.REVERSE);

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
            //leftDrive.setDirection(DcMotor.Direction.FORWARD);
            //rightDrive.setDirection(DcMotor.Direction.REVERSE);

        // Wait for the game to start (driver presses PLAY)
        double armPower = 0; // power for the arm motor
        int armUp = 0; // is the arm up? has it been returned to the down state? // 1-5 inclusive means it is up.

        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Setup a variable for each drive wheel to save power level for telemetry
            // these lines should be reversed because we always have right above left but sike its not so sux2sux
            double leftPower; // this is the discriber variable for storing the power of all the left drive motors
            double rightPower; // same as above but for the right side

            double itkPower; // and for the intake motor


            // Tank Mode uses one stick to control each wheel.
            // - This requires no math, but it is hard to drive forward slowly and keep straight.
            rightPower = -gamepad1.right_stick_y; // set the powers with the game sticks every frame
            leftPower  = -gamepad1.left_stick_y;  // same as above but not

            itkPower = -gamepad2.left_stick_x;

            // do the catapult
            if ( ! gamepad2.right_bumper) { // the bumper is released
                if (armUp > 0) { // if the arm is in the up state
                    mArm.setPower(-armPower);
                    armUp --; // subtract one from the arm state
                } else {
                    mArm.setPower(0);
                    armPower = Math.abs(gamepad2.right_stick_y); // this is here so that the power cannot be changed while the arm is moving
                }
            } else { // bumper is pressed
                mArm.setPower(armPower);
                armUp ++; // add one to the arm state. this allows us to keep track of how much the arm went up and go back down that much
            }

            // nicassa's function -- this sets power to the non-catapult motors
            power(leftPower, rightPower, itkPower);

            // Show the elapsed game time and wheel power.
            // telemetry is that little black console below all the controls that appears while the robot is running
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Drive", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.addData("arm charge", "(%.2f)", armPower);
            telemetry.addData("arm state", "(%d)", armUp);
            if (!gamepad2.right_bumper) {
                telemetry.addData("to release", "press the right bumper");
            } else {
                telemetry.addData("bumper", "pressed");
            }
            telemetry.update();
        }
    }
}
