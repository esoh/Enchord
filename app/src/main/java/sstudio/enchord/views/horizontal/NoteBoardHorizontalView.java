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
            /* type:    -1:    show neutral note. showAll must be true.
             *          0:     show colored wire note. showOctaves must be true.
             *          1:     show colored full note. will always be true.
             *          -2:    INVALID
             */
            int type;
            if(capoPos < 0){
                type = getType(notesToShowAll[openNotes[i]]);
                if (type != -2) {
                    float x = verticalFretboardPadding;
                    float y = horizontalFretboardPadding + i*stringBtwnDist;
                    int note = openNotes[i];
                    drawNoteString(x, y, note, type, canvas);
                    drawNote(x, y, note, type, canvas);
                }
            } else if(capoPos >= 0){
                type = getType(notesToShowAll[noteBoard[i][0] + capoPos]);
                if(type != -2) {
                    float x = (float) (midFretRatios[capoPos] * fretboardHeight + verticalFretboardPadding);
                    float y = horizontalFretboardPadding + i*stringBtwnDist;
                    int note = noteBoard[i][0] + capoPos;
                    drawNoteString(x, y, note, type, canvas);
                    drawNote(x, y, note, type, canvas);
                }
            }

            // check all notes along this string
            for(int j = 0; j < noteBoard[i].length; j++) {
                if (j > capoPos){
                    type = getType((notesToShowAll[noteBoard[i][j]]));
                    if(type != -2) {
                        float x = (float) (midFretRatios[j] * fretboardHeight + verticalFretboardPadding);
                        float y = horizontalFretboardPadding + i*stringBtwnDist;
                        drawNote(x, y, noteBoard[i][j], type, canvas);
                    }
                }
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.w = w;
        this.h = h;
        this.stringThickness = h/displayConstants.STRING_THICKNESS_RATIO;
        this.fretThickness = w/displayConstants.FRET_THICKNESS_RATIO;
        this.verticalFretboardPadding = w/displayConstants.TOP_BOTTOM_PADDING_RATIO; // proportion of fretboard height
        this.fretboardHeight = w - 2 * verticalFretboardPadding;
        this.fretboardWidth = w/displayConstants.FRETBOARD_WIDTH_RATIO;
        if(fretboardWidth * displayConstants.MAX_FRETBOARD_WIDTH_RATIO > h){
            fretboardWidth = h/displayConstants.MAX_FRETBOARD_WIDTH_RATIO;
        }
        this.horizontalFretboardPadding = (h - fretboardWidth)/2f;
        if(noteBoard.length-1 == 0){
            this.stringBtwnDist = -1;
        } else {
            this.stringBtwnDist = fretboardWidth / (noteBoard.length - 1);
        }
        noteStringPaint.setStrokeWidth(stringThickness*displayConstants.STRING_BOLD_RATIO);
        noteStringInnerPaint.setStrokeWidth(stringThickness*displayConstants.STRING_BOLD_RATIO - minPadding);

        // set the size of the notes to account for fret length and string distance
        if(fretRatios != null){
            double lastFretDistRatio = 0;
            if(fretRatios.length > 1){
                lastFretDistRatio = (fretRatios[fretRatios.length - 1] - fretRatios[fretRatios.length - 2]);
            } else if(fretRatios.length == 1){
                lastFretDistRatio = fretRatios[0];
            }
            if(lastFretDistRatio != 0) {
                float fretDistDependant = (float)(lastFretDistRatio / 2f * fretboardHeight - fretThickness/2f - minPadding);
                float max = getResources().getDimensionPixelSize(R.dimen.max_fret_font_size)*displayConstants.FONT_SIZE_TO_NOTE_RADIUS_RATIO;
                float stringDistDependant = (stringBtwnDist - minPadding)/2f;
                if(max >= fretDistDependant){
                    noteRadius = fretDistDependant;
                    noteTextPaint.setTextSize(noteRadius/displayConstants.FONT_SIZE_TO_NOTE_RADIUS_RATIO);
                } else {
                    noteRadius = max;
                    noteTextPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.max_fret_font_size));
                }
                if(noteRadius > stringDistDependant){
                    noteRadius = stringDistDependant;
                    noteTextPaint.setTextSize(noteRadius/displayConstants.FONT_SIZE_TO_NOTE_RADIUS_RATIO);
                }
                textOffset = ((noteTextPaint.descent() - noteTextPaint.ascent()) / 2f) - noteTextPaint.descent();
                noteBorderInnerRadius = noteRadius - minPadding;
            }
        }
        invalidate();
    }

    @Override
    protected void drawNoteString(float x, float y, int note, int type, Canvas canvas) {
        if(type == -1){
            return;
        }
        int octaveType = getOctaveType(note);
        noteStringPaint.setColor(noteColors[octaveType][note % NUM_NOTES_OCTAVE]);
        canvas.drawLine(x, y, w - verticalFretboardPadding + fretThickness/2f, y, noteStringPaint);

        if (type == 0) {
            canvas.drawLine(x, y, w - verticalFretboardPadding, y, noteStringInnerPaint);
        }
    }
}
