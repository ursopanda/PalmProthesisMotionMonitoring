package app.edi.palmprothesismotionmonitoring;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ParametersActivity extends AppCompatActivity {

    public static int prescribedFlexion = 70;
    public static long prescribedLength = 10000L;
    public static int prescribedAmount = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameters);
    }

    public void setPrescribedFlexionValue(View v) {
        EditText prescribedFlexionValueTextView = (EditText) findViewById(R.id.prescribedFlexionValue);
        String prescribedFlexionValueText = prescribedFlexionValueTextView.getText().toString();
        if (isInteger(prescribedFlexionValueText))
            prescribedFlexion = Integer.parseInt(prescribedFlexionValueText);
    }

    public void setPrescribedRehabLengthValue(View v) {
        EditText prescribedRehabLengthValueTextView = (EditText) findViewById(R.id.prescribedRehabLengthValue);
        String prescribedRehabLengthValueText = prescribedRehabLengthValueTextView.getText().toString();
        if (isInteger(prescribedRehabLengthValueText))
            prescribedLength = Integer.parseInt(prescribedRehabLengthValueText);
    }

    public void setPrescribedMovementAmountValue(View v) {
        EditText prescribedMovementAmountValueTextView = (EditText) findViewById(R.id.prescribedMovementAmountValue);
        String setPrescribedMovementAmountValueText = prescribedMovementAmountValueTextView.getText().toString();
        if (isInteger(setPrescribedMovementAmountValueText))
            prescribedAmount = Integer.parseInt(setPrescribedMovementAmountValueText);
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
}
