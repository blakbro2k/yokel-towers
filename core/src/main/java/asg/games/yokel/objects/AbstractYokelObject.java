package asg.games.yokel.objects;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Objects;

import asg.games.yokel.utils.YokelUtilities;

public abstract class AbstractYokelObject implements YokelObject, Json.Serializable {
    protected String id;
    protected String name;
    protected long created;
    protected long modified;

    AbstractYokelObject(){
        setCreated(TimeUtils.millis());
        setModified(TimeUtils.millis());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractYokelObject)) return false;
        AbstractYokelObject that = (AbstractYokelObject) o;
        if(getId() != null) {
            return Objects.equals(getId(), that.getId());
        } else {
            return Objects.equals(name, that.getName()) &&
                    Objects.equals(created, that.getCreated())&&
                    Objects.equals(modified, that.getModified());
        }
    }

    @Override
    public int hashCode() {
        if(getId() != null) {
            return Objects.hash(id);
        } else {
            return Objects.hash(name, created, modified);
        }
    }

    @Override
    public void write(Json json) {
        if(json != null) {
            json.writeValue("id", id);
            json.writeValue("name", name);
            json.writeValue("created", created);
            json.writeValue("modified", modified);
        }
    }

    @Override
    public String toString() {
        return YokelUtilities.getJsonString(this.getClass(), this);
    }

    @Override
    public void read(Json json, JsonValue jsonValue) {
        if(json != null) {
            id = json.readValue("id", String.class, jsonValue);
            name = json.readValue("name", String.class, jsonValue);
            created = json.readValue("created", Long.class, jsonValue);
            modified = json.readValue("modified", Long.class, jsonValue);
        }
    }

    public void setId(String id){ this.id = id;}

    public String getId(){ return id;}

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setCreated(long dateTime) {
        this.created = dateTime;
    }

    public long getCreated() {
        return created;
    }

    public void setModified(long dateTime) {
        this.modified = dateTime;
    }

    public long getModified() {
        return modified;
    }

    protected void copyParent(YokelObject o){
        if(o != null) {
            o.setId(this.getId());
            o.setName(this.getName());
            o.setCreated(this.getCreated());
            o.setModified(this.getModified());
        }
    }
}
