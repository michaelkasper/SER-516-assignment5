package Compiler.Model.Elements;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class ThreadCloseElement extends AbstractElement {

    public ThreadCloseElement() {
        super("|-", -1, 1);
    }

    public ThreadCloseElement(JSONObject data) {
        super(data);
    }

    public boolean hasOpenInConnections() {
        return true;
    }

    public void addFromConnections(AbstractElement fromElement) {
        super.addFromConnections(fromElement);
        this.inCount++;
    }

    protected ArrayList<String> validateCompileConnections() {
        ArrayList<String> errors = new ArrayList<>();

        if (this.getToConnections().size() == 0) {
            errors.add("Missing In Connections");
        }

        if (this.getFromConnections().size() == 0) {
            errors.add("Missing Out Connection");
        }

        return errors;
    }

}
