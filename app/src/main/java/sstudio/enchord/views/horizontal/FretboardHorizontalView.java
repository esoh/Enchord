package sstudio.enchord.views.horizontal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;

import sstudio.enchord.R;
import sstudio.enchord.views.FretboardView;

/**
 * Created by seanoh on 6/29/16.
 */
public class FretboardHorizontalView extends FretboardView {

    private int w, h;
    private Paint fretboardPaint;
    private TextPaint fretNumberPaint;
    private float textOffset;
    private double[] fretRatios, midFretRatios;
    private int startFret, endFret;

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

    @Override
    protected void onDraw(Canvas canvas) {
        // nothing to draw. numFrets not set.
        if(fretRatios == null){
            return;
        }

        int stringThickness = h/120;
        int fretThickness = w/250;
        fretboardPaint.setStrokeWidth(stringThickness);
        double d = h/7.71428571429; // proportion of screen width
        double padding = w/23.1; // proportion of screen w
        double fretboardHeight = w - 2 * padding;

        // draw the guitar strings
        for(int i = 0; i < 6; i++){
            canvas.drawLine((float) padding, (float)(h/2-d*(i-2.5)),
                            (float) (w - padding), (float)(h/2-d*(i-2.5)), fretboardPaint);
        }

        //draw the first fret
        if(startFret == 1) {
            fretboardPaint.setStrokeWidth(fretThickness * 2);
        } else {
            fretboardPaint.setStrokeWidth(fretThickness);
        }
        canvas.drawLine((float) padding, (float) (h/2-2.5*d)-stringThickness/2,
                        (float) padding, (float) (h/2+2.5*d)+stringThickness/2, fretboardPaint);

        //draw the frets
        fretboardPaint.setStrokeWidth(fretThickness);
        for(int i = 0; i < fretRatios.length; i++){
            canvas.drawText((startFret+i)+"", (float)(midFretRatios[i] * fretboardHeight + padding), (float)(h/2+3*d) + textOffset, fretNumberPaint);
            canvas.drawLine((float)(fretRatios[i] * fretboardHeight + padding), (float)(h /2-5*d/2)-stringThickness/2,
                            (float)(fretRatios[i] * fretboardHeight + padding), (float)(h /2+5*d/2)+stringThickness/2,  fretboardPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setFretRatios(double[] fretRatios, double[] fingeringRatios) {
        this.fretRatios = fretRatios;
        this.midFretRatios = fingeringRatios;
    }

    public void setFretRange(int startFret, int endFret) {
        this.startFret = startFret;
        this.endFret = endFret;
    }
}
