package Compiler.Model;

import java.util.UUID;

public class Space {

    private final UUID id;

    /**
     * TODO: Add ArrayList of AbstractElements
     * TODO: Add way to add to list
     * TODO: Broadcast view update when list updated
     */
    public Space() {
        this.id = UUID.randomUUID();
    }

}
