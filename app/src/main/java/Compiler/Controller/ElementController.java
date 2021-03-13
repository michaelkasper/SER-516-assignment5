package Compiler.Controller;

import Compiler.Model.Elements.AbstractElement;
import Compiler.View.Components.Element;
import Compiler.Service.PropertyChangeDecorator;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class ElementController extends PropertyChangeDecorator {

    private AbstractElement elementModel;
    private Element elementView;

    //TODO: Listen for double click, trigger popup for edit
    //TODO: Listen for popup buttons and save results
    public ElementController(Element elementView, AbstractElement elementModel) {
        this.elementView = elementView;
        this.elementModel = elementModel;

        this.registerListeners();
    }


    private void registerListeners() {
        // listen for double click on
    }

}
