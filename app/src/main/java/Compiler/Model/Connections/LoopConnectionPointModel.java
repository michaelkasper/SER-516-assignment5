package Compiler.Model.Connections;

import Compiler.Model.Elements.AbstractElement;
import org.json.simple.JSONObject;

public class LoopConnectionPointModel extends ConnectionPointModel {

    public LoopConnectionPointModel() {
        super();
    }

    public LoopConnectionPointModel(JSONObject data) {
        super(data);
    }

    public LoopConnectionPointModel(Type type, AbstractElement abstractElement) {
        super(type, abstractElement);
    }
}
