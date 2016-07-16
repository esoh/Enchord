package sstudio.enchord.views.horizontal;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import sstudio.enchord.R;
import sstudio.enchord.constants.Dimens;
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
        this.stringThickness = h/ Dimens.STRING_THICKNESS_RATIO;
        this.fretThickness = w/ Dimens.FRET_THICKNESS_RATIO;
        this.verticalFretboardPadding = w/ Dimens.TOP_BOTTOM_PADDING_RATIO; // proportion of fretboard height
        this.fretboardHeight = w - 2 * verticalFretboardPadding;
        this.fretboardWidth = w/ Dimens.FRETBOARD_WIDTH_RATIO;
        if(fretboardWidth * Dimens.MAX_FRETBOARD_WIDTH_RATIO > h){
            fretboardWidth = h/ Dimens.MAX_FRETBOARD_WIDTH_RATIO;
        }
        this.horizontalFretboardPadding = (h - fretboardWidth)/2f;
        if(numStrings-1 == 0){
            this.stringBtwnDist = -1;
        } else {
            this.stringBtwnDist = fretboardWidth / (numStrings - 1);
        }
    }
}
