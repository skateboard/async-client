package me.brennan.client;

import me.brennan.client.listener.SocketListener;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Brennan
 * @since 6/11/2021
 **/
public class SocketClient implements Runnable {
    private AsynchronousSocketChannel client;

    private final String address;
    private final int port;

    private SocketListener socketListener;

    public SocketClient(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void start() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        reconnect:
        while (true) {
            try {
                if(client == null) {
                    this.client = AsynchronousSocketChannel.open();
                    final Future<Void> result = client.connect(new InetSocketAddress(address, port));
                    result.get();

                    socketListener.onConnect(this);
                }
            } catch (IOException e) {
                e.printStackTrace();

                this.delayReconnection();
                continue reconnect;
            } catch (InterruptedException | ExecutionException e) {
                if(e.getMessage().contains("he remote computer refused the network connection.")) {
                    System.exit(-1);
                }
                e.printStackTrace();
            }

            while (client.isOpen()) {
                try {
                    final ByteBuffer buffer = ByteBuffer.allocate(1024);
                    final Future<Integer> read = client.read(buffer);
                    read.get();

                    final String string = new String(buffer.array()).trim();

                    //#TODO receive packet

                    buffer.clear();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    if(e.getMessage().contains("An existing connection was forcibly closed by the remote host")) {
                        socketListener.onDisconnect(this);
                    }
                }

                this.delayReconnection();
                continue reconnect;
            }

            this.delayReconnection();
            continue reconnect; //Retry connection
        }
    }

    public void setSocketListener(SocketListener socketListener) {
        this.socketListener = socketListener;
    }

    public SocketListener getSocketListener() {
        return socketListener;
    }

    private void delayReconnection() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
