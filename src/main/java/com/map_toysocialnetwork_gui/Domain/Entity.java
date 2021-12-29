package com.map_toysocialnetwork_gui.Domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Template class defining an entity correlated with an id.
 * @param <ID> type that defines the id of the entity.
 */
public class Entity<ID> implements Serializable {

    @Serial
    private static final long serialVersionUID = 7331115341259248461L;
    private ID id;

    /**
     * Gets the id of the entity.
     * @return object of type ID, representing the id of the entity.
     */
    public ID getId() {
        return id;
    }

    /**
     * Sets the id of the entity.
     * pre: id must be unique
     * @param id object of type ID that is going to be the id of the entity.
     */
    public void setId(ID id) {
        this.id = id;
    }

    /**
     * Determines if two entities are equal.
     * @param o object to be compared with.
     * @return true if they are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity<?> entity = (Entity<?>) o;
        return Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
