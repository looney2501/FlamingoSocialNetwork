package com.map_toysocialnetwork_gui.Repository.SQLDataBaseRepository;

import com.map_toysocialnetwork_gui.Domain.Factory.UserFactory;
import com.map_toysocialnetwork_gui.Domain.User;
import com.map_toysocialnetwork_gui.Repository.RepositoryExceptions.RepositoryException;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserDBRepositoryTest {

    @Test
    void findOne() {
        UserDBRepository userDBRepository = new UserDBRepository("jdbc:postgresql://localhost:5432/ToySocialNetworkTests",
                "postgres",
                "postgres");
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ToySocialNetworkTests",
                "postgres",
                "postgres")) {
            PreparedStatement delete_users = connection.prepareStatement
                    ("delete from users");
            delete_users.executeUpdate();
            PreparedStatement delete_friendships = connection.prepareStatement
                    ("delete from friendships");
            delete_friendships.executeUpdate();
            PreparedStatement insert_some_users = connection.prepareStatement
                    ("insert into users values ('user1','Prenume1','Nume1'),('user2','Prenume2','Nume2')");
            insert_some_users.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            userDBRepository.findOne(null);
        }
        catch (RepositoryException e) {
            assertTrue(true);
        }
        assertNull(userDBRepository.findOne("user3"));
        assertEquals(userDBRepository.findOne("user2").getFirstName(), "Prenume2");
    }

    @Test
    void findAll() {
        UserDBRepository userDBRepository = new UserDBRepository("jdbc:postgresql://localhost:5432/ToySocialNetworkTests",
                "postgres",
                "postgres");
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ToySocialNetworkTests",
                "postgres",
                "postgres")) {
            PreparedStatement delete_users = connection.prepareStatement
                    ("delete from users");
            delete_users.executeUpdate();
            PreparedStatement delete_friendships = connection.prepareStatement
                    ("delete from friendships");
            delete_friendships.executeUpdate();
            PreparedStatement insert_some_users = connection.prepareStatement
                    ("insert into users values ('user1','Prenume1','Nume1'),('user2','Prenume2','Nume2')");
            insert_some_users.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        var all = userDBRepository.findAll();
        assertEquals(((Collection<User>) all).size(), 2);
        UserFactory userFactory = UserFactory.getInstance();
        userDBRepository.save(userFactory.createObject("user3", "Prenume3", "Nume3"));
        assertEquals(((Collection<User>) userDBRepository.findAll()).size(), 3);
    }

    @Test
    void save() {
        UserDBRepository userDBRepository = new UserDBRepository("jdbc:postgresql://localhost:5432/ToySocialNetworkTests",
                "postgres",
                "postgres");
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ToySocialNetworkTests",
                "postgres",
                "postgres")) {
            PreparedStatement delete_users = connection.prepareStatement
                    ("delete from users");
            delete_users.executeUpdate();
            PreparedStatement delete_friendships = connection.prepareStatement
                    ("delete from friendships");
            delete_friendships.executeUpdate();
            PreparedStatement insert_some_users = connection.prepareStatement
                    ("insert into users values ('user1','Prenume1','Nume1'),('user2','Prenume2','Nume2')");
            insert_some_users.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        UserFactory userFactory = UserFactory.getInstance();
        try {
            userDBRepository.save(null);
        }
        catch (RepositoryException e) {
            assertTrue(true);
        }
        User existinguser = userFactory.createObject("user1", "aa", "sfsd");
        User newuser = userFactory.createObject("user3", "Prenume3", "Nume3");
        assertNull(userDBRepository.save(newuser));
        assertNotNull(userDBRepository.save(existinguser));
        assertEquals(userDBRepository.findOne("user3").getFirstName(), "Prenume3");
    }

    @Test
    void delete() {
        UserDBRepository userDBRepository = new UserDBRepository("jdbc:postgresql://localhost:5432/ToySocialNetworkTests",
                "postgres",
                "postgres");
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ToySocialNetworkTests",
                "postgres",
                "postgres")) {
            PreparedStatement delete_users = connection.prepareStatement
                    ("delete from users");
            delete_users.executeUpdate();
            PreparedStatement delete_friendships = connection.prepareStatement
                    ("delete from friendships");
            delete_friendships.executeUpdate();
            PreparedStatement insert_some_users = connection.prepareStatement
                    ("insert into users values ('user1','Prenume1','Nume1'),('user2','Prenume2','Nume2')");
            insert_some_users.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            userDBRepository.delete(null);
        }
        catch (RepositoryException e) {
            assertTrue(true);
        }
        assertNull(userDBRepository.delete("user3"));
        UserFactory userFactory = UserFactory.getInstance();
        userDBRepository.save(userFactory.createObject("user3", "asda", "asdasa"));
        assertEquals(userDBRepository.delete("user3").getFirstName(), "asda");
    }

    @Test
    void update() {
    }
}