import collection.CollectionManager;
import collection.Dragon;
import commands.CommandInvoker;
import commands.CommandContainer;
import file.FileManager;
import file.XmlParser;
import io.UserIO;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class App {

    CollectionManager collectionManager;

    FileManager fileManager;

    XmlParser xmlParser;

    UserIO userIO;

    CommandInvoker commandInvoker;

    ServerConnection serverConnection;

    private boolean isConnected;

    private static final Logger appLogger = LogManager.getLogger(App.class);

    App() {
        collectionManager = new CollectionManager();
        fileManager = new FileManager();
        xmlParser = new XmlParser();
        userIO = new UserIO();
        appLogger.info("Конструктор класса Application был загружен.");
    }

    public void start(String inputFile) throws IOException, ParserConfigurationException, SAXException {

        try {
            File ioFile = new File(inputFile);
            if (!ioFile.canWrite() || ioFile.isDirectory() || !ioFile.isFile()) throw new IOException();
            String file = fileManager.readFromFile(inputFile);

            Dragon[] dragons = xmlParser.parseToCollection(new InputSource(new StringReader(file)));
            for (Dragon dragon : dragons) collectionManager.insert(dragon.getId(), dragon, System.out);

            this.commandInvoker = new CommandInvoker(collectionManager, inputFile);

            appLogger.printf(Level.INFO, "Элементы коллекций из файла %1$s были загружены.", inputFile);

            serverConnection = new ServerConnection();

            Scanner scanner = new Scanner(System.in);

            do {
                System.out.print("Введите порт: ");
                int port = scanner.nextInt();
                if (port <= 0){
                    appLogger.error("Введенный порт невалиден.");
                }
                else{
                    isConnected = serverConnection.createFromPort(port);
                }
            } while (!isConnected);
            appLogger.info("Порт установлен.");
        }catch(NoSuchElementException ex){
            appLogger.error("Аварийное завершение работы");
            System.exit(-1);
        }
        try {
            cycle(commandInvoker);
        } catch (NoSuchElementException | InterruptedException ex) {
            appLogger.warn(ex.getMessage());
            appLogger.warn("Работа сервера завершена.");
        }
    }

    private void cycle(CommandInvoker commandInvoker) throws InterruptedException {

        RequestReader requestReader = new RequestReader(serverConnection.getServerSocket());

        ResponseSender responseSender = new ResponseSender(serverConnection.getServerSocket());

        CommandProcessor commandProcessor = new CommandProcessor(commandInvoker);

        while (isConnected) {
            try {
                requestReader.readCommand();
                CommandContainer command = requestReader.getCommandContainer();

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                PrintStream printStream = new PrintStream(byteArrayOutputStream);

                commandProcessor.executeCommand(command, printStream);

                Thread.sleep(1000);

                responseSender.send(byteArrayOutputStream.toString(), requestReader.getSenderAddress(), requestReader.getSenderPort());
                appLogger.info("Пакет был отправлен " + requestReader.getSenderAddress().getHostAddress() + " " + requestReader.getSenderPort());

            } catch (IOException ex) {
                appLogger.warn("Произошла ошибка при чтении: " + ex.getMessage());
            } catch (ClassNotFoundException ex) {
                appLogger.error("Неизвестная ошибка: " + ex);
            }
        }
    }

    public CollectionManager getCollectionManager() {
        return collectionManager;
    }
}