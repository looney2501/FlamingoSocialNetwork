package com.map_toysocialnetwork_gui.Domain;

/**
 * Defines the object User that extends the template class Entity.
 */
public class User extends Entity<String> {
    private String firstName;
    private String lastName;
    private String password;

    /**
     * Creates a new user with the first name and last name given.
     * @param firstName String representing the first name of the user.
     * @param lastName String representing the last name of the user.
     */
    public User(String firstName, String lastName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    @Override
    public String toString() {
        return lastName + ' ' + firstName;
    }

    /**
     * Get the first name of the use.
     * @return String representing the first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user with the given value.
     * @param firstName String representing the new value.
     */
    public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

    /**
     Get the last name of the use.
     * @return String representing the last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the first name of the user with the given value.
     * @param lastName String representing the new value.
     */
    public void setLastName(String lastName) {
            this.lastName = lastName;
        }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

