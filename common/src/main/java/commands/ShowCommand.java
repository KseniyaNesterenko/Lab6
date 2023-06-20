package commands;

import collection.CollectionManager;
import exceptions.CannotExecuteCommandException;

import java.io.PrintStream;

public class ShowCommand extends Command {
    /**
     * Поле, хранящее ссылку на объект класса CollectionManager.
     */
    private CollectionManager collectionManager;

    /**
     * Конструктор класса.
     */
    public ShowCommand() {
        super("show");
    }

    public ShowCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Метод, исполняющий команду. Показывает подробное содержание содержимого коллекции.
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
            printStream.println(collectionManager.show());
        }
    }

    /**
     * Метод, возвращающий описание команды.
     *
     * @return Метод, возвращающий описание команды.
     * @see Command
     */
    @Override
    public String getDescription() {
        return "показывает подробное содержимое всех элементов коллекции";
    }
}