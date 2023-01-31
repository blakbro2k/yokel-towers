package asg.games.yokel.persistence;

import com.badlogic.gdx.utils.ObjectMap;

import asg.games.yokel.objects.YokelPlayer;

public interface ClientPlayerController {
    /** Player Controls
     *  Methods control the flow of a player and client */
    /** Must link clientID to Player ID to Player Object. */
    void registerPlayer(String clientId, YokelPlayer player) throws Exception;

    /** Remove Registered Player. */
    /** Should remove the from all tables, seats, games and rooms **/
    void unRegisterPlayer(String clientID) throws Exception;

    /** Get Registered Player given Yokel Id. */
    YokelPlayer getRegisteredPlayerGivenId(String playerId);

    /** Get Registered Player given YokelObject. */
    YokelPlayer getRegisteredPlayer(YokelPlayer player);

    /** Gets all registered players. */
    ObjectMap.Values<YokelPlayer> getAllRegisteredPlayers();

    /** Check if client id is registered **/
    boolean isClientRegistered(String clientId);

    /** Check if player id is registered **/
    boolean isPlayerRegistered(String playerId);
}
