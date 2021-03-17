package Compiler.Model.Elements;

import org.json.simple.JSONObject;

public class CloseIfElement extends AbstractElement {

    /**
     * Class will hold custom logic used to validate its implementation
     */
    public CloseIfElement() {
        super(")", 1, 0);
    }

    public CloseIfElement(JSONObject data) {
        super(data);
    }
}
