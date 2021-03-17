package Compiler.Model.Elements;

import org.json.simple.JSONObject;

public class MethodEndElement extends AbstractElement {

    public MethodEndElement() {
        super(">", 2, 1);
    }

    public MethodEndElement(JSONObject data) {
        super(data);
    }
}
