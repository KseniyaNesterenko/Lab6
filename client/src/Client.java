import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.channels.DatagramChannel;

public class Client {

    //private static final Logger rootLogger = LogManager.getRootLogger();
    private DatagramChannel clientChannel;

    public void connect(InetSocketAddress inetServerAddress) throws IOException {
        try {
            clientChannel = DatagramChannel.open();

            clientChannel.configureBlocking(false);

            clientChannel.connect(inetServerAddress);
        } catch (IllegalArgumentException ex) {
            System.out.println("Указан недопустимый порт: " + inetServerAddress.getPort());
        } catch (SocketException ex) {
            System.out.println("Установка соденинения не удалась\n" + ex);
        }
    }

    public DatagramChannel getClientChannel() {
        return clientChannel;
    }
}