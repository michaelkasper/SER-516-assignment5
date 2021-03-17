package Compiler.Model.Elements;

import org.json.simple.JSONObject;

public class CloseIfElement extends AbstractElement {

    public CloseIfElement() {
        super(")", 1, 0);
    }

    public CloseIfElement(JSONObject data) {
        super(data);
    }
}
