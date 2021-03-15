package Compiler.Model.Elements;

import Compiler.Model.Connections.ConnectionPointModel;
import Compiler.Model.ValidationError;

import java.util.ArrayList;

public class ThreadElement extends AbstractElement {

    /**
     * Class will hold custom logic used to validate its implementation
     */
    public ThreadElement() {
        super("||", -1, -1);
    }


    protected ArrayList<ValidationError> validateConnections() {
        ArrayList<ValidationError> errors = new ArrayList<>();

        boolean foundInConnection = false;
        for (ConnectionPointModel connectionPoint : this.inConnectionPoints) {
            if (connectionPoint.getConnectsTo() != null) {
                foundInConnection = true;
                break;
            }
        }

        boolean foundOutConnection = false;
        for (ConnectionPointModel connectionPoint : this.outConnectionPoints) {
            if (connectionPoint.getConnectsTo() != null) {
                foundOutConnection = true;
                break;
            }
        }


        if (!foundInConnection) {
            errors.add(new ValidationError(this, "Missing Out Connections"));
        }

        if (!foundOutConnection) {
            errors.add(new ValidationError(this, "Missing Out Connections"));
        }

        return errors;
    }
}
