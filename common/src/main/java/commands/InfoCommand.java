package commands;

import collection.CollectionManager;
import exceptions.CannotExecuteCommandException;

import java.io.PrintStream;

/**
 * Класс команды, отвечающей за получение информации о текущей коллекции.
 */
public class InfoCommand extends Command {

    /**
     * Поле, хранящее ссылку на объект класса CollectionManager.
     */
    private CollectionManager collectionManager;

    /**
     * Конструктор класса.
     *
     * @param collectionManager Хранит ссылку на объект CollectionManager.
     */
    public InfoCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Конструктор без параметров.
     */
    public InfoCommand() {
        super("info");
    }

    /**
     * Метод, исполняющий команду. Выводит описание коллекции TreeMap экземпляров Dragon.
     *
     * @param invocationEnum режим, с которым должна быть исполнена данная команда.
     * @param printStream поток вывода.
     * @param arguments аргументы команды.
     */
    @Override
    public void execute(String[] arguments, Status invocationEnum, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationEnum.equals(Status.CLIENT)) {
            if (arguments.length > 0) {
                throw new CannotExecuteCommandException("У данной команды нет аргументов.");
            }

        } else if (invocationEnum.equals(Status.SERVER)) {
            printStream.println(collectionManager.info());
        }
    }

    /**
     * Метод, возвращющий описание команды.
     * @return Описание команды.
     *
     * @see HelpCommand
     */
    @Override
    public String getDescription() {
        return "команда получает информацию о коллекции (тип, дата инициализации, количество элементов, тип элементов коллекции)";
    }
}
