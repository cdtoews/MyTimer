package info.yourtechguys.mytimer;

/**
 * Created by ct0ews on 1/2/2016.
 */
public class TimeSetting {
    private int seconds;
    private long ms;

    public TimeSetting(int seconds){
        this.seconds = seconds;
        this.ms = seconds * 1000;

    }

    @Override
    public String toString(){
        return TimerActivity.formatMillis(this.ms);
    }


    public int getSeconds() {
        return seconds;
    }

    public long getMs() {
        return ms;
    }
}
