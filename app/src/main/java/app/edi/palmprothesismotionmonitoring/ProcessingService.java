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
                float[] acc = sensors.get(1).getAccNorm();
                float[] magn0 = sensors.get(0).getMagNorm();
                float[] magn = sensors.get(1).getMagNorm();

                Log.d("PROCESSING_SERVICE", " " + acc0[0] + " " + acc0[1] + " " + acc[2]);

                float[][] R = SensorDataProcessing.getRotationTRIAD(acc0, magn0, acc, magn);

                Log.d("PROCESSING_SERVICE", "R(1,:)="+R[0][0]+" "+R[0][1]+" "+R[0][2]);

                float angle = SensorDataProcessing.angleFromR(R);
                float[] axis = SensorDataProcessing.axisFromR(R);
                Log.d("PROCESSING_SERVICE", "axis "+axis[0]+" "+axis[1]+" "+axis[2]);
                SensorDataProcessing.normalizeVector(axis);
                float projZ = SensorDataProcessing.dotProduct(axis, Z);// rotation axis projection on Z axis used to determine angle sign of rotation

                if(projZ>0.1){      // ignoring rotation not around sensor Z axis and determining sign
                    projZ=1;
                } else{
                    if(projZ<-0.1){
                        projZ=-1;
                    } else{
                        projZ=0;
                    }
                }


                Log.d("PROCESSING_SERVICE", "Z size "+axis.length+" axis size: "+axis.length);
                Log.d("PROCESSING_SERVICE", "rotation axis projection: "+projZ);

                Log.d("PROCESSING_SERVICE", "ANGLE "+Math.toDegrees(angle));
                for(ProcessingServiceEventListener i : listeners){
                    i.onProcessingResult((float)Math.toDegrees(projZ*angle));
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
}
