package sstudio.enchord.views.horizontal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;

import sstudio.enchord.R;
import sstudio.enchord.objects.Note;
import sstudio.enchord.views.NoteBoardView;

/**
 * Created by seanoh on 6/30/16.
 */
public class NoteBoardHorizontalView extends NoteBoardView {
    int w, h;
    private double[] fretRatios, midFretRatios;
    private TextPaint noteTextPaint;
    float textOffset;
    int[][] noteBoard;
    private int capoPos;

    public NoteBoardHorizontalView(Context context, AttributeSet attrs) {
        super(context, attrs);

        noteTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        noteTextPaint.setColor(Color.RED);
        noteTextPaint.setTextAlign(Paint.Align.CENTER);
        noteTextPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.max_fret_font_size));
        textOffset = ((noteTextPaint.descent() - noteTextPaint.ascent()) / 2) - noteTextPaint.descent();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(midFretRatios == null || noteBoard == null){
            return;
        }
        for(int i = 0; i < noteBoard.length; i++){
            for(int j = 0; j < noteBoard[i].length; j++){
                if(j > capoPos) {
                    canvas.drawText(Note.IDToNote(noteBoard[i][j], true).getShort(true) + "",
                            (float) (midFretRatios[j] * (w - 2 * w / 23.1) + w / 23.1),
                            (float) (h / 2 - h / 7.71428571429 * (i - 2.5)) + textOffset,
                            noteTextPaint);
                }
            }
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setFretRatios(double[] fretRatios, double[] midFretRatios) {
        this.fretRatios = fretRatios;
        this.midFretRatios = midFretRatios;
    }

    public void setNoteBoard(int[][] noteBoard){
        this.noteBoard = noteBoard;
    }

    public void setCapo(int capoPos){
        this.capoPos = capoPos;
        this.invalidate();
    }
}
