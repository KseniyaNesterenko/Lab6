package commands;

import collection.CollectionManager;
import exceptions.CannotExecuteCommandException;
import exceptions.RecursiveCallException;
import file.DragonFieldsReader;
import io.UserIO;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ExecuteScriptCommand extends Command {

    private CollectionManager collectionManager;

    private UserIO userIO;

    private DragonFieldsReader dragonFieldsReader;

    private String scriptPath;

    private Script script;

    public ExecuteScriptCommand(UserIO userIO, DragonFieldsReader dragonFieldsReader, Script script) {
        super("execute_script");
        this.userIO = userIO;
        this.dragonFieldsReader = dragonFieldsReader;
        this.script = script;
    }
    public ExecuteScriptCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }
    @Override
    public void execute(String[] arguments, Status invocationEnum, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationEnum.equals(Status.CLIENT)) {
            result = new ArrayList<>();
            try {
                if (arguments.length == 1) {
                    scriptPath = arguments[0].trim();
                    if (script.scriptPaths.contains(scriptPath)) throw new RecursiveCallException();
                    else script.putScript(scriptPath);
                } else throw new IllegalArgumentException();
                File ioFile = new File(scriptPath);
                Scanner scriptScanner = new Scanner(ioFile);
                if (!ioFile.canWrite() || ioFile.isDirectory() || !ioFile.isFile()) throw new IOException();
                Scanner tmpScanner = UserIO.getScanner();
                UserIO.setScanner(scriptScanner);
                userIO = new UserIO(scriptScanner);
                CommandInvoker commandInvoker = new CommandInvoker(userIO, dragonFieldsReader, script);
                super.result.add(scriptPath);
                PrintStream nullStream = (new PrintStream(new OutputStream() {
                    public void write(int b) {
                    }
                }));
                while (scriptScanner.hasNext()) {
                    if (commandInvoker.executeClient(scriptScanner.nextLine(), nullStream)) {
                        super.result.add(commandInvoker.getLastCommandContainer());
                    }
                }
                script.removeScript(scriptPath);
                return;
                //UserIO.setScanner(tmpScanner);
            } catch (FileNotFoundException ex) {
                printStream.println("Файл скрипта не найден");
            } catch (NullPointerException ex) {
                printStream.println("Не выбран файл, из которого читать скрипт");
            } catch (IOException ex) {
                printStream.println("Доступ к файлу невозможен");
            } catch (IllegalArgumentException ex) {
                printStream.println("скрипт не передан в качестве аргумента команды, либо кол-во агрументов больше 1");
            } catch (RecursiveCallException ex) {
                printStream.println("Скрипт " + scriptPath + " уже существует (Рекурсивный вызов)");
            }
            script.removeScript(scriptPath);
            throw new CannotExecuteCommandException("Принудительное завершение работы команды execute_script");
        } else if (invocationEnum.equals(Status.SERVER)) {
            printStream.println("Файл, который исполняется скриптом: " + this.getResult().get(0));
            Object[] arr = result.toArray();
            arr = Arrays.copyOfRange(arr, 1, arr.length);
            CommandContainer[] containerArray = Arrays.copyOf(arr, arr.length, CommandContainer[].class);

            CommandInvoker commandInvoker = new CommandInvoker(collectionManager);
            for (CommandContainer command : containerArray) {
                commandInvoker.executeServer(command.getName(), command.getResult(), printStream);
            }
        }
    }

    @Override
    public String getDescription() {
        return "выполняет команды, описанные в скрипте";
    }

    static class Script {

        private final ArrayList<String> scriptPaths = new ArrayList<>();

        /**
         * Метод, добавляющий скрипт в коллекцию.
         *
         * @param scriptPath адрес скрипта, требующий добавляения в коллекцию.
         */
        public void putScript(String scriptPath) {
            scriptPaths.add(scriptPath);
        }

        /**
         * Метод, убирающий скрипт из коллекции.
         *
         * @param scriptPath адрес скрипта, требующий удаления из коллекции.
         */
        public void removeScript(String scriptPath) {
            scriptPaths.remove(scriptPath);
        }
    }
}
