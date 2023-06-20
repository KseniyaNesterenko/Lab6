package commands;

import collection.CollectionManager;
import exceptions.CannotExecuteCommandException;

import java.io.PrintStream;

public class MaxElementByAgeCommand extends Command {
    /**
     * Поле, хранящее ссылку на объект класса CollectionManager.
     */

    private CollectionManager collectionManager;

    /**
     * Конструктор класса.
     */
    public MaxElementByAgeCommand() {
        super("max_by_age");
    }

    public MaxElementByAgeCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Метод, исполняющий команду. В случае успешного выполнения высветится элемент коллекции, значение поля age которого максимально, иначе предупреждение.
     *
     * @param Status режим, с которым должна быть исполнена данная команда.
     * @param printStream поток вывода.
     * @param arguments аргументы команды.
     */
    @Override
    public void execute(String[] arguments, Status Status, PrintStream printStream) throws CannotExecuteCommandException {
        if (Status.equals(Status.CLIENT)) {
            if (arguments.length > 0) {
                throw new CannotExecuteCommandException("Введены неверные аргументы команды. ");
            }
        } else if (Status.equals(Status.SERVER)) {
            if (collectionManager.maxAge() == null) printStream.println("Коллекция пуста.");
            else printStream.println(collectionManager.maxAge().toString());
        }
    }

    /**
     * Метод, возвращающий описание команды.
     *
     * @return Возвращает описание команды.
     * @see Command
     */
    @Override
    public String getDescription() {
        return "выводит информацию об элементе коллекции, с максимальным возрастом";
    }
}
