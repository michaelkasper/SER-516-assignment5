package Compiler.Model.Elements;

import org.json.simple.JSONObject;

import java.awt.*;

public class MethodStartElement extends AbstractElement {

    public MethodStartElement() {
        super("(", 0, 1);
    }

    public MethodStartElement(JSONObject data) {
        super(data);
    }

    public MethodStartElement(Point position) {
        this();
        this.setPosition(position);
    }
}
