package asg.games.yokel.objects;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import asg.games.yokel.utils.YokelUtilities;

/**
 * Created by Blakbro2k on 1/28/2018.
 */

public class YokelPlayer extends AbstractYokelObject implements Copyable<YokelPlayer>, Json.Serializable {
    public final static int DEFAULT_RATING_NUMBER = 1500;
    public final static YokelPlayer BLANK_PLAYER = new YokelPlayer("", DEFAULT_RATING_NUMBER, 0);

    private int rating;
    private int icon;

    //Empty Constructor required for Json.Serializable
    public YokelPlayer(){}

    public YokelPlayer(Class<YokelPlayer> clazz, String data){
        super();
        YokelPlayer temp = YokelUtilities.getObjectFromJsonString(clazz, data);
        if(temp != null) {
            setId(temp.getId());
            setName(temp.getName());
            setCreated(temp.getCreated());
            setModified(temp.getModified());
            setRating(temp.getRating());
            setIcon(temp.getIcon());
        }
    }

    public YokelPlayer(String name){
        this(name, DEFAULT_RATING_NUMBER, 1);
    }

    public YokelPlayer(String name, int rating){
        this(name, rating, 1);
    }

    public YokelPlayer(String name, int rating, int icon){
        super();
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
    public String toString() {
        return YokelUtilities.getJsonString(this.getClass(), this);
    }

    @Override
    public YokelPlayer copy() {
        YokelPlayer copy;
        try {
            copy = ClassReflection.newInstance(YokelPlayer.class);
        } catch (ReflectionException e) {
            throw new GdxRuntimeException("There was an issue copying " + getName());
        }
        copy.setName(this.name);
        copy.setRating(this.rating);
        copy.setIcon(this.icon);
        return copy;
    }

    @Override
    public YokelPlayer deepCopy() {
        YokelPlayer copy = copy();
        copyParent(copy);
        return copy;
    }

    @Override
    public void write(Json json) {
        super.write(json);
        if(json != null) {
            json.writeValue("rating", rating);
            json.writeValue("icon", icon);
        }
    }

    @Override
    public void read(Json json, JsonValue jsonValue){
        super.read(json, jsonValue);
        if(json != null) {
            rating = json.readValue("rating", Integer.class, jsonValue);
            icon = json.readValue("icon", Integer.class, jsonValue);
        }
    }
}