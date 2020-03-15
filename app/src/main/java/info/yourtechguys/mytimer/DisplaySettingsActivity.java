package info.yourtechguys.mytimer;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class DisplaySettingsActivity extends AppCompatActivity {

    //Spinner spnTotalTime;
    //Spinner spnWarning;
    Spinner spnSounds;
    NumberPicker npTotalSeconds;
    NumberPicker npTotalMinutes;
    NumberPicker npTotalHours;
    NumberPicker npWarningSeconds;
    NumberPicker npWarningMinutes;
    NumberPicker npWarningHours;

    public static int minSeconds = 60;
    public static int hourSeconds = minSeconds * 60;
    public static int daySeconds = hourSeconds * 24;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_settings);


        spnSounds = (Spinner) this.findViewById(R.id.spnSounds);

        npTotalSeconds = (NumberPicker) this.findViewById(R.id.npTotalSecond) ;
        npTotalSeconds.setMinValue(0);
        npTotalSeconds.setMaxValue(59);
        npTotalSeconds.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });

        npTotalMinutes = (NumberPicker) this.findViewById(R.id.npTotalMinute);
        npTotalMinutes.setMinValue(0);
        npTotalMinutes.setMaxValue(59);
        npTotalMinutes.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });

        npTotalHours = (NumberPicker) this.findViewById(R.id.npTotalHour);
        npTotalHours.setMinValue(0);
        npTotalHours.setMaxValue(24);
        npTotalHours.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });

        npWarningSeconds = (NumberPicker) this.findViewById(R.id.npWarningSecond) ;
        npWarningSeconds.setMinValue(0);
        npWarningSeconds.setMaxValue(59);
        npWarningSeconds.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });

        npWarningMinutes = (NumberPicker) this.findViewById(R.id.npWarningMinute);
        npWarningMinutes.setMinValue(0);
        npWarningMinutes.setMaxValue(59);
        npWarningMinutes.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });

        npWarningHours = (NumberPicker) this.findViewById(R.id.npWarningHour);
        npWarningHours.setMinValue(0);
        npWarningHours.setMaxValue(24);
        npWarningHours.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });

        //spnTotalTime = (Spinner) this.findViewById(R.id.spnTotalTime);

        //spnWarning = (Spinner) this.findViewById(R.id.spnWarning);

        //get incoming settings
        Bundle data = getIntent().getExtras();
        int newSeconds = data.getInt("timerSeconds", 180);
        int newWarningSeconds = data.getInt("WarningSeconds", 30);
        Long soundSetting = data.getLong("soundSetting",1);
        setTotalTime(newSeconds);
        setWarningTime(newWarningSeconds);
        spnSounds.setSelection(soundSetting.intValue());
        //Toast.makeText(this.getApplicationContext(), newSeconds + " seconds, warning at " + newWarningSeconds, Toast.LENGTH_LONG).show();


    }



    public void setTotalTimeEvent(View view){
        try{
            int totalSeconds = Integer.parseInt(view.getTag().toString());
            setTotalTime(totalSeconds);
        }catch (Exception ex){
            setTotalTime(0);
        }

    }

    public void setTotalTime(int totalSeconds){


        if(totalSeconds > daySeconds){
            totalSeconds = daySeconds;
        }
        int seconds = (int) totalSeconds % 60 ;
        int minutes = (int) ((totalSeconds / 60) % 60);
        int hours   = (int) ((totalSeconds / (60*60)) % 24);
        npTotalHours.setValue(hours);
        npTotalMinutes.setValue(minutes);
        npTotalSeconds.setValue(seconds);


    }

    public void setWarningTimeEvent(View view){
        try{
            int WarningSeconds = Integer.parseInt(view.getTag().toString());
            setWarningTime(WarningSeconds);
        }catch (Exception ex){
            setWarningTime(0);
        }

    }

    public void setWarningTime(int WarningSeconds){


        if(WarningSeconds > daySeconds){
            WarningSeconds = daySeconds;
        }
        int seconds = (int) WarningSeconds % 60 ;
        int minutes = (int) ((WarningSeconds / 60) % 60);
        int hours   = (int) ((WarningSeconds / (60*60)) % 24);
        npWarningHours.setValue(hours);
        npWarningMinutes.setValue(minutes);
        npWarningSeconds.setValue(seconds);


    }

    public void setButtonEvent(View view) {
        String message = "from settings";
        Intent intent  = new Intent();
        intent.putExtra("mystuff","I chose: ");
        intent.putExtra("success",true);


        //determine number of seconds
        int timerSeconds = npTotalSeconds.getValue() + (npTotalMinutes.getValue() * 60 ) + (npTotalHours.getValue() * (60 * 60));;//number of seconds

        //determine warning time
        int timerWarning = npWarningSeconds.getValue() + (npWarningMinutes.getValue() * 60 ) + (npWarningHours.getValue() * (60 * 60));; //in seconds

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
