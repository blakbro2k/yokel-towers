package asg.games.yokel.objects;

import com.badlogic.gdx.utils.Disposable;

import asg.games.yokel.utils.YokelUtilities;

/**
 * Created by Blakbro2k on 1/28/2018.
 */

public class YokelSeat extends AbstractYokelObject implements Disposable {
    private static final String ATTR_SEAT_NUM_SEPARATOR = "-";

    private YokelPlayer seatedPlayer;
    private String tableId;
    private boolean isSeatReady = false;

    //Empty Constructor required for Json.Serializable
    public YokelSeat(){}

     public YokelSeat(String tableId, int seatNumber){
        if(seatNumber < 0 || seatNumber > 7) throw new IllegalArgumentException("Seat number must be between 0 - 7.");
        setTableId(tableId);
        setName(tableId + ATTR_SEAT_NUM_SEPARATOR + seatNumber);
    }

    public boolean sitDown(YokelPlayer player){
        if(!isOccupied()){
            seatedPlayer = player;
            return true;
        }
        return false;
    }

    public YokelPlayer standUp(){
        YokelPlayer var = seatedPlayer;
        seatedPlayer = null;
        setSeatReady(false);
        return var;
    }

    public boolean isOccupied(){
        return seatedPlayer != null;
    }

    public void setSeatReady(boolean isSeatReady){
        this.isSeatReady = isSeatReady;
    }

    public boolean isSeatReady(){
        return isOccupied() && isSeatReady;
    }

    public YokelPlayer getSeatedPlayer(){
        return seatedPlayer;
    }

    public int getSeatNumber(){
        return Integer.parseInt(YokelUtilities.split(getName(),ATTR_SEAT_NUM_SEPARATOR)[1]);
    }

    @Override
    public void dispose() {
        if(isOccupied()){
            standUp();
        }
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }
}