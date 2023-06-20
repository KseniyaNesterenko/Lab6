package commands;

import collection.CollectionManager;
import exceptions.CannotExecuteCommandException;
import java.io.PrintStream;
/**
 * Класс команды, которая выводит средний возраст всех элементов коллекции.
 */
public class AverageAgeCommand extends Command {

    private CollectionManager collectionManager;

    /**
     * Конструктор класса.
     */
    public AverageAgeCommand() {
        super("average_of_age");
    }

    /**
     * Конструктор класса для реализации команды, работающей с менеджером команд.
     *
     * @param collectionManager менеджер коллекции.
     */
    public AverageAgeCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Метод команды. Выводит средний возраст всех элементов коллекции.
     *
     * @param arguments      аргументы команды.
     * @param invocationEnum режим работы команды
     * @param printStream поток, куда следует выводит результат команды
     */
    @Override
    public void execute(String[] arguments, Status invocationEnum, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationEnum.equals(Status.CLIENT)) {
            if (arguments.length > 0) {
                throw new CannotExecuteCommandException("У данной команды не может быть аргументов");
            }
        } else if (invocationEnum.equals(Status.SERVER)) {
            printStream.printf("Средний возраст всех элементов коллекции: %.2f", collectionManager.averageAge());
        }
    }

    /**
     * Метод, возвращающий описание команды.
     * @return Описание команды.
     *
     * @see HelpCommand
     */
    @Override
    public String getDescription() {
        return "выводит средний возраст элементов из коллекции";
    }
}
