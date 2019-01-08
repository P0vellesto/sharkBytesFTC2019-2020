
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * This file provides basic Telop driving for a Pushbot robot.
 * The code is structured as an Iterative OpMode
 *
 * This OpMode uses the common Pushbot hardware class to define the devices on the robot.
 * All device access is managed through the HardwarePushbot class.
 *
 * This particular OpMode executes a basic Tank Drive Teleop for a PushBot
 * It raises and lowers the claw using the Gampad Y and A buttons respectively.
 * It also opens and closes the claws slowly using the left and right Bumper buttons.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="run_port_0")
//@Disabled
public class port_0_test extends LinearOpMode{

    /* Declare OpMode members. */
    private DcMotor port_0 = null; // motor on port zero

    private Servo servo_0;
    private Servo servo_1;

    public double clip(double var, double min, double max) {
        if (var > max) {        // if past max
            return max;
        } else if (var < min) { // if past min
            return min;
        } else {                // else its okay
            return var;
        }
    }

    @Override
    public void runOpMode() {

        port_0 = hardwareMap.get(DcMotor.class, "mDrv_l0"); // get the port thingy

        servo_0 = hardwareMap.get(Servo.class, "sArm");
        servo_1 = hardwareMap.get(Servo.class, "sBox");

        // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)

        waitForStart();

        double arm_pos = 0;
        double box_pos = 0;
        double mot_pow = 0;

        double sensitivity = 0.01;

        while (opModeIsActive()) {
            // change vars
            mot_pow = -gamepad1.right_stick_y; // change the motor power var

            if (-gamepad1.left_stick_y > 0.2) { // change the arm and box position vars
                arm_pos += sensitivity;
                box_pos -= sensitivity;
            } else if (-gamepad1.left_stick_y < -0.2) {
                arm_pos -= sensitivity;
                box_pos += sensitivity;
            }

            // validate vars
            arm_pos = clip(arm_pos, -1, 1);
            box_pos = clip(box_pos, -1, 1);
            
            // push vars to power
            port_0.setPower(mot_pow);

            servo_0.setPosition(arm_pos);
            servo_1.setPosition(box_pos);

            // debug: telemetry
            telemetry.addData("Motor pow", "pow = %.2f", mot_pow);
            telemetry.addData("Servo pos", "arm = %.2f", arm_pos);
            telemetry.addData("Servo pos", "box = %.2f", box_pos);
            telemetry.update();

        }

        // Instead of using the two lines of code below, you can just use my method.
        //robot.leftDrive.setPower(left);
        //robot.rightDrive.setPower(right);

        // Use gamepad left & right Bumpers to open and close the claw

    }

    /*
     * Code to run ONCE after the driver hits STOP
     */

}