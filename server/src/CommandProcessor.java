import commands.CommandInvoker;
import commands.CommandContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintStream;

public class CommandProcessor {

    private final CommandInvoker commandInvoker;

    private static final Logger commandLogger = LogManager.getLogger(CommandProcessor.class);

    public CommandProcessor(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
    }

    public void executeCommand(CommandContainer command, PrintStream printStream) {

        if (commandInvoker.executeServer(command.getName(), command.getResult(), printStream)) {
            commandLogger.info("Была исполнена команда " + command.getName());
        } else {
            commandLogger.info("Не была исполнена команда " + command.getName());
        }
    }
}