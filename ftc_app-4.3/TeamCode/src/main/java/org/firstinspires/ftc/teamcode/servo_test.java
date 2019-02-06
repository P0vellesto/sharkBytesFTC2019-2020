
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

@TeleOp(name="SERVO TEST")

public class servo_test extends LinearOpMode{
    private Servo servo = null; // servo on port zero

    @Override
    public void runOpMode() {

        servo = hardwareMap.get(Servo.class, "servo"); // get the port thingy

        waitForStart();

        while (opModeIsActive())
        {
            float thresh = 0.1f;
            float direction = (-gamepad1.right_stick_y); // 10f to say that its a float cuz java syntax is weird
            //servo.setPosition(direction);
            if (direction > thresh)
            {
                servo.setPosition(direction);
            }
            else if (direction < -thresh)
            {
                servo.setPosition(direction);
            }
            else
            {
                servo.setPosition(0.5);
            }
            telemetry.addData("servo",  "power = %.2f", direction);
            telemetry.update();
        }
    }
}