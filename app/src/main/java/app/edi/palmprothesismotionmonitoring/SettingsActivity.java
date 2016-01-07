package app.edi.palmprothesismotionmonitoring;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import lv.edi.BluetoothLib.*;

/**
 * Created by Richards on 21.12.2015..
 * Class representing settings screen for bluetooth etc. configuration
 */
public class SettingsActivity extends AppCompatActivity {
    private static Preference btPreference;
    private static final int REQUEST_SELECT_TARGET_DEVICE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    /**
     * Simple preference fragment
     */
    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
        BluetoothAdapter mBluetoothAdapter;
        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);

            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            addPreferencesFromResource(R.xml.settings);
            btPreference = findPreference("pref_bt_target");

            btPreference.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

            btPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(), DeviceListActivity.class);
                    startActivityForResult(intent, REQUEST_SELECT_TARGET_DEVICE);
                    return true;
                }
            });

            String setting = btPreference.getSharedPreferences().getString("pref_bt_target", "none");
            Resources res = getResources();
            if(setting.equals("none")) {
                btPreference.setSummary(res.getString(R.string.pref_summary_bt_target)+": none");
            } else{
                BluetoothDevice btDevice = mBluetoothAdapter.getRemoteDevice(setting);
                btPreference.setSummary(res.getString(R.string.pref_summary_bt_target)+": "+btDevice.getName());
            }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data){
            switch(requestCode){
                case REQUEST_SELECT_TARGET_DEVICE: // if
                    // When DeviceListActivity returns with a device to connect
                    if (resultCode == Activity.RESULT_OK) {
                        // Get the device MAC address
                        String address = data.getExtras()
                                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                        // Get the BLuetoothDevice object

                        if (mBluetoothAdapter == null) {
                            return;
                        }
                        BluetoothDevice btDevice = mBluetoothAdapter.getRemoteDevice(address); // instantate bluetooth target device
                        SharedPreferences.Editor editor = btPreference.getSharedPreferences().edit();
                        editor.putString("pref_bt_target", btDevice.getAddress());
                        editor.commit();
                        Log.d("ON_ACTIVITY_RESULT", btPreference.getSharedPreferences().getString("pref_bt_target", "none"));
                    }
                    break;
            }
        }

        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key){
            if(key.equals("pref_bt_target")) {
                if(mBluetoothAdapter==null){
                    return;
                }
                Preference pref = findPreference(key);
                String targetDeviceAddress=sharedPreferences.getString("pref_bt_target", "none");
                Resources res = getResources();

                if(targetDeviceAddress.equals("none")){
                    pref.setSummary(res.getString(R.string.pref_summary_bt_target)+": none");
                } else{
                    BluetoothDevice btDevice = mBluetoothAdapter.getRemoteDevice(targetDeviceAddress);
                    pref.setSummary(res.getString(R.string.pref_summary_bt_target) + ": " + btDevice.getName());
                }
                Log.d("SETTINGS", "preference changed");
            }
        }
    }


}
