package app.edi.palmprothesismotionmonitoring;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayStatistics extends AppCompatActivity {

    TextView sessionLengthStats;
    TextView movementAmountStats;
    TextView sessionResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_statistics);

        sessionLengthStats = (TextView) findViewById(R.id.sessionLengthStatsValue);
        movementAmountStats = (TextView) findViewById(R.id.movementAmountStatsValue);
        sessionResult = (TextView) findViewById(R.id.sessionResult);

        movementAmountStats.setText(String.valueOf(MainActivity.totalMovementAmount));
        if (MainActivity.totalMovementAmount == MainActivity.prescribedAmount) {
            movementAmountStats.setBackgroundColor(Color.GREEN);
            sessionResult.setText("Rehab Session Successfully Completed!");
        }
        else {
            movementAmountStats.setBackgroundColor(Color.RED);
            sessionResult.setText("You did not fulfill rehab requirements!");
        }

        sessionLengthStats.setText(String.valueOf(MainActivity.totalRehabLength));

    }

    public void showLineGraph() {
        Intent intent = new Intent(this, GraphActivity.class);
        startActivity(intent);
    }
}
