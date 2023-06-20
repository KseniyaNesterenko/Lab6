package commands;
import java.io.Serializable;
import java.util.ArrayList;

public class CommandContainer implements Serializable {
    private final String name;

    private final ArrayList<Object> result;

    public CommandContainer(String name, ArrayList<Object> result) {
        this.name = name;
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Object> getResult() {
        return result;
    }
}