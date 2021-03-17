package Compiler.Model.Elements;

import Compiler.Model.Connections.ConnectionPointModel;
import Compiler.Model.Connections.LoopConnectionPointModel;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class LoopElement extends AbstractElement {

    public LoopElement() {
        super("@", 2, 2);
    }

    public LoopElement(JSONObject data) {
        super(data);
    }

    protected void createConnectionPoints() {
        inConnectionPoints = new ArrayList<>();
        inConnectionPoints.add(new ConnectionPointModel(ConnectionPointModel.Type.IN, this));
        inConnectionPoints.add(new LoopConnectionPointModel(ConnectionPointModel.Type.IN, this));

        outConnectionPoints = new ArrayList<>();
        outConnectionPoints.add(new ConnectionPointModel(ConnectionPointModel.Type.OUT, this));
        outConnectionPoints.add(new LoopConnectionPointModel(ConnectionPointModel.Type.OUT, this));
    }


    public boolean verifyNoLoop(ConnectionPointModel toPoint) {
        ConnectionPointModel loopInputPoint = this.getInConnectionPoints().get(0);

        if (loopInputPoint.getConnectsTo() != null) {
            if (loopInputPoint.getConnectsTo().getElementModel().equals(toPoint.getElementModel())) {
                return false;
            } else {
                return loopInputPoint.getConnectsTo().getElementModel().verifyNoLoop(toPoint);
            }
        }

        return true;
    }
}
