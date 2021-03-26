package Compiler.Model.Elements;


import org.json.simple.JSONObject;

public class LoopElement extends AbstractElement {

    public LoopElement() {
        super("@", 2, 2);
    }

    public LoopElement(JSONObject data) {
        super(data);
    }


    public boolean verifyNoLoop(AbstractElement toElement) {
//        ConnectionPointModel loopInputPoint = this.getInConnectionPoints().get(0);
//
//        if (loopInputPoint.getConnectsTo() != null) {
//            if (loopInputPoint.getConnectsTo().getElementModel().equals(toPoint.getElementModel())) {
//                return false;
//            } else {
//                return loopInputPoint.getConnectsTo().getElementModel().verifyNoLoop(toPoint);
//            }
//        }

        return true;
    }
}
