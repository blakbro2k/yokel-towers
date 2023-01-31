package asg.games.yokel.objects;

/**
 * Created by Blakbro2k on 1/28/2018.
 */

public class YokelPlayer extends AbstractYokelObject implements Copyable<YokelPlayer>{
    public final static int DEFAULT_RATING_NUMBER = 1500;
    public final static YokelPlayer BLANK_PLAYER = new YokelPlayer("", DEFAULT_RATING_NUMBER, 0);

    private int rating;
    private int icon;

    //Empty Constructor required for Json.Serializable
    public YokelPlayer(){}

    public YokelPlayer(String name){
        this(name, DEFAULT_RATING_NUMBER, 1);
    }

    public YokelPlayer(String name, int rating){
        this(name, rating, 1);
    }

    public YokelPlayer(String name, int rating, int icon){
        setName(name);
        setRating(rating);
        setIcon(icon);
    }

    public int getRating(){
        return rating;
    }

    public void setRating(int rating){
        this.rating = rating;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }

    public int getIcon(){
        return this.icon;
    }

    public void increaseRating(int inc){
        rating += inc;
    }

    public void decreaseRating(int dec){
        rating -= dec;
    }

    @Override
    public void dispose() {}

    @Override
    public YokelPlayer copy() {
        return new YokelPlayer(this.name, this.rating, this.icon);
    }

    public YokelPlayer deepCopy() {
        YokelPlayer copy = copy();
        copyParent(copy);
        return copy;
    }
}