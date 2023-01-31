package asg.games.yokel.server;

import com.github.czyzby.websocket.serialization.impl.ManualSerializer;

/** Utility class. Allows to easily register packets in the same order on both client and server.
 *
 * @author blakbro2k */
public class Packets {
    public static void register(final ManualSerializer serializer) {
        // Note that the packets use simple, primitive data, but nothing stops you from using more complex types like
        // strings, arrays or even other transferables. Both Serializer and Deserializer APIs are well documented: make
        // sure to check them out.
        serializer.register(new ClientRequest(-1, null,null, null, null));
        serializer.register(new ServerResponse(-1, null, null, null, -1));
    }
}