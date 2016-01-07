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
            public void run() {
                // fetch data
                float[] acc0 = sensors.get(0).getAccNorm();
                float[] acc = sensors.get(1).getAccNorm();
                float[] magn0 = sensors.get(0).getMagNorm();
                float[] magn = sensors.get(1).getMagNorm();

                float[][] R = new float[3][3];

                SensorDataProcessing.getRotationTRIAD(acc0, magn0, acc, magn);
                float angle = SensorDataProcessing.angleFromR(R);

                Log.d("PROCESSING_SERVICE", "ANGLE "+Math.toDegrees(angle));
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
}
