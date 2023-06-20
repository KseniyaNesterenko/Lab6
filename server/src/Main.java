import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Main {
    private static final Logger mainLogger = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
        App app = new App();
        if (args.length > 0) {
            if (!args[0].equals("")) {
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    mainLogger.info("Сохранение коллекции в файле.");
                    app.getCollectionManager().save(args[0]);
                    mainLogger.info("Коллекция была сохранена "+ args[0]);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        mainLogger.error("Ошибка с потоками: "+ ex);
                    }
                    mainLogger.info("Завершение работы сервера.");
                }));
                try {
                    app.start(args[0]);
                } catch (ParserConfigurationException | SAXException | IOException ex) {
                    mainLogger.warn("По указанному адресу нет подходящего файла "+ args[0]);
                }
            }
        }
        else {
            String file = "C:\\Users\\Home\\Desktop\\data\\inputfile.txt";
            try {
                app.start(file);
            } catch (ParserConfigurationException | SAXException | IOException ex) {
                mainLogger.warn("По указанному адресу нет подходящего файла " + file);
            }
        }
    }
}