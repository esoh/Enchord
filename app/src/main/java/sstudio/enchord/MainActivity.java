package sstudio.enchord;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

import sstudio.enchord.fragments.FretboardFragment;

/**
 * Created by seanoh on 6/30/16.
 */
public class MainActivity extends AppCompatActivity {
    private Switch switchAllNotes, switchOctaves, switchSharps;
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

        fretboard = (FretboardFragment) getFragmentManager().findFragmentById(R.id.fretboard_fragment);

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
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
