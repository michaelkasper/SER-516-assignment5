package Compiler.Model.Elements;

import Compiler.Service.Store;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.function.Function;

public class LoopElement extends AbstractElement {

    private final ArrayList<AbstractElement> loopConnections = new ArrayList<>();

    public LoopElement() {
        super("@", 2, 2);
    }

    public LoopElement(JSONObject data) {
        super(data);
    }

    public boolean isAllowedToConnectTo(AbstractElement toElement) {
        boolean isAllowed = super.isAllowedToConnectTo(toElement);

        if (this.toConnections.contains(toElement)) {
            return false;
        }

        return isAllowed;
    }

    public boolean isAllowedToConnectFrom(AbstractElement fromElement) {
        return !this.fromConnections.contains(fromElement);
    }

    public void verifyNoLoop(AbstractElement toElement) throws Exception {
        try {
            super.verifyNoLoop(toElement);
        } catch (Exception e) {
            if (!this.isLoopAllowed()) {
                throw e;
            }
        }
    }

    protected void verifyNoLoop(ArrayList<String> priorConnections) throws Exception {
        for (AbstractElement element : this.getFromConnections()) {
            if (this.isLoopAllowed() || !this.loopConnections.contains(element)) {
                String flag = element.getId();
                if (priorConnections.contains(flag)) {
                    if (!this.isLoopAllowed() && !element.isLoopAllowed()) {
                        throw new Exception("loop found");
                    }
                } else {
                    element.verifyNoLoop(new ArrayList<>(priorConnections) {{
                        add(flag);
                    }});
                }
            }
        }
    }

    public void updateLoopFlag(String flag, Function<AbstractElement, ArrayList<AbstractElement>> getCollection) {
        for (AbstractElement element : getCollection.apply(this)) {
            if (this.getId().equals(flag)) {
                this.loopConnections.add(element);
                return;
            } else {
                element.updateLoopFlag(this.getId(), getCollection);
            }
        }
    }

    public JSONObject export() {
        JSONObject obj = super.export();

        JSONArray loopConnections = new JSONArray();
        for (AbstractElement element : this.loopConnections) {
            loopConnections.add(element.getId());
        }
        obj.put("loopConnections", loopConnections);

        return obj;
    }


    public void importRelationships(JSONObject json) {
        super.importRelationships(json);

        JSONArray loopConnectionsJson = (JSONArray) json.get("loopConnections");
        loopConnectionsJson.forEach(elementId -> {
            this.loopConnections.add(Store.getInstance().getElementById((String) elementId));
        });
    }

    protected boolean isLoopAllowed() {
        return this.loopConnections.size() == 0;
    }

}
