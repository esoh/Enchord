package sstudio.enchord.views.horizontal;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import sstudio.enchord.R;
import sstudio.enchord.constants.Dimens;
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
        for(int i = 0; i < noteBoard.length; i++) {
            int type;
            if (capoPos < startFret) {
                type = getType(notesToShowAll[openNotes[i] + capoPos]);
                if (type != DRAW_INVALID) {
                    float x = verticalFretboardPadding;
                    float y = horizontalFretboardPadding + i * stringBtwnDist;
                    int note = openNotes[i] + capoPos;
                    drawNoteString(x, y, note, type, canvas);
                    drawNote(x, y, note, type, canvas);
                }
            } else if (capoPos >= startFret) {
                type = getType(notesToShowAll[openNotes[i] + capoPos]);
                if (type != DRAW_INVALID) {
                    float x = (float) (midFretRatios[capoPos - startFret] * fretboardHeight + verticalFretboardPadding);
                    float y = horizontalFretboardPadding + i * stringBtwnDist;
                    int note = openNotes[i] + capoPos;
                    drawNoteString(x, y, note, type, canvas);
                    drawNote(x, y, note, type, canvas);
                }
            }

            // check all notes along this string
            for (int j = 0; j < noteBoard[i].length; j++) {
                if (j > capoPos - startFret) {
                    type = getType((notesToShowAll[noteBoard[i][j]]));
                    if (type != DRAW_INVALID) {
                        float x = (float) (midFretRatios[j] * fretboardHeight + verticalFretboardPadding);
                        float y = horizontalFretboardPadding + i * stringBtwnDist;
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
        this.stringThickness = h/ Dimens.STRING_THICKNESS_RATIO;
        this.fretThickness = w/ Dimens.FRET_THICKNESS_RATIO;
        this.verticalFretboardPadding = w/ Dimens.TOP_BOTTOM_PADDING_RATIO; // proportion of fretboard height
        this.fretboardHeight = w - 2 * verticalFretboardPadding;
        this.fretboardWidth = w/ Dimens.FRETBOARD_WIDTH_RATIO;
        if(fretboardWidth * Dimens.MAX_FRETBOARD_WIDTH_RATIO > h){
            fretboardWidth = h/ Dimens.MAX_FRETBOARD_WIDTH_RATIO;
        }
        this.horizontalFretboardPadding = (h - fretboardWidth)/2f;
        if(noteBoard.length-1 == 0){
            this.stringBtwnDist = -1;
        } else {
            this.stringBtwnDist = fretboardWidth / (noteBoard.length - 1);
        }
        noteStringPaint.setStrokeWidth(stringThickness* Dimens.STRING_BOLD_RATIO);
        noteStringInnerPaint.setStrokeWidth(stringThickness* Dimens.STRING_BOLD_RATIO - MIN_PADDING);

        // set the size of the notes to account for fret length and string distance
        if(fretRatios != null){
            double lastFretDistRatio = 0;
            if(fretRatios.length > 1){
                lastFretDistRatio = (fretRatios[fretRatios.length - 1] - fretRatios[fretRatios.length - 2]);
            } else if(fretRatios.length == 1){
                lastFretDistRatio = fretRatios[0];
            }
            if(lastFretDistRatio != 0) {
                float fretDistDependant = (float)(lastFretDistRatio / 2f * fretboardHeight - fretThickness/2f - MIN_PADDING);
                float max = getResources().getDimensionPixelSize(R.dimen.max_fret_font_size)* Dimens.FONT_SIZE_TO_NOTE_RADIUS_RATIO;
                float stringDistDependant = (stringBtwnDist - MIN_PADDING)/2f;
                if(max >= fretDistDependant){
                    noteRadius = fretDistDependant;
                    noteTextPaint.setTextSize(noteRadius/ Dimens.FONT_SIZE_TO_NOTE_RADIUS_RATIO);
                } else {
                    noteRadius = max;
                    noteTextPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.max_fret_font_size));
                }
                if(noteRadius > stringDistDependant){
                    noteRadius = stringDistDependant;
                    noteTextPaint.setTextSize(noteRadius/ Dimens.FONT_SIZE_TO_NOTE_RADIUS_RATIO);
                }
                textOffset = ((noteTextPaint.descent() - noteTextPaint.ascent()) / 2f) - noteTextPaint.descent();
                noteBorderInnerRadius = noteRadius - MIN_PADDING;
            }
        }
        invalidate();
    }

    @Override
    protected void drawNoteString(float x, float y, int note, int type, Canvas canvas) {
        // check for valid
        if(type == DRAW_INVALID || type == DRAW_NO_MATCH){
            return;
        }
        if(type != DRAW_MATCH_OCTAVE && type != DRAW_MATCH){
            throw new IllegalArgumentException("Unknown draw id [-2, 1]: " + type);
        }

        int octaveType = getOctaveType(note);
        noteStringPaint.setColor(noteColors[octaveType][note % NUM_NOTES_OCTAVE]);
        canvas.drawLine(x, y, w - verticalFretboardPadding + fretThickness/2f, y, noteStringPaint);

        if (type == DRAW_MATCH_OCTAVE) {
            canvas.drawLine(x, y, w - verticalFretboardPadding, y, noteStringInnerPaint);
        }
    }
}
