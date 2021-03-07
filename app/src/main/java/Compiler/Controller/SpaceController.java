package Compiler.Controller;

import Compiler.Model.Elements.AbstractElement;
import Compiler.Model.SpaceModel;
import Compiler.View.Components.Element;
import Compiler.View.Space;
import Decorator.PropertyChangeDecorator;

import java.awt.event.ActionEvent;
import java.util.ArrayList;


public class SpaceController extends PropertyChangeDecorator {

    private SpaceModel spaceModel;
    private Space spaceView;
    private ArrayList<Element> elementViews = new ArrayList<>();

    //TODO: Drag and Drop should be setup here, more so the drop
    //TODO: The drop logic would be one listener. The listener would first check to see if the
    // element is in the SpaceModel already, if not, create a new model and pass it to the SpaceModel to be added. After its added or
    // if its already added it would then update its render position

    public SpaceController(Space spaceView, SpaceModel spaceModel) {

        this.spaceView = spaceView;
        this.spaceModel = spaceModel;

        this.registerListeners();
    }

    public void onDrop(ActionEvent e) {

    }

    private void registerListeners() {
        // listen for drop event on Space View

        this.spaceView.setDropListener(this::onDrop);

        this.spaceModel.addPropertyChangeListener(SpaceModel.EVENT_ELEMENT_ADDED, e -> {
            if (e.getNewValue() != null) {
                AbstractElement elementModel = (AbstractElement) e.getNewValue();
                this.elementViews.add(new Element(elementModel));
                //TODO: trigger graph to update with new element
            }
        });

    }


}
