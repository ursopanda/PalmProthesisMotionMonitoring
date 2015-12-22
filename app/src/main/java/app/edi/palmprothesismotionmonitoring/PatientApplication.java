package app.edi.palmprothesismotionmonitoring;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Richards on 21.12.2015..
 */
public class PatientApplication extends Application implements SharedPreferences.OnSharedPreferenceChangeListener{
    private SharedPreferences sharedPrefs;
    BluetoothDevice btDevice;
    BluetoothAdapter btAdapter;
    @Override
    public void onCreate(){
        super.onCreate();

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
        Log.d("APPLICATION", "bt device " + btDevice.getName());
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

}
