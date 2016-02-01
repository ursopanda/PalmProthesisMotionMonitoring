package app.edi.palmprothesismotionmonitoring;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import lv.edi.BluetoothLib.BluetoothEventListener;
import lv.edi.BluetoothLib.BluetoothService;
import lv.edi.SmartWearProcessing.Sensor;

/**
 * Created by Richards on 21.12.2015..
 *
 * Patient Application is class containing data that is relevant for wide parts of application
 * components.
 */
public class PatientApplication extends Application implements SharedPreferences.OnSharedPreferenceChangeListener, BluetoothEventListener {
    private SharedPreferences sharedPrefs;
    BluetoothDevice btDevice;
    BluetoothAdapter btAdapter;
    BluetoothService btService;
    ProcessingService processingService;
    Vector<Sensor> sensors = new Vector<Sensor>();
    static final int NUM_SENSORS=2;
    static final int BATTERY_PACKET=4;
    MainActivity mainActivity;
    Handler uiHandler;

    private File extStorageDirectory;
    private File appDirectory;
    private File calibrationFile;
    private String appDirName="PalmProsthesis";
    private String calibrationDataFileName="calibration_data.csv";


    @Override
    public void onCreate(){
        super.onCreate();

        // manage files
        extStorageDirectory = Environment.getExternalStorageDirectory();
        appDirectory = new File(extStorageDirectory, appDirName);
        if(!appDirectory.exists()){
            appDirectory.mkdir();
        }

        File[] files = appDirectory.listFiles();
        for(File i : files){
            Log.d("APPLICATION", "list of files in folder: "+i);
        }
        calibrationFile = new File(appDirectory, calibrationDataFileName);
        Log.d("APPLICATION", "calibration file "+calibrationFile);

        Log.d("APPLICATION", "external storage directory "+appDirectory);
        Log.d("APPLICATION", "app folder "+ appDirectory);
        Log.d("APPLICATION", "app folder exists?"+appDirectory.exists());

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPrefs.registerOnSharedPreferenceChangeListener(this);

        // initialize objects from preferences
        String btDeviceAddress = sharedPrefs.getString("pref_bt_target", "none");
        if(btDeviceAddress.equals("none")){
            btDevice = null;
        } else{
            btDevice = btAdapter.getRemoteDevice(btDeviceAddress);
        }

        // allocate memory for sensor data
        for(int i=0; i<NUM_SENSORS; i++){
            sensors.add(new Sensor(i, true));
            sensors.get(i).setMountTransformMatrix(1, 2, 3, 1, 2, 3);
        }

        if(calibrationFile.exists()){

            try {
                Sensor.setMagnetometerCalibData(calibrationFile, sensors);
                Log.d("APPLICATION", "calibration data loaded");
            } catch(IllegalArgumentException ex){
                Log.e("APPLICATION", ""+ex.getStackTrace());
            } catch(IOException ex){
                Log.e("APPLICATION", ""+ex.getStackTrace());
            }
        }
        if(btDevice!=null){
            Log.d("APPLICATION", "bt device " + btDevice.getName());
        }
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key){
        if(key.equals("pref_bt_target")){
            if(btAdapter==null){
                return;
            }
            String btDeviceAddress = sharedPrefs.getString("pref_bt_target", "none");
            if(btDeviceAddress.equals("none")){
                btDevice = null;
            } else{
                btDevice = btAdapter.getRemoteDevice(btDeviceAddress);
            }
            Log.d("APPLICATION", "bt device " + btDevice.getName());
        }
    }

    @Override
    public void onBluetoothDeviceConnecting(){
        mainActivity.btConnecting();
    }
    @Override
    public void onBluetoothDeviceConnected(){
        mainActivity.btConnected();
    }
    @Override
    public void onBluetoothDeviceDisconnected(){
        mainActivity.btDisconnected();
    }

}
