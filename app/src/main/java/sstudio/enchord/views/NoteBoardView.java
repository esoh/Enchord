package sstudio.enchord.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import sstudio.enchord.R;
import sstudio.enchord.constants.displayConstants;
import sstudio.enchord.objects.Note;

/**
 * Created by seanoh on 6/30/16.
 */
public class NoteBoardView extends View {
    private int minPadding = getResources().getDimensionPixelSize(R.dimen.one_sp);
    private int w, h;
    private double[] fretRatios, midFretRatios;
    private TextPaint noteTextPaint;
    private float textOffset;
    private int[][] noteBoard;
    private Paint notePaint, noteInnerPaint, noteStringPaint, noteStringInnerPaint;
    private int capoPos;
    private float noteRadius, noteBorderInnerRadius;
    private boolean includeText;
    private int[][] noteColors;
    private int[] notesToShowAll;
    private boolean showOctaves;
    private int[] openNotes;

    public NoteBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        notesToShowAll = new int[132];

        notePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        noteInnerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        noteInnerPaint.setColor(ContextCompat.getColor(context, R.color.colorBackground));
        noteStringPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        noteStringInnerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        noteStringInnerPaint.setColor(ContextCompat.getColor(context, R.color.colorBackground));

        noteColors = new int[4][12];

        noteColors[0][0] = ContextCompat.getColor(context, R.color.colorC4);
        noteColors[0][1] = ContextCompat.getColor(context, R.color.colorCSharp4);
        noteColors[0][2] = ContextCompat.getColor(context, R.color.colorD4);
        noteColors[0][3] = ContextCompat.getColor(context, R.color.colorDSharp4);
        noteColors[0][4] = ContextCompat.getColor(context, R.color.colorE4);
        noteColors[0][5] = ContextCompat.getColor(context, R.color.colorF4);
        noteColors[0][6] = ContextCompat.getColor(context, R.color.colorFSharp4);
        noteColors[0][7] = ContextCompat.getColor(context, R.color.colorG4);
        noteColors[0][8] = ContextCompat.getColor(context, R.color.colorGSharp4);
        noteColors[0][9] = ContextCompat.getColor(context, R.color.colorA4);
        noteColors[0][10] = ContextCompat.getColor(context, R.color.colorASharp4);
        noteColors[0][11] = ContextCompat.getColor(context, R.color.colorB4);

        noteColors[1][0] = ContextCompat.getColor(context, R.color.colorC5);
        noteColors[1][1] = ContextCompat.getColor(context, R.color.colorCSharp5);
        noteColors[1][2] = ContextCompat.getColor(context, R.color.colorD5);
        noteColors[1][3] = ContextCompat.getColor(context, R.color.colorDSharp5);
        noteColors[1][4] = ContextCompat.getColor(context, R.color.colorE5);
        noteColors[1][5] = ContextCompat.getColor(context, R.color.colorF5);
        noteColors[1][6] = ContextCompat.getColor(context, R.color.colorFSharp5);
        noteColors[1][7] = ContextCompat.getColor(context, R.color.colorG5);
        noteColors[1][8] = ContextCompat.getColor(context, R.color.colorGSharp5);
        noteColors[1][9] = ContextCompat.getColor(context, R.color.colorA5);
        noteColors[1][10] = ContextCompat.getColor(context, R.color.colorASharp5);
        noteColors[1][11] = ContextCompat.getColor(context, R.color.colorB5);

        noteColors[2][0] = ContextCompat.getColor(context, R.color.colorC6);
        noteColors[2][1] = ContextCompat.getColor(context, R.color.colorCSharp6);
        noteColors[2][2] = ContextCompat.getColor(context, R.color.colorD6);
        noteColors[2][3] = ContextCompat.getColor(context, R.color.colorDSharp6);
        noteColors[2][4] = ContextCompat.getColor(context, R.color.colorE6);
        noteColors[2][5] = ContextCompat.getColor(context, R.color.colorF6);
        noteColors[2][6] = ContextCompat.getColor(context, R.color.colorFSharp6);
        noteColors[2][7] = ContextCompat.getColor(context, R.color.colorG6);
        noteColors[2][8] = ContextCompat.getColor(context, R.color.colorGSharp6);
        noteColors[2][9] = ContextCompat.getColor(context, R.color.colorA6);
        noteColors[2][10] = ContextCompat.getColor(context, R.color.colorASharp6);
        noteColors[2][11] = ContextCompat.getColor(context, R.color.colorB6);

