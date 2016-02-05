package app.edi.palmprothesismotionmonitoring;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import lv.edi.SmartWearProcessing.Filter;
import lv.edi.SmartWearProcessing.Sensor;
import lv.edi.SmartWearProcessing.SensorDataProcessing;

/**
 * Created by richards on 16.7.1.
 *
 * class for background service for processing sensor data. Service computes sensor orientation
 * and provides with data like angle between sensors
 *
 * After processing service  session main metrics are obtained with methods:
 *      getSessionLength()   -- session length in milliseconds
 *      getMovementCount()   -- movement count for session
 *      getAveragePeriod()   -- average period between two movements in milliseconds
 *      getMaxAngle()        -- max angle in session (this value may be large because of accelerometers errors due to movement)
 *      getSessionStartTime() -- UTC start time of the session in [ms]
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


    private long sessionStartTime;       // session start time in [ms] UTC
    private long sessionLength;          // time elapsed since session start
    private long sessionSetLength=10000;       // how long session mus run in [ms]
    private long previousTime;
    private long currentTime;            // current elapsed time in [ms]

    private float maxAngle = 0;         // maximum angle for seassion
    private float currentAngle;         // computed angle in degrees
    private Vector<Long> periods;       // periods between movements in milliseconds
    private Filter filterAngle;

    Vector<ProcessingServiceEventListener> listeners = new Vector<ProcessingServiceEventListener>();

    /**
     *  Constructor specifying lower and upper angle thresholds for movement counting
     *  @param sensors Vector containing Sensor objects on which to perform processing.
     *  @param lowerThreshold sets lower threshold for angle
     *  @param upperThreshold sets upper threshold  for angle (reaching this value count movement)
     */
    public ProcessingService(Vector<Sensor> sensors, float lowerThreshold, float upperThreshold){
        this(sensors);
        this.lowerThreshold = lowerThreshold;
        this.upperThreshold = upperThreshold;
    }

    /**
     *  Constructor specifying lower and upper angle thresholds for movement counting
     *  @param sensors Vector containing Sensor objects on which to perform processing.
     *  @param lowerThreshold sets lower threshold for angle
     *  @param upperThreshold sets upper threshold  for angle (reaching this value count movement)
     *  @param sessionLength sets length how long session should run
     */
    public ProcessingService(Vector<Sensor> sensors, float lowerThreshold, float upperThreshold, int sessionLength){
        this(sensors);
        this.lowerThreshold = lowerThreshold;
        this.upperThreshold = upperThreshold;
        this.sessionSetLength = sessionLength;
    }
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
        filterAngle = new Filter();
        timer = new Timer();
        periods = new Vector<Long>();
        sessionStartTime=System.currentTimeMillis();
        previousTime = sessionStartTime;
        movementCounts = 0;
        for(ProcessingServiceEventListener i : listeners){
            i.onMovementCount(movementCounts);
        }
        timer.scheduleAtFixedRate(new TimerTask(){
            public void run() {// fetch data
                float[] acc0 = sensors.get(0).getAccNorm();
                float[] acc = sensors.get(1).getAccNorm();
                float[] magn0 = sensors.get(0).getMagNorm();
                float[] magn = sensors.get(1).getMagNorm();

                Log.d("PROCESSING_SERVICE", " " + acc0[0] + " " + acc0[1] + " " + acc[2]);
                Log.d("PROCESSING_SERVICE ", "acc difference "+(acc0[0]-acc[0])+" "+(acc0[1]-acc[1])+" "+(acc0[2]-acc[2]));
                Log.d("PROCESSING_SERVICE", "magn difference "+(magn0[0]-magn[0])+" "+(magn0[1]-magn[1])+" "+(magn0[2]-magn[2]));
                float[][] R = SensorDataProcessing.getRotationTRIAD(acc0, magn0, acc, magn);

                currentAngle = filterAngle.filter((float)Math.toDegrees(Math.atan2(R[1][0], R[0][0])));
                maxAngle = (currentAngle>maxAngle) ? currentAngle : maxAngle;

                Log.d("PROCESSING_SERVICE", "computed angle:  "+currentAngle);
                currentTime=System.currentTimeMillis();
                sessionLength=currentTime-sessionStartTime;
                Log.d("PROCESSING_SERVICE_TIME", "session length "+sessionLength+" [ms]");
                if(goingUp){
                    if(currentAngle>=upperThreshold){
                        periods.add(currentTime-previousTime);
                        previousTime=currentTime;
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
                if(sessionLength>=sessionSetLength){
                    stopProcessing();
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

        // average movement periods
        long sum=0;
        for(Long i : periods){
            sum+=i.longValue();
        }
        try {
            Log.d("PROCESSING_SERVICE", "average period: " + getAveragePeriod() +
                    " [ms], movementCount: " + getMovementCount() +
                    ", session length: " + getSessionLength() + " [ms]," +
                    " max angle: " + getMaxAngle() + " [deg]" +
                    " session start time: " + getSessionStartTime());
        } catch(ArithmeticException ex){

        }

        for(ProcessingServiceEventListener i: listeners){
            i.onStopProcessing();
        }
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

    /**
     * return length in milliseconds of current running processing session
     * @return long sessionLength in milliseconds.
     */
    public long getSessionLength(){
        return this.sessionLength;
    }

    /**
     * returns number of movements during processing session
     * @return integer. number of times movements were detected
     */
    public int getMovementCount(){
        return this.movementCounts;
    }

    /**
     * returns average period between two movements in milliseconds
     * @return average period between movements in milliseconds
     */
    public int getAveragePeriod(){
        return (int)getSessionLength()/getMovementCount();
    }

    /**
     * return UTC start time of last session in [ms]
     * @return long value time in ms
     */
    public long getSessionStartTime(){
        return this.sessionStartTime;
    }

    /**
     * returs maximum angle for current session
     * @return float value maximum angle for session.
     */
    public float getMaxAngle(){
        return this.maxAngle;
    }
}

/**
 * interface for processing event capturing
 */
interface ProcessingServiceEventListener{
    void onProcessingResult(float processingResult);
    void onMovementCount(int movementCount);
    void onStopProcessing();
}
