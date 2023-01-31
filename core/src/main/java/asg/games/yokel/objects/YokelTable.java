package asg.games.yokel.objects;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.OrderedMap;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;

import asg.games.yokel.persistence.YokelStorageAdapter;
import asg.games.yokel.utils.YokelUtilities;

public class YokelTable extends AbstractYokelObject implements YokelObjectJPAVisitor {
    public static final String ARG_TYPE = "type";
    public static final String ARG_RATED = "rated";
    public static final String ENUM_VALUE_PRIVATE = "PRIVATE";
    public static final String ENUM_VALUE_PUBLIC = "PUBLIC";
    public static final String ENUM_VALUE_PROTECTED = "PROTECTED";
    public static final String ATT_NAME_PREPEND = "#";
    public static final int MAX_SEATS = 8;

    public enum ACCESS_TYPE {
        PRIVATE(ENUM_VALUE_PRIVATE), PUBLIC(ENUM_VALUE_PUBLIC), PROTECTED(ENUM_VALUE_PROTECTED);
        private String accessType;

        ACCESS_TYPE(String accessType) {
            this.accessType = accessType;
        }

        public String getValue() {
            return accessType;
        }
    }

    private ACCESS_TYPE accessType;

    private Array<YokelSeat> seats = GdxArrays.newArray();

    private Array<YokelPlayer> watchers = GdxArrays.newArray();


    private boolean isRated;

    private boolean isSoundOn;

    private String roomId;

    //Empty Constructor required for Json.Serializable
    public YokelTable() {
    }

    public YokelTable(String roomId, int nameNumber) {
        this(roomId, nameNumber, null);
    }

    public YokelTable(String roomId, int nameNumber, OrderedMap<String, Object> arguments) {
        this.roomId = roomId;
        initialize(nameNumber, arguments);
    }

    private void initialize(int nameNumber, OrderedMap<String, Object> arguments) {
        System.out.println("initialize.");
        setTableName(nameNumber);
        setUpSeats();
        setSound(true);
        setUpArguments(arguments);
    }

    private void setTableName(int tableNumber) {
        setName(roomId + ATT_NAME_PREPEND + tableNumber);
    }

    public int getTableNumber() {
        return Integer.parseInt(YokelUtilities.split(getName(), ATT_NAME_PREPEND)[1]);
    }

    private void setUpArguments(OrderedMap<String, Object> arguments) {
        if (arguments != null) {
            for (String key : YokelUtilities.getMapKeys(arguments)) {
                if (key != null) {
                    Object o = arguments.get(key);
                    processArg(key, o);
                }
            }
        } else { //Setting up Table with Default arguments
            setAccessType(ACCESS_TYPE.PUBLIC);
            setRated(false);
        }
    }

    private void processArg(String arg, Object value) {
        if (arg != null && value != null) {
            if (YokelUtilities.equalsIgnoreCase(ARG_TYPE, arg)) {
                setAccessType(YokelUtilities.otos(value));
            } else if (YokelUtilities.equalsIgnoreCase(ARG_RATED, arg)) {
                setRated(YokelUtilities.otob(value));
            }
        }
    }

    public void setAccessType(ACCESS_TYPE accessType) {
        this.accessType = accessType;
    }

    public void setAccessType(String accessType) {
        if (YokelUtilities.equalsIgnoreCase(ACCESS_TYPE.PRIVATE.toString(), accessType)) {
            setAccessType(ACCESS_TYPE.PRIVATE);
        } else if (YokelUtilities.equalsIgnoreCase(ACCESS_TYPE.PROTECTED.toString(), accessType)) {
            setAccessType(ACCESS_TYPE.PROTECTED);
        } else {
            setAccessType(ACCESS_TYPE.PUBLIC);
        }
    }

    public ACCESS_TYPE getAccessType() {
        return accessType;
    }

    public void setRated(boolean rated) {
        this.isRated = rated;
    }

    public void setSound(boolean sound) {
        this.isSoundOn = sound;
    }

    public boolean isRated() {
        return isRated;
    }

    public boolean isSoundOn() {
        return isSoundOn;
    }

    public boolean isGroupReady(int g) {
        if (g < 0 || g > 3) {
            return false;
        }
        int seatNumber = g * 2;
        return isSeatReady(seatNumber) || isSeatReady(seatNumber + 1);
    }

    public boolean isSeatReady(YokelSeat seat) {
        if (seat != null) {
            return seat.isSeatReady();
        }
        return false;
    }

    public boolean isSeatReady(int seatNum) {
        return isSeatReady(getSeat(seatNum));
    }

    public boolean isTableStartReady() {
        if (isGroupReady(0)) {
            return isGroupReady(1) || isGroupReady(2) || isGroupReady(3);
        }
        if (isGroupReady(1)) {
            return isGroupReady(0) || isGroupReady(2) || isGroupReady(3);
        }
        if (isGroupReady(2)) {
            return isGroupReady(0) || isGroupReady(1) || isGroupReady(3);
        }
        if (isGroupReady(3)) {
            return isGroupReady(0) || isGroupReady(1) || isGroupReady(2);
        }
        return false;
    }

    private void setUpSeats() {
        for (int i = 0; i < MAX_SEATS; i++) {
            seats.add(new YokelSeat(getName(), i));
        }
    }

    public void makeTableUnready() {
        for (YokelSeat seat : YokelUtilities.safeIterable(seats)) {
            if (seat != null) {
                seat.setSeatReady(false);
            }
        }
    }

    public Array<YokelSeat> getSeats() {
        return seats;
    }

    private void setSeats(Array<YokelSeat> seats) {
        this.seats = seats;
    }

    public YokelSeat getSeat(int seatNum) {
        return seats.get(seatNum);
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void addWatcher(YokelPlayer player) {
        if (player != null) {
            watchers.add(player);
        }
    }

    public void removeWatcher(YokelPlayer player) {
        if (player != null) {
            watchers.removeValue(player, true);
        }
    }

    private void setWatchers(Array<YokelPlayer> watchers) {
        this.watchers = watchers;
    }

    public Array<YokelPlayer> getWatchers() {
        return watchers;
    }

    @Override
    public void dispose() {
        if (seats != null) {
            seats.clear();
        }
    }

    public YokelTable deepCopy() {
        YokelTable copy = new YokelTable();
        copyParent(copy);
        copy.setAccessType(accessType);
        copy.setRated(isRated);
        copy.setSound(isSoundOn);
        copy.setRoomId(roomId);
        copy.setSeats(seats);
        copy.setWatchers(watchers);
        return copy;
    }

    @Override
    public void visitSave(YokelStorageAdapter adapter) {
        try{
            if(adapter != null) {
                adapter.putAllPlayers(watchers);
                adapter.putAllSeats(seats);
            }
        } catch (Exception e) {
            throw new GdxRuntimeException("Issue visiting save for " + this.getClass().getSimpleName() + ": ", e);
        }
    }
}