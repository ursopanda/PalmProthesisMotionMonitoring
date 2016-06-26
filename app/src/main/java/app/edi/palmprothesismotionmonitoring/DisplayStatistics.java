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

    String patientID = "Patient";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Initializing Firebase
        Firebase.setAndroidContext(this);
        Firebase statisticsFirebase = new Firebase("https://palm-prothesis.firebaseio.com/");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_statistics);

        sessionLengthStats = (TextView) findViewById(R.id.sessionLengthStatsValue);
        movementAmountStats = (TextView) findViewById(R.id.movementAmountStatsValue);
        sessionResult = (TextView) findViewById(R.id.sessionResult);

        // VAJAG ŠO!
        movementAmountStats.setText(String.valueOf(MainActivity.totalMovementAmount));
        if (MainActivity.totalMovementAmount == MainActivity.prescribedAmount) {
            movementAmountStats.setBackgroundColor(Color.GREEN);
            sessionResult.setText("Rehab Session Successfully Completed!");
        }
        else {
            movementAmountStats.setBackgroundColor(Color.RED);
            sessionResult.setText("You did not fulfill rehab requirements!");
        }

        // VAJAG ŠO ARī! Paldies!
        sessionLengthStats.setText(String.valueOf(MainActivity.totalRehabLength));

    }

//    public void showLineGraph() {
//        Intent intent = new Intent(this, GraphActivity.class);
//        startActivity(intent);
//    }
}
