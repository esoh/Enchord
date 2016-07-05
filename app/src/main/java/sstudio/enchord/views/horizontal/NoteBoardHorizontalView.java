package sstudio.enchord.views.horizontal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;

import sstudio.enchord.R;
import sstudio.enchord.constants.displayConstants;
import sstudio.enchord.objects.Note;
import sstudio.enchord.views.NoteBoardView;

/**
 * Created by seanoh on 6/30/16.
 */
public class NoteBoardHorizontalView extends NoteBoardView {

    public NoteBoardHorizontalView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
                float x = (float)(w/displayConstants.TOP_BOTTOM_PADDING_RATIO);
                float y = (float) (h / 2 - h / displayConstants.STRING_DISTANCE_RATIO * (-i + 2.5));
                int note = openNotes[i];
                drawNoteString(x, y, note, canvas);
                drawNote(x, y, note, canvas);
            } else if(capoPos >= 0 &&
                    ((notesToShowAll[noteBoard[i][0] + capoPos] == 1 && showOctaves) ||
                            (notesToShowAll[noteBoard[i][0] + capoPos] == 2))){
                float x = (float) (midFretRatios[capoPos] * (w - 2 * w / displayConstants.TOP_BOTTOM_PADDING_RATIO) + w / displayConstants.TOP_BOTTOM_PADDING_RATIO);
                float y = (float) (h / 2 - h / displayConstants.STRING_DISTANCE_RATIO * (-i + 2.5));
                int note = noteBoard[i][0] + capoPos;
                drawNoteString(x, y, note, canvas);
                drawNote(x, y, note, canvas);
            }

            // check all notes along this string
            for(int j = 0; j < noteBoard[i].length; j++) {
                if (j > capoPos &&
                        ((notesToShowAll[noteBoard[i][j]] == 1 && showOctaves) ||
                                (notesToShowAll[noteBoard[i][j]] == 2))) {
                    float x = (float) (midFretRatios[j] * (w - 2 * w / displayConstants.TOP_BOTTOM_PADDING_RATIO) + w / displayConstants.TOP_BOTTOM_PADDING_RATIO);
                    float y = (float) (h / 2 - h / displayConstants.STRING_DISTANCE_RATIO * (-i + 2.5));
                    drawNote(x, y, noteBoard[i][j], canvas);
                }
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.w = w;
        this.h = h;
        noteStringPaint.setStrokeWidth(h/(displayConstants.STRING_THICKNESS_RATIO/3*2));
        noteStringInnerPaint.setStrokeWidth(h/(displayConstants.STRING_THICKNESS_RATIO/3*2) - minPadding);

        // set the size of the notes to account for fret length and string distance
        if(fretRatios != null){
            double lastFretDistRatio = 0;
            if(fretRatios.length > 1){
                lastFretDistRatio = (fretRatios[fretRatios.length - 1] - fretRatios[fretRatios.length - 2]);
            } else if(fretRatios.length == 1){
                lastFretDistRatio = fretRatios[0];
            }
            if(lastFretDistRatio != 0) {
                float fretDistDependant = (float)(lastFretDistRatio / 2 * (w - 2*(w/displayConstants.TOP_BOTTOM_PADDING_RATIO)) - w/(displayConstants.FRET_THICKNESS_RATIO*2) - minPadding);
                float max = getResources().getDimensionPixelSize(R.dimen.max_fret_font_size)*5/6;
                float stringDistDependant = (float)((h/displayConstants.STRING_DISTANCE_RATIO) - minPadding)/2;
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
    }

    @Override
    protected void drawNoteString(float x, float y, int note, Canvas canvas) {
        int type = getOctaveType(note);
        noteStringPaint.setColor(noteColors[type][note % 12]);
        canvas.drawLine(x, y, (float)(w - w/displayConstants.TOP_BOTTOM_PADDING_RATIO + w/(displayConstants.FRET_THICKNESS_RATIO*3/2)), y, noteStringPaint);

        if (notesToShowAll[note] == 1) {
            canvas.drawLine(x, y, (float)(w - w/displayConstants.TOP_BOTTOM_PADDING_RATIO + w/(displayConstants.FRET_THICKNESS_RATIO*3/2)), y, noteStringInnerPaint);
        }
    }
}
