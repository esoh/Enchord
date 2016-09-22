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
            canvas.drawLine(longFretboardPadding, wideFretboardPadding + i * stringBtwnDist,
                            w- longFretboardPadding, wideFretboardPadding + i * stringBtwnDist,
                            fretboardPaint);
        }

        //draw the first fret
        if(startFret == 1) {
            fretboardPaint.setStrokeWidth(fretThickness * 2);
        } else {
            fretboardPaint.setStrokeWidth(fretThickness);
        }
        canvas.drawLine(longFretboardPadding, wideFretboardPadding - (stringThickness/2f),
                longFretboardPadding, wideFretboardPadding + (stringThickness/2f) + fretboardWidth, fretboardPaint);

        //draw the frets
        fretboardPaint.setStrokeWidth(fretThickness);
        for(int i = 0; i < midFretRatios.length; i++){
            float noteRadius = getResources().getDimensionPixelSize(R.dimen.note_radius);
            canvas.drawText((i+startFret)+"", (float)(midFretRatios[i] * fretboardLength + longFretboardPadding),
                            fretboardWidth + wideFretboardPadding + noteRadius*2 + textOffset, fretNumberPaint);
            canvas.drawLine((float)(fretRatios[i] * fretboardLength + longFretboardPadding),
                            wideFretboardPadding - (stringThickness/2f),
                            (float)(fretRatios[i] * fretboardLength + longFretboardPadding),
                            wideFretboardPadding + fretboardWidth + (stringThickness/2f),fretboardPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.stringThickness = h/ Dimens.STRING_THICKNESS_RATIO;
        this.fretThickness = Dimens.calcFretThickness(w);
        this.longFretboardPadding = Dimens.calcLongFretboardPadding(w); // proportion of fretboard height
        this.fretboardLength = Dimens.calcFretboardLength(w);
        this.fretboardWidth = Dimens.calcFretboardWidth(h, w);
        this.wideFretboardPadding = (h - fretboardWidth)/2f;
        if(numStrings-1 == 0){
            this.stringBtwnDist = -1;
        } else {
            this.stringBtwnDist = fretboardWidth / (numStrings - 1);
        }
    }
}
