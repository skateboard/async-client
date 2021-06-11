package me.brennan.client.listener;

import me.brennan.client.SocketClient;

/**
 * @author Brennan
 * @since 6/11/2021
 **/
public interface SocketListener {

    void onConnect(SocketClient client);

    //#TODO receive packet

    void onDisconnect(SocketClient client);

}
