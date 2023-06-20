import commands.CommandInvoker;
import io.UserIO;
import java.net.InetSocketAddress;
import java.net.PortUnreachableException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.NoSuchElementException;

public class App {
    //private static final Logger rootLogger = LogManager.getRootLogger();

    UserIO userIO;

    CommandInvoker commandInvoker;

    private final int port;

    public App(Integer port) {
        this.port = port;
        userIO = new UserIO();
        commandInvoker = new CommandInvoker(userIO);
        System.out.println("Конструктор класса Apр был загружен.");

    }

    public void start() {

        try {
            InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", port);

            Client clientConnection = new Client();
            clientConnection.connect(inetSocketAddress);
            ResponseSender responseSender = new ResponseSender(clientConnection.getClientChannel());
            RequestReader requestReader = new RequestReader(clientConnection.getClientChannel());
            CommandProcessor commandProcessor = new CommandProcessor(commandInvoker);

            System.out.println("Клиент готов к чтению команд.");
            boolean isConnected = true;
            boolean isNeedInput = true;
            boolean isCommandAcceptable = false;

            String line = "";

            while (isConnected) {
                if (isNeedInput) {
                    System.out.println("Введите название команды:");
                    userIO.printPreamble();
                    line = userIO.readLine();
                    isCommandAcceptable =  commandProcessor.executeCommand(line);
                }
                try {
                    if (isCommandAcceptable) {

                        responseSender.sendContainer(commandInvoker.getLastCommandContainer(), inetSocketAddress);

                        System.out.println("Данные были отправлены.");
                        ByteBuffer byteBuffer = requestReader.receiveBuffer();
                        byteBuffer.flip();
                        System.out.println("Данные были получены.");
                        System.out.println(new String(byteBuffer.array(), StandardCharsets.UTF_8).trim() + "\n");
                        isNeedInput = true;
                    }
                } catch (PortUnreachableException | SocketTimeoutException ex) {
                    if (ex instanceof PortUnreachableException) {
                        System.out.println("Порт " + port + " не доступен. Повторить отправку команды? y/n");
                    } else {
                        System.out.println("Сервер не отвечает. Повторить отправку команды? y/n");
                    }
                    String result = userIO.readLine().trim().toLowerCase(Locale.ROOT).split("\\s+")[0];
                    if (result.equals("n")) {
                        System.out.println("Завершение работы клиента");
                        isConnected = false;
                    } else {
                        isNeedInput = false;
                    }
                }
            }
        } catch (NoSuchElementException ex) {
            System.out.println("\nАварийное завершение работы.");
        } catch (SocketException ex) {
            System.out.println("Ошибка подключения сокета к порту, или сокет не может быть открыт."
                    + ex.getMessage() + "/n" + "localhost" + " ; " + port);
        } catch (IllegalArgumentException ex) {
            System.out.println("Порт не принадлежит ОДЗ: " + port);
        } catch (Exception ex) {
            System.out.println("Неизвестная ошибка: " + ex);
        }
    }
}