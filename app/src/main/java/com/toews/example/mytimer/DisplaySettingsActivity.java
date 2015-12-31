package com.toews.example.mytimer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DisplaySettingsActivity extends AppCompatActivity {

    RadioGroup btnGroup;
    RadioButton btn2Min;
    RadioButton btn3Min;
    RadioButton btn4Min;
    RadioButton btn5Min;
    RadioButton btnCustom;

    TextView lblTotal;
    TextView lblWarning;
    TextView lblTotalMin;
    TextView lblTotalSec;
    TextView lblWarningMin;
    TextView lblWarningSec;
    TextView txtTotalMin;
    TextView txtTotalSec;
    TextView txtWarningMin;
    TextView txtWarningSec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_display_settings);
        btnGroup = (RadioGroup) this.findViewById(R.id.btnGroup);
        btn2Min = (RadioButton) this.findViewById(R.id.btn2Min);
        btn3Min = (RadioButton) this.findViewById(R.id.btn3Min);
        btn4Min = (RadioButton) this.findViewById(R.id.btn4Min);
        btn5Min = (RadioButton) this.findViewById(R.id.btn5Min);
        btnCustom = (RadioButton) this.findViewById(R.id.btnCustom);

        lblTotal = (TextView) this.findViewById(R.id.lblTotal);
        lblWarning = (TextView) this.findViewById(R.id.lblWarning);
        lblTotalMin = (TextView) this.findViewById(R.id.lblTotalMin);
        lblTotalSec = (TextView) this.findViewById(R.id.lblTotalSec);
        lblWarningMin = (TextView) this.findViewById(R.id.lblWarningMin);
        lblWarningSec = (TextView) this.findViewById(R.id.lblWarningSec);
        txtTotalMin = (TextView) this.findViewById(R.id.txtTotalMin);
        txtTotalSec = (TextView) this.findViewById(R.id.txtTotalSec);
        txtWarningMin = (TextView) this.findViewById(R.id.txtWarningMin);
        txtWarningSec = (TextView) this.findViewById(R.id.txtWarningSec);

        //get incoming settings
        Bundle data = getIntent().getExtras();
        int newSeconds = data.getInt("timerSeconds", -1);
        int newWarningSeconds = data.getInt("WarningSeconds", -1);
        //Toast.makeText(this.getApplicationContext(), newSeconds + " seconds, warning at " + newWarningSeconds, Toast.LENGTH_LONG).show();
        setCustomVisibility(View.INVISIBLE);

        //let's see which radio button we should select
        if(newSeconds == 120 && newWarningSeconds == 30){
            btn2Min.setChecked(true);
        }else if(newSeconds == 180 && newWarningSeconds == 30){
            btn3Min.setChecked(true);
        }else if(newSeconds == 240 && newWarningSeconds == 30){
            btn4Min.setChecked(true);
        }else if(newSeconds == 300 && newWarningSeconds == 30){
            btn5Min.setChecked(true);
        }else{
            btnCustom.setChecked(true);
            //now let's populate the custom fields
            setCustomVisibility(View.VISIBLE);
            int newMinutes =  (int) Math.floor(newSeconds / 60);
            int leftoverSecs = newSeconds % 60;
            int newWarningMins = (int) Math.floor(newWarningSeconds/60);
            int warningLeftoverSecs = newWarningSeconds % 60;

            txtTotalMin.setText((String.valueOf( newMinutes)));
            txtTotalSec.setText(String.valueOf( leftoverSecs));
            txtWarningMin.setText(String.valueOf( newWarningMins));
            txtWarningSec.setText(String.valueOf( warningLeftoverSecs));
        }




    }


    public void setCustomVisibility(int isVisible){
        lblTotal.setVisibility(isVisible);
        lblWarning.setVisibility(isVisible);
        lblTotalMin.setVisibility(isVisible);
        lblTotalSec.setVisibility(isVisible);
        lblWarningMin.setVisibility(isVisible);
        lblWarningSec.setVisibility(isVisible);
        txtTotalMin.setVisibility(isVisible);
        txtTotalSec.setVisibility(isVisible);
        txtWarningMin.setVisibility(isVisible);
        txtWarningSec.setVisibility(isVisible);
    }

    public void buttonChangeEvent(View view) {

        if(btnCustom.isChecked()){
            setCustomVisibility(View.VISIBLE);
        }else{
            setCustomVisibility(View.INVISIBLE);
        }
    }

    public void setButtonEvent(View view) {
        String message = "from settings";
        Intent intent  = new Intent();
        intent.putExtra("mystuff","I chose: ");
        intent.putExtra("success",true);

        int timerSeconds;
        int timerWarning;
        //determine time
        int radioButtonID = btnGroup.getCheckedRadioButtonId();
        View radioButton = btnGroup.findViewById(radioButtonID);
        int idx = btnGroup.indexOfChild(radioButton);
        switch (idx){
            case 0: timerSeconds = 120;
                    timerWarning = 30;
                    break;
            case 1: timerSeconds = 180;
                    timerWarning = 30;
                    break;
            case 2: timerSeconds = 240;
                    timerWarning = 30;
                    break;
            case 3: timerSeconds = 300;
                    timerWarning = 30;
                    break;
            case 4: timerSeconds = getTimerSeconds();
                    timerWarning = getWarningSeconds();
                    break;
            default: timerSeconds = -1;
                    timerWarning = -1;
                    break;

        }//end of swtich/case


        intent.putExtra("timerSeconds",timerSeconds);
        intent.putExtra("WarningSeconds",timerWarning);
        intent.putExtra("index",idx);
        setResult(2, intent);
        finish();

    }

    public int getTimerSeconds(){
        return readTextViews(txtTotalMin,txtTotalSec);
    }

    public int getWarningSeconds(){
        return readTextViews(txtWarningMin,txtWarningSec);
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
