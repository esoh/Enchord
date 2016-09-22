package sstudio.enchord;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Switch;

import sstudio.enchord.fragments.FretboardFragment;
import sstudio.enchord.objects.Note;

/**
 * Created by seanoh on 6/30/16.
 */
public class MainActivity extends AppCompatActivity {
    private final int MAX_FRET_VAL = 24;
    private Switch switchAllNotes, switchOctaves, switchSharps;
    private Button buttonClearNotes;
    private FretboardFragment fretboard;
    private boolean showOctaves, showAllNotes, showSharps;
    private NumberPicker startFretPicker, endFretPicker;
    private int startFret, endFret;
    private int capo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showOctaves = true;
        showAllNotes = false;
        showSharps = true;
        startFret = 1;
        endFret = 15;
        capo = 2;

        switchAllNotes = (Switch) findViewById(R.id.show_all_notes_switch);
        switchOctaves = (Switch) findViewById(R.id.show_octaves_switch);
        switchSharps = (Switch) findViewById(R.id.show_sharps_switch);
        buttonClearNotes = (Button) findViewById(R.id.clear_notes_button);
        startFretPicker = (NumberPicker) findViewById(R.id.start_fret_number_picker);
        endFretPicker = (NumberPicker) findViewById(R.id.end_fret_number_picker);

        fretboard = (FretboardFragment) getFragmentManager().findFragmentById(R.id.fretboard_fragment);
        //TODO: save showNotes && capo through bundle
        //TODO: save startFret, endFret, tuning, showOctaves, showAll, showSharps in settings
        fretboard.showNote(Note.noteToID('c', 0, 5));
        fretboard.showNote(Note.noteToID('e', 0, 5));
        fretboard.showNote(Note.noteToID('g', 0, 5));
        try {
            fretboard.setCapo(capo);

            switchAllNotes.setChecked(showAllNotes);
            fretboard.setShowAllNotes(showAllNotes);

            switchOctaves.setChecked(showOctaves);
            fretboard.setShowOctaves(showOctaves);

            switchSharps.setChecked(showSharps);
            fretboard.setSharps(showSharps);

            startFretPicker.setMaxValue(MAX_FRET_VAL-1);
            endFretPicker.setMaxValue(MAX_FRET_VAL-1);

            startFretPicker.setMinValue(1);
            if(capo > 0){
                endFretPicker.setMinValue(capo);
            } else {
                endFretPicker.setMinValue(1);
            }

            startFretPicker.setValue(startFret);
            endFretPicker.setValue(endFret);
            fretboard.setFretRange(startFret, endFret + 1);

            startFretPicker.setWrapSelectorWheel(false);
            endFretPicker.setWrapSelectorWheel(false);

            switchAllNotes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        fretboard.setShowAllNotes(isChecked);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            switchOctaves.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        fretboard.setShowOctaves(isChecked);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            switchSharps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        fretboard.setSharps(isChecked);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            buttonClearNotes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        fretboard.clearNotes();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            startFretPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    if(endFretPicker.getValue() < newVal){
                        endFretPicker.setValue(newVal);
                    }
                    try {
                        fretboard.setFretRange(newVal, endFretPicker.getValue() + 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            endFretPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    if(startFretPicker.getValue() > newVal){
                        startFretPicker.setValue(newVal);
                    }
                    try {
                        fretboard.setFretRange(startFretPicker.getValue(), newVal + 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
