package Compiler.Model;

import Compiler.Model.Elements.AbstractElement;

public class Connection {
    public AbstractElement fromElement;
    public AbstractElement toElement;

    public Connection(AbstractElement fromElement, AbstractElement toElement) {
        this.fromElement = fromElement;
        this.toElement = toElement;
    }
}
