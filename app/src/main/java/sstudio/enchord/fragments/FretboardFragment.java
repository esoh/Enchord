package sstudio.enchord.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sstudio.enchord.R;
import sstudio.enchord.views.FretboardView;

public class FretboardFragment extends Fragment {

    //TODO: move to settings file
    private int startFret = 1;// inclusive. standard: 1
    private int endFret = 20;// not inclusive. standard: 20
    private double[] fretRatios, midFretRatios;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fretboard_view, container, false);

        // calculate fret ratios
        try {
            fretRatios = getFretRatios(endFret - startFret);
        } catch(Exception e){
            e.printStackTrace();
        }

        FretboardView myFretboard = (FretboardView) rootView.findViewById(R.id.fretboard);
        if(myFretboard != null) {
            myFretboard.setFretRange(startFret, endFret);
            myFretboard.setFretRatios(fretRatios, midFretRatios);
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