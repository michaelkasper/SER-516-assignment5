package Compiler.Model.Elements;

import org.json.simple.JSONObject;

public class IfStartElement extends AbstractElement {

    public IfStartElement() {
        super(">", 2, 1);
    }

    public IfStartElement(JSONObject data) {
        super(data);
    }
}
