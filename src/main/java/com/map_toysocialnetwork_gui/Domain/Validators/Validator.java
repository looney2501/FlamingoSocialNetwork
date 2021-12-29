package com.map_toysocialnetwork_gui.Domain.Validators;

import com.map_toysocialnetwork_gui.Domain.Entity;
import com.map_toysocialnetwork_gui.Service.ServiceExceptions.ServiceException;

/**
 * Entity validator.
 * @param <E> type of objects to be validated.
 */
public interface Validator<E extends Entity> {
    /**
     * Validates the entity given.
     * @param entity Entity object to be validated.
     * @throws ServiceException if entity is not valid.
     */
    void validate(E entity) throws ServiceException;
}
