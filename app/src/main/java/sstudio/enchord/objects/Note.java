package sstudio.enchord.objects;

import sstudio.enchord.constants.displayConstants;

/**
 * Created by seanoh on 6/30/16.
 *
 * Note: Notes are in scientific pitch notation.
 * Example: C5 is Middle C.
 *          Octave increases when going from B to C.
 *          B4->C5.
 *
 * Additional note: C0 has ID of 0 in this custom object.
 */
public class Note {
    //Scientific pitch notation. middle C is octave
    //character of the basic note
    private char note;
    // accidental: -1 for flat, 0 for natural, 1 for sharp.
    private int accidental;
    //octave of the note
    private int octave;
    // full id of the note [0,132)
    private int noteId;

    // calculate fields given the given parameters
    public Note(char note, int accidental, int octave){
        char lowerCaseNote = Character.toLowerCase(note);
        switch(lowerCaseNote){
            case 'c':
                if(accidental == -1){
                    octave--;
                    note = 'b';
                    accidental = 0;
                    noteId = displayConstants.NUM_NOTES_OCTAVE - 1;
                } else {
                    noteId = 0;
                }
                break;
            case 'd':
                noteId = 2;
                break;
            case 'e':
                if(accidental == 1){
                    note = 'f';
                    accidental = 0;
                    noteId = 5;
                } else {
                    noteId = 4;
                }
                break;
            case 'f':
                if(accidental == -1){
                    note = 'e';
                    accidental = 0;
                    noteId = 4;
                } else {
                    noteId = 5;
                }
                break;
            case 'g':
                noteId = 7;
                break;
            case 'a':
                noteId = 9;
                break;
            case 'b':
                if(accidental == 1){
                    octave++;
                    note = 'c';
                    accidental = 0;
                    noteId = 0;
                } else {
                    noteId = displayConstants.NUM_NOTES_OCTAVE - 1;
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid note: " + note);
        }
        switch(accidental){
            case -1:
                noteId--;
                break;
            case 1:
                noteId++;
                break;
            case 0:
                break;
            default:
                throw new IllegalArgumentException("Invalid accidental (-1, 0, or 1): " + accidental);
        }
        if(octave < 0 || octave > displayConstants.NUM_OCTAVE - 1){
            throw new IllegalArgumentException("Octave must be between 0 and 10: " + octave);
        }
        this.note = note;
        this.accidental = accidental;
        this.octave = octave;
        this.noteId += octave* displayConstants.NUM_NOTES_OCTAVE;
    }

    public Note(int id, boolean sharps){
        if(id < 0 || id > displayConstants.NUM_OCTAVE * displayConstants.NUM_NOTES_OCTAVE){
            throw new IllegalArgumentException("Id must be in [0, 132): " + id);
        }
        octave = id/displayConstants.NUM_NOTES_OCTAVE;
        int sNoteId = id%displayConstants.NUM_NOTES_OCTAVE;
        switch (sNoteId){
            case 1:
            case 3:
            case 6:
            case 8:
            case 10:
                accidental = 0;
                //if flats
                if(!sharps){
                    sNoteId++;
                    accidental = -1;
                } else {
                    sNoteId--;
                    accidental = 1;
                }
                break;
            default:
                break;
        }
        switch (sNoteId) {
            case 0:
                note = 'c';
                break;
            case 2:
                note = 'd';
                break;
            case 4:
                note = 'e';
                break;
            case 5:
                note = 'f';
                break;
            case 7:
                note = 'g';
                break;
            case 9:
                note = 'a';
                break;
            case 11:
                note = 'b';
                break;
            default:
                throw new IllegalArgumentException("Invalid natural note (0,2,4,5,7,9,10): " + sNoteId);
        }
        noteId = id;
    }

    public static int noteToID(Character note, int accidental, int octave){
        Note temp = new Note(note, accidental, octave);
        return temp.getNoteId();
    }

    // accidentalFormat: false for flats, true for sharps
    public static Note IDToNote(int id, boolean sharps){
        return new Note(id, sharps);
    }

    public Note add(int toAdd){
        return add(toAdd, true);
    }

    public Note add(int toAdd, boolean sharps){
        return new Note(noteId + toAdd, sharps);
    }

    public int getNoteId() {
        return noteId;
    }

    public char getNote() {
        return note;
    }

    public int getAccidental() {
        return accidental;
    }

    public char getNoteUpperCase() {
        return Character.toUpperCase(note);
    }

    @Override
    public String toString() {
        String toReturn = "";
        toReturn += note;
        if(accidental == -1){
            toReturn += "Flat";
        } else if(accidental == 1){
            toReturn += "Sharp";
        }
        toReturn += ("(" + octave + ":" + noteId + ")");
        return toReturn;
    }


}
