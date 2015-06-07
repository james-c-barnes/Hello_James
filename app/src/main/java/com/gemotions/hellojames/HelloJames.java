package com.gemotions.hellojames;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;

import org.w3c.dom.Text;

public class HelloJames extends Activity {

    private SeekBar seekBar;
    private TextView barText;
    private CompoundButton compoundButton;
    private TextView buttonText;
    private RadioGroup radioGroupLeft;
    private RadioGroup radioGroupMiddle;
    private RadioGroup radioGroupRight;
    private Button saveButton;
    private TextView optionalTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_james);

        // connect to the xml resources
        seekBar = (SeekBar) findViewById(R.id.intensityLevelSeekBar);
        barText = (TextView) findViewById(R.id.intensityLevelText);
        compoundButton = (CompoundButton) findViewById(R.id.emotionPolaritySwitch);
        buttonText = (TextView) findViewById(R.id.emotionPolarityValueText);
        radioGroupLeft = (RadioGroup) findViewById(R.id.radioGroupLeft);
        radioGroupMiddle = (RadioGroup) findViewById(R.id.radioGroupMiddle);
        radioGroupRight = (RadioGroup) findViewById(R.id.radioGroupRight);
        saveButton = (Button) findViewById(R.id.saveButton);
        optionalTextView = (TextView) findViewById(R.id.optionalTextView);

        // initialize seekbar text
        updateSeekBarText();

        // initialize radio buttons to off
        // radioGroupLeft.clearCheck();
        // radioGroupMiddle.clearCheck();
        // radioGroupRight.clearCheck();

        // initialize emotion names to negative set
        setEmotionNames(radioGroupLeft, radioGroupMiddle, radioGroupRight, false);

        // emotion intensity listener
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                updateSeekBarText();
                // textView.setText(Integer.toString(seekBar.getProgress() + 1));
                // Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                updateSeekBarText();
                // textView.setText(Integer.toString(seekBar.getProgress() + 1));
                // Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // textView.setText(Integer.toString(seekBar.getProgress() + 1));
                updateSeekBarText();
                // Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
            }
        });

        // emotion polarity listener
        compoundButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    buttonText.setText("positive");
                } else {
                    // The toggle is disabled
                    buttonText.setText("negative");
                }
                // change the polarity of the emotion names
                setEmotionNames(radioGroupLeft, radioGroupMiddle, radioGroupRight, isChecked);
            }
        });

        // radio group left listener
        radioGroupLeft.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1) {
                    // radioGroupLeft.setOnCheckedChangeListener(null); // remove the listener before clearing so we don't throw that stackoverflow exception(like Vladimir Volodin pointed out)
                    RadioButton checkedRadioButton = (RadioButton)radioGroupLeft.findViewById(radioGroupLeft.getCheckedRadioButtonId());
                    boolean isChecked = checkedRadioButton.isChecked();
                    Log.d("XXXLeft", "Radio Group Left:" + checkedId + "," + isChecked);
                    if (isChecked) {
                        radioGroupMiddle.clearCheck();
                        radioGroupRight.clearCheck();
                        Log.d("XXXLeft", "do the work");
                        // potentially enable the save button
                        potentiallyEnableSaveButton();
                    }
                }
            }
        });
        // radio group middle listener
        radioGroupMiddle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1) {
                    RadioButton checkedRadioButton = (RadioButton)radioGroupMiddle.findViewById(radioGroupMiddle.getCheckedRadioButtonId());
                    boolean isChecked = checkedRadioButton.isChecked();
                    Log.d("XXXMiddle", "Radio Group Middle:" + checkedId + "," + isChecked);
                    if (isChecked) {
                        radioGroupLeft.clearCheck();
                        radioGroupRight.clearCheck();
                        Log.d("XXXMiddle", "do the work");
                        // potentially enable the save button
                        potentiallyEnableSaveButton();
                    }
                }
            }
        });
        // radio group right listener
        radioGroupRight.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1) {
                    RadioButton checkedRadioButton = (RadioButton)radioGroupRight.findViewById(radioGroupRight.getCheckedRadioButtonId());
                    boolean isChecked = checkedRadioButton.isChecked();
                    Log.d("XXXRight", "Radio Group Right:" + checkedId + "," + isChecked);
                    if (isChecked) {
                        radioGroupLeft.clearCheck();
                        radioGroupMiddle.clearCheck();
                        Log.d("XXXRight", "do the work");
                        // potentially enable the save button
                        potentiallyEnableSaveButton();
                    }
                }
            }
        });

        // save button listener
        saveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                boolean polarity;
                int intensity;
                int rb_id;
                int rbl_id;
                int rbm_id;
                int rbr_id;
                CharSequence optional_text;

                Log.d("XXXSaveButton", "clicked");
                // get the emotion polarity
                polarity = compoundButton.isChecked();
                // get the emotion intensity
                intensity = seekBar.getProgress();
                // get the attributes to send in the API call here
                rbl_id = radioGroupLeft.getCheckedRadioButtonId();
                rbm_id = radioGroupMiddle.getCheckedRadioButtonId();
                rbr_id = radioGroupRight.getCheckedRadioButtonId();
                if (rbl_id != -1) {
                    rb_id = rbl_id;
                }
                else if (rbm_id != -1) {
                    rb_id = rbm_id;
                }
                else {
                    rb_id = rbr_id;
                }
                // get the optional text
                optional_text = optionalTextView.getText();
                Log.d("XXXSaveButton", "API call -- Polarity: " + polarity);
                Log.d("XXXSaveButton", "API call -- Radio Button Id: " + rb_id);
                Log.d("XXXSaveButton", "API call -- Intensity: " + intensity);
                Log.d("XXXSaveButton", "API call -- Optional Text: " + optional_text);
            }
        });
    }

    private void updateSeekBarText() {
        int progress;
        progress = seekBar.getProgress();
        barText.setText(Integer.toString(progress));
        potentiallyEnableSaveButton();
    }

    private void potentiallyEnableSaveButton() {
        int intensity;
        boolean enable;
        int rbl_id;
        int rbm_id;
        int rbr_id;

        intensity = seekBar.getProgress();
        // assume failure
        enable = false;
        // user must enter an intensity to save
        if (intensity != 0) {
            // user must press a radio button to save
            rbl_id = radioGroupLeft.getCheckedRadioButtonId();
            rbm_id = radioGroupMiddle.getCheckedRadioButtonId();
            rbr_id = radioGroupRight.getCheckedRadioButtonId();
            if ((rbl_id != -1) || (rbm_id != -1) || (rbr_id != -1)) {
                enable = true;
            }
        }
        // enable save button
        saveButton.setEnabled(enable);
    }

    private void setEmotionNames(RadioGroup left, RadioGroup middle, RadioGroup right, boolean positive) {
        String[] negativeEmotionsLeft;
        String[] negativeEmotionsMiddle;
        String[] negativeEmotionsRight;

        String[] positiveEmotionsLeft;
        String[] positiveEmotionsMiddle;
        String[] positiveEmotionsRight;

        String[] currentEmotionsLeft;
        String[] currentEmotionsMiddle;
        String[] currentEmotionsRight;

        negativeEmotionsLeft = new String[]{"terrified", "scared", "creepy", "nervous", "overwhelm", "stupid", "dizzy", "confused"};
        negativeEmotionsMiddle = new String[]{"dread", "wrong/failure", "bad", "worried", "hatred", "crazed", "denial", "stressed"};
        negativeEmotionsRight = new String[]{"ashamed/judged", "revenge", "guilty", "disbelief", "unloved", "loss/grief", "rejected", "sad/depressed"};
        positiveEmotionsLeft = new String[]{"wonder", "awed", "thrilled", "excited", "safe", "happy", "clear", "able"};
        positiveEmotionsMiddle = new String[]{"certainty", "right/success", "good", "confident", "relief", "peace", "acceptance", "calm"};
        positiveEmotionsRight = new String[]{"pride/know", "wisdom", "grateful", "truth", "joy", "love", "treasured", "freedom/bliss"};

        if (positive) {
            currentEmotionsLeft = positiveEmotionsLeft;
            currentEmotionsMiddle= positiveEmotionsMiddle;
            currentEmotionsRight = positiveEmotionsRight;
        }
        else {
            currentEmotionsLeft = negativeEmotionsLeft;
            currentEmotionsMiddle = negativeEmotionsMiddle;
            currentEmotionsRight = negativeEmotionsRight;
        }
        for (int i = 0; i < currentEmotionsLeft.length; i++) {
            ((RadioButton) left.getChildAt(i)).setText(currentEmotionsLeft[i]);
            ((RadioButton) middle.getChildAt(i)).setText(currentEmotionsMiddle[i]);
            ((RadioButton) right.getChildAt(i)).setText(currentEmotionsRight[i]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hello_james, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
