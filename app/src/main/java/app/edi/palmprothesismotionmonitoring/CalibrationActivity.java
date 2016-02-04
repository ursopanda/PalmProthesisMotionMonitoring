package app.edi.palmprothesismotionmonitoring;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.ejml.data.DenseMatrix64F;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import lv.edi.SmartWearProcessing.Calibration;

/**
 * Created by richards on 16.2.2.
 */
public class CalibrationActivity extends AppCompatActivity {
    PatientApplication application;
    private Vector<DenseMatrix64F> data; // data vector
    private ProgressBar calibrationProgress;
    private CalibrationView calibView;
    private ToggleButton  calibButton;
    private Calibration calib;
    private Vector<DenseMatrix64F> offsets;
    private Vector<DenseMatrix64F> W_inverted;

    private int SAMPLES_COUNT = 2500;        // how many samples to qcquire
    Timer dataAcquisitionTimer;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calibration_layout);

        application = (PatientApplication)getApplication();
        calibView = (CalibrationView) findViewById(R.id.calibration_view);
        calibButton = (ToggleButton) findViewById(R.id.calibration_button);
        calibrationProgress = (ProgressBar) findViewById(R.id.calibration_progress);
        calib = new Calibration();
        calib.init(); // intialize calibration
        offsets = new Vector<DenseMatrix64F>();
        W_inverted = new Vector<DenseMatrix64F>();
    }

    public void onClickCalibrate(View button){
        ToggleButton tb = (ToggleButton) button;
        if(tb.isChecked()) {
            if (application.btService.isConnected()) {
                data = new Vector<DenseMatrix64F>(SAMPLES_COUNT);
                data.setSize(application.sensors.size());
                for(int i=0; i<data.size(); i++){
                    data.set(i, new DenseMatrix64F(SAMPLES_COUNT, 3));
                }
                dataAcquisitionTimer = new Timer();
                calibView.setData(data);

                dataAcquisitionTimer.scheduleAtFixedRate(new TimerTask(){
                    int sampleCount=0;
                    public void run() {    // fetch data
                        for(int i=0; i<application.sensors.size(); i++){
                            data.get(i).set(sampleCount, 0, application.sensors.get(i).getMagRawX());
                            data.get(i).set(sampleCount, 1, application.sensors.get(i).getMagRawY());
                            data.get(i).set(sampleCount, 2, application.sensors.get(i).getMagRawZ());
                        }

                        sampleCount++;
                        runOnUiThread(new Runnable(){
                            public void run(){
                                int progress = (int)((float)sampleCount/SAMPLES_COUNT*100);
                                calibrationProgress.setProgress(progress);
                            }
                        });
                        calibView.postInvalidate();
                        if(sampleCount==SAMPLES_COUNT){   // if calibration completed
                            dataAcquisitionTimer.cancel();
                            calib.calibrateAllSensors(data, offsets, W_inverted);
                            Log.d("CALIBRATION", "CALIBRATION FINISHED "+offsets.size()+" "+W_inverted.size());
                            try {
                                calib.writeCalibDataToFile(offsets, W_inverted, application.calibrationFile);
                                application.updateCalibrationData(application.calibrationFile);
                            } catch(IOException ex){
                                Log.e("CALIBRATION", ex.toString());
                            }
                            runOnUiThread(new Runnable(){
                                public void run(){
                                    calibButton.setChecked(false);
                                    Toast.makeText(getApplicationContext(), "Calibation finished!", Toast.LENGTH_SHORT).show();
                                }
                                });
                        }
                        Log.d("CALIBRATION", "Sensor data frame added");
                    }} ,0 , 20);

            } else {
                Toast.makeText(this, "Connect to bt device first!", Toast.LENGTH_SHORT).show();
                ((ToggleButton) button).setChecked(false);
            }
        } else{
            dataAcquisitionTimer.cancel();
        }
    }
}
