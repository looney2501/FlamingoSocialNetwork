package com.map_toysocialnetwork_gui.Repository.FileRepository;

import com.map_toysocialnetwork_gui.Domain.Entity;
import com.map_toysocialnetwork_gui.Repository.Repository;
import com.map_toysocialnetwork_gui.Repository.RepositoryExceptions.RepositoryException;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Defines an abstract repository that stores data in a CSV file.
 * @param <ID> type of the id that the objects in the repository have.
 * @param <E> type of the objects in the repository.
 */
public abstract class AbstractFileRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
    private Map<ID,E> entities;
    private String fileName;

    /**
     * Constructor for an abstract file repository.
     * @param fileName name and path of the file in which the entities are stored.
     */
    public AbstractFileRepository(String fileName) {
        this.fileName = fileName;
        entities = new HashMap<>();
        loadData();
    }

    /**
     * Loads data from the aggregated file into memory.
     */
    private void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while((line = br.readLine()) != null) {
                List<String> attributes = Arrays.asList(line.split(";"));
                E entity = extractEntity(attributes);
                entities.put(entity.getId(), entity);
            }
        } catch (IOException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    /**
     * Abstract method that generates the entity object from its attributes given as Strings.
     * @param attributes list of Strings representing the entity attributes.
     * @return entity with the attributes matching the ones in the Strings given.
     */
    protected abstract E extractEntity(List<String> attributes);

    /**
     * Finds the entity with the given id.
     * @param id The id of the entity to be returned. Must not be null.
     * @return The entity with the specified id or null if there is no entity with the given id.
     * @throws RepositoryException if id is null.
     */
    @Override
    public E findOne(ID id) throws RepositoryException {
        if (id == null) {
            throw new RepositoryException("ID must not be null!");
        }
        return entities.get(id);
    }

    /**
     * Get all entities from the repository.
     * @return an iterable object containing all entities from the repository.
     */
    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    /**
     * Saves a given entity into the repository.
     * @param entity Entity to be saved into the repository. Must not be null.
     * @return null if the given entity is saved. Otherwise, returns the entity.
     * @throws RepositoryException if the entity is null.
     */
    @Override
    public E save(E entity) throws RepositoryException {
        if (entity==null) {
            throw new RepositoryException("Entity must not be null!");
        }
        if (entities.get(entity.getId()) != null) {
            return entity;
        }
        entities.put(entity.getId(), entity);
        writeOneToFile(entity);
        return null;
    }

    /**
     * Saves the given entity in the file aggregated by the repository.
     * @param entity object of type entity to be written to file.
     */
    private void writeOneToFile(E entity) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write(createEntityAsString(entity));
            bw.newLine();
        } catch (IOException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    /**
     * Saves all entities in the file aggregated by the repository.
     */
    private void writeAllToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, false))) {
            for (E entity :
                    entities.values()) {
                bw.write(createEntityAsString(entity));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    /**
     * Generates a String representing the Entity's attributes.
     * @param entity object to be parsed as String.
     * @return String representing the Entity.
     */
    protected abstract String createEntityAsString(E entity);

    /**
     * Deletes the entity with the given id.
     * @param id ID of the entity to be removed. Must not be null.
     * @return null if there is no entity with the given id, otherwise return the entity.
     * @throws RepositoryException if ID is null
     */
    @Override
    public E delete(ID id) throws RepositoryException {
        if (id == null) {
            throw new RepositoryException("ID must not be null!");
        }
        E deletedEntity = entities.remove(id);
        if (deletedEntity != null) {
            writeAllToFile();
        }
        return deletedEntity;
    }

    @Override
    public E update(E entity) {
        return null;
    }
}
