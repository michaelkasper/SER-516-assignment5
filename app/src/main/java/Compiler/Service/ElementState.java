package Compiler.Service;

public enum ElementState {
    DEFAULT,
    SELECTED,
    HIGHLIGHTED;

    public String toString() {
        return this.name();
    }
}
