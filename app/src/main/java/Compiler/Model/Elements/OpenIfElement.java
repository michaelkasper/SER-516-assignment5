package Compiler.Model.Elements;

import org.json.simple.JSONObject;

import java.awt.*;

public class OpenIfElement extends AbstractElement {

    public OpenIfElement() {
        super("(", 0, 1);
    }

    public OpenIfElement(Point position) {
        this();
        this.setPosition(position);
    }

    public OpenIfElement(JSONObject data) {
        super(data);
    }
}
