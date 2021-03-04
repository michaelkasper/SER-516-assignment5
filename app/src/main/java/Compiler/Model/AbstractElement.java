package Compiler.Model;

import Compiler.View.Components.Element;

public abstract class AbstractElement {
    protected final Element elementView;

    public AbstractElement() {
        this.elementView = new Element();
    }
}
