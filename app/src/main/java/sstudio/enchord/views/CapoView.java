package sstudio.enchord.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import sstudio.enchord.R;

/**
 * Created by seanoh on 7/10/16.
 */

/*
 * call invalidate() when snaps to new position
 */
public class CapoView extends View implements View.OnTouchListener {
    protected int w, h;
    protected double[] fretRatios, midFretRatios;
    protected final int MIN_PADDING = getResources().getDimensionPixelSize(R.dimen.one_sp);


    public CapoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d("touchEvent", "down");
                break;
            case MotionEvent.ACTION_UP:
                Log.d("touchEvent", "up");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("touchEvent", "move");
                break;
        }
        return true;
    }

    public void setFretRatios(double[] fretRatios, double[] midFretRatios) {
        this.fretRatios = fretRatios;
        this.midFretRatios = midFretRatios;
        if (fretRatios.length >= 2) {
            //fretRatios[fretRatios.length - 1] - fretRatios[fretRatios.length - 2];
        }


    }
}
