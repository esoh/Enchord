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

        int stringThickness = h/displayConstants.STRING_THICKNESS_RATIO;
        int fretThickness = w/displayConstants.FRET_THICKNESS_RATIO;
        fretboardPaint.setStrokeWidth(stringThickness);
        double d = h/displayConstants.STRING_DISTANCE_RATIO; // proportion of screen width
        double padding = w/displayConstants.TOP_BOTTOM_PADDING_RATIO; // proportion of screen w
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
            canvas.drawText((startFret+i)+"", (float)(midFretRatios[i] * fretboardHeight + padding), (float)(h/2+3.25*d) + textOffset, fretNumberPaint);
            canvas.drawLine((float)(fretRatios[i] * fretboardHeight + padding), (float)(h /2-5*d/2)-stringThickness/2,
                            (float)(fretRatios[i] * fretboardHeight + padding), (float)(h /2+5*d/2)+stringThickness/2,  fretboardPaint);
        }
    }
}
