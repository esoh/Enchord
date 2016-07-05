package sstudio.enchord.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sstudio.enchord.R;
import sstudio.enchord.objects.Note;
import sstudio.enchord.views.FretboardView;
import sstudio.enchord.views.NoteBoardView;

public class FretboardFragment extends Fragment {

    //TODO: move to settings file
    private int startFret;// inclusive. standard: 1
    private int endFret;// not inclusive. standard: 20
    private int numStrings;
    private double[] fretRatios, midFretRatios;
    private int[] openNotes;
    private int[][] fretboardNotes;
    private int capo; // position on the chart, NOT the fret # it's on
    private boolean[] notesToShow;

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
        //capo off = -1. if capo == 0, that means it's on the 1st fret.
        capo = -1;

        notesToShow = new boolean[132];
        notesToShow[60] = true;
        notesToShow[64] = true;
        notesToShow[67] = true;

        // calculate fret ratios
        try {
            fretRatios = getFretRatios(endFret - startFret);
        } catch(Exception e){
            e.printStackTrace();
        }

        // add fretboard notes
        fretboardNotes = new int[numStrings][endFret-startFret];
        for(int i = 0; i < numStrings; i++){
            for(int j = 0; j < endFret-startFret; j++){
                fretboardNotes[i][j] = openNotes[i] + startFret + j;
            }
        }

        FretboardView mFretboard = (FretboardView) rootView.findViewById(R.id.fretboard);
        if(mFretboard != null) {
            mFretboard.setFretRange(startFret, endFret);
            mFretboard.setFretRatios(fretRatios, midFretRatios);
        }

        NoteBoardView mNoteBoard = (NoteBoardView) rootView.findViewById(R.id.note_board);
        if(mNoteBoard != null) {
            mNoteBoard.setFretRatios(fretRatios, midFretRatios);
            mNoteBoard.setNoteBoard(fretboardNotes);
            mNoteBoard.setCapo(capo);
            mNoteBoard.setNotes(notesToShow);
        }
        return rootView;
    }

    /* calculates the ratios at which the frets are drawn
     * also calculates the ratios of the midpoints of the frets */
    private double[] getFretRatios(int numFrets) throws Exception {
        if(startFret < 1 || endFret > 24){
            throw new Exception("The frets must be between 1 and 24 (inclusive).");
        }
        if(startFret >= endFret){
            throw new Exception("The start fret must be greater than the end fret.");
        }

        double[] fretRatios = new double[numFrets];
        midFretRatios = new double[numFrets];

        fretRatios[0] = 1/17.817;
        midFretRatios[0] = (0 + fretRatios[0])/2;
        for(int i = 1; i < numFrets; i++){
            fretRatios[i] = (1 - fretRatios[i-1])/17.817 + fretRatios[i-1];
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
