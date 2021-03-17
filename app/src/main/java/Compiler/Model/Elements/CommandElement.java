package Compiler.Model.Elements;

import org.json.simple.JSONObject;

public class CommandElement extends AbstractElement {

    /**
     * Class will hold custom logic used to validate its implementation
     */
    public CommandElement() {
        super("-", 1, 1);
    }

    public CommandElement(JSONObject data) {
        super(data);
    }
}
