package com.toews.example.mytimer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.Activity;
import android.os.CountDownTimer;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;


public class TimerActivity extends Activity implements View.OnClickListener {
    private RelativeLayout myLayout;
    private myCountdownTimer countDownTimer;
    private boolean timerHasStarted = false;
    private Button btnStartStop;
    public TextView text;
    public TextView startingTextView;
    public TextView txtFeedback;
    public ImageButton btnMute;
    private long startTime ;
    private long warningTime ;
    private final long interval = 1 * 1000;
    private long msLeft;
    private boolean hasWarned;
    private boolean overTime;
    //private boolean muted;

    protected Vibrator vibrate;
    MediaPlayer mMediaPlayer;
    Uri mWarningSound;
    Uri mTimeupSound;
    boolean vibrateOn;
    long soundSetting;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //stop screen saver
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        //let's get our settings
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        startTime = sharedPref.getLong(getString(R.string.timer_length_ms), 180 * 1000);
        warningTime = sharedPref.getLong(getString(R.string.warning_length_ms), 30 * 1000);
        soundSetting = sharedPref.getLong("soundSetting", 1);
        setSoundUris();
        //muted = sharedPref.getBoolean(getString(R.string.muted_key_name), true);


        setContentView(R.layout.activity_timer);
        myLayout = (RelativeLayout) this.findViewById(R.id.timerLayout);
        btnStartStop = (Button) this.findViewById(R.id.btnStartStop);
        btnStartStop.setOnClickListener(this);
        btnMute = (ImageButton) findViewById(R.id.btnMute);
        btnMute.setVisibility(View.INVISIBLE);
//        setBtnMuteAccurate();
        text = (TextView) findViewById(R.id.txtTimer);
        txtFeedback = (TextView) findViewById(R.id.txtFeedback);
        startingTextView = (TextView) findViewById(R.id.startingTextView);
        countDownTimer = new myCountdownTimer(startTime, interval, true);
        setStartingTextView();
        msLeft = startTime;
        text.setText(formatMillis(startTime));
        mMediaPlayer = new MediaPlayer();
//        mWarningSound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.doorchime);
//        mTimeupSound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tornado);
        vibrate = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                btnMute.setVisibility(View.INVISIBLE);

            }
        });


        if (vibrate == null) {
            System.out.println("no vibrate service");
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void onClick(View v) {
        //new AlertDialog.Builder(this).setTitle("Argh").setMessage("inside onClick").setNeutralButton("Close", null).show();
        System.out.println("click");
        //System.out.println("ms left: " + countDownTimer.timeLeft());
        if (!timerHasStarted) {
            if(msLeft == startTime){
                msLeft += 1000;
            }
            countDownTimer.start(msLeft); // add one second so it will be below start in 1k ms


            timerHasStarted = true;
            btnStartStop.setText(R.string.stop);
        } else {
            //msLeft = countDownTimer.howManyMSleft();
            System.out.println("msleft: " + msLeft);
            countDownTimer.cancel();
            mMediaPlayer.stop();
            btnMute.setVisibility(View.INVISIBLE);
            timerHasStarted = false;
            btnStartStop.setText(R.string.resume);
        }
    }

    private void setSoundUris(){
        if(soundSetting == 0){
            //silent no vibrate
            vibrateOn = false;
            mTimeupSound = null;
            mWarningSound = null;
        }else if(soundSetting == 1){
            //vibrate only
            vibrateOn = true;
            mTimeupSound = null;
            mWarningSound = null;
        }else if(soundSetting == 2){
            //gentle sounds
            vibrateOn = true;
            mWarningSound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.doorchime);
            mTimeupSound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.chimes1);
        }else  if (soundSetting == 3){
            //annoying sounds
            vibrateOn = true;
            mWarningSound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.doorchime);
            mTimeupSound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tornado);
        }
    }

    private void playSound(Uri uri) {
        if (uri == null) return;
        try {

            btnMute.setVisibility(View.VISIBLE);
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(this, uri);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (Exception e) {
            Toast.makeText(this.getApplicationContext(), "MediaPlayer Exception: \n"+ e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    public static String formatMillis(long millisUntilFinished) {
        String timeString = "";
        //let's see if we need a minus sign
        if (millisUntilFinished < 0) {
            timeString += "-";
            millisUntilFinished = millisUntilFinished * -1;
        }

        int totalSecs = (int) millisUntilFinished / 1000;
        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;


        if (hours == 0) {
            //let's not show hours if there are none
            timeString += String.format("%2d:%02d", minutes, seconds);
        } else {
            timeString += String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }

        return timeString;

    }

    public void resetTimer(View view) {
        if (timerHasStarted) {
            //if it's running, stop it
            countDownTimer.cancel();
            timerHasStarted = false;

        }
        btnStartStop.setText(R.string.start);
        text.setText(formatMillis(startTime));
        msLeft = startTime;
        hasWarned = false;
        overTime = false;
        txtFeedback.setText("");
        myLayout.setBackgroundColor(Color.BLACK);
        mMediaPlayer.stop();
        btnMute.setVisibility(View.INVISIBLE);
    }

    /**
     * opens the settings activity
     *
     * @param view
     */
    public void openSettings(View view) {
        countDownTimer.cancel();
        mMediaPlayer.stop();
        btnMute.setVisibility(View.INVISIBLE);
        timerHasStarted = false;
        btnStartStop.setText(R.string.resume);
        Intent intent = new Intent(this, DisplaySettingsActivity.class);
        int timerSeconds = (int) (startTime / 1000);
        int warningSeconds = (int) (warningTime/1000);
        intent.putExtra("timerSeconds",timerSeconds);
        intent.putExtra("WarningSeconds", warningSeconds);
        intent.putExtra("soundSetting",soundSetting);
        //startActivity(intent);
        startActivityForResult(intent, 2);
    }

    /**
     * gets data back from other activities
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 2) {
            // Make sure the request was successful
            if (data.getBooleanExtra("success", false)) {
                int newSeconds = data.getIntExtra("timerSeconds", -1);
                int newWarning = data.getIntExtra("WarningSeconds", -1);
                soundSetting = data.getLongExtra("sounds", 0);
                setSoundUris();



                //if we have valid values, let's use new timer vlaues
                if (newSeconds > 0 && newWarning >= 0) {
                    startTime = newSeconds * 1000;
                    warningTime = newWarning * 1000;
                    resetTimer(null);
                    setStartingTextView();
                    //let's write these settings
                    SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putLong(getString(R.string.timer_length_ms),startTime);
                    editor.putLong(getString(R.string.warning_length_ms),warningTime);
                    editor.putLong("soundSetting",soundSetting);
                    editor.commit();


                }

            }
        }
    }

    public void setStartingTextView() {
        String startingTime = "";
        startingTime += "Starting Time:    Warning Time:\n" + formatMillis(startTime) + "     " + formatMillis(warningTime);
        startingTime += "\n";
        try{
            String[] values = getResources().getStringArray(R.array.sound_choices);
            startingTime += values[(int)soundSetting];
        }catch (Exception ex){
            startingTime += "unknown sounds";
        }
        startingTextView.setText(startingTime);

    }

    //    public void flashScreen(long msEachFlash, long numOfFlashes) {
//        for (int i = 0; i < numOfFlashes; i++) {
//            myLayout.setBackgroundColor(Color.WHITE);
//            try {
//                Thread.sleep(msEachFlash);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            myLayout.setBackgroundColor(Color.BLACK);
//            try {
//                Thread.sleep(msEachFlash);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }//end of flashScreen

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Timer Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.toews.example.mytimer/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Timer Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.toews.example.mytimer/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
//
    public void btnMuteClicked(View view) {
        mMediaPlayer.stop();
        btnMute.setVisibility(View.INVISIBLE);

    }

//    public void setBtnMuteAccurate(){
//        if(muted){
//            btnMute.setImageResource(R.drawable.muted);
//        }else{
//            btnMute.setImageResource(R.drawable.speaker);
//        }
//    }

    //############  CountDownTimerExtended CLASS   ####################

    public class myCountdownTimer extends CountDownTimerExtended {
        public myCountdownTimer(long startTime, long interval, boolean runAtStart) {
            super(startTime, interval);

        }

        @Override
        public void onFinish() {
            txtFeedback.setText("Time's up!");
            overTime = true;
            myLayout.setBackgroundColor(
                    0xff000000
                            + 128 * 0x10000
                            + 128 * 0x100
                            + 128
            );
        }

        @Override
        public void onTick(long millisUntilFinished) {
            //System.out.println("tick,");
            text.setText(formatMillis(millisUntilFinished));
            msLeft = millisUntilFinished;
            if (msLeft < warningTime && !hasWarned) {
                txtFeedback.setText("WARNING!!!");
                playSound(mWarningSound);
                if(vibrateOn) vibrate.vibrate(1000);
                hasWarned = true;
            }

            if (msLeft < 0) {
                txtFeedback.setText("Time's up!");
                if (!overTime) {
                    playSound(mTimeupSound);
                    if(vibrateOn) vibrate.vibrate(2000);
                    overTime = true;
                }
                ColorDrawable viewColor = (ColorDrawable) myLayout.getBackground();
                int colorId = viewColor.getColor();
                //System.out.println("colorid: " + colorId + "     white: " + Color.WHITE);
                if (colorId == Color.WHITE) {
                    myLayout.setBackgroundColor(Color.BLACK);
                } else {
                    myLayout.setBackgroundColor(Color.WHITE);
                }


            }
        }


    }

}