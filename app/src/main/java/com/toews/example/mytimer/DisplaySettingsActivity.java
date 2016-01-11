package com.toews.example.mytimer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DisplaySettingsActivity extends AppCompatActivity {

    Spinner spnTotalTime;
    Spinner spnWarning;
    Spinner spnSounds;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_display_settings);


        spnSounds = (Spinner) this.findViewById(R.id.spnSounds);
        spnTotalTime = (Spinner) this.findViewById(R.id.spnTotalTime);
        spnWarning = (Spinner) this.findViewById(R.id.spnWarning);

        //get incoming settings
        Bundle data = getIntent().getExtras();
        int newSeconds = data.getInt("timerSeconds", 180);
        int newWarningSeconds = data.getInt("WarningSeconds", 30);
        Long soundSetting = data.getLong("soundSetting",1);
        spnSounds.setSelection(soundSetting.intValue());
        //Toast.makeText(this.getApplicationContext(), newSeconds + " seconds, warning at " + newWarningSeconds, Toast.LENGTH_LONG).show();

        //populate time spinners
        ArrayList<TimeSetting> timelist = createTimeStrings(20);
        ArrayAdapter<TimeSetting> adapter = new ArrayAdapter<TimeSetting>(
                this, android.R.layout.simple_spinner_item, timelist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnTotalTime.setAdapter(adapter);
        spnWarning.setAdapter(adapter);

        //now choose the correct item in spinners
        int timeSelection = Math.abs(newSeconds -30) / 30;
        spnTotalTime.setSelection(timeSelection);

        int warningSelection = Math.abs(newWarningSeconds -30) / 30;
        spnWarning.setSelection(warningSelection);





    }

    public static ArrayList<TimeSetting> createTimeStrings(int Minutes){

        ArrayList<TimeSetting> result = new ArrayList<TimeSetting>();

        for(Integer i = 1;i<=Minutes;i++){
            result.add(new TimeSetting((i*60) -30));
            result.add(new TimeSetting((i*60) ));

        }

        return  result;

    }//end of CreateTimeStrings

//    public void setCustomVisibility(int isVisible){
//        lblTotal.setVisibility(isVisible);
//        lblWarning.setVisibility(isVisible);
//        lblTotalMin.setVisibility(isVisible);
//        lblTotalSec.setVisibility(isVisible);
//        lblWarningMin.setVisibility(isVisible);
//        lblWarningSec.setVisibility(isVisible);
//        txtTotalMin.setVisibility(isVisible);
//        txtTotalSec.setVisibility(isVisible);
//        txtWarningMin.setVisibility(isVisible);
//        txtWarningSec.setVisibility(isVisible);
//    }

//    public void buttonChangeEvent(View view) {
//
//        if(btnCustom.isChecked()){
//            setCustomVisibility(View.VISIBLE);
//        }else{
//            setCustomVisibility(View.INVISIBLE);
//        }
//    }

    public void setButtonEvent(View view) {
        String message = "from settings";
        Intent intent  = new Intent();
        intent.putExtra("mystuff","I chose: ");
        intent.putExtra("success",true);

        //determine total time
        TimeSetting totalSetting =  (TimeSetting) spnTotalTime.getSelectedItem();
        int timerSeconds = totalSetting.getSeconds();

        //determine warning time
        TimeSetting warningSetting = (TimeSetting) spnWarning.getSelectedItem();
        int timerWarning = warningSetting.getSeconds();

        //send the data back
        intent.putExtra("timerSeconds",timerSeconds);
        intent.putExtra("WarningSeconds",timerWarning);
        intent.putExtra("sounds",spnSounds.getSelectedItemId());

        setResult(2, intent);
        finish();

    }



    private int readTextViews(TextView minuteText, TextView secondText){
        try{
            int minutes = StringToInt(minuteText.getText().toString());
            int seconds = StringToInt(secondText.getText().toString());


            int totalSeconds = (minutes * 60) + seconds;
            return totalSeconds;
        }catch(Exception ex){
            return -1;
        }//end of try/catch
    }//end of readTextViews

    private int  StringToInt(String input){
        try{
            if(input == null || input.equals("")){
                return 0;
            }
            return Integer.parseInt(input);
        }catch(Exception ex){
            return 0;
        }
    }//end of StringToInt

    @Override
    public void onBackPressed() {
        // super.onBackPressed();

        Intent intent = new Intent();
        //intent.putIntegerArrayListExtra(SELECTION_LIST, selected);
        setResult(RESULT_CANCELED, intent);
        finish();
    }


}//end of class
