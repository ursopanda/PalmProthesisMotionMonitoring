package app.edi.palmprothesismotionmonitoring;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.firebase.client.Firebase;

public class DisplayStatistics extends AppCompatActivity {

    TextView sessionLengthStats;
    TextView movementAmountStats;
    TextView sessionResult;
    String isOkay;
    String patientID = MainActivity.patientSurname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Firebase.setAndroidContext(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_statistics);

        sessionLengthStats = (TextView) findViewById(R.id.sessionLengthStatsValue);
        movementAmountStats = (TextView) findViewById(R.id.movementAmountStatsValue);
        sessionResult = (TextView) findViewById(R.id.sessionResult);

        // VAJAG ŠO!

        Firebase surnameFirebase = new Firebase("https://palm-prothesis.firebaseio.com/patients/"
                + patientID
                + "/surname");
        surnameFirebase.setValue(MainActivity.patientSurname);

        movementAmountStats.setText(String.valueOf(MainActivity.totalMovementAmount));
        Firebase movementFirebase = new Firebase("https://palm-prothesis.firebaseio.com/patients/"
                + patientID
                + "/movementAmount");
        movementFirebase.setValue(MainActivity.totalMovementAmount);

        if (MainActivity.totalMovementAmount == MainActivity.prescribedAmount) {
            movementAmountStats.setBackgroundColor(Color.GREEN);
            sessionResult.setText("Rehab Session Successfully Completed!");
            isOkay = "true";
        }
        else {
            movementAmountStats.setBackgroundColor(Color.RED);
            sessionResult.setText("You did not fulfill rehab requirements!");
            isOkay = "false";
        }

        Firebase isOkayFirebase = new Firebase("https://palm-prothesis.firebaseio.com/patients/"
                + patientID
                + "/isOkay");
        isOkayFirebase.setValue(isOkay);

        // VAJAG ŠO ARī! Paldies!

        sessionLengthStats.setText(String.valueOf(MainActivity.totalRehabLength));
        Firebase lengthFirebase = new Firebase("https://palm-prothesis.firebaseio.com/patients/"
                + patientID
                + "/totalRehabLength");
        lengthFirebase.setValue(MainActivity.totalRehabLength);
    }

//    public void showLineGraph() {
//        Intent intent = new Intent(this, GraphActivity.class);
//        startActivity(intent);
//    }
}
