package Compiler.Model;

import java.util.Arrays;

public class ConnectionModel {
    public ConnectionPointModel outPoint;
    public ConnectionPointModel inPoint;

    public ConnectionModel() {
    }

    public ConnectionModel(ConnectionPointModel outPoint, ConnectionPointModel inPoint) {
        this.outPoint = outPoint;
        this.inPoint = inPoint;
    }

    public boolean isComplete() {
        return this.outPoint != null && this.inPoint != null;
    }

    public boolean isValid() {

        if (this.outPoint != null && !this.isPointValid(this.outPoint, this.inPoint)) {
            return false;
        }

        if (this.inPoint != null && (!this.isPointValid(this.inPoint, this.outPoint))) {
            return false;
        }

        return this.isComplete() && !this.isLoop();
    }


    private boolean isPointValid(ConnectionPointModel point, ConnectionPointModel otherPoint) {
        if (point.getCurrentConnection() == null) {

            if (otherPoint != null) {
                ConnectionPointModel[] oppositeConnectionPoints = point.getType() == ConnectionPointModel.Type.IN
                        ? point.getElementModel().getOutConnectionPoints()
                        : point.getElementModel().getInConnectionPoints();

                if (Arrays.stream(oppositeConnectionPoints)
                        .filter(oppositeConnectionPoint -> oppositeConnectionPoint.equals(otherPoint))
                        .findFirst()
                        .orElse(null) != null) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean isLoop() {
        //TODO add condition for Loop Element
        return !(this.outPoint.getElementModel().getCallStack().search(this.inPoint.getId()) == -1);
    }

    public ConnectionPointModel getOutPoint() {
        return outPoint;
    }

    public void setOutPoint(ConnectionPointModel outPoint) {
        this.outPoint = outPoint;
    }

    public ConnectionPointModel getInPoint() {
        return inPoint;
    }

    public void setInPoint(ConnectionPointModel inPoint) {
        this.inPoint = inPoint;
    }

    public void accept() {
        this.inPoint.setCurrentConnection(this);
        this.outPoint.setCurrentConnection(this);

        this.inPoint.getElementModel().addConnection(this);
        this.outPoint.getElementModel().addConnection(this);

    }
}
