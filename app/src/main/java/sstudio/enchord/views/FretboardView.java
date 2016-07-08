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
    protected int w, h;
    protected int numStrings;
    protected Paint fretboardPaint;
    protected TextPaint fretNumberPaint;
    protected float textOffset;
    protected double[] fretRatios, midFretRatios;
    protected int startFret, endFret;
    protected float stringThickness, fretThickness;
    protected float verticalFretboardPadding, horizontalFretboardPadding;
    protected float fretboardHeight, fretboardWidth;
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
            canvas.drawLine(horizontalFretboardPadding + i * stringBtwnDist, verticalFretboardPadding,
                            horizontalFretboardPadding + i * stringBtwnDist, h-verticalFretboardPadding,
                            fretboardPaint);
        }

        //draw the first fret
        if(startFret == 1) {
            fretboardPaint.setStrokeWidth(fretThickness * 2);
        } else {
            fretboardPaint.setStrokeWidth(fretThickness);
        }
        canvas.drawLine(horizontalFretboardPadding - (stringThickness/2f), verticalFretboardPadding,
                        horizontalFretboardPadding + (stringThickness/2f) + fretboardWidth,
                        verticalFretboardPadding, fretboardPaint);

        //draw the frets
        fretboardPaint.setStrokeWidth(fretThickness);
        for(int i = 0; i < fretRatios.length; i++){
            float noteRadius = getResources().getDimensionPixelSize(R.dimen.note_radius);
            canvas.drawText((i+startFret)+"", horizontalFretboardPadding - noteRadius*2,
                    (float)(midFretRatios[i] * fretboardHeight + textOffset + verticalFretboardPadding),
                    fretNumberPaint);
            canvas.drawLine(horizontalFretboardPadding - (stringThickness/2f),
                    (float)(fretRatios[i] * fretboardHeight + verticalFretboardPadding),
                    horizontalFretboardPadding + fretboardWidth + (stringThickness/2),
                    (float)(fretRatios[i] * fretboardHeight + verticalFretboardPadding), fretboardPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        this.stringThickness = w/displayConstants.STRING_THICKNESS_RATIO;
        this.fretThickness = h/displayConstants.FRET_THICKNESS_RATIO;
        this.verticalFretboardPadding = h/displayConstants.TOP_BOTTOM_PADDING_RATIO; // proportion of fretboard height
        this.fretboardHeight = h - 2 * verticalFretboardPadding;
        this.fretboardWidth = h/displayConstants.FRETBOARD_WIDTH_RATIO;
        if(fretboardWidth * displayConstants.MAX_FRETBOARD_WIDTH_RATIO > w){
            fretboardWidth = w/displayConstants.MAX_FRETBOARD_WIDTH_RATIO;
        }

        this.horizontalFretboardPadding = (w - fretboardWidth)/2f;
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
    }

    public void setNumStrings(int numStrings){
        this.numStrings = numStrings;
    }
}
