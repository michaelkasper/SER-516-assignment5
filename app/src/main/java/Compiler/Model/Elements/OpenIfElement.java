package Compiler.Model.Elements;

import org.json.simple.JSONObject;

public class OpenIfElement extends AbstractElement {

    public OpenIfElement() {
        super("(", 0, 1);
    }

    public OpenIfElement(JSONObject data) {
        super(data);
    }
}
