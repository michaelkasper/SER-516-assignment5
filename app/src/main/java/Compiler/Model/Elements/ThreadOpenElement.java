package Compiler.Model.Elements;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class ThreadOpenElement extends AbstractElement {

    public ThreadOpenElement() {
        super("-|", 1, -1);
    }

    public ThreadOpenElement(JSONObject data) {
        super(data);
    }

    protected ArrayList<String> validateCompileConnections() {
        ArrayList<String> errors = new ArrayList<>();

        if (this.getToConnections().size() == 0) {
            errors.add("Missing In Connection");
        }

        if (this.getFromConnections().size() == 0) {
            errors.add("Missing Out Connections");
        }

        return errors;
    }

    public boolean hasOpenOutConnections() {
        return true;
    }

    public void addToConnections(AbstractElement toElement) {
        super.addToConnections(toElement);
        this.outCount++;
    }
}
