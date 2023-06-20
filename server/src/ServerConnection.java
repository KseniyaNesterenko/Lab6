import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.DatagramSocket;
import java.net.SocketException;

public class ServerConnection {

    private final static Logger serverConnectionLogger = LogManager.getLogger(ServerConnection.class);

    private DatagramSocket serverSocket;

    public boolean createFromPort(Integer port) {

        try {
            serverSocket = new DatagramSocket(port);
            serverConnectionLogger.info("Сервер готов.");
            return true;
        } catch (SocketException ex) {
            serverConnectionLogger.warn("Порт " + port + " занят или сокет не может быть открыт. Попробуйте с другим портом.");
            System.exit(-1);
        }
        return false;
    }

    public DatagramSocket getServerSocket() {
        return serverSocket;
    }
}