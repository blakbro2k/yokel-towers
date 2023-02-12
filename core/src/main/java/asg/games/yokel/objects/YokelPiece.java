package asg.games.yokel.objects;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import asg.games.yokel.utils.YokelUtilities;

public class YokelPiece extends AbstractYokelObject implements Json.Serializable {
    private final int[] cells = new int[3];
    private int index;
    public int row;
    public int column;

    //Empty Constructor required for Json.Serializable
    public YokelPiece(){}

    public YokelPiece(int index, int block1, int block2, int block3){
        this.index = index;
        setBlock1(block1);
        setBlock2(block2);
        setBlock3(block3);
    }

    public int getValueAt(int i) {
        return cells[i];
    }

    public int[] getCells(){
        return cells;
    }

    public int getBlock1(){
        return cells[2];
    }

    public void setBlock1(int block){
        cells[2] = block;
    }

    public int getBlock2(){
        return cells[1];
    }

    public void setBlock2(int block){
        cells[1] = block;
    }

    public int getBlock3(){
        return cells[0];
    }

    public void setBlock3(int block){
        cells[0] = block;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index){
        this.index = index;
    }

    public void setPosition(int r, int c) {
        if(r < 0) throw new RuntimeException("Row value cannot be less than zero!");
        if(c < 0) throw new RuntimeException("Column value cannot be less than zero!");
        this.row = r;
        this.column = c;
    }

    public void cycleDown() {
        int tempBlock1 = getBlock1();
        int tempBlock2 = getBlock2();
        int tempBlock3 = getBlock3();
        setBlock1(tempBlock3);
        setBlock2(tempBlock1);
        setBlock3(tempBlock2);
    }

    public void cycleUp() {
        int tempBlock1 = getBlock1();
        int tempBlock2 = getBlock2();
        int tempBlock3 = getBlock3();
        setBlock1(tempBlock2);
        setBlock2(tempBlock3);
        setBlock3(tempBlock1);
    }

    @Override
    public void write(Json json) {
        super.write(json);
        if(json != null) {
            json.writeValue("index", index);
            json.writeValue("row", row);
            json.writeValue("column", column);
            json.writeArrayStart("cells");
            json.writeValue(getBlock1());
            json.writeValue(getBlock2());
            json.writeValue(getBlock3());
            json.writeArrayEnd();
        }
    }

    @Override
    public void read(Json json, JsonValue jsonValue){
        super.read(json, jsonValue);
        if(json != null) {
            index = json.readValue("index", Integer.class, jsonValue);
            row = json.readValue("row", Integer.class, jsonValue);
            column = json.readValue("column", Integer.class, jsonValue);
            //column = json.readValue("column", Array.class, jsonValue);
            //icon = json.readValue("icon", Integer.class, jsonValue);
        }
    }

    @Override
    public String toString() {
        return YokelUtilities.getJsonString(this.getClass(), this);
    }
}