package com.map_toysocialnetwork_gui.Domain.Validators;

import com.map_toysocialnetwork_gui.Domain.Factory.UserFactory;
import com.map_toysocialnetwork_gui.Domain.User;
import com.map_toysocialnetwork_gui.Domain.Validators.ValidatorExceptions.ValidatorException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    @Test
    void validate() {
        UserFactory userFactory = UserFactory.getInstance();
        User u1 = userFactory.createObject("user1", "Ion", "Pop");
        UserValidator validator = new UserValidator();
        try {
            validator.validate(u1);
            assertTrue(true);
        }
        catch (ValidatorException e) {
            fail();
        }
        u1.setId(null);
        try {
            validator.validate(u1);
            fail();
        }
        catch (ValidatorException e) {
            assertEquals(e.getMessage(), "Invalid username!");
        }
        u1.setId("user1");
        u1.setFirstName("   Miron");
        try {
            validator.validate(u1);
            fail();
        }
        catch (ValidatorException e) {
            assertEquals(e.getMessage(), "Invalid name format!");
        }
    }
}