package commands;

import collection.CollectionManager;
import collection.DragonFieldValidation;
import io.UserIO;
import exceptions.CannotExecuteCommandException;
import io.UserIO;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class ReplaceIfLowerCommand extends Command {
    /**
     * Поле, хранящее ссылку на объект класса CollectionManager.
     */
    private CollectionManager collectionManager;
    /**
     * Поле, хранящее ссылку на объект класса UserIO.
     */
    private UserIO userIO;

    /**
     * Конструктор класса.
     *
     * @param userIO чтение-запись с помощью указанного сканнера.
     */
    public ReplaceIfLowerCommand(UserIO userIO) {
        super("replace_if_greater");
        this.userIO = userIO;
    }

    public ReplaceIfLowerCommand(CollectionManager collectionManager) {

        this.collectionManager = collectionManager;
    }

    /**
     * Метод, исполняющий команду. Если поля указанные значения полей меньше, то они изменятся, иначе нет. В случае некорректного ввода высветится предупреждение.
     *
     * @param invocationEnum режим, с которым должна быть исполнена данная команда.
     * @param printStream    поток вывода.
     * @param arguments      аргументы команды.
     */
    @Override
    public void execute(String[] arguments, Status invocationEnum, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationEnum.equals(Status.CLIENT)) {
            result = new ArrayList<>();
            try {
                if (arguments.length != 1) {
                    throw new CannotExecuteCommandException("Количество аргументов данной команды должно равняться 1.");
                }
                if (!DragonFieldValidation.validate("id", arguments[0])) {
                    throw new CannotExecuteCommandException("Введены невалидные аргументы: id " + arguments[0]);
                } else {
                    result.add(arguments[0]);
                    printStream.println(CollectionManager.getFieldNames());
                    printStream.println("\nВыберите поля для изменения:");
                    String[] line;

                    boolean isInputEnd = false;

                    do {
                        line = userIO.readLine().trim().split("\\s+");
                        if (line.length == 0 || line[0] == null || line[0].equals("")) isInputEnd = true;
                        else {
                            if (line.length == 1) {
                                if (DragonFieldValidation.validate(line[0], "")) {
                                    result.add(line[0] + ";");
                                } else printStream.println("Введены некорректные данные: \"" + line[0] + "\" + null");
                            }
                            if (line.length == 2) {
                                if (DragonFieldValidation.validate(line[0], line[1])) {
                                    result.add(line[0] + ";" + line[1]);
                                } else printStream.println("Введены некорректные данные: " + line[0] + " + " + line[1]);
                            }
                        }
                    } while (!isInputEnd);
                }
            } catch (NoSuchElementException ex) {
                throw new CannotExecuteCommandException("Сканнер достиг конца файла.");
            }
        } else if (invocationEnum.equals(Status.SERVER)) {
            String[] spArguments = result.toArray(new String[0]);
            Integer id = Integer.parseInt(spArguments[0]);
            if (collectionManager.containsKey(id)) {
                for (int i = 1; i < spArguments.length; i++) {

                    String[] subStr;
                    String delimeter = ";"; // Разделитель
                    subStr = spArguments[i].split(delimeter);
                    collectionManager.replaceIfLower(id, subStr[0], subStr[1], printStream);
                }
                printStream.println("Указанные поля были заменены.");
            } else {
                printStream.println("Элемента с указанным id не существует");
            }
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
        return "Заменяет поле указанного элемента коллекции (по id) если то больше, чем введенного значения поля";
    }
}
