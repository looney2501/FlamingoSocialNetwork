package com.map_toysocialnetwork_gui.Domain.Factory;

/**
 * Defines the abstract factory operations.
 * @param <E> type of an object to be created by the factory.
 */
public interface Factory<E> {
    /**
     * Create an object of type E from the String attributes given as arguments.
     * @param attributes array of String atttributes.
     * @return object of type E having the attributes given as arguments.
     */
    E createObject(String... attributes);
}