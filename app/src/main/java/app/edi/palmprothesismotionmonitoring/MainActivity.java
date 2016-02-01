package app.edi.palmprothesismotionmonitoring;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.os.Handler;

import java.util.concurrent.TimeUnit;

import lv.edi.BluetoothLib.BluetoothService;

public class MainActivity extends AppCompatActivity implements ProcessingServiceEventListener{
    private PatientApplication application;
    private final int REQUEST_ENABLE_BT = 1;
    private MenuItem btConnect;
    private ToggleButton startProcessingButton;

    private ProgressBar flexionValue;
    private AngleView angleView;
    private TextView amplitudeValue, sessionTime, movementAmount;
    private long sessionTimer, timeInMilliSeconds = 0L;
    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flexionValue = (ProgressBar) findViewById(R.id.flexionValue);
        angleView = (AngleView) findViewById(R.id.angle_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        startProcessingButton = (ToggleButton)findViewById(R.id.button_start);
        setSupportActionBar(toolbar);

        application = (PatientApplication) getApplication();
        application.btAdapter = BluetoothAdapter.getDefaultAdapter();
        if(application.btAdapter == null){
            Toast.makeText(this, getString(R.string.no_bt_support), Toast.LENGTH_SHORT).show();
            finish();
        }
        if(!application.btAdapter.isEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENABLE_BT);
        }
        if(application.btService==null) {
            application.btService = new BluetoothService(application.sensors, application.BATTERY_PACKET);
            application.btService.registerBluetoothEventListener(application);
        }

        if(application.processingService==null){
            application.processingService = new ProcessingService(application.sensors);
        }

        application.processingService.registerProcessingServiceEventListener(this);

        // Working with fields about required Rehab Session values
        TextView requiredLength = (TextView) findViewById(R.id.requiredSessionLength);
        TextView requiredMovementAmount = (TextView) findViewById(R.id.requiredMovementAmount);
        TextView requiredAmpitudeValue = (TextView) findViewById(R.id.requiredAmplitudeValue);

//        TODO setText from the DB Table "PatientData"
        requiredLength.setText("5");
        requiredMovementAmount.setText("20");
        requiredAmpitudeValue.setText("70");

        // Fields about current statistics
        sessionTime = (TextView) findViewById(R.id.sessionTime);
        Log.d("MAIN_ACTIVITY", "sessionTime view"+ sessionTime);
        movementAmount = (TextView) findViewById(R.id.movementAmount);

    }

    @Override
    protected void onResume(){
        super.onResume();
        application.mainActivity = this;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d("MAIN_ACTIVITY", "CREATING MENU ");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        btConnect = menu.findItem(R.id.action_bt);
        if(application.btService!=null){
            Log.d("MAIN_ACTIVITY", "BT SERVICE STATUS "+application.btService.isConnected()+" "+application.btService.isConnecting());
            if(application.btService.isConnected()){
                btConnect.setIcon(R.drawable.check);
                return true;
            }
            if(application.btService.isConnecting()){
                btConnect.setIcon(R.drawable.loading);
                return true;
            }
            if(!(application.btService.isConnected())){
                btConnect.setIcon(R.drawable.not);
                return true;
            }
        }
        Log.d("MAIN_ACTIVITY", btConnect.toString());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
           Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_bt){
            //if is connecting block this button
            if(application.btService.isConnecting()){
                return false;
            }
            if(!(application.btService.isConnected())){
                if(application.btDevice == null){
                    Toast.makeText(this, getString(R.string.must_select_bt_device), Toast.LENGTH_SHORT).show();
                    return false;
                }
                application.btService.connectDevice(application.btDevice);
            } else{
                application.btService.disconnectDevice();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_ENABLE_BT){
            if(resultCode == RESULT_CANCELED){
                Toast.makeText(this, getString(R.string.bt_must_be_turned_on), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void btConnected(){
        runOnUiThread(new Runnable() {
            public void run() {
                btConnect.setIcon(R.drawable.check);
            }
        });
    }

    public void btDisconnected(){
        runOnUiThread(new Runnable(){
            public void run(){
                btConnect.setIcon(R.drawable.not);
            }
        });
    }

    public void btConnecting(){
        runOnUiThread(new Runnable(){
            public void run(){
                btConnect.setIcon(R.drawable.loading);
            }
        });
    }

    public void onClickStartProcessing(View v){

        if(startProcessingButton.isChecked()) {
            if (!application.btService.isConnected()) {
                Toast.makeText(this, getString(R.string.must_connect_bt), Toast.LENGTH_SHORT).show();
                startProcessingButton.setChecked(false);
                return;
            } else {
                application.processingService.startProcessing(100);

                // TODO Starting Timer of Rehabilitation

            }
        } else {
            application.processingService.stopProcessing();

            // Starting timer for rehab session length
            sessionTimer = SystemClock.uptimeMillis();
            //handler.postDelayed(updateTimer, 0); CURRENTLY produces EXCEPTION. RUNNABLE DOESN'T get valu of sessionTime view
        }

    }

    // Updatinng timer value
    public Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            timeInMilliSeconds = SystemClock.uptimeMillis() - sessionTimer;
            Log.e("MAIN_ACTIVITY", "sessionTIme "+sessionTime);
            sessionTime.setText("" + ((int) (timeInMilliSeconds / 1000))/60 + ":" +
                    String.format("%02d", (int) (timeInMilliSeconds / 1000)) + ":"
                    + String.format("%03d", (int) (timeInMilliSeconds % 1000)));

            sessionTime.setTextColor(Color.RED);
            handler.postDelayed(this, 0);
        }
    };

    @Override
    public void onProcessingResult(float angle){
        final float anglef = angle;
        runOnUiThread(new Runnable(){
            public void run(){
                int progress = (int)(100*anglef/90);
                flexionValue.setProgress(progress);
                long sessionTimeL=application.processingService.getSessionLength();
                String time = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(sessionTimeL), TimeUnit.MILLISECONDS.toSeconds(sessionTimeL));
                sessionTime.setText(time);

                angleView.setCurrentAngle(anglef / 90);
                angleView.invalidate();
            }
        });
    }

    @Override
    public void onMovementCount(int count){
        final int countf = count;
        runOnUiThread(new Runnable(){
            public void run(){
                Log.d("MAIN_ACTIVITY", "movement count view "+movementAmount);
                movementAmount.setText("" + countf);
            }
        });
    }
}
