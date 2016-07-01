package sstudio.enchord.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import sstudio.enchord.R;
import sstudio.enchord.objects.Note;

/**
 * Created by seanoh on 6/30/16.
 */
public class NoteBoardView extends View {
    int w, h;
    private double[] fretRatios, midFretRatios;
    private TextPaint noteTextPaint;
    float textOffset;
    int[][] noteBoard;
    Paint[] notePaint;
    int capoPos;

    public NoteBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        notePaint = new Paint[12];// 12 is the number of notes in an octave
        noteTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        noteTextPaint.setColor(Color.RED);
        noteTextPaint.setTextAlign(Paint.Align.CENTER);
        noteTextPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.fret_number_font_size));
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
                            (float) (w / 2 - w / 7.71428571429 * (i - 2.5)),
                            (float) (midFretRatios[j] * (h - 2 * h / 23.1) + textOffset + h / 23.1),
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

    public void setFretRatios(double[] fretRatios, double[] fingeringRatios) {
        this.fretRatios = fretRatios;
        this.midFretRatios = fingeringRatios;
    }

    public void setNoteBoard(int[][] noteBoard){
        this.noteBoard = noteBoard;
    }

    public void setCapo(int capoPos){
        this.capoPos = capoPos;
        this.invalidate();
    }
}
