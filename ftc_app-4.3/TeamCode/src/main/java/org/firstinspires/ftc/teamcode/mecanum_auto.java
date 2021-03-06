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
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
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

@Autonomous(name="MecanumAuto", group="Mecanum")
//@Disabled
public class mecanum_auto extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor mDrv_l0 = null;
    private DcMotor mDrv_l1 = null;
    private DcMotor mDrv_r0 = null;
    private DcMotor mDrv_r1 = null;

    DcMotor[] mDrive = new DcMotor[]{ mDrv_l0, mDrv_l1, mDrv_r0, mDrv_r1 };

    //DcMotor[] mDrv_l = new DcMotor[]{ mDrv_l0, mDrv_l1 };
    //DcMotor[] mDrv_r = new DcMotor[]{ mDrv_r0, mDrv_r1 };

    public void runMotors(DcMotor[] motors, double[] power, double holdTime)
    { // take an array of motors and powers and drive those motors at that power for holdTime
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
    };
    public void turnLeft(double power, double holdTime)
    { // turn left for holdTime at power
        telemetry.addData("Turn Left",  "pow:%3f hold:%3f", power, holdTime);
        runMotors(new DcMotor[]{ mDrv_l0, mDrv_r0, mDrv_l1, mDrv_r1 }, new double[]{ -power, power }, holdTime); // run the motors
    };

    // lattice shifting
    public void shift_f(double power, double holdTime)
    { // move forward for holdTime at power
        telemetry.addData("Shift Forwards",  "pow:%3f hold:%3f", power, holdTime);
        runMotors(new DcMotor[]{ mDrv_l0, mDrv_r0, mDrv_l1, mDrv_r1 }, new double[]{ power }, holdTime); // run the motors
    };
    public void shift_b(double power, double holdTime)
    { // move backward for holdTime at power
        telemetry.addData("Shift Backwards",  "pow:%3f hold:%3f", power, holdTime);
        runMotors(new DcMotor[]{ mDrv_l0, mDrv_r0, mDrv_l1, mDrv_r1 }, new double[]{ -power }, holdTime); // run the motors
    };
    public void shift_l(double power, double holdTime)
    { // shift left for holdTime at power
        telemetry.addData("Shift Left",  "pow:%3f hold:%3f", power, holdTime);
        runMotors(new DcMotor[]{ mDrv_r0, mDrv_l0, mDrv_l1, mDrv_r1 }, new double[]{ power, -power }, holdTime); // run the motors
    };
    public void shift_r(double power, double holdTime)
    { // shift right for holdTime at power
        telemetry.addData("Shift Right",  "pow:%3f hold:%3f", power, holdTime);
        runMotors(new DcMotor[]{ mDrv_r0, mDrv_l0, mDrv_l1, mDrv_r1 }, new double[]{ -power, power }, holdTime); // run the motors
    };

    // diagonal shifting
    public void shift_fl(double power, double holdTime)
    { // diagonal shift left and forwards
        telemetry.addData("Shift Front Left",  "pow:%3f hold:%3f", power, holdTime);
        runMotors(new DcMotor[]{ mDrv_r0, mDrv_l1 }, new double[]{ power }, holdTime); // run the motors
    };
    public void shift_fr(double power, double holdTime)
    { // diagonal shift right and forwards
        telemetry.addData("Shift Front Right",  "pow:%3f hold:%3f", power, holdTime);
        runMotors(new DcMotor[]{ mDrv_r1, mDrv_l0 }, new double[]{ power }, holdTime); // run the motors
    };
    public void shift_bl(double power, double holdTime)
    { // diagonal shift left and backwards
        telemetry.addData("Shift Back Left",  "pow:%3f hold:%3f", power, holdTime);
        runMotors(new DcMotor[]{ mDrv_r1, mDrv_l0 }, new double[]{ -power }, holdTime); // run the motors
    };
    public void shift_br(double power, double holdTime)
    { // diagonal shift right and backwards
        telemetry.addData("Shift Back Right",  "pow:%3f hold:%3f", power, holdTime);
        runMotors(new DcMotor[]{ mDrv_r0, mDrv_l1 }, new double[]{ -power }, holdTime); // run the motors
    };

    // control
    public void pause(double holdTime)
    {
        telemetry.addData("holding for", "time: %3f", holdTime);
        ElapsedTime holdTimer = new ElapsedTime(); // make a timer
        holdTimer.reset(); // set to 0
        while (opModeIsActive() && holdTimer.time() < holdTime) {} // wait for timer to end

    }
    public void stop_drive()
    {
        telemetry.addLine("Stopped");
        for (int i=0; i<mDrive.length; i++)
        {
            mDrive[i].setPower(0);
        }
    }
    @Override
    public void runOpMode() {
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

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        double debug_hold = 5;

        shift_f(1, debug_hold*4); pause(1);
        shift_b(1, debug_hold); pause(1);
        shift_l(1, debug_hold); pause(1);
        shift_r(1, debug_hold); pause(1);
        shift_fl(1, debug_hold); pause(1);
        shift_br(1, debug_hold); pause(1);
        shift_fr(1, debug_hold); pause(1);
        shift_bl(1, debug_hold); pause(1);
        turnRight(1, debug_hold); pause(1);
        turnLeft(1, debug_hold); pause(1);


        // crater pseudo code:
        /*
        raise rack to lower robot
        move slightly to unhook
        forward right diagonal to first sample
        check sample
        horizontal left to second sample
        check sample
        horizontal left to third sample
        check sample
        move horizontal left till you are at the end of the crater, at the wall
        turn right 45 degrees so that you face the wall
        move horizontal left to depo
        drop marker
        move horizontal right to crater and park
         */
        // depo pseudo code
        /*
        raise rack to lower robot
        move slightly to unhook
        forward right diagonal to first sample
        check sample
        horizontal left to second sample
        check sample
        horizontal left to third sample
        check sample
        horizontal left until you are at the wall to the left of the depo
        turn right 45 degrees so that you face the wall
        move horizontal right to go into depo
        drop marker
        move horizontal left to crater and park
         */

        // TODO: add the actual auto and also get the vars for holdtime to distance/rotation

        telemetry.update();
    }
}
