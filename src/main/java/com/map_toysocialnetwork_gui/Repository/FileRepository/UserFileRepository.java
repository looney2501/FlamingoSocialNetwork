package com.map_toysocialnetwork_gui.Repository.FileRepository;

import com.map_toysocialnetwork_gui.Domain.User;

import java.util.List;

/**
 * Defines a repository that stores User type objects into csv file.
 */
public class UserFileRepository extends AbstractFileRepository<String, User> {

    /**
     * Constructor for a User file repository.
     *
     * @param fileName name and path of the file in which the entities are stored.
     */
    public UserFileRepository(String fileName) {
        super(fileName);
    }

    /**
     * Method that generates the user object from its attributes given as Strings.
     * @param attributes list of Strings representing the entity attributes.
     * @return User with the attributes matching the ones in the Strings given.
     */
    @Override
    protected User extractEntity(List<String> attributes) {
        User u = new User(attributes.get(1), attributes.get(2));
        u.setId(attributes.get(0));
        return u;
    }

    /**
     * Generates a String representing the user's attributes.
     * @param entity User object to be parsed as String.
     * @return String representing the User.
     */
    @Override
    protected String createEntityAsString(User entity) {
        return entity.getId() + ";" + entity.getFirstName() + ";" + entity.getLastName();
    }
}
