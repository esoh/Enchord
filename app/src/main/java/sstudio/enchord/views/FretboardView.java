package sstudio.enchord.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import sstudio.enchord.constants.displayConstants;

import sstudio.enchord.R;

/**
 * Created by seanoh on 6/29/16.
 */
public class FretboardView extends View {

    private int w, h;
    private Paint fretboardPaint;
    private TextPaint fretNumberPaint;
    private float textOffset;
    private double[] fretRatios, midFretRatios;
    private int startFret, endFret;

    // initialize the paints
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
        fretNumberPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.max_fret_font_size));
        textOffset = ((fretNumberPaint.descent() - fretNumberPaint.ascent()) / 2) - fretNumberPaint.descent();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // nothing to draw. numFrets not set.
        if(fretRatios == null){
            return;
        }

        int stringThickness = w/displayConstants.STRING_THICKNESS_RATIO;
        int fretThickness = h/displayConstants.FRET_THICKNESS_RATIO;
        fretboardPaint.setStrokeWidth(stringThickness);
        double d = w/displayConstants.STRING_DISTANCE_RATIO; // proportion of screen width
        double padding = h/displayConstants.TOP_BOTTOM_PADDING_RATIO; // proportion of screen height
        double fretboardHeight = h - 2 * padding;

        // draw the guitar strings
        for(int i = 0; i < 6; i++){
            canvas.drawLine((float)(w/2-d*(i-2.5)), (float) padding,
                            (float)(w/2-d*(i-2.5)), (float)(h-padding), fretboardPaint);
        }

        //draw the first fret
        if(startFret == 1) {
            fretboardPaint.setStrokeWidth(fretThickness * 2);
        } else {
            fretboardPaint.setStrokeWidth(fretThickness);
        }
        canvas.drawLine((float) (w / 2 - 2.5 * d) - stringThickness / 2, (float) padding,
                        (float) (w / 2 + 2.5 * d) + stringThickness / 2, (float) padding, fretboardPaint);

        //draw the frets
        fretboardPaint.setStrokeWidth(fretThickness);
        for(int i = 0; i < fretRatios.length; i++){
            float noteRadius = getResources().getDimensionPixelSize(R.dimen.note_radius);
            canvas.drawText((i+startFret)+"", (float)(w/2-2.5*d) - noteRadius*2, (float)(midFretRatios[i] * fretboardHeight + textOffset + padding), fretNumberPaint);
            canvas.drawLine((float)(w/2-5*d/2)-stringThickness/2, (float)(fretRatios[i] * fretboardHeight + padding),
                            (float)(w/2+5*d/2)+stringThickness/2, (float)(fretRatios[i] * fretboardHeight + padding), fretboardPaint);
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
