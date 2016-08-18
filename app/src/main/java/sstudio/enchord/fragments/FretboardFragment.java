package sstudio.enchord.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sstudio.enchord.R;
import sstudio.enchord.constants.Dimens;
import sstudio.enchord.objects.Note;
import sstudio.enchord.views.CapoView;
import sstudio.enchord.views.FretboardView;
import sstudio.enchord.views.NoteBoardView;

public class FretboardFragment extends Fragment{
    private static final int NUM_NOTES_ALL = Dimens.NUM_NOTES_OCTAVE * Dimens.NUM_OCTAVE;
    //TODO: move to settings file
    private int startFret;// inclusive. standard: 1
    private int endFret;// not inclusive. standard: 20
    private int numStrings;
    private double[] fretRatios, midFretRatios;
    private int[] openNotes;
    private int capo; // position on the chart, NOT the fret # it's on
    private boolean[] notesToShow;
    private boolean showOctaves, showAll, sharps;
    private NoteBoardView mNoteBoard;
    private FretboardView mFretboard;
    private CapoView mCapo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fretboard_view, container, false);

        // initialize settings, openNotes, capo
        // 19 fret guitar, can be adjusted to <= 24. start < end.
        startFret = 1;
        endFret = 16;
        // 6 string guitar. not adjustable as far as right now
        numStrings = 6;
        // standard openNotes
        if(numStrings > 8 || numStrings < 6){
            throw new IllegalArgumentException("Unsupported number of strings (supported: 6-8): " + numStrings);
        }
        openNotes = new int[numStrings];
        switch(numStrings){
            case 8:
                openNotes[7] = Note.noteToID('f', 1, 2);
            case 7:
                openNotes[6] = Note.noteToID('b', 0, 2);
            case 6:
                openNotes[5] = Note.noteToID('e', 0, 3);
                openNotes[4] = Note.noteToID('a', 0, 3);
                openNotes[3] = Note.noteToID('d', 0, 4);
                openNotes[2] = Note.noteToID('g', 0, 4);
                openNotes[1] = Note.noteToID('b', 0, 4);
                openNotes[0] = Note.noteToID('e', 0, 5);
                break;
            default:
        }
        //capo off = 0. if capo == 1, that means it's on the 1st fret.
        capo = 2;

        notesToShow = new boolean[NUM_NOTES_ALL];
        notesToShow[Note.noteToID('c', 0, 5)] = true;
        notesToShow[Note.noteToID('e', 0, 5)] = true;
        notesToShow[Note.noteToID('g', 0, 5)] = true;

        //showOctaves = true;
        //showAll = true;
        sharps = true;

        // calculate fret ratios
        try {
            fretRatios = getFretRatios(endFret - startFret);
        } catch(Exception e){
            e.printStackTrace();
        }

        mFretboard =  (FretboardView) rootView.findViewById(R.id.fretboard);
        if(mFretboard != null) {
            mFretboard.setFretRange(startFret, endFret);
            mFretboard.setFretRatios(fretRatios, midFretRatios);
            mFretboard.setNumStrings(numStrings);
        }

        mCapo = (CapoView) rootView.findViewById(R.id.capo);
        if(mCapo != null){
            mCapo.setFretRatios(fretRatios, midFretRatios);
            mCapo.setStartFret(startFret);
            mCapo.setCapo(capo);
        }

        mNoteBoard = (NoteBoardView) rootView.findViewById(R.id.note_board);

        if(mNoteBoard != null) {
            mNoteBoard.setFretRatios(fretRatios, midFretRatios);
            mNoteBoard.setNoteBoard(openNotes, startFret, endFret);
            mNoteBoard.setCapo(capo);
            mNoteBoard.showNotes(notesToShow);
            mNoteBoard.setShowOctaves(showOctaves);
            mNoteBoard.setShowAll(showAll);
            mNoteBoard.setSharps(sharps);
        }
        return rootView;
    }

    public void setShowAllNotes(boolean show) throws Exception{
        if(mNoteBoard != null) {
            mNoteBoard.setShowAll(show);
            this.showAll = show;
        } else {
            throw new Exception("Noteboard doesn't exist!");
        }
    }

    public void setShowOctaves(boolean show) throws Exception{
        if(mNoteBoard != null) {
            mNoteBoard.setShowOctaves(show);
            this.showOctaves = show;
        } else {
            throw new Exception("Noteboard doesn't exist!");
        }
    }

    public void setSharps(boolean sharps) throws Exception{
        if(mNoteBoard != null) {
            mNoteBoard.setSharps(sharps);
            this.sharps = sharps;
        } else {
            throw new Exception("Noteboard doesn't exist!");
        }
    }

    public void setTuning(int[] openNotes) throws Exception{
        if(mNoteBoard != null){
            this.openNotes = openNotes;
            mNoteBoard.setNoteBoard(openNotes, startFret, endFret);
        } else {
            throw new Exception("Noteboard doesn't exist!");
        }
    }

    public void setCapo(int capo) throws Exception{
        if(mCapo != null && mNoteBoard != null){
            this.capo = capo;
            mCapo.setCapo(capo);
            mNoteBoard.setCapo(capo);
        }
    }

    public void setNotesToShow(boolean[] notesToShow) throws Exception {
        if(mNoteBoard == null) {
            throw new Exception("Noteboard doesn't exist!");
        } else if(notesToShow.length != NUM_NOTES_ALL) {
            throw new Exception("Wrong size of notes to show");
        } else {
            mNoteBoard.showNotes(notesToShow);
            this.notesToShow = notesToShow;
        }
    }

    public void hideNote(int noteId) throws IndexOutOfBoundsException{
        try{
            if(!notesToShow[noteId]){
                return;
            }
            notesToShow[noteId] = false;
            mNoteBoard.hideNote(noteId);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void showNote(int noteId) throws IndexOutOfBoundsException{
        try{
            if(notesToShow[noteId]){
                return;
            }
            notesToShow[noteId] = true;
            mNoteBoard.showNote(noteId);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void moveNote(int oldNoteId, int newNoteId) throws IndexOutOfBoundsException{
        try{
            if(!notesToShow[oldNoteId] && notesToShow[newNoteId]){
                return;
            }
            notesToShow[oldNoteId] = false;
            notesToShow[newNoteId] = true;
            mNoteBoard.moveNote(oldNoteId, newNoteId);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void clearNotes() throws Exception{
        if(mNoteBoard == null){
            throw new Exception("Noteboard doesn't exist!");
        }
        mNoteBoard.clearNotes();
        notesToShow = new boolean[NUM_NOTES_ALL];
        Log.d("AYYY", "CLEARED.");
    }

    public void setFretRange(int startFret, int endFret) throws Exception {
        // calculate fret ratios
        try {
            fretRatios = getFretRatios(endFret - startFret);
        } catch(Exception e){
            e.printStackTrace();
        }

        if(mFretboard != null && mCapo != null && mNoteBoard != null){
            if(this.endFret - this.startFret != endFret - startFret){
                mFretboard.setFretRatios(fretRatios, midFretRatios);
                mCapo.setFretRatios(fretRatios, midFretRatios);
                mNoteBoard.setFretRatios(fretRatios, midFretRatios);
            }
            this.startFret = startFret;
            this.endFret = endFret;
            mFretboard.setFretRange(startFret, endFret);
            mCapo.setStartFret(startFret);
            mNoteBoard.setNoteBoard(openNotes, startFret, endFret);
        } else {
            throw new Exception("Fretboard or its parts don't exist!");
        }
    }

    /* calculates the ratios at which the frets are drawn
     * also calculates the ratios of the midpoints of the frets */
    private double[] getFretRatios(int numFrets) throws Exception {
        if(startFret < 1 || endFret > Dimens.MAX_FRET){
            throw new Exception("The frets must be between 1 and 24 (inclusive).");
        }
        if(startFret >= endFret){
            throw new Exception("The start fret must be greater than the end fret.");
        }

        double[] fretRatios = new double[numFrets];
        midFretRatios = new double[numFrets];

        fretRatios[0] = 1/ Dimens.NEXT_FRET_CONST;
        midFretRatios[0] = (0 + fretRatios[0])/2;
        for(int i = 1; i < numFrets; i++){
            fretRatios[i] = (1 - fretRatios[i-1])/ Dimens.NEXT_FRET_CONST + fretRatios[i-1];
            midFretRatios[i] = (fretRatios[i-1] + fretRatios[i])/2;
        }
        //base the size off of the position of the last fret
        double ratio = 1/fretRatios[numFrets - 1];
        for(int i = 0; i < numFrets; i++){
            fretRatios[i] = fretRatios[i] * ratio;
            midFretRatios[i] = midFretRatios[i] * ratio;
        }

        return fretRatios;
    }
}
