package asg.games.yokel.objects;

public class YokelBlockMove extends AbstractYokelObject {
    public int x;
    public int y;
    public int targetRow;

    //Empty Constructor required for Json.Serializable
    public YokelBlockMove(){}

    YokelBlockMove(int x, int y, int targetRow){
        this.x = x;
        this.y = y;
        this.targetRow = targetRow;
    }
}
