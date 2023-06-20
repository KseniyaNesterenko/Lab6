package commands;
import exceptions.CannotExecuteCommandException;
import exceptions.CannotExecuteCommandException;;

import java.io.PrintStream;
import java.util.ArrayList;

public abstract class Command {
    private String name;
    protected ArrayList<Object> result;

    protected Command(String name) {
        this.name = name;
    }

    protected Command() {
    }

    public abstract void execute(String[] arguments, Status statusEnum, PrintStream printStream)
            throws CannotExecuteCommandException;

    public String getName() {
        return name;
    }


    public ArrayList<Object> getResult() {
        return result;
    }

    public void setResult(ArrayList<Object> result) {
        this.result = result;
    }

    public String getDescription() {
        return "команад имеет описание";
    }

    @Override
    public String toString() {
        return "Uмя :" + getName() + "\nСодержимое на исполнение серверу: " + result.stream().toString();
    }
}