package commands;

import collection.CollectionManager;
import exceptions.CannotExecuteCommandException;
import exceptions.CannotExecuteCommandException;;
import file.DragonFieldsReader;
import io.UserIO;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

public class CommandInvoker {

    private HashMap<String, Command> clientCommands;

    private HashMap<String, Command> serverCommands;

    private CollectionManager collectionManager;

    private DragonFieldsReader dragonFieldsReader;

    ExecuteScriptCommand.Script script;

    private UserIO userIO;

    private CommandContainer lastCommandContainer;

    private String inputFile;

    public CommandInvoker(UserIO userIO) {
        this.clientCommands = new HashMap<>();
        this.userIO = userIO;
        this.dragonFieldsReader = new DragonFieldsReader(userIO);

        this.script = new ExecuteScriptCommand.Script();
        this.putClientCommands();
        System.out.println("Элементы коллекции для клиента были загружены.");
    }

    public CommandInvoker(UserIO userIO, DragonFieldsReader dragonFieldsReader, ExecuteScriptCommand.Script script) {
        this.clientCommands = new HashMap<>();

        this.userIO = userIO;
        this.dragonFieldsReader = dragonFieldsReader;
        this.script = script;

        this.putClientCommands();
    }

    public CommandInvoker(CollectionManager collectionManager, String inputFile) {
        this.serverCommands = new HashMap<>();

        this.collectionManager = collectionManager;

        this.inputFile = inputFile;

        script = new ExecuteScriptCommand.Script();

        this.putServerCommands();
        System.out.println("Элементы коллекции для сервера были загружены");
    }

    public CommandInvoker(CollectionManager collectionManager) {
        this.serverCommands = new HashMap<>();

        this.collectionManager = collectionManager;

        this.putServerCommands();
    }

    private void putClientCommands() {
        clientCommands.put("info", new InfoCommand());
        clientCommands.put("show", new ShowCommand());
        clientCommands.put("clear", new ClearCommand());
        clientCommands.put("exit", new ExitCommand());
        clientCommands.put("average_of_age", new AverageAgeCommand());
        clientCommands.put("max_by_age", new MaxElementByAgeCommand());
        clientCommands.put("help", new HelpCommand(clientCommands));

        clientCommands.put("insert", new InsertElementCommand(dragonFieldsReader));
        clientCommands.put("update", new UpdateElementCommand(userIO));
        clientCommands.put("remove_key", new RemoveElementCommand());
        clientCommands.put("execute_script", new ExecuteScriptCommand(userIO, dragonFieldsReader, script));
        clientCommands.put("replace_if_greater", new ReplaceIfGreaterCommand(userIO));
        clientCommands.put("replace_if_lower", new ReplaceIfLowerCommand(userIO));
        clientCommands.put("remove_lower_key", new RemoveLowerIdCommand());
        clientCommands.put("remove_any_by_color", new RemoveAnyByColor());
    }

    private void putServerCommands() {
        serverCommands.put("info", new InfoCommand(collectionManager));//y
        serverCommands.put("show", new ShowCommand(collectionManager));//y
        serverCommands.put("clear", new ClearCommand(collectionManager));//y
        serverCommands.put("save", new SaveCommand(collectionManager, inputFile));//y
        serverCommands.put("average_of_age", new AverageAgeCommand(collectionManager));//y
        serverCommands.put("max_by_age", new MaxElementByAgeCommand(collectionManager));//y
        serverCommands.put("help", new HelpCommand(serverCommands));//y

        serverCommands.put("insert", new InsertElementCommand(collectionManager));//y
        serverCommands.put("update", new UpdateElementCommand(collectionManager));//y
        serverCommands.put("remove_key", new RemoveElementCommand(collectionManager));//y
        serverCommands.put("replace_if_greater", new ReplaceIfGreaterCommand(collectionManager));//y
        serverCommands.put("replace_if_lower", new ReplaceIfLowerCommand(collectionManager));//y
        serverCommands.put("remove_lower_key", new RemoveLowerIdCommand(collectionManager));
        serverCommands.put("remove_any_by_color", new RemoveAnyByColor(collectionManager));
        serverCommands.put("execute_script", new ExecuteScriptCommand(collectionManager));
    }

    public boolean executeClient(String firstCommandLine, PrintStream printStream) {

        String[] words = firstCommandLine.trim().split("\\s+");
        String[] arguments = Arrays.copyOfRange(words, 1, words.length);

        try {
            if (clientCommands.containsKey(words[0].toLowerCase(Locale.ROOT))) {
                Command command;
                command = clientCommands.get(words[0].toLowerCase(Locale.ROOT));

                command.execute(arguments, Status.CLIENT, printStream);
                lastCommandContainer = new CommandContainer(command.getName(), command.getResult());
                return true;
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } catch (CannotExecuteCommandException ex) {
            printStream.println(ex.getMessage());
        }
        return false;
    }

    public boolean executeServer(String firstCommandLine, ArrayList<Object> result, PrintStream printStream) {

        String[] words = firstCommandLine.trim().split("\\s+");
        String[] arguments = Arrays.copyOfRange(words, 1, words.length);
        try {
            if (serverCommands.containsKey(words[0].toLowerCase(Locale.ROOT))) {
                Command command;
                command = serverCommands.get(words[0].toLowerCase(Locale.ROOT));

                command.setResult(result);
                command.execute(arguments, Status.SERVER, printStream);
                return true;
            }
        } catch (NullPointerException ex) {
            System.out.println("Команда " + words[0] + " не распознана, для получения справки введите команду help");
            ex.printStackTrace();
        } catch (CannotExecuteCommandException ex) {
            printStream.println(ex.getMessage());
        }
        return false;
    }

    public CommandContainer getLastCommandContainer() {
        return lastCommandContainer;
    }
}
