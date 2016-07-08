package sstudio.enchord.views.horizontal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;

import sstudio.enchord.R;
import sstudio.enchord.constants.displayConstants;
import sstudio.enchord.views.FretboardView;

/**
 * Created by seanoh on 6/29/16.
 */
public class FretboardHorizontalView extends FretboardView {

    public FretboardHorizontalView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // nothing to draw. numFrets not set.
        if(fretRatios == null){
            return;
        }
        fretboardPaint.setStrokeWidth(stringThickness);

        // draw the guitar strings
        for(int i = 0; i < numStrings; i++){
            canvas.drawLine(verticalFretboardPadding, horizontalFretboardPadding + i * stringBtwnDist,
                            w-verticalFretboardPadding, horizontalFretboardPadding + i * stringBtwnDist,
                            fretboardPaint);
        }

        //draw the first fret
        if(startFret == 1) {
            fretboardPaint.setStrokeWidth(fretThickness * 2);
        } else {
            fretboardPaint.setStrokeWidth(fretThickness);
        }
        canvas.drawLine(verticalFretboardPadding, horizontalFretboardPadding - (stringThickness/2f),
                        verticalFretboardPadding, horizontalFretboardPadding + (stringThickness/2f) + fretboardWidth, fretboardPaint);

        //draw the frets
        fretboardPaint.setStrokeWidth(fretThickness);
        for(int i = 0; i < fretRatios.length; i++){
            float noteRadius = getResources().getDimensionPixelSize(R.dimen.note_radius);
            canvas.drawText((i+startFret)+"", (float)(midFretRatios[i] * fretboardHeight + verticalFretboardPadding),
                            fretboardWidth + horizontalFretboardPadding + noteRadius*2 + textOffset, fretNumberPaint);
            canvas.drawLine((float)(fretRatios[i] * fretboardHeight + verticalFretboardPadding),
                            horizontalFretboardPadding - (stringThickness/2f),
                            (float)(fretRatios[i] * fretboardHeight + verticalFretboardPadding),
                            horizontalFretboardPadding + fretboardWidth + (stringThickness/2f),fretboardPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.stringThickness = h/displayConstants.STRING_THICKNESS_RATIO;
        this.fretThickness = w/displayConstants.FRET_THICKNESS_RATIO;
        this.verticalFretboardPadding = w/displayConstants.TOP_BOTTOM_PADDING_RATIO; // proportion of fretboard height
        this.fretboardHeight = w - 2 * verticalFretboardPadding;
        this.fretboardWidth = w/displayConstants.FRETBOARD_WIDTH_RATIO;
        this.horizontalFretboardPadding = (h - fretboardWidth)/2f;
        if(numStrings-1 == 0){
            this.stringBtwnDist = -1;
        } else {
            this.stringBtwnDist = fretboardWidth / (numStrings - 1);
        }
    }
}
