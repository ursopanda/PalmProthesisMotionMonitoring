package app.edi.palmprothesismotionmonitoring;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


import org.ejml.data.DenseMatrix64F;

import java.util.Iterator;
import java.util.Vector;

/**
 * Created by richards on 16.2.2.
 */
public class CalibrationView extends View {

    private Paint framePaint;
    private Paint pointPaint;
    private Vector<DenseMatrix64F> data;
    private float norm = 600;

    public CalibrationView(Context context, AttributeSet attrs) {
        super(context, attrs);

        framePaint = new Paint();
        framePaint.setColor(Color.BLACK);
        framePaint.setStyle(Paint.Style.STROKE);
        framePaint.setStrokeWidth(3);
        framePaint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));

        pointPaint = new Paint();
        pointPaint.setColor(Color.BLUE);
        pointPaint.setStyle(Paint.Style.FILL);

    }

    /**
     * sets calibration data for view to visualise
     * @param data
     */
    public void setData(Vector<DenseMatrix64F> data){
        this.data=data;
    }

    /**
     * removes calibration data from view
     */
    public void removeData(){
        this.data=null;
    }

    /**
     * draw callback
     * @param canvas
     */
    protected void onDraw(Canvas canvas){


        int width = canvas.getWidth()-1;
        int height = canvas.getHeight()-1;

        int offsetX = width/2;
        int offsetY = height/2;

        int radius = Math.min(width, height)/2;
        canvas.drawCircle(width/2, height/2, radius, framePaint);
        if(data!=null){

            DenseMatrix64F sensor1 = data.get(0);
            for(int i=0; i<sensor1.numRows; i++) {


                float rawX = (float) sensor1.get(i,0);
                float rawY = (float) sensor1.get(i, 1);

                Log.d("CALIBRATION", "raw x y:" + rawX + " " + rawY);
                if(rawX!=0.0f && rawY!=0.0f)
                    canvas.drawCircle(offsetX + (rawX / norm) * radius, offsetY + (rawY / norm) * radius, 10, pointPaint);

            }
        }
    }
}
