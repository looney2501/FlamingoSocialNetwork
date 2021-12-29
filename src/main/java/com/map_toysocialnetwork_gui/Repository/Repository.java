package com.map_toysocialnetwork_gui.Repository;

import com.map_toysocialnetwork_gui.Domain.Entity;
import com.map_toysocialnetwork_gui.Repository.RepositoryExceptions.RepositoryException;

/**
 * CRUD operations repository.
 * @param <ID> type of the id that the objects in the repository have.
 * @param <E> type of the objects in the repository.
 */
public interface Repository<ID, E extends Entity<ID>> {
    /**
     * Finds the entity with the given id.
     * @param id The id of the entity to be returned. Must not be null.
     * @return The entity with the specified id or null if there is no entity with the given id.
     * @throws RepositoryException if id is null.
     */
    E findOne(ID id) throws RepositoryException;

    /**
     * Get all enitities from the repository.
     * @return an iterable object containing all entities from the repository.
     */
    Iterable<E> findAll();

    /**
     * Saves a given entity into the repository.
     * @param entity Entity to be saved into the repository. Must not be null.
     * @return null if the given entity is saved. Otherwise returns the entity.
     * @throws RepositoryException if the entity is null.
     */
    E save(E entity) throws RepositoryException;

    /**
     * Deletes the entity with the given id.
     * @param id ID of the entity to be removed. Must not be null.
     * @return null if there is no entity with the given id, otherwise return the entity.
     * @throws RepositoryException if ID is null
     */
    E delete(ID id) throws RepositoryException;

    /**
     * Updates an Entity from the main.com.map_toysocialnetwork_gui.com.map_toysocialnetwork_gui.Domain.com.map_toysocialnetwork_gui.Repository.
     * @param entity Updated entity; must be valid.
     * @return old entity or null if entity does not exist.
     * @throws RepositoryException if entity is null.
     */
    E update(E entity);
}
