package Compiler.Model;

import Compiler.Model.Elements.AbstractElement;

public class ValidationError {
    private AbstractElement abstractElement;
    private String errMessage;

    public ValidationError(AbstractElement abstractElement, String errMessage) {
        this.abstractElement = abstractElement;
        this.errMessage = errMessage;
    }
}
