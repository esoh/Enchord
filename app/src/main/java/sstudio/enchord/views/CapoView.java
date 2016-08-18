package sstudio.enchord.views;

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

/**
 * Created by seanoh on 7/10/16.
 */

/*
 * call invalidate() when snaps to new position
 */
public class CapoView extends View implements View.OnTouchListener{
    protected int canvasWidth, canvasLength;
    protected float capoWidth, capoHeight;
    protected double[] fretRatios, midFretRatios;
    protected final int MIN_PADDING = getResources().getDimensionPixelSize(R.dimen.one_sp);
    protected boolean isHidden;
    protected Paint capoPaint;
    protected int startFret;
    protected float midYPos;
    protected float longFretboardPadding, wideFretboardPadding, fretboardLength, fretboardWidth;
    protected int capo;
    protected RectF capoShape;


    public CapoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        isHidden = true;
        capoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        capoPaint.setColor(ContextCompat.getColor(context, R.color.colorCapo));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.canvasWidth = w;
        this.canvasLength = h;
        longFretboardPadding = Dimens.calcLongFretboardPadding(h);
        wideFretboardPadding = Dimens.calcWideFretboardPadding(w, h);
        fretboardLength = Dimens.calcFretboardLength(h);
        fretboardWidth = Dimens.calcFretboardWidth(w, h);
        setCapo(capo);
        invalidate();
    }

    public void setFretRatios(double[] fretRatios, double[] midFretRatios) {
        this.fretRatios = fretRatios;
        this.midFretRatios = midFretRatios;
        if (fretRatios.length >= 2) {
            double lastFretRatio = fretRatios[fretRatios.length - 1] - fretRatios[fretRatios.length - 2];
            capoHeight = calcCapoHeight(canvasLength, lastFretRatio);
        }
        capoWidth = Dimens.calcFretboardWidth(canvasWidth, canvasLength);
    }

    protected float calcCapoHeight(int canvasLength, double lastFretRatio){
        float capoHeight = getResources().getDimensionPixelSize(R.dimen.max_fret_font_size)* Dimens.FONT_SIZE_TO_NOTE_RADIUS_RATIO * 2 * 1.2f;
        if(canvasLength != 0 && capoHeight > lastFretRatio * canvasLength - MIN_PADDING - Dimens.calcFretThickness(canvasLength)){
            capoHeight = (float) (lastFretRatio * canvasLength - MIN_PADDING - Dimens.calcFretThickness(canvasLength));
        }
        return capoHeight;
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
                midYPos = longFretboardPadding + (float)(fretboardLength * midFretRatios[capo - startFret]);
            }
            float noteRadius = getResources().getDimensionPixelSize(R.dimen.note_radius);
            capoShape = new RectF(wideFretboardPadding - noteRadius/2f, midYPos - capoHeight/2f,
                    wideFretboardPadding + fretboardWidth + noteRadius/2f, midYPos + capoHeight/2f);
        }

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(midYPos != 0 && !isHidden){
            float noteRadius = getResources().getDimensionPixelSize(R.dimen.note_radius);

            canvas.drawRoundRect(capoShape, noteRadius/8f, noteRadius/8f, capoPaint);
        }
    }

    public void setStartFret(int startFret) {
        this.startFret = startFret;
        invalidate();
    }
}
