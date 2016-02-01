package app.edi.palmprothesismotionmonitoring;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import lv.edi.SmartWearProcessing.Sensor;
import lv.edi.SmartWearProcessing.SensorDataProcessing;

/**
 * Created by richards on 16.7.1.
 *
 * class for background service for processing sensor data. Service computes sensor orientation
 * and provides with data like angle between sensors
 */
public class ProcessingService {
    private Vector<Sensor> sensors;
    private boolean isProcessing = false;
    private Timer timer;
    private final float[] Z ={0, 0, 1};

    private float lowerThreshold=40;       // lower threshold for movement counting
    private float upperThreshold=70;       // upper threshold for movement counting
    private int movementCounts=0;
    private boolean goingUp=true;           // shows if direction of movement (increasing angle)

    private float currentAngle;         // computed angle in degrees
    Vector<ProcessingServiceEventListener> listeners = new Vector<ProcessingServiceEventListener>();
    /**
     * Constructor specifying allocated Sensor objects to use for processing
     * at the moment
     * @param sensors Vector containing Sensor objects on which to perform processing.
     *                current implementation uses only first two sensors
     * @throws IllegalArgumentException if vector has less than two sensors
     */
    public ProcessingService(Vector<Sensor> sensors) {
        if (sensors.size() < 2) {
            throw (new IllegalArgumentException("not enough sensors. service works with >= 2"));

        }
        this.sensors = sensors;
    }

    /**
     * starts processing service and executes at specified time interval
     * @param period processing period in milliseconds
     */
    public void startProcessing(long period){
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask(){
            public void run() {// fetch data
                float[] acc0 = sensors.get(0).getAccNorm();
                float[] acc = sensors.get(3).getAccNorm();
                float[] magn0 = sensors.get(0).getMagNorm();
                float[] magn = sensors.get(3).getMagNorm();

                Log.d("PROCESSING_SERVICE", " " + acc0[0] + " " + acc0[1] + " " + acc[2]);
                Log.d("PROCESSING_SERVICE ", "acc difference "+(acc0[0]-acc[0])+" "+(acc0[1]-acc[1])+" "+(acc0[2]-acc[2]));
                Log.d("PROCESSING_SERVICE", "magn difference "+(magn0[0]-magn[0])+" "+(magn0[1]-magn[1])+" "+(magn0[2]-magn[2]));
                float[][] R = SensorDataProcessing.getRotationTRIAD(acc0, magn0, acc, magn);
                currentAngle = (float)Math.toDegrees(Math.atan2(R[1][0], R[0][0]));
                Log.d("PROCESSING_SERVICE", "computed angle:  "+currentAngle);

                if(goingUp){
                    if(currentAngle>upperThreshold){
                        movementCounts++;
                        Log.d("PROC_MOVEMENT", "Movement detected. Nr of movements: "+movementCounts);
                        for(ProcessingServiceEventListener i : listeners){
                            i.onMovementCount(movementCounts);
                        }
                        goingUp=false;
                    }
                } else{
                    if(currentAngle<=lowerThreshold){
                        goingUp=true;
                    }
                }

                for(ProcessingServiceEventListener i : listeners){
                    i.onProcessingResult(currentAngle);
                }
            }
                } ,0 , period);

        this.isProcessing=true;

    }

    /**
     * stops processing
     */
    public void stopProcessing(){
        timer.cancel();
        timer=null;
        isProcessing = false;
    }
    public boolean isProcessing(){
        return isProcessing;
    }

    /**
     * Registers processing event listener
     * @param listener processing event lintener that implements callback method
     */
    public void registerProcessingServiceEventListener(ProcessingServiceEventListener listener){
        this.listeners.add(listener);
    }
}

/**
 * interface for processing event capturing
 */
interface ProcessingServiceEventListener{
    void onProcessingResult(float processingResult);
    void onMovementCount(int movementCount);
}
