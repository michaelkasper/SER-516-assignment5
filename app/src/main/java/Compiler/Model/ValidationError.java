package Compiler.Model;

import Compiler.Model.Elements.AbstractElement;

public class ValidationError {
    private AbstractElement abstractElement;
    private SpaceModel spaceModel;
    private String errMessage;

    public ValidationError(AbstractElement abstractElement, String errMessage) {
        this.abstractElement = abstractElement;
        this.errMessage = errMessage;
    }

    public ValidationError(SpaceModel spaceModel, String errMessage) {
        this.spaceModel = spaceModel;
        this.errMessage = errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }
}

