package ch.e7n.metronome;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import ch.e7n.metronome.parser.Parser;

/**
 * Created by robertre on 9/9/14.
 */
public class SaveActivity extends Activity {

    private static final String SAVED_RHYTHMS = "savedRhythms";
    private static final String SAVED_BPMS = "savedBpms";
    public static final String LABEL_RHYTHM = "rhythm";
    public static final String LABEL_BPM = "bpm";
    private static final String NO_SELECTION = "---";
    private static final String CLIPBOARD = "(Clipboard)";
    private String toSave;
    private int bpmToSave;
    private EditText filename;
    private TextView previewText;
    private TextView toSaveText;
    private Spinner filenameSpinner;
    private int lastSpinnerValue;
    private SharedPreferences prefs;
    private SharedPreferences prefsBpm;
    private List<String> listOfFilenames;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_view);
        Intent data = getIntent();
        toSave = data.getStringExtra(LABEL_RHYTHM);
        bpmToSave = data.getIntExtra(LABEL_BPM, 90);

        filename = (EditText) findViewById(R.id.saveFilenameEntry);
        previewText = (TextView) findViewById(R.id.previewText);
        toSaveText = (TextView) findViewById(R.id.toSaveText);

        Parser p = new Parser(toSave);
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        // ssb.append("To save: ");
        ssb.append(p.parseToString(toSaveText.getResources()));
        toSaveText.setText(ssb);

        filenameSpinner = (Spinner) findViewById(R.id.saveFilenameSpinner);
        refreshPreferences();
    }

    public void refreshPreferences() {
        prefs = getSharedPreferences(SAVED_RHYTHMS, 0);
        prefsBpm = getSharedPreferences(SAVED_BPMS, 0);
        listOfFilenames = new ArrayList<String>(new TreeSet<String>(prefs.getAll().keySet()));
        listOfFilenames.add(CLIPBOARD);
        listOfFilenames.add(NO_SELECTION);

        ArrayAdapter<String> arrayBeats =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, listOfFilenames);
        filenameSpinner.setAdapter(arrayBeats);
        filenameSpinner.setSelection(listOfFilenames.size()-1);
        arrayBeats.setDropDownViewResource(R.layout.spinner_dropdown);
        filenameSpinner.setOnItemSelectedListener(filenameSpinnerListener);
        lastSpinnerValue = listOfFilenames.size()-1;
    }

    public void onSaveClick(View view) {
        String fn = filename.getText().toString();
        if(!fn.equals(NO_SELECTION) && !fn.equals(CLIPBOARD)) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString(fn, toSave);
            edit.commit();
            edit = prefsBpm.edit();
            edit.putInt(fn, bpmToSave);
            edit.commit();
            setResult(RESULT_CANCELED);
            finish();
        } else if(fn.equals(CLIPBOARD)) {
            ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("rhythm", toSave);
            clipboard.setPrimaryClip(clip);
            finish();
        }
    }

    public void onCancelClick(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void onLoadClick(View view) {
        String fn = filename.getText().toString();
        if(!fn.equals(NO_SELECTION) && !fn.equals(CLIPBOARD)) {
            String loaded = prefs.getString(fn, "");
            int loadedBpm = prefsBpm.getInt(fn, 90);
            Intent data = new Intent();
            data.putExtra(LABEL_RHYTHM, loaded);
            data.putExtra(LABEL_BPM, loadedBpm);
            setResult(RESULT_OK, data);
            finish();
        } else if(fn.equals(CLIPBOARD)) {
            ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData cdata = clipboard.getPrimaryClip();
            if(cdata != null && cdata.getItemCount() > 0) {
                String s = cdata.getItemAt(0).coerceToText(this).toString();
                Intent data = new Intent();
                data.putExtra(LABEL_RHYTHM, s);
                data.putExtra(LABEL_BPM, bpmToSave);
                setResult(RESULT_OK, data);
                finish();
            }
        }
    }

    public void onDeleteClick(View view) {
        String fn = filename.getText().toString();
        if(!fn.equals(NO_SELECTION) && !fn.equals(CLIPBOARD)) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.remove(fn);
            edit.commit();

            refreshPreferences();
        }
    }

    private AdapterView.OnItemSelectedListener filenameSpinnerListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            if(arg2 != lastSpinnerValue) {
                String fn = (String)arg0.getItemAtPosition(arg2);
                if(! fn.equals(NO_SELECTION) && !fn.equals(CLIPBOARD)) {
                    filename.setText(fn);
                    String loaded = prefs.getString(filename.getText().toString(), "");
                    Parser p = new Parser(loaded);
                    SpannableStringBuilder ssb = new SpannableStringBuilder();
                    // ssb.append("Loaded: ");
                    ssb.append(p.parseToString(previewText.getResources()));
                    previewText.setText(ssb);
                } else if(fn.equals(CLIPBOARD)) {
                    filename.setText(fn);
                    ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData cdata = clipboard.getPrimaryClip();
                    if(cdata != null && cdata.getItemCount() > 0) {
                        String s = cdata.getItemAt(0).coerceToText(SaveActivity.this).toString();
                        if(s.length() > 1000) {
                            s = s.substring(0, 999);
                        }
                        Parser p = new Parser(s);
                        SpannableStringBuilder ssb = new SpannableStringBuilder();
                        // ssb.append("Loaded: ");
                        ssb.append(p.parseToString(previewText.getResources()));
                        previewText.setText(ssb);
                    }
                }
            }
            lastSpinnerValue = arg2;
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }

    };
}
