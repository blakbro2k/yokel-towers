package asg.games.yokel.objects;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;

import asg.games.yokel.persistence.YokelStorageAdapter;
import asg.games.yokel.utils.YokelUtilities;

/**
 * Created by Blakbro2k on 1/28/2018.
 */

public class YokelRoom extends AbstractYokelObject implements YokelObjectJPAVisitor, Copyable<YokelRoom>, Disposable {
    public static final String SOCIAL_GROUP = "Social";
    public static final String BEGINNER_GROUP = "Beginner";
    public static final String INTERMEDIATE_GROUP = "Intermediate";
    public static final String ADVANCED_GROUP = "Advanced";

    private Array<YokelPlayer> players = GdxArrays.newArray();
    private ObjectMap<Integer, YokelTable> tables = GdxMaps.newObjectMap();
    private String loungeName;

    //Empty Constructor required for Json.Serializable
    public YokelRoom() {
    }

    public YokelRoom(String name) {
        setName(name);
    }

    public YokelRoom(String name, String loungeName) {
        setName(name);
        setLoungeName(loungeName);
    }

    public Array<YokelTable> getAllTables() {
        return YokelUtilities.getMapValues(tables).toArray();
    }

    public Array<Integer> getAllTableIndexes() {
        return YokelUtilities.getMapKeys(tables).toArray();
    }

    public void setAllTables(ObjectMap<Integer, YokelTable> tables) {
        this.tables = tables;
    }

    public Array<YokelPlayer> getAllPlayers() {
        return players;
    }

    public void setAllPlayers(Array<YokelPlayer> players) {
        this.players = players;
    }

    public void joinRoom(YokelPlayer player) {
        if (player != null && !players.contains(player, false)) {
            players.add(player);
        }
    }

    public void leaveRoom(YokelPlayer player) {
        players.removeValue(player, false);
    }

    public YokelTable addTable() {
        return addTable(null);
    }

    public YokelTable addTable(OrderedMap<String, Object> arguments) {
        YokelTable table;
        int tableNumber = YokelUtilities.getNextTableName(this);

        if (arguments != null) {
            table = new YokelTable(getId(), tableNumber, arguments);
        } else {
            table = new YokelTable(getId(), tableNumber);
        }
        tables.put(tableNumber, table);
        return table;
    }

    public YokelTable getTable(int tableNumber) {
        return tables.get(tableNumber);
    }

    public void removeTable(int tableNumber) {
        tables.remove(tableNumber);
    }

    public String getLoungeName() {
        return loungeName;
    }

    public void setLoungeName(String loungeName) {
        this.loungeName = loungeName;
    }

    @Override
    public void dispose() {
        YokelUtilities.clearArrays(players);
        if (tables != null) {
            tables.clear();
        }
    }

    @Override
    public YokelRoom copy() {
        YokelRoom copy;
        try {
            copy = ClassReflection.newInstance(YokelRoom.class);
        } catch (ReflectionException e) {
            throw new GdxRuntimeException("There was an issue copying " + getName());
        }
        copy.setName(this.name);
        copy.setLoungeName(this.loungeName);
        return copy;
    }

    @Override
    public YokelRoom deepCopy() {
        YokelRoom copy = copy();
        copyParent(copy);
        copy.setAllPlayers(players);
        copy.setAllTables(tables);
        return copy;
    }

    @Override
    public void visitSave(YokelStorageAdapter adapter) {
        try {
            if (adapter != null) {
                adapter.putAllTables(YokelUtilities.getMapValues(tables));
                adapter.putAllPlayers(players);
            }
        } catch (Exception e) {
            throw new GdxRuntimeException("Issue visiting save for " + this.getClass().getSimpleName(), e);
        }
    }
}