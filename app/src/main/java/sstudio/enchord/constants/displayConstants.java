package sstudio.enchord.constants;

/**
 * Created by seanoh on 7/5/16.
 */
public final class displayConstants {
    private displayConstants(){ }

    //thickness of the string as a proportion of fretboard width
    public static final int STRING_THICKNESS_RATIO = 120;
    //thickness of the fret as a proportion of fretboard height
    public static final int FRET_THICKNESS_RATIO = 250;
    //length of the padding to the fretboard (top and bottom) as a proportion of fretboard height
    public static final float TOP_BOTTOM_PADDING_RATIO = 23.1f;
    //proportion of the note radius at which to display the octave information
    public static final float OCTAVE_DISPLAY_RADIUS_RATIO = .7f;
    public static final int NUM_NOTES_OCTAVE = 12; // number of notes in an octave
    public static final int NUM_OCTAVE = 11; // supported number of octaves (0 to 132 notes)
    public static final double NEXT_FRET_CONST = 17.817; // fretboard constant to calculate fret position.
    public static final int MAX_FRET = 24; // maximum fret supported. Mostly due to guitar restraints.
    public static final float FONT_SIZE_TO_NOTE_RADIUS_RATIO = 5f/6; //ratio of font size to note radius.
    public static final float FRETBOARD_WIDTH_RATIO = 5.2f; // ratio of fretboard width : height
    public static final float STRING_BOLD_RATIO = 1.5f;
}
