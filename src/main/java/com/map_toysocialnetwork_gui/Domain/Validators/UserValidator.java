package com.map_toysocialnetwork_gui.Domain.Validators;

import com.map_toysocialnetwork_gui.Domain.User;
import com.map_toysocialnetwork_gui.Domain.Validators.ValidatorExceptions.ValidatorException;
import com.map_toysocialnetwork_gui.Service.ServiceExceptions.ServiceException;


/**
 * Type of validator that validates User type objects.
 */
public class UserValidator implements Validator<User> {
    /**
     * Validates a User type object.
     * @param user User type object
     * @throws ServiceException if the user attributes are not valid.
     */
    @Override
    public void validate(User user) throws ServiceException {
        validateUsername(user.getId());
        validateName(user.getFirstName());
        validateName(user.getLastName());
    }

    /**
     * Validates a name.
     * @param name String representing a name.
     * @throws ValidatorException if the name is not valid.
     */
    public void validateName(String name) {
        if(name == null || !name.matches("^[a-zA-Z]+[ -]?[a-zA-Z]+?$")) {
            throw new ValidatorException("Invalid name format!");
        }
    }

    /**
     * Validates a username.
     * @param username String representing a username.
     * @throws ValidatorException if the name is null.
     */
    public void validateUsername(String username) {
        if(username == null || username.equals("")) {
            throw new ValidatorException("Invalid username!");
        }
    }


}
