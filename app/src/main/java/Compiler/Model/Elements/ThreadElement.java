package Compiler.Model.Elements;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class ThreadElement extends AbstractElement {

    public ThreadElement() {
        super("||", -1, -1);
    }

    public ThreadElement(JSONObject data) {
        super(data);
    }

    protected ArrayList<String> validateCompileConnections() {
        ArrayList<String> errors = new ArrayList<>();

        if (this.getToConnections().size() == 0) {
            errors.add("Missing Out Connections");
        }

        if (this.getFromConnections().size() == 0) {
            errors.add("Missing Out Connections");
        }

        return errors;
    }
}
