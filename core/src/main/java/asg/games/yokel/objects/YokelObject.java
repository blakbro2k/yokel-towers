package asg.games.yokel.objects;

import com.badlogic.gdx.utils.Disposable;

import java.util.Date;

public interface YokelObject extends Disposable {

    /** Sets the Object ID*/
    void setId(String id);

    /** Returns the Object ID*/
    String getId();

    /** Sets the Object Name*/
    void setName(String name);

    /** Returns the Object Name*/
    String getName();

    /** Sets the Object created date*/
    void setCreated(Date date);

    /** Returns the Object created date*/
    Date getCreated();

    /** Sets the Object modified date*/
    void setModified(Date date);

    /** Returns the Object modified date*/
    Date getModified();
}