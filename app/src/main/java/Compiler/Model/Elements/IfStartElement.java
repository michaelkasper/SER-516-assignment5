package Compiler.Model.Elements;

import org.json.simple.JSONObject;

public class IfStartElement extends AbstractElement {

    public IfStartElement() {
        super("<", 1, 2);
    }

    public IfStartElement(JSONObject data) {
        super(data);
    }
}
