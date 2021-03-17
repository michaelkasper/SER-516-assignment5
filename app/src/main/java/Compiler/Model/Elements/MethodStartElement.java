package Compiler.Model.Elements;

import org.json.simple.JSONObject;

public class MethodStartElement extends AbstractElement {

    public MethodStartElement() {
        super("<", 1, 2);
    }

    public MethodStartElement(JSONObject data) {
        super(data);
    }
}
