
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
// motor imports
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
// vuforia/tensor imports
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

//@Disabled
@Autonomous(name="Crater Unhook w/ Tensor sampling", group="Final")
public class full_auto_base extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private static final String VUFORIA_KEY = "AXintg3/////AAABmfNtfFit30hUjY6OwyDJ+ApUdprK4NLN3D7Fo0PdokHRKNToq2vdRLN6q9SaOptQwr7stamCCbjslhfHa4xoVwA9S5CcAC6JzJZryjK5Epvtv/r9ifHBmEvr4Fe88f1jEye7/vHmfNwiM6sY9BTb59KyeNLxqkCCVKefNfwqGzl7GpGUiZQYbZhRYgxgnA6KbcYa55gQu9Lu0HbuxYgmmeqVUFDhGp6OMAxExRVQ7/jvsx4EXsuc1XAPjzTECzw5RyzrAzfuInK5dNAGVmIzzwucIBxq9y9TLiWKSh2KmVIp/54/Vh6YVD9pGrmRCWUo+pFpcS9m7J9DV4u13BkYqOWFenYmfI/2NymnyI80H50u";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the Tensor Flow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    // from here on out all imports and stuff are for motor control

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

    // others
    public void dropMarker()
    { // TODO: thISSSS

    }

    @Override
    public void runOpMode() {
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();



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
        double ONE_FOOT = .25;
        double NINTEY_DEG = 1;

        waitForStart();



        state ++;
        /** 1 Unhook from lander  */
        // extend rack and pinion to lower robot to ground
        turnLeft(1, NINTEY_DEG * 2); // unhook
        shift_bl(1, ONE_FOOT * 3.33); // go to first sample
        // TODO: this
        /** 2 Sampling sequence */
        int current = 0; // robot's perspective 0 = right, 1 = middle, 2 = left (facing field corner from lander)
        // current is how many times we've checked and the mineral was silver
        // when current is negative, then we have correctly sampled already and dont need to use the camera anymore

        if (tfod != null) {
            tfod.activate();
        }
        // if state starts at zero, state = 1 at this point...
        while (state == 2) {
            if (tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    /** ALBERT'S MODIFIED DETECTION: DETECT WHEN ONE OBJECT V0.1 */
                    if (updatedRecognitions.size() == 0) {
                        telemetry.addLine("Found nothing");
                    }
                    if (updatedRecognitions.size() == 1) { // when detecting one object
                        for (Recognition recognition : updatedRecognitions) { // for all one objects we detect (cuz i dont know how to get an item out of this weirdo array)
                            if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) { // if its a gold mineral
                                telemetry.addLine("Gold Detected!");
                                // TODO: code for pushing the mineral over
                                for (int i=0; i < 2-current; i++) { // depending on how many we passed before sucessful sapmling,
                                    shift_r(1, ONE_FOOT);
                                    // TODO: move left one foot
                                }
                                state ++;
                            } else if (recognition.getLabel().equals(LABEL_SILVER_MINERAL)) {
                                telemetry.addLine("Silver Detected!");
                                current ++;
                                shift_r(1, ONE_FOOT);
                            }
                        }
                    }
                    telemetry.update();
                    /** END ALBERT'S WEIRD THING */

                    // If needed, plug in the triple tensorflow here.
                }
            }
        }

        if (tfod != null) {
            tfod.shutdown();
        }

        /** 3 End of sampling, move to the depot */
        shift_r(1, ONE_FOOT * 2.08); // shift over until we are at the wall
        turnRight(1, 0.5*NINTEY_DEG); // turn to face the wall
        shift_l(1, ONE_FOOT * 6.25); // drive to depot
        state ++;
        

        /** 4 Drop marker */
        dropMarker(); // drop the marker
        state ++;
        
        /** 5 Turn around and park to crater */

        shift_r(1, ONE_FOOT * 6.66); // drive to and park in crater
        state ++;



    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
}
