package sstudio.enchord.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Arrays;

import sstudio.enchord.R;
import sstudio.enchord.constants.Dimens;
import sstudio.enchord.objects.Note;

/**
 * Created by seanoh on 6/30/16.
 *
 * important vars:
 *      int type: describes the type of drawing that should be made.
 *          DRAW_MATCH              matches a requested note: draw the full note
 *          DRAW_MATCH_OCTAVE       matches an octave of the requested note AND showOctaves is true: draw octave note
 *          DRAW_NO_MATCH           none of the above, but showAll is true: draw neutral note.
 *          DRAW_INVALID            invalid, none of the above: draw nothing.
 */
public class NoteBoardView extends View {
    protected final int UNSET = -11;
    protected final int NUM_NOTES_OCTAVE = Dimens.NUM_NOTES_OCTAVE;
    protected final int NUM_OCTAVE = Dimens.NUM_OCTAVE;
    protected final int MIN_PADDING = getResources().getDimensionPixelSize(R.dimen.one_sp);
    protected final int DRAW_MATCH = 1;
    protected final int DRAW_MATCH_OCTAVE = 0;
    protected final int DRAW_NO_MATCH = -1;
    protected final int DRAW_INVALID = -2;
    protected int w, h;
    protected double[] fretRatios, midFretRatios;
    protected int[][] noteBoard;
    protected float noteRadius, noteBorderInnerRadius;
    protected boolean showOctaves, showAll;
    protected int[] openNotes;
    protected int[] notesToShowAll;
    protected Paint notePaint, noteInnerPaint, noteStringPaint, noteStringInnerPaint, noteOctavePaint, noteAccidentalPaint;
    protected int[][] noteColors;
    protected int noteNeutralColor;
    protected TextPaint noteTextPaint;
    protected float textOffset;
    protected int capoPos;
    protected float fretThickness, stringThickness;
    protected float longFretboardPadding, wideFretboardPadding;
    protected float fretboardWidth, fretboardLength;
    protected float stringBtwnDist;
    protected Path border;
    protected boolean sharps;
    protected int startFret, endFret;

    public NoteBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        notesToShowAll = new int[NUM_NOTES_OCTAVE*NUM_OCTAVE];

        notePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        noteInnerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        noteInnerPaint.setColor(ContextCompat.getColor(context, R.color.colorBackground));
        noteStringPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        noteStringInnerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        noteStringInnerPaint.setColor(ContextCompat.getColor(context, R.color.colorBackground));

        noteColors = new int[4][NUM_NOTES_OCTAVE];