        noteColors[3][0] = ContextCompat.getColor(context, R.color.colorC7);
        noteColors[3][1] = ContextCompat.getColor(context, R.color.colorCSharp7);
        noteColors[3][2] = ContextCompat.getColor(context, R.color.colorD7);
        noteColors[3][3] = ContextCompat.getColor(context, R.color.colorDSharp7);
        noteColors[3][4] = ContextCompat.getColor(context, R.color.colorE7);
        noteColors[3][5] = ContextCompat.getColor(context, R.color.colorF7);
        noteColors[3][6] = ContextCompat.getColor(context, R.color.colorFSharp7);
        noteColors[3][7] = ContextCompat.getColor(context, R.color.colorG7);
        noteColors[3][8] = ContextCompat.getColor(context, R.color.colorGSharp7);
        noteColors[3][9] = ContextCompat.getColor(context, R.color.colorA7);
        noteColors[3][10] = ContextCompat.getColor(context, R.color.colorASharp7);
        noteColors[3][11] = ContextCompat.getColor(context, R.color.colorB7);

        noteTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        noteTextPaint.setTextAlign(Paint.Align.CENTER);
        noteTextPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.max_fret_font_size));
        textOffset = ((noteTextPaint.descent() - noteTextPaint.ascent()) / 2) - noteTextPaint.descent();
        capoPos = -1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(midFretRatios == null || noteBoard == null || openNotes == null){
            return;
        }
        for(int i = 0; i < noteBoard.length; i++){
            // check this string + capo
            // if no capo, use open notes
            if(capoPos < 0 &&
                    ((notesToShowAll[openNotes[i]] == 1 && showOctaves) ||
                     (notesToShowAll[openNotes[i]] == 2))){
                float x = (float) (w / 2 - w / displayConstants.STRING_DISTANCE_RATIO * (i - 2.5));
                float y = (float)(h/displayConstants.TOP_BOTTOM_PADDING_RATIO);
                int note = openNotes[i];
                drawNote(x, y, note, canvas);
                drawNoteString(x, y + noteRadius, note, canvas);
            } else if(capoPos >= 0 &&
                    ((notesToShowAll[noteBoard[i][0] + capoPos] == 1 && showOctaves) ||
                    (notesToShowAll[noteBoard[i][0] + capoPos] == 2))){
                float x = (float) (w / 2 - w / displayConstants.STRING_DISTANCE_RATIO * (i - 2.5));
                float y = (float) (midFretRatios[capoPos] * (h - 2 * h / displayConstants.TOP_BOTTOM_PADDING_RATIO) + h / displayConstants.TOP_BOTTOM_PADDING_RATIO);
                int note = noteBoard[i][0] + capoPos;
                drawNote(x, y, note, canvas);
                drawNoteString(x, y + noteRadius, note, canvas);
            }

            // check all notes along this string
            for(int j = 0; j < noteBoard[i].length; j++) {
                if (j > capoPos &&
                        ((notesToShowAll[noteBoard[i][j]] == 1 && showOctaves) ||
                                (notesToShowAll[noteBoard[i][j]] == 2))) {
                    float x = (float) (w / 2 - w / displayConstants.STRING_DISTANCE_RATIO * (i - 2.5));
                    float y = (float) (midFretRatios[j] * (h - 2 * h / displayConstants.TOP_BOTTOM_PADDING_RATIO) + h / displayConstants.TOP_BOTTOM_PADDING_RATIO);
                    drawNote(x, y, noteBoard[i][j], canvas);
                }
            }
        }
    }

    private void drawNote(float x, float y, int note, Canvas canvas){
        int type = getOctaveType(note);
        notePaint.setColor(noteColors[type][note % 12]);
        canvas.drawCircle(x, y, noteRadius, notePaint);

        if (notesToShowAll[note] == 1) {
            canvas.drawCircle(x, y, noteBorderInnerRadius, noteInnerPaint);
            noteTextPaint.setColor(noteColors[type][note % 12]);
        } else {
            noteTextPaint.setColor(Color.WHITE);
        }

        if (includeText) {
            canvas.drawText(Note.IDToNote(note, true).getShort(true) + "",
                    x, y + textOffset, noteTextPaint);
        }
    }

    private void drawNoteString(float x, float y, int note, Canvas canvas){
        int type = getOctaveType(note);
        noteStringPaint.setColor(noteColors[type][note % 12]);
        canvas.drawLine(x, y, x, (float)(h - h/displayConstants.TOP_BOTTOM_PADDING_RATIO + h/(displayConstants.FRET_THICKNESS_RATIO*3/2)), noteStringPaint);

        if (notesToShowAll[note] == 1) {
            canvas.drawLine(x, y, x, (float)(h - h/displayConstants.TOP_BOTTOM_PADDING_RATIO + h/(displayConstants.FRET_THICKNESS_RATIO*3/2)), noteStringInnerPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        this.includeText = true;
        noteStringPaint.setStrokeWidth(w/(displayConstants.STRING_THICKNESS_RATIO/3*2));
        noteStringInnerPaint.setStrokeWidth(w/(displayConstants.STRING_THICKNESS_RATIO/3*2) - minPadding);

        // set the size of the notes to account for fret length and string distance
        if(fretRatios != null){
            double lastFretDistRatio = 0;
            if(fretRatios.length > 1){
                lastFretDistRatio = (fretRatios[fretRatios.length - 1] - fretRatios[fretRatios.length - 2]);
            } else if(fretRatios.length == 1){
                lastFretDistRatio = fretRatios[0];
            }
            if(lastFretDistRatio != 0) {
                float fretDistDependant = (float)(lastFretDistRatio / 2 * (h - 2*(h/displayConstants.TOP_BOTTOM_PADDING_RATIO)) - h/(displayConstants.FRET_THICKNESS_RATIO*2) - minPadding);
                float max = getResources().getDimensionPixelSize(R.dimen.max_fret_font_size)*5/6;
                float stringDistDependant = (float)((w/displayConstants.STRING_DISTANCE_RATIO) - minPadding)/2;
                if(max >= fretDistDependant){
                    noteRadius = fretDistDependant;
                    noteTextPaint.setTextSize(noteRadius * 6/5);
                } else {
                    noteRadius = max;
                    noteTextPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.max_fret_font_size));
                }
                if(noteRadius > stringDistDependant){
                    noteRadius = stringDistDependant;
                    noteTextPaint.setTextSize(noteRadius * 6/5);
                }
                textOffset = ((noteTextPaint.descent() - noteTextPaint.ascent()) / 2) - noteTextPaint.descent();
                noteBorderInnerRadius = noteRadius - minPadding;
            }
        }

        invalidate();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private int getOctaveType(int note){
        if (note > 12 * 6) {
            return 0;
        } else if (note > 12 * 5) {
            return 1;
        } else if (note > 12 * 4) {
            return 2;
        } else {
            return 3;
        }
    }

    public void setFretRatios(double[] fretRatios, double[] midFretRatios) {
        this.fretRatios = fretRatios;
        this.midFretRatios = midFretRatios;
        this.noteRadius = getResources().getDimensionPixelSize(R.dimen.max_fret_font_size)*5/6;
        this.noteBorderInnerRadius = noteRadius - minPadding;
    }

    public void setNoteBoard(int[][] noteBoard){
        this.noteBoard = noteBoard;
    }

    public void setCapo(int capoPos){
        this.capoPos = capoPos;
        invalidate();
    }

    public void setNotes(boolean[] notesToShow){
        for(int i = 0; i < notesToShow.length; i++){
            if(notesToShow[i]){
                for(int note = i%12; note < 132; note+=12) {
                    if (notesToShowAll[note] != 2) {
                        notesToShowAll[note] = 1;
                    }
                }
                notesToShowAll[i] = 2;
            }
        }
        invalidate();
    }

    public void setShowOctaves(boolean showOctaves) {
        this.showOctaves = showOctaves;
        invalidate();
    }

    public void setOpenNotes(int[] openNotes) {
        this.openNotes = openNotes;
    }
}
