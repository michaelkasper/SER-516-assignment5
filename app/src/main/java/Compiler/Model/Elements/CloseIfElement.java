package Compiler.Model.Elements;

import org.json.simple.JSONObject;

import java.awt.*;

public class CloseIfElement extends AbstractElement {

    public CloseIfElement() {
        super(")", 1, 0);
    }

    public CloseIfElement(Point position) {
        this();
        this.setPosition(position);
    }

    public CloseIfElement(JSONObject data) {
        super(data);
    }
}
