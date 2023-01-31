package asg.games.yokel.persistence;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import asg.games.yokel.objects.YokelRoom;

public class YokelRoomType extends YokelUserType {
    @Override
    public Class returnedClass() {
        return YokelRoom.class;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {

        //id	created	modified	name	lounge_name
        YokelRoom result = null;
        String id = rs.getString(names[0]);
        Date created = new Date(rs.getLong(names[1]));
        Date modified = new Date(rs.getLong(names[2]));
        String name = rs.getString(names[3]);
        String lounge_name = rs.getString(names[4]);


        if (!rs.wasNull()) {
            result = new YokelRoom();
            result.setId(id);
            result.setName(name);
            result.setCreated(created);
            result.setModified(modified);
            result.setLoungeName(lounge_name);
        }
        return result;
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        YokelRoom copy;

        if (value == null) {
            copy = null;
        } else if (!(value instanceof YokelRoom)) {
            copy = null;
        } else {
            copy = ((YokelRoom) value).deepCopy();
        }
        return copy;
    }
}