        noteNeutralColor = ContextCompat.getColor(context, R.color.colorNeutral);

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
        noteOctavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        noteAccidentalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        border = new Path();
        textOffset = ((noteTextPaint.descent() - noteTextPaint.ascent()) / 2f) - noteTextPaint.descent();
        capoPos = 0;
    }

    // description: handles the drawing of notes to display on the fretboard.
    @Override
    protected void onDraw(Canvas canvas) {
        if(midFretRatios == null || noteBoard == null || openNotes == null || fretboardLength == 0){
            return;
        }
        for(int i = 0; i < noteBoard.length; i++){
            // color the strings if necessary
            // if no capo, use open notes
            // see class description for more info on type
            int type;
            //capo unset or at a fret that's not visible? draw note at the first fret line then color strings
            if(capoPos < startFret){
                type = getType(notesToShowAll[openNotes[i] + capoPos]);
                if (type != DRAW_INVALID) {
                    float x = fretboardWidth + wideFretboardPadding - i * stringBtwnDist;
                    float y = longFretboardPadding;
                    int note = openNotes[i] + capoPos;
                    drawNoteString(x, y, note, type, canvas);
                    drawNote(x, y, note, type, canvas);
                }
            //capo set? draw note at capo then color strings
            } else if(capoPos >= startFret){
                type = getType(notesToShowAll[openNotes[i] + capoPos]);
                if(type != DRAW_INVALID) {
                    float x = fretboardWidth + wideFretboardPadding - i * stringBtwnDist;
                    float y;
                    if(midFretRatios.length == 1){
                        y = 0.5f * fretboardLength + longFretboardPadding;
                    } else {
                        y = (float) (midFretRatios[capoPos - startFret] * fretboardLength + longFretboardPadding);
                    }
                    int note = openNotes[i] + capoPos;
                    drawNoteString(x, y, note, type, canvas);
                    drawNote(x, y, note, type, canvas);
                }
            }

            // check all notes along this string
            for(int j = 0; j < noteBoard[i].length; j++) {
                if (j > capoPos - startFret){
                    type = getType((notesToShowAll[noteBoard[i][j]]));
                    if(type != DRAW_INVALID) {
                        float x = fretboardWidth + wideFretboardPadding - i * stringBtwnDist;
                        float y = (float) (midFretRatios[j] * fretboardLength + longFretboardPadding);
                        drawNote(x, y, noteBoard[i][j], type, canvas);
                    }
                }
            }
        }
    }

    /* description: gets the type of note to draw
     * int noteValue:
     *      0 if directly matches requested note
     *      UNSET if does not match requested note or its octaves
     *      else, represents the number of octaves from a requested note
     *
     *
     * return type:
     *      DRAW_MATCH          if note is a requested note
     *      DRAW_MATCH_OCTAVE   if note is an octave of requested note and showOctaves is turned on
     *      DRAW_NO_MATCH       if note is none of the above but show all notes is turned on
     *      DRAW_INVALID        if none of the above.
     */
    protected int getType(int noteValue){
        if(noteValue == 0){
            return DRAW_MATCH;
        } else if(noteValue != UNSET && showOctaves){
            return DRAW_MATCH_OCTAVE;
        } else if(showAll){
            return DRAW_NO_MATCH;
        } else {
            return DRAW_INVALID;
        }
    }

    /* description: draws a note at x,y.
     *
     * int note: ID representation [0,132)
     */
    protected void drawNote(float x, float y, int note, int type, Canvas canvas){
        // check for valid
        if(type == DRAW_INVALID){
            return;
        }
        if(type != DRAW_MATCH_OCTAVE && type != DRAW_MATCH && type != DRAW_NO_MATCH){
            throw new IllegalArgumentException("Unknown draw id [-2, 1]: " + type);
        }

        // which octave is this note in
        int octaveType = getOctaveType(note);

        // set color and draw outer circle
        if(type == -1){
            notePaint.setColor(noteNeutralColor);
        } else {
            notePaint.setColor(noteColors[octaveType][note % NUM_NOTES_OCTAVE]);
        }
        canvas.drawCircle(x, y, noteRadius, notePaint);

        // draw inner circle if type is not full note
        if(type != 1){
            canvas.drawCircle(x, y, noteBorderInnerRadius, noteInnerPaint);
        }

        // set text color
        switch(type){
            case -1:
                noteTextPaint.setColor(noteNeutralColor);
                noteAccidentalPaint.setColor(noteNeutralColor);
                break;
            case 0:
                noteTextPaint.setColor(noteColors[octaveType][note % NUM_NOTES_OCTAVE]);
                noteAccidentalPaint.setColor(noteColors[octaveType][note % NUM_NOTES_OCTAVE]);
                break;
            case 1:
                noteTextPaint.setColor(Color.WHITE);
                noteAccidentalPaint.setColor(Color.WHITE);
                break;
        }

        // draw the text
        Note tempNote = Note.IDToNote(note, sharps);
        if(tempNote.getAccidental() != 0){
            float height = 2 * textOffset;
            float width = height * Dimens.ACCIDENTAL_WIDTH_RATIO;
            float accidentalX = (noteTextPaint.measureText(tempNote.getNoteUpperCase() + "") - width)/2;

            if(tempNote.getAccidental() == 1){
                drawSharp(x + accidentalX, y - textOffset, width, height, canvas);
            } else {
                drawFlat(x + accidentalX, y - textOffset, width, height, canvas);
            }

            canvas.drawText(tempNote.getNoteUpperCase()+"", x - width/2, y + textOffset, noteTextPaint);
        } else {
            canvas.drawText(tempNote.getNoteUpperCase()+"", x, y + textOffset, noteTextPaint);
        }

        // show octaves if type is colored outline note
        if(type == 0){
            drawOctaves(notesToShowAll[note], x, y, noteBorderInnerRadius, textOffset, notePaint, canvas);
        }
    }

    // draws the sharp accidental.
    protected void drawSharp(float x, float y, float width, float height, Canvas canvas){
        // draw the 2 vertical lines
        noteAccidentalPaint.setStyle(Paint.Style.STROKE);
        noteAccidentalPaint.setStrokeWidth(width * Dimens.SHARP_VERTICAL_LINE_THICKNESS_RATIO);
        canvas.drawLine(x + width * Dimens.SHARP_VERTICAL_LINE_POS_RATIO,
                y + height * (1 - Dimens.SHARP_VERTICAL_LINE_HEIGHT_RATIO),
                x + width * Dimens.SHARP_VERTICAL_LINE_POS_RATIO,
                y + height,
                noteAccidentalPaint);

        canvas.drawLine(x + width * (1  - Dimens.SHARP_VERTICAL_LINE_POS_RATIO),
                y,
                x + width * (1 - Dimens.SHARP_VERTICAL_LINE_POS_RATIO),
                y + height * Dimens.SHARP_VERTICAL_LINE_HEIGHT_RATIO,
                noteAccidentalPaint);

        // draw the 2 horizontal lines
        noteAccidentalPaint.setStyle(Paint.Style.FILL);
        border.reset();
        // start top left corner-> top right -> bottom right -> bottom left
        border.moveTo(x, y + height * (Dimens.SHARP_HORIZONTAL_LINE_LOWER_POS_RATIO - Dimens.SHARP_HORIZONTAL_LINE_THICKNESS_RATIO/2f));
        border.lineTo(x + width,
                y + height * (Dimens.SHARP_HORIZONTAL_LINE_UPPER_POS_RATIO - Dimens.SHARP_HORIZONTAL_LINE_THICKNESS_RATIO/2f));
        border.lineTo(x + width,
                y + height * (Dimens.SHARP_HORIZONTAL_LINE_UPPER_POS_RATIO + Dimens.SHARP_HORIZONTAL_LINE_THICKNESS_RATIO/2f));
        border.lineTo(x,
                y + height * (Dimens.SHARP_HORIZONTAL_LINE_LOWER_POS_RATIO + Dimens.SHARP_HORIZONTAL_LINE_THICKNESS_RATIO/2f));
        border.close();
        canvas.drawPath(border, noteAccidentalPaint);

        border.reset();
        border.moveTo(x, y + height * (1 - Dimens.SHARP_HORIZONTAL_LINE_UPPER_POS_RATIO - Dimens.SHARP_HORIZONTAL_LINE_THICKNESS_RATIO/2f));
        border.lineTo(x + width,
                y + height * (1 - Dimens.SHARP_HORIZONTAL_LINE_LOWER_POS_RATIO - Dimens.SHARP_HORIZONTAL_LINE_THICKNESS_RATIO/2f));
        border.lineTo(x + width,
                y + height * (1 - Dimens.SHARP_HORIZONTAL_LINE_LOWER_POS_RATIO + Dimens.SHARP_HORIZONTAL_LINE_THICKNESS_RATIO/2f));
        border.lineTo(x,
                y + height * (1 - Dimens.SHARP_HORIZONTAL_LINE_UPPER_POS_RATIO + Dimens.SHARP_HORIZONTAL_LINE_THICKNESS_RATIO/2f));
        border.close();
        canvas.drawPath(border, noteAccidentalPaint);
    }

    //draws the flat accidental.
    protected void drawFlat(float x, float y, float width, float height, Canvas canvas){
        noteAccidentalPaint.setStyle(Paint.Style.FILL);
        border.reset();
        border.moveTo(x + width * Dimens.FLAT_VERTICAL_LINE_THICKNESS_TOP_RATIO, y);
        border.lineTo(x, y);
        border.lineTo(x, y + height * Dimens.FLAT_VERTICAL_LINE_HEIGHT_RATIO);
        border.lineTo(x + width * Dimens.FLAT_VERTICAL_LINE_THICKNESS_BOTTOM_RATIO, y + height * Dimens.FLAT_VERTICAL_LINE_HEIGHT_RATIO);
        border.close();
        canvas.drawPath(border, noteAccidentalPaint);

        border.reset();
        border.moveTo(x, y + height * Dimens.FLAT_ROUND_START_OUTER_Y_RATIO);
        border.cubicTo(x + width * Dimens.FLAT_ROUND_OUTER_CUBIC_UPPER_X_RATIO,
                y + height * Dimens.FLAT_ROUND_OUTER_CUBIC_UPPER_Y_RATIO,
                x + width * Dimens.FLAT_ROUND_OUTER_CUBIC_LOWER_X_RATIO,
                y + height * Dimens.FLAT_ROUND_OUTER_CUBIC_LOWER_Y_RATIO,
                x, y + height);
        border.lineTo(x, y + height * Dimens.FLAT_ROUND_END_INNER_Y_RATIO);
        border.cubicTo(x + width * Dimens.FLAT_ROUND_INNER_CUBIC_LOWER_X_RATIO,
                y + height * Dimens.FLAT_ROUND_INNER_CUBIC_LOWER_Y_RATIO,
                x + width * Dimens.FLAT_ROUND_INNER_CUBIC_UPPER_X_RATIO,
                y + height * Dimens.FLAT_ROUND_INNER_CUBIC_UPPER_Y_RATIO,
                x, y + height * Dimens.FLAT_ROUND_START_INNER_Y_RATIO);
        border.close();
        canvas.drawPath(border, noteAccidentalPaint);
    }

    /* highlights the string
     *
     * int note: ID representation [0,132)
     */
    protected void drawNoteString(float x, float y, int note, int type, Canvas canvas){
        // check for valid
        if(type == DRAW_INVALID || type == DRAW_NO_MATCH){
            return;
        }
        if(type != DRAW_MATCH_OCTAVE && type != DRAW_MATCH){
            throw new IllegalArgumentException("Unknown draw id [-2, 1]: " + type);
        }

        int octaveType = getOctaveType(note);
        noteStringPaint.setColor(noteColors[octaveType][note % NUM_NOTES_OCTAVE]);
        canvas.drawLine(x, y, x, h - longFretboardPadding + fretThickness/2f, noteStringPaint);

        if (type == DRAW_MATCH_OCTAVE) {
            canvas.drawLine(x, y, x, h - longFretboardPadding, noteStringInnerPaint);
        }
    }

    /* draws octaves on the circle note
     *
     * int octave: number of octaves away from requested note
     */
    protected void drawOctaves(int octave, float noteX, float noteY, float radius, float textOffset, Paint paint, Canvas canvas){
        float y;
        if(octave < 0){
            y = noteY + radius * Dimens.OCTAVE_DISPLAY_RADIUS_RATIO;
        } else if(octave > 0){
            y = noteY - radius * Dimens.OCTAVE_DISPLAY_RADIUS_RATIO;
        } else {
            return;
        }
        octave = Math.abs(octave);

        //use the pythagorean theorem to calculate the distance from the y coordinate to the edge of the circle
        float octaveChordDist = (float) (2f * (Math.sqrt(Math.pow(radius, 2) - Math.pow((radius * Dimens.OCTAVE_DISPLAY_RADIUS_RATIO), 2))));

        //draw the octave dots along the chord
        for(float xPos = -octaveChordDist/2f + octaveChordDist/(octave + 1); xPos < octaveChordDist/2f; xPos += octaveChordDist/(octave + 1)){
            canvas.drawCircle(noteX + xPos, y, textOffset/5, paint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        this.stringThickness = w/ Dimens.STRING_THICKNESS_RATIO;
        this.fretThickness = Dimens.calcFretThickness(h);
        this.longFretboardPadding = Dimens.calcLongFretboardPadding(h); // proportion of fretboard height
        this.fretboardLength = h - 2 * longFretboardPadding;
        this.fretboardWidth = Dimens.calcFretboardWidth(w, h);
        this.wideFretboardPadding = Dimens.calcWideFretboardPadding(w, h);
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
                float fretDistDependant = (float)(lastFretDistRatio / 2f * fretboardLength - fretThickness/2f - MIN_PADDING);
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
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /* returns the shade of the note it should draw, to be used with noteColors
     * int note: ID representation [0, 132)
     *
     * return:
     *      0   if note is in the 6th octave or higher
     *      1   if note is in the 5th octave
     *      2   if note is in the 4th octave
     *      3   if note is in the 3rd octave or lower
     */
    protected int getOctaveType(int note){
        if (note > NUM_NOTES_OCTAVE * 6) {
            return 0;
        } else if (note > NUM_NOTES_OCTAVE * 5) {
            return 1;
        } else if (note > NUM_NOTES_OCTAVE * 4) {
            return 2;
        } else {
            return 3;
        }
    }

    public void setFretRatios(double[] fretRatios, double[] midFretRatios) {
        this.fretRatios = fretRatios;
        this.midFretRatios = midFretRatios;
        this.noteRadius = getResources().getDimensionPixelSize(R.dimen.max_fret_font_size)*5/6;
        this.noteBorderInnerRadius = noteRadius - MIN_PADDING;

        // set the size of the notes to account for fret length and string distance
        if(fretRatios != null){
            double lastFretDistRatio = 0;
            if(fretRatios.length > 1){
                lastFretDistRatio = (fretRatios[fretRatios.length - 1] - fretRatios[fretRatios.length - 2]);
            } else if(fretRatios.length == 1){
                lastFretDistRatio = fretRatios[0];
            }
            if(lastFretDistRatio != 0) {
                float fretDistDependant = (float)(lastFretDistRatio / 2f * fretboardLength);
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

    public void setCapo(int capoPos){
        this.capoPos = capoPos;
        invalidate();
    }

    // if invalid, set to UNSET
    public void showNotes(boolean[] notesToShow){
        Arrays.fill(notesToShowAll, UNSET);
        for(int i = 0; i < notesToShow.length; i++){
            if(notesToShow[i]){
                for(int note = i%NUM_NOTES_OCTAVE; note < NUM_NOTES_OCTAVE*NUM_OCTAVE; note+=NUM_NOTES_OCTAVE) {
                    if(notesToShowAll[note] == UNSET || notesToShowAll[note] > 0){
                        notesToShowAll[note] = (note - i)/NUM_NOTES_OCTAVE;
                    }
                }
                notesToShowAll[i] = 0;
            }
        }
        invalidate();
    }

    public void showNote(int noteId){
        // when showing a note, we need to update the octave parameter if the same note in multiple octaves are toShow

        notesToShowAll[noteId] = 0;
        for(int note = noteId%NUM_NOTES_OCTAVE; note < NUM_NOTES_OCTAVE*NUM_OCTAVE; note+=NUM_NOTES_OCTAVE) {
            // if toShow note is spotted, we want to traverse each octave and reset its display.
            if(notesToShowAll[note] == 0 && note >= NUM_NOTES_OCTAVE){
                // we start from the note and traverse downwards until we reach the end or another toShow note.
                for(int octaveNote = note - NUM_NOTES_OCTAVE; octaveNote >= 0; octaveNote -= NUM_NOTES_OCTAVE){
                    // if we run into another toShow note, we stop.
                    if(notesToShowAll[octaveNote] == 0){
                        break;
                    }
                    notesToShowAll[octaveNote] = (octaveNote - note)/NUM_NOTES_OCTAVE;
                }
                // then we start from the note and traverse upwards, setting all of their displays.
                for(int octaveNote = note + NUM_NOTES_OCTAVE; octaveNote < NUM_NOTES_OCTAVE*NUM_OCTAVE; octaveNote += NUM_NOTES_OCTAVE){
                    if(notesToShowAll[octaveNote] != 0){
                        notesToShowAll[octaveNote] = (octaveNote - note) / NUM_NOTES_OCTAVE;
                    }
                }
            }
            // then we find the next toShow note, and repeat
        }
        invalidate();
    }

    public void hideNote(int noteId){
        boolean reset = false; // determine if we need to reset the octaves the hard way
        for(int note = noteId%NUM_NOTES_OCTAVE; note < NUM_NOTES_OCTAVE*NUM_OCTAVE; note+=NUM_NOTES_OCTAVE) {
            // if there exists the same note of a different octave, we must reset the octaves' display the hard way.
            if(notesToShowAll[note] == 0 && note != noteId){
                reset = true;
                break;
            }
        }
        notesToShowAll[noteId] = UNSET;
        // resetting the octaves' display parameters to match up with the updated octaves
        if(reset){
            for(int note = noteId%NUM_NOTES_OCTAVE; note < NUM_NOTES_OCTAVE*NUM_OCTAVE; note+=NUM_NOTES_OCTAVE) {
                // if toShow note is spotted, we want to traverse each octave and reset its display.
                if(notesToShowAll[note] == 0 && note >= NUM_NOTES_OCTAVE){
                    // we start from the note and traverse downwards until we reach the end or another toShow note.
                    for(int octaveNote = note - NUM_NOTES_OCTAVE; octaveNote >= 0; octaveNote -= NUM_NOTES_OCTAVE){
                        // if we run into another toShow note, we stop.
                        if(notesToShowAll[octaveNote] == 0){
                            break;
                        }
                        notesToShowAll[octaveNote] = (octaveNote - note)/NUM_NOTES_OCTAVE;
                    }
                    // then we start from the note and traverse upwards, setting all of their displays.
                    for(int octaveNote = note + NUM_NOTES_OCTAVE; octaveNote < NUM_NOTES_OCTAVE*NUM_OCTAVE; octaveNote += NUM_NOTES_OCTAVE){
                        notesToShowAll[octaveNote] = (octaveNote - note)/NUM_NOTES_OCTAVE;
                    }
                }
                // then we find the next toShow note, and repeat
            }
        // if there are not repeating notes in different octaves, we can unset the note in every octave.
        } else {
            for(int note = noteId%NUM_NOTES_OCTAVE; note < NUM_NOTES_OCTAVE*NUM_OCTAVE; note+=NUM_NOTES_OCTAVE) {
                notesToShowAll[note] = UNSET;
            }
        }
        invalidate();
    }

    public void moveNote(int oldNoteId, int newNoteId){
        //hid the new note
        boolean reset = false;
        for(int note = oldNoteId%NUM_NOTES_OCTAVE; note < NUM_NOTES_OCTAVE*NUM_OCTAVE; note+=NUM_NOTES_OCTAVE) {
            // if there exists the same note of a different octave, we must reset the octaves' display.
            if(notesToShowAll[note] == 0 && note != oldNoteId){
                reset = true;
                break;
            }
        }
        notesToShowAll[oldNoteId] = UNSET;
        if(reset){
            for(int note = oldNoteId%NUM_NOTES_OCTAVE; note < NUM_NOTES_OCTAVE*NUM_OCTAVE; note+=NUM_NOTES_OCTAVE) {
                // if toShow note is spotted, we want to traverse each octave and reset its display.
                if(notesToShowAll[note] == 0 && note >= NUM_NOTES_OCTAVE){
                    // we start from the note and traverse downwards until we reach the end or another toShow note.
                    for(int octaveNote = note - NUM_NOTES_OCTAVE; octaveNote >= 0; octaveNote -= NUM_NOTES_OCTAVE){
                        // if we run into another toShow note, we stop.
                        if(notesToShowAll[octaveNote] == 0){
                            break;
                        }
                        notesToShowAll[octaveNote] = (octaveNote - note)/NUM_NOTES_OCTAVE;
                    }
                    // then we start from the note and traverse upwards, setting all of their displays.
                    for(int octaveNote = note + NUM_NOTES_OCTAVE; octaveNote < NUM_NOTES_OCTAVE*NUM_OCTAVE; octaveNote += NUM_NOTES_OCTAVE){
                        if(notesToShowAll[octaveNote] != 0) {
                            notesToShowAll[octaveNote] = (octaveNote - note) / NUM_NOTES_OCTAVE;
                        }
                    }
                }
                // then we find the next toShow note, and repeat
            }
            // if there are not repeating notes in different octaves, we can unset the note in every octave.
        } else {
            for(int note = oldNoteId%NUM_NOTES_OCTAVE; note < NUM_NOTES_OCTAVE*NUM_OCTAVE; note+=NUM_NOTES_OCTAVE) {
                notesToShowAll[note] = UNSET;
            }
        }

        // show the new note.
        notesToShowAll[newNoteId] = 0;
        for(int note = newNoteId%NUM_NOTES_OCTAVE; note < NUM_NOTES_OCTAVE*NUM_OCTAVE; note+=NUM_NOTES_OCTAVE) {
            // if toShow note is spotted, we want to traverse each octave and reset its display.
            if(notesToShowAll[note] == 0 && note >= NUM_NOTES_OCTAVE){
                // we start from the note and traverse downwards until we reach the end or another toShow note.
                for(int octaveNote = note - NUM_NOTES_OCTAVE; octaveNote >= 0; octaveNote -= NUM_NOTES_OCTAVE){
                    // if we run into another toShow note, we stop.
                    if(notesToShowAll[octaveNote] == 0){
                        break;
                    }
                    notesToShowAll[octaveNote] = (octaveNote - note)/NUM_NOTES_OCTAVE;
                }
                // then we start from the note and traverse upwards, setting all of their displays.
                for(int octaveNote = note + NUM_NOTES_OCTAVE; octaveNote < NUM_NOTES_OCTAVE*NUM_OCTAVE; octaveNote += NUM_NOTES_OCTAVE){
                    notesToShowAll[octaveNote] = (octaveNote - note)/NUM_NOTES_OCTAVE;
                }
            }
            // then we find the next toShow note, and repeat
        }
        invalidate();
    }

    public void clearNotes(){
        Arrays.fill(notesToShowAll, UNSET);
        invalidate();
    }

    public void setShowOctaves(boolean showOctaves) {
        this.showOctaves = showOctaves;
        invalidate();
    }

    public void setShowAll(boolean showAll) {
        this.showAll = showAll;
        invalidate();
    }

    public void setNoteBoard(int[] openNotes, int startFret, int endFret){
        noteBoard = new int[openNotes.length][endFret-startFret];
        for(int i = 0; i < openNotes.length; i++){
            for(int j = 0; j < endFret-startFret; j++){
                noteBoard[i][j] = openNotes[i] + startFret + j;
            }
        }
        this.openNotes = openNotes;
        this.startFret = startFret;
        this.endFret = endFret;
        invalidate();
    }

    public void setSharps(boolean sharps){
        this.sharps = sharps;
        invalidate();
    }
}
