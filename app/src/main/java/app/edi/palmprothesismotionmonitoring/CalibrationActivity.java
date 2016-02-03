package app.edi.palmprothesismotionmonitoring;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.ejml.data.DenseMatrix64F;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import lv.edi.SmartWearProcessing.SensorDataProcessing;

/**
 * Created by richards on 16.2.2.
 */
public class CalibrationActivity extends AppCompatActivity {
    PatientApplication application;
    private Vector<DenseMatrix64F> data; // data vector
    Timer dataAcquisitionTimer;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calibration_layout);

        application = (PatientApplication)getApplication();
    }

    public void onClickCalibrate(View button){
        ToggleButton tb = (ToggleButton) button;
        if(tb.isChecked()) {
            if (application.btService.isConnected()) {
                data = new Vector<DenseMatrix64F>();
                dataAcquisitionTimer = new Timer();

                dataAcquisitionTimer.scheduleAtFixedRate(new TimerTask(){
                    public void run() {    // fetch data

                    }} ,0 , 100);

            } else {
                Toast.makeText(this, "Connect to bt device first!", Toast.LENGTH_SHORT).show();
                ((ToggleButton) button).setChecked(false);
            }
        } else{
            dataAcquisitionTimer.cancel();
        }
    }
}
