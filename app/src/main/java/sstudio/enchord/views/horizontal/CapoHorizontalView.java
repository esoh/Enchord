package sstudio.enchord.views.horizontal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import sstudio.enchord.R;
import sstudio.enchord.constants.Dimens;
import sstudio.enchord.views.CapoView;

/**
 * Created by seanoh on 7/10/16.
 */

/*
 * call invalidate() when snaps to new position
 */
public class CapoHorizontalView extends CapoView{

    public CapoHorizontalView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.canvasLength = w;
        this.canvasWidth = h;
        longFretboardPadding = Dimens.calcLongFretboardPadding(canvasLength);
        wideFretboardPadding = Dimens.calcWideFretboardPadding(canvasWidth, canvasLength);
        fretboardLength = Dimens.calcFretboardLength(canvasLength);
        fretboardWidth = Dimens.calcFretboardWidth(canvasWidth, canvasLength);
        setCapo(capo);
        invalidate();
    }

    public void setCapo(int capo){
        this.capo = capo;
        if(capo <= 0){
            isHidden = true;
        } else {
            isHidden = false;
            if(capo < startFret){
                //draw the capo at the "nut"
                midYPos = longFretboardPadding;
            } else {
                // draw the capo at the midfret of midfret[startFret - capo]
                if(midFretRatios.length == 1){
                    midYPos = longFretboardPadding + fretboardLength * 0.5f;
                } else {
                    midYPos = longFretboardPadding + (float) (fretboardLength * midFretRatios[capo - startFret]);
                }
            }
            capoShape = new RectF(midYPos - capoHeight/2f, wideFretboardPadding - NOTE_RADIUS/2f,
                    midYPos + capoHeight/2f, wideFretboardPadding + fretboardWidth + NOTE_RADIUS/2f);
        }
        invalidate();
    }
}
