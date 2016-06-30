package sstudio.enchord;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by seanoh on 6/29/16.
 */
public class FretboardHorizontalView extends FretboardView {

    private int w, h;
    private Paint fretboardPaint;
    private TextPaint fretNumberPaint;
    private float textOffset;
    private int numFrets;

    public FretboardHorizontalView(Context context, AttributeSet attr) {
        super(context, attr);
        final int version = Build.VERSION.SDK_INT;

        fretboardPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fretNumberPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        if (version >= 23) {
            fretboardPaint.setColor(ContextCompat.getColor(context, R.color.colorFretboard));
            fretNumberPaint.setColor(ContextCompat.getColor(context, R.color.colorFretboard));
        } else {
            fretboardPaint.setColor(context.getResources().getColor(R.color.colorFretboard));
            fretNumberPaint.setColor(context.getResources().getColor(R.color.colorFretboard));
        }

        fretNumberPaint.setTextAlign(Paint.Align.CENTER);
        fretNumberPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.fret_number_font_size));
        textOffset = ((fretNumberPaint.descent() - fretNumberPaint.ascent()) / 2) - fretNumberPaint.descent();
    }

    public void setNumFrets(int numFrets){
        this.numFrets = numFrets;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // nothing to draw. numFrets not set.
        if(numFrets == 0){
            return;
        }
        Log.d("dimen", "height:" + h);
        Log.d("dimen", "width:" + w);
        int stringThickness = h /120;
        int fretThickness = w /250;
        fretboardPaint.setStrokeWidth(stringThickness);
        double d = h /7.71428571429; // convert this into a proportion of h
        double padding = w /23.1; // convert this into a proportion of w

        // draw the guitar strings
        for(int i = 0; i < 6; i++){
            canvas.drawLine((float)padding, (float)(h/2-5*d/2+d*i), (float)(w-padding), (float)(h/2-5*d/2+d*i), fretboardPaint);
        }

        double scaleLength = 1.50084054172*(w -2*padding) + padding;
        double currentFret = padding;
        double prevFret;

        //draw the nut
        fretboardPaint.setStrokeWidth(fretThickness*2);
        canvas.drawLine((float)currentFret, (float) (h /2-5*d/2), (float)currentFret, (float)(h /2+5*d/2), fretboardPaint);
        prevFret = currentFret;
        currentFret = (scaleLength - prevFret)/17.817f + prevFret;
        float fretNumberX = (float) (prevFret + currentFret)/2;

        //draw the frets
        fretboardPaint.setStrokeWidth(fretThickness);
        for(int i = 0; i < numFrets; i++){
            canvas.drawText((i+1)+"", fretNumberX, (float)(h /2+3*d) + textOffset, fretNumberPaint);
            canvas.drawLine((float)currentFret, (float)(h /2-5*d/2), (float)currentFret, (float)(h /2+5*d/2), fretboardPaint);
            prevFret = currentFret;
            currentFret = (scaleLength - prevFret)/17.817f + prevFret;
            fretNumberX = (float) (prevFret + currentFret)/2;
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
