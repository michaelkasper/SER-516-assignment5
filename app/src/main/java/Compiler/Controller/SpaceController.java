package Compiler.Controller;

import Compiler.Model.Elements.AbstractElement;
import Compiler.Model.SpaceModel;
import Compiler.View.Space;

import java.awt.event.ActionEvent;


public class SpaceController extends AbstractController {

    private SpaceModel spaceModel;
    private Space spaceView;

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
                //TODO: trigger graph to update with new element

            }
        });

    }


}
