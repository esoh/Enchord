package sstudio.enchord;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import sstudio.enchord.fragments.FretboardFragment;
import sstudio.enchord.objects.Note;

/**
 * Created by seanoh on 6/30/16.
 */
public class MainActivity extends AppCompatActivity {
    private Switch switchAllNotes, switchOctaves, switchSharps;
    private Button buttonClearNotes;
    private FretboardFragment fretboard;
    private boolean showOctaves, showAllNotes, showSharps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showOctaves = true;
        showAllNotes = false;
        showSharps = true;

        switchAllNotes = (Switch) findViewById(R.id.show_all_notes_switch);
        switchOctaves = (Switch) findViewById(R.id.show_octaves_switch);
        switchSharps = (Switch) findViewById(R.id.show_sharps_switch);
        buttonClearNotes = (Button) findViewById(R.id.clear_notes_button);

        fretboard = (FretboardFragment) getFragmentManager().findFragmentById(R.id.fretboard_fragment);
        //TODO: save showNotes && capo through bundle
        //TODO: save startFret, endFret, tuning, showOctaves, showAll, showSharps in settings
        fretboard.showNote(Note.noteToID('c', 0, 5));
        fretboard.showNote(Note.noteToID('e', 0, 5));
        fretboard.showNote(Note.noteToID('g', 0, 5));
        try {

            switchAllNotes.setChecked(showAllNotes);
            fretboard.setShowAllNotes(showAllNotes);

            switchOctaves.setChecked(showOctaves);
            fretboard.setShowOctaves(showOctaves);

            switchSharps.setChecked(showSharps);
            fretboard.setSharps(showSharps);

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
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
