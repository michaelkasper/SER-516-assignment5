package Compiler.Controller;

import Compiler.Model.Elements.AbstractElement;
import Compiler.Service.PropertyChangeDecorator;

public class ElementController extends PropertyChangeDecorator {

    private AbstractElement elementModel;

    //TODO: Listen for double click, trigger popup for edit
    //TODO: Listen for popup buttons and save results
    public ElementController(AbstractElement elementModel) {
        this.elementModel = elementModel;

    }
    
    public AbstractElement getElementModel() {
        return elementModel;
    }

}
