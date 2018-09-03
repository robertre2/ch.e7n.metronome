package ch.e7n.metronome;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import ch.e7n.metronome.parser.DefaultSequence;
import ch.e7n.metronome.parser.MusicObject;
import ch.e7n.metronome.parser.Parser;
import ch.e7n.metronome.parser.ParserException;

public class MetronomeActivity extends Activity {

    private final int minBpm = 10;
    private final int maxBpm = 240;

    private int bpm = 90;
    private MetronomeAsyncTask metroTask;

    private Button plusButton;
    private Button minusButton;
    private EditText bpsText;
    private EditText rhythmText;
    private Spinner beatSpinner;
    private boolean dontChangeSpinner = false;
    private int lastSpinnerValue = -1;
    private boolean isRunning = false;

    private Handler mHandler;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        CustomKeyboard mCustomKeyboard= new CustomKeyboard(this, R.id.keyboardview, R.xml.notekbd, R.xml.advancedkbd );

        mCustomKeyboard.registerEditText(R.id.rhythmEntry);
        rhythmText = (EditText) findViewById(R.id.rhythmEntry);


        metroTask = new MetronomeAsyncTask();

        plusButton = (Button) findViewById(R.id.plus);
        plusButton.setOnLongClickListener(plusListener);

        minusButton = (Button) findViewById(R.id.minus);
        minusButton.setOnLongClickListener(minusListener);

        bpsText = (EditText) findViewById(R.id.bpsText);
        bpsText.setOnFocusChangeListener(bpsFocusChangeListener);

        beatSpinner = (Spinner) findViewById(R.id.bpsSpinner);
        ArrayAdapter<TempoMarking> arrayBeats =
                new ArrayAdapter<TempoMarking>(this,
                        android.R.layout.simple_spinner_item, TempoMarking.values());
        beatSpinner.setAdapter(arrayBeats);
        beatSpinner.setSelection(TempoMarking.andante.ordinal());
        arrayBeats.setDropDownViewResource(R.layout.spinner_dropdown);
        beatSpinner.setOnItemSelectedListener(bpsSpinnerListener);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public synchronized void onStartStopClick(View view) {
        Button button = (Button) view;
        if(!isRunning) {
            if(!alterRhythm(rhythmText.getText())) {
                return;
            }
            try {
                bpm = Integer.parseInt(bpsText.getText().toString());
            } catch(NumberFormatException e) {
                // Ignore: bpm stay the same
            }
            alterBpm(bpm);
            button.setText(R.string.stop);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                metroTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
            else
                metroTask.execute();
        } else {
            button.setText(R.string.start);
            metroTask.stop();
            metroTask = new MetronomeAsyncTask();
            Runtime.getRuntime().gc();
        }
        isRunning = !isRunning;
    }

    public void onMainSaveClick(View view) {
        Intent intent = new Intent(this, SaveActivity.class);
        intent.putExtra(SaveActivity.LABEL_RHYTHM, convertRhythm(rhythmText.getText()));
        intent.putExtra(SaveActivity.LABEL_BPM, bpm);
        startActivityForResult(intent, 42);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            String loaded = data.getStringExtra(SaveActivity.LABEL_RHYTHM);
            Parser p = new Parser(loaded);
            rhythmText.setText(p.parseToString(rhythmText.getResources()));
            alterRhythm(rhythmText.getText());
            int newBpm = data.getIntExtra(SaveActivity.LABEL_BPM, bpm);
            alterBpm(newBpm);
        }
    }



    public void onPlusClick(View view) {
        alterBpm(bpm+1);
    }

    private View.OnLongClickListener plusListener = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
        alterBpm(bpm + 20);
        return true;
        }

    };


    public void onMinusClick(View view) {
        alterBpm(bpm - 1);
    }

    private void alterBpm(int newBpm) {
        bpm = newBpm;
        if (bpm < minBpm) {
            bpm = minBpm;
        } else if(bpm > maxBpm) {
            bpm = maxBpm;
        }
        metroTask.setBpm(bpm);
        bpsText.setText(bpm+"");
        if(!dontChangeSpinner) {
            for(TempoMarking tm: TempoMarking.values()) {
                if(tm.getMinBeat() <= bpm && bpm <= tm.getMaxBeat()) {
                    lastSpinnerValue = tm.ordinal(); // Skip the listener
                    beatSpinner.setSelection(lastSpinnerValue);
                    break;
                }
            }
        }
    }

    boolean alterRhythm(Editable newRhythm) {
        String convertedRhythm = convertRhythm(newRhythm);
        if(convertedRhythm.trim().equals("")) {
            metroTask.setRhythm(new DefaultSequence());
        } else {
            try {
                metroTask.setRhythm(new Parser(convertedRhythm).parse());
            } catch (ParserException e) {
                new AlertDialog.Builder(this)
                        .setTitle("Could not parse rhythm.")
                        .setMessage(e.getMessage())
                        /*.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })*/
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return false;
            }
        }
        return true;
    }

    private String convertRhythm(Editable newRhythm) {
        StringBuilder converted = new StringBuilder();
        for(int i=0;i<newRhythm.length();++i) {
            SmileySpan[] spans = newRhythm.getSpans(i, i+1, SmileySpan.class);
            if(spans.length == 0) {
                converted.append(newRhythm.charAt(i));
            } else {
                for(SmileySpan ss: spans) {
                    converted.append(ss.getSmiley().getText());
                }
            }
        }

        return converted.toString();
    }

    private View.OnLongClickListener minusListener = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
            alterBpm(bpm-20);
            return true;
        }

    };


    private AdapterView.OnItemSelectedListener bpsSpinnerListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            if(arg2 != lastSpinnerValue) {
                TempoMarking beat = (TempoMarking) arg0.getItemAtPosition(arg2);
                dontChangeSpinner = true;
                alterBpm(beat.getAverageBeat());
                dontChangeSpinner = false;
            }
            lastSpinnerValue = arg2;
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }

    };

    private AdapterView.OnFocusChangeListener bpsFocusChangeListener = new AdapterView.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus) {
                try {
                     bpm = Integer.parseInt(bpsText.getText().toString());
                } catch(NumberFormatException e) {
                    // Ignore: bpm stay the same
                }
                alterBpm(bpm);
            }
        }
    };

    @Override
    public boolean onKeyUp(int keycode, KeyEvent e) {
        return super.onKeyUp(keycode, e);
    }

    public void onBackPressed() {
        metroTask.stop();
//    	metroTask = new MetronomeAsyncTask();
        Runtime.getRuntime().gc();
        finish();
    }

    private class MetronomeAsyncTask extends AsyncTask<Void,Void,String> {
        Metronome metronome;

        MetronomeAsyncTask() {
            metronome = new Metronome(MetronomeActivity.this);
        }

        protected String doInBackground(Void... params) {
            metronome.setBpm(bpm);

            metronome.play();

            return null;
        }

        public void stop() {
            metronome.stop();
            metronome = null;
        }

        public void setBpm(int bpm) {
            metronome.setBpm(bpm);
            metronome.calcSilence();
        }

        public void setRhythm(MusicObject rhythm) {
            metronome.setSequenceToPlay(rhythm);
        }

    }

}