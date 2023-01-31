package asg.games.yokel.objects;

import com.badlogic.gdx.utils.TimeUtils;

public class YokelClock extends AbstractYokelObject {
    private long start;
    private boolean isRunning;

    //Empty Constructor required for Json.Serializable
    public YokelClock(){
        resetTimer();
    }

    public void start(){
        isRunning = true;
        start = TimeUtils.millis();
    }

    public int getElapsedSeconds(){
        return (int) ((TimeUtils.millis() - start) / 1000);
    }

    public void stop(){
        resetTimer();
    }

    private void resetTimer(){
        start = -1;
        isRunning = false;
    }

    private int getSeconds(){
        if(isRunning()){
            return Math.round(getElapsedSeconds()) % 60;
        }
        return -1;
    }

    private int getMinutes(){
        if(isRunning()){
            return Math.round(getElapsedSeconds()) / 60;
        }
        return -1;
    }

    public boolean isRunning(){
        return this.isRunning;
    }

    @Override
    public void dispose() {}
}