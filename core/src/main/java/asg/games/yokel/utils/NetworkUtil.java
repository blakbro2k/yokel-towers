package asg.games.yokel.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.github.czyzby.kiwi.util.common.UtilitiesClass;


/**
 * Created by Blakbro2k on 2/18/2018.
 */

public class NetworkUtil extends UtilitiesClass {
    private static NetworkUtil myInstance = new NetworkUtil();
    private static final String NO_NETWORK_CONNECTIVITY = "failed";
    private NetworkUtil(){}
    private static String status;

    public static NetworkUtil getInstance(){
        return myInstance;
    }

    public static boolean isInternetAvailable() {
        try{
            HttpRequest httpGet = new HttpRequest(HttpMethods.GET);
            httpGet.setTimeOut(2500);
            httpGet.setUrl("http://www.example.com");

            Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {
                public void handleHttpResponse(HttpResponse httpResponse) {
                    status = httpResponse.getResultAsString();
                    //do stuff here based on response
                }

                public void failed(Throwable t) {
                    status = NO_NETWORK_CONNECTIVITY;
                    //do stuff here based on the failed attempt
                }

                public void cancelled() {
                    status = NO_NETWORK_CONNECTIVITY;
                    //do stuff here based on the failed attempt
                }
            });

            return !NO_NETWORK_CONNECTIVITY.equals(status);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /*
    REQUEST_ALL_DEBUG_PLAYERS,
    REQUEST_LOGIN,
    REQUEST_PLAYER_REGISTER,
    REQUEST_CREATE_GAME,
    REQUEST_PLAY_GAME,
    REQUEST_TABLE_STAND,
    REQUEST_TABLE_JOIN,
    REQUEST_TABLE_SIT,
    REQUEST_ROOM,
    REQUEST_ROOM_JOIN,
    REQUEST_ROOM_LEAVE,
    REQUEST_LOUNGE,
    REQUEST_TABLE_INFO,
    REQUEST_LOUNGE_ALL
     */
}
