package sstudio.gnote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    //for now, numFrets is set.
    private final int numFrets = 19;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FretboardView myFretboard = (FretboardView) findViewById(R.id.fretboard);
        if(myFretboard != null) {
            myFretboard.setNumFrets(numFrets);
        }
    }
}
