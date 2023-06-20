package commands;

import collection.CollectionManager;
import exceptions.CannotExecuteCommandException;

import java.io.PrintStream;

public class SaveCommand extends Command {
    private final String inputFile;
    private final CollectionManager collectionManager;
    public SaveCommand(CollectionManager collectionManager, String inputFile) {
        this.collectionManager = collectionManager;
        this.inputFile = inputFile;
    }

    @Override
    public void execute(String[] arguments, Status invocationEnum, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationEnum.equals(Status.CLIENT)) {
            throw new CannotExecuteCommandException("У данной команды для клиента нет выполнения.");
        } else if (invocationEnum.equals(Status.SERVER)) {
            collectionManager.save(inputFile);
            printStream.println("Коллекция " + collectionManager.getClass().getSimpleName() + " была сохранена.");
        }
    }

    @Override
    public String getDescription() {
        return "сохраняет коллекцию в указанный файл";
    }
}