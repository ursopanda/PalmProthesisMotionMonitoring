package app.edi.palmprothesismotionmonitoring;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


import org.ejml.data.DenseMatrix64F;

import java.util.Vector;

/**
 * Created by richards on 16.2.2.
 */
public class CalibrationView extends View {

    private Paint framePaint;
    private Paint pointPaint;
    private Vector<DenseMatrix64F> data;
    private float norm = 364;

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

        data = new Vector<DenseMatrix64F>();
        double[] dt = {0, 200, 0};
        data.add(new DenseMatrix64F(1,3, true, dt));
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
            for(DenseMatrix64F point : data){
                float rawX = (float)point.get(0,0);
                float rawY = (float)point.get(0,1);

                canvas.drawCircle(offsetX+(rawX/norm)*radius, offsetY+(rawY/norm)*radius, 10, pointPaint);
            }
        }
    }
}
