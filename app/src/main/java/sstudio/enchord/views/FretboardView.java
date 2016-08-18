package sstudio.enchord.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import sstudio.enchord.constants.Dimens;

import sstudio.enchord.R;

/**
 * Created by seanoh on 6/29/16.
 */
public class FretboardView extends View {
    protected int w, h;
    protected int numStrings;
    protected Paint fretboardPaint;
    protected TextPaint fretNumberPaint;
    protected float textOffset;
    protected double[] fretRatios, midFretRatios;
    protected int startFret, endFret;
    protected float stringThickness, fretThickness;
    protected float longFretboardPadding, wideFretboardPadding;
    protected float fretboardLength, fretboardWidth;
    protected float stringBtwnDist;

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
        textOffset = ((fretNumberPaint.descent() - fretNumberPaint.ascent()) / 2f) - fretNumberPaint.descent();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // nothing to draw. numFrets not set.
        if(fretRatios == null || numStrings == 0 || stringThickness == 0){
            return;
        }
        fretboardPaint.setStrokeWidth(stringThickness);

        // draw the guitar strings
        for(int i = 0; i < numStrings; i++){
            canvas.drawLine(wideFretboardPadding + i * stringBtwnDist, longFretboardPadding,
                            wideFretboardPadding + i * stringBtwnDist, h-longFretboardPadding,
                            fretboardPaint);
        }

        //draw the first fret
        if(startFret == 1) {
            fretboardPaint.setStrokeWidth(fretThickness * 2);
        } else {
            fretboardPaint.setStrokeWidth(fretThickness);
        }
        canvas.drawLine(wideFretboardPadding - (stringThickness/2f), longFretboardPadding,
                        wideFretboardPadding + (stringThickness/2f) + fretboardWidth,
                longFretboardPadding, fretboardPaint);

        //draw the frets
        fretboardPaint.setStrokeWidth(fretThickness);
        for(int i = 0; i < fretRatios.length; i++){
            float noteRadius = getResources().getDimensionPixelSize(R.dimen.note_radius);
            canvas.drawText((i+startFret)+"", wideFretboardPadding - noteRadius*2,
                    (float)(midFretRatios[i] * fretboardLength + textOffset + longFretboardPadding),
                    fretNumberPaint);
            canvas.drawLine(wideFretboardPadding - (stringThickness/2f),
                    (float)(fretRatios[i] * fretboardLength + longFretboardPadding),
                    wideFretboardPadding + fretboardWidth + (stringThickness/2),
                    (float)(fretRatios[i] * fretboardLength + longFretboardPadding), fretboardPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        this.stringThickness = w/ Dimens.STRING_THICKNESS_RATIO;
        this.fretThickness = Dimens.calcFretThickness(h);
        this.longFretboardPadding = Dimens.calcLongFretboardPadding(h); // proportion of fretboard height
        this.fretboardLength = h - 2 * longFretboardPadding;
        this.fretboardWidth = Dimens.calcFretboardWidth(w, h);

        this.wideFretboardPadding = (w - fretboardWidth)/2f;
        if(numStrings-1 == 0){
            this.stringBtwnDist = -1;
        } else {
            this.stringBtwnDist = fretboardWidth / (numStrings - 1);
        }

        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setFretRatios(double[] fretRatios, double[] fingeringRatios) {
        this.fretRatios = fretRatios;
        this.midFretRatios = fingeringRatios;
    }

    public void setFretRange(int startFret, int endFret) {
        this.startFret = startFret;
        this.endFret = endFret;
        invalidate();
    }

    public void setNumStrings(int numStrings){
        this.numStrings = numStrings;
    }
}
