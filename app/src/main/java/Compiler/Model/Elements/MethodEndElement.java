package Compiler.Model.Elements;

import org.json.simple.JSONObject;

import java.awt.*;

public class MethodEndElement extends AbstractElement {

    public MethodEndElement() {
        super(")", 1, 0);
    }

    public MethodEndElement(JSONObject data) {
        super(data);
    }

    public MethodEndElement(Point position) {
        this();
        this.setPosition(position);
    }
}
