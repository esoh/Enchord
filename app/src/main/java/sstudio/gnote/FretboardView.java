package sstudio.gnote;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by seanoh on 6/29/16.
 */
public class FretboardView extends View {

    private int w;
    private int h;
    private Paint fretboardPaint;
    private TextPaint fretNumberPaint;
    private float textOffset;
    private int numFrets;

    public FretboardView(Context context, AttributeSet attr) {
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
        Log.d("dimen", "width:" + w);
        Log.d("dimen", "height:" + h);
        int stringWidth = w/120;
        int fretHeight = h/250;
        fretboardPaint.setStrokeWidth(stringWidth);
        double d = w/7.71428571429; // convert this into a proportion of w
        double padding = h/23.1; // convert this into a proportion of h

        // draw the guitar strings
        for(double i = w/2-5*d/2; i <= w/2+5*d/2; i+=d){
            canvas.drawLine((float)i, (float)padding, (float)i, (float)(h-padding), fretboardPaint);
        }

        double scaleLength = 1.50084054172*(h-2*padding) + padding;
        double currentFret = padding;
        double prevFret;

        //draw the nut
        fretboardPaint.setStrokeWidth(fretHeight*2);
        canvas.drawLine((float) (w/2-5*d/2), (float)currentFret, (float)(w/2+5*d/2), (float)currentFret, fretboardPaint);
        prevFret = currentFret;
        currentFret = (scaleLength - prevFret)/17.817f + prevFret;
        float fretNumberY = (float) (prevFret + currentFret)/2;

        //draw the frets
        fretboardPaint.setStrokeWidth(fretHeight);
        for(int i = 0; i < numFrets; i++){
            canvas.drawText((i+1)+"", (float)(w/2-3*d), fretNumberY + textOffset, fretNumberPaint);
            canvas.drawLine((float)(w/2-5*d/2), (float)currentFret, (float)(w/2+5*d/2), (float)currentFret, fretboardPaint);
            prevFret = currentFret;
            currentFret = (scaleLength - prevFret)/17.817f + prevFret;
            fretNumberY = (float) (prevFret + currentFret)/2;
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
