package Compiler.Model;

import Compiler.Model.Elements.AbstractElement;

public class ConnectionModel {
    public AbstractElement fromElement;
    public AbstractElement toElement;

    public ConnectionModel(AbstractElement fromElement, AbstractElement toElement) {
        this.fromElement = fromElement;
        this.toElement = toElement;
    }
}
