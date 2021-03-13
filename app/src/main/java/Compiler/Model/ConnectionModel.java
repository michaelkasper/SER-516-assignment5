package Compiler.Model;

public class ConnectionModel {
    public ConnectionPointModel outPoint;
    public ConnectionPointModel inPoint;

    public ConnectionModel(ConnectionPointModel outPoint, ConnectionPointModel inPoint) {
        this.outPoint = outPoint;
        this.inPoint = inPoint;
    }
}
