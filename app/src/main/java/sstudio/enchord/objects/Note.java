package sstudio.enchord.objects;

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
    private char note;
    private int accidental;
    private int octave;
    private int noteId;

    public Note(char note, int accidental, int octave){
        char lowerCaseNote = Character.toLowerCase(note);
        switch(lowerCaseNote){
            case 'c':
                if(accidental == -1){
                    octave--;
                    note = 'b';
                    accidental = 0;
                    noteId = 11;
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
                    noteId = 11;
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
        if(octave < 0 || octave > 10){
            throw new IllegalArgumentException("Octave must be between 0 and 10: " + octave);
        }
        this.note = note;
        this.accidental = accidental;
        this.octave = octave;
        this.noteId += octave*12;
    }

    public Note(int id, boolean sharps){
        if(id < 0 || id > 132){
            throw new IllegalArgumentException("Id must be in [0, 132): " + id);
        }
        octave = id/12;
        int sNoteId = id%12;
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

    public String getShort(boolean sharps) {
        Note temp = new Note(noteId, sharps);
        String toReturn = Character.toUpperCase(note) + "";
        switch(temp.getAccidental()){
            case -1:
                toReturn += "b";
                break;
            case 0:
                break;
            case 1:
                toReturn += "#";
                break;
        }
        return toReturn;
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