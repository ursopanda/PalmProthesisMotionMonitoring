package app.edi.palmprothesismotionmonitoring;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Class representing current angle value visualisation
 * of joints for palm prosthesis. View is horizontal bar with
 * three sections (small angle, angle under threshold, angle over threhold)
 * Created by richards on 16.1.2.
 */
public class AngleView extends View {

    private Paint framePaint;
    private Paint fillPaint;
    private float lowerThreshFrac=0.3f;
    private float upperThreshFrac=0.7f;
    private float currentAngle=0.0f;
    public AngleView(Context context, AttributeSet attrs){
        super(context, attrs);

        framePaint = new Paint();
        framePaint.setColor(Color.BLACK);
        framePaint.setStrokeWidth(3);
        framePaint.setStyle(Paint.Style.STROKE);


        fillPaint = new Paint();
        fillPaint.setColor(Color.RED);
        fillPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * Sets the value for low threshold of amplitude. in terms of fraction of views width
     * @param lowerThreshFrac low value of angle view threshold 0-1.0
     */
    public void setLowerThreshPercent(float lowerThreshFrac){
        this.lowerThreshFrac=lowerThreshFrac;
    }
    /**
     * Sets the value for upper threshold of amplitude. in terms of fraction of views width
     * @param upperThreshFrac low value of angle view threshold 0-1.0
     */
    public void setUpperThreshFrac(float upperThreshFrac){
        this.upperThreshFrac=upperThreshFrac;
    }

    /**
     * Sets current normalised angle value (0-1), normalised to full angle amplitude (90 degrees)
     * @param angle
     */
    public void setCurrentAngle(float angle){
        this.currentAngle=angle;
    }


    /**
     * draw callback
     * @param canvas
     */
    protected void onDraw(Canvas canvas){


        int width = canvas.getWidth()-1;
        int height = canvas.getHeight()-1;

        fillPaint.setColor(Color.BLUE);
        canvas.drawRect(0, 0, width*currentAngle, height, fillPaint);

        if(currentAngle<lowerThreshFrac){
            fillPaint.setColor(Color.RED);
        } else{
            if(currentAngle<upperThreshFrac){
                fillPaint.setColor(Color.YELLOW);
            } else{
                fillPaint.setColor(Color.GREEN);
            }
        }
        canvas.drawRect(width*currentAngle, 0, width*currentAngle+width/15, height, fillPaint);

        // draw low part
        canvas.drawRect(0, 0, width*lowerThreshFrac, height, framePaint);
        canvas.drawRect(width*lowerThreshFrac, 0, width*upperThreshFrac, height, framePaint);
        canvas.drawRect(width*upperThreshFrac, 0, width, height, framePaint);
    }
}
