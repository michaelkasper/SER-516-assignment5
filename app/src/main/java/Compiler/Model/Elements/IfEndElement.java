package Compiler.Model.Elements;

import org.json.simple.JSONObject;

public class IfEndElement extends AbstractElement {

    public IfEndElement() {
        super("<", 1, 2);
    }

    public IfEndElement(JSONObject data) {
        super(data);
    }
}
