package asg.games.yokel.objects;

import java.util.Date;
import java.util.Objects;

import asg.games.yokel.utils.YokelUtilities;

public abstract class AbstractYokelObject implements YokelObject {
    protected String id;
    protected String name;
    protected Date created;
    protected Date modified;

    AbstractYokelObject(){
        setCreated(new Date());
        setModified(new Date());
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
                    Objects.equals(created, that.getCreated());
        }
    }

    @Override
    public int hashCode() {
        if(getId() != null) {
            return Objects.hash(id);
        } else {
            return Objects.hash(name, created);
        }
    }

    @Override
    public String toString() { return YokelUtilities.getJsonString(this);}

    public <T> String toString(Class<T> type) { return YokelUtilities.getJsonString(type,this);}

    @Override
    public abstract void dispose();

    public void setId(String id){ this.id = id;}

    public String getId(){ return id;}

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setCreated(Date date) {
        this.created = date;
    }

    public Date getCreated() {
        return created;
    }

    public void setModified(Date date) {
        this.modified = date;
    }

    public Date getModified() {
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
