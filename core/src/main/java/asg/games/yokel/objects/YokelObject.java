package asg.games.yokel.objects;

public interface YokelObject {
    /** Sets the Object ID*/
    void setId(String id);

    /** Returns the Object ID*/
    String getId();

    /** Sets the Object Name*/
    void setName(String name);

    /** Returns the Object Name*/
    String getName();

    /** Sets the Object created date*/
    void setCreated(long dateTime);

    /** Returns the Object created date*/
    long getCreated();

    /** Sets the Object modified date*/
    void setModified(long dateTime);

    /** Returns the Object modified date*/
    long getModified();
}