package Compiler.Model.Elements;

import org.json.simple.JSONObject;

public class MethodStartElement extends AbstractElement {

    /**
     * Class will hold custom logic used to validate its implementation
     */
    public MethodStartElement() {
        super("<", 1, 2);
    }

    public MethodStartElement(JSONObject data) {
        super(data);
    }
}
