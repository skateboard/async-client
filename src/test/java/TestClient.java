import me.brennan.client.SocketClient;
import me.brennan.client.listener.SocketListener;

/**
 * @author Brennan
 * @since 6/11/2021
 **/
public class TestClient {

    public static void main(String[] args) {
        final SocketClient socketClient = new SocketClient("localhost", 1337);

        socketClient.setSocketListener(new SocketListener() {
            @Override
            public void onConnect(SocketClient client) {
                System.out.println("Connected");
            }

            @Override
            public void onDisconnect(SocketClient client) {
                System.out.println("Disconnected");

            }
        });
        socketClient.start();
    }

}
