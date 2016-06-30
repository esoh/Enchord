package sstudio.gnote;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by seanoh on 6/29/16.
 */
public class FretboardView extends View {

    private int w;
    private int h;
    private Paint paint;

    public FretboardView(Context context, AttributeSet attr) {
        super(context, attr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            paint.setColor(ContextCompat.getColor(context, R.color.colorFretboard));
        } else {
            paint.setColor(context.getResources().getColor(R.color.colorFretboard));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("dimen", "width:" + w);
        Log.d("dimen", "height:" + h);
        int stringWidth = w/120;
        int fretHeight = h/250;
        paint.setStrokeWidth(stringWidth);
        //canvas.drawLine(w/2, 0,  w/2, h, paint);
        int d = 70; // convert this into a proportion of w
        int padding = 80; // convert this into a proportion of h
        canvas.drawLine(w/2 + d/2, padding, w/2 + d/2, h-padding, paint);
        canvas.drawLine(w/2 - d/2, padding, w/2 - d/2, h-padding, paint);
        canvas.drawLine(w/2 + 3*d/2, padding, w/2 + 3*d/2, h-padding, paint);
        canvas.drawLine(w/2 - 3*d/2, padding, w/2 - 3*d/2, h-padding, paint);
        canvas.drawLine(w/2 + 5*d/2, padding, w/2 + 5*d/2, h-padding, paint);
        canvas.drawLine(w/2 - 5*d/2, padding, w/2 - 5*d/2, h-padding, paint);

        double scaleLength = 1.50084054172*(h-2*padding) + padding;
        double currentFret = padding;
        //draw the nut
        paint.setStrokeWidth(fretHeight*2);
        canvas.drawLine(w/2-5*d/2, (float)currentFret, w/2+5*d/2, (float)currentFret, paint);
        currentFret = (scaleLength - currentFret)/17.817f + currentFret;
        paint.setStrokeWidth(fretHeight);
        for(int i = 0; i < 19; i++){
            canvas.drawLine(w/2-5*d/2, (float)currentFret, w/2+5*d/2, (float)currentFret, paint);
            currentFret = (scaleLength - currentFret)/17.817f + currentFret;
        }

        //canvas.drawLine(0, h/2, w, h/2, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
