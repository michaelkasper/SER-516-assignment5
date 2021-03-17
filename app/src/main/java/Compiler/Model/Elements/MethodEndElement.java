package Compiler.Model.Elements;

import org.json.simple.JSONObject;

public class MethodEndElement extends AbstractElement {

    /**
     * Class will hold custom logic used to validate its implementation
     */
    public MethodEndElement() {
        super(">", 2, 1);
    }

    public MethodEndElement(JSONObject data) {
        super(data);
    }
}
