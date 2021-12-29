package com.map_toysocialnetwork_gui.Repository.FileRepository;

import com.map_toysocialnetwork_gui.Domain.Factory.UserFactory;
import com.map_toysocialnetwork_gui.Domain.User;
import com.map_toysocialnetwork_gui.Repository.RepositoryExceptions.RepositoryException;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserFileRepositoryTest {

    @Test
    void findOne() {
        File file = new File(".\\src\\tests\\main.com.map_toysocialnetwork_gui.Repository\\testUserRepo.csv");
        try {
            assertTrue(file.createNewFile());
            UserFileRepository repo = new UserFileRepository("src\\tests\\main.com.map_toysocialnetwork_gui.Repository\\testUserRepo.csv");
            UserFactory userFactory= UserFactory.getInstance();
            assertNull(repo.save(userFactory.createObject("id1", "Ion", "Pop")));
            try{
                repo.findOne(null);
            }
            catch (RepositoryException e) {
                assertTrue(true);
            }
            User found = repo.findOne("id2");
            assertNull(found);
            assertNull(repo.save(userFactory.createObject("id2", "Ionel", "Popel")));
            assertNull(repo.save(userFactory.createObject("id3", "Ion3", "Pop3")));
            found = repo.findOne("id2");
            assertEquals(found.getId(), "id2");
            assertEquals(found.getFirstName(), "Ionel");
            assertEquals(found.getLastName(), "Popel");
            assertTrue(file.delete());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void findAll() {
        File file = new File(".\\src\\tests\\main.com.map_toysocialnetwork_gui.Repository\\testUserRepo.csv");
        try {
            assertTrue(file.createNewFile());
            UserFileRepository repo = new UserFileRepository("src\\tests\\main.com.map_toysocialnetwork_gui.Repository\\testUserRepo.csv");
            UserFactory userFactory= UserFactory.getInstance();
            assertNull(repo.save(userFactory.createObject("id1", "Ion", "Pop")));
            assertNull(repo.save(userFactory.createObject("id2", "Ionel", "Popel")));
            assertEquals(((Collection<User>) repo.findAll()).size(), 2);
            assertNull(repo.save(userFactory.createObject("id3", "Ion3", "Pop3")));
            assertEquals(((Collection<User>) repo.findAll()).size(), 3);
            assertTrue(file.delete());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void save() {
        File file = new File(".\\src\\tests\\main.com.map_toysocialnetwork_gui.Repository\\testUserRepo.csv");
        try {
            boolean created = file.createNewFile();
            assertTrue(created);
            UserFileRepository repo = new UserFileRepository("src\\tests\\main.com.map_toysocialnetwork_gui.Repository\\testUserRepo.csv");
            UserFactory userFactory= UserFactory.getInstance();
            assertNull(repo.save(userFactory.createObject("id1", "Ion", "Pop")));
            Collection<User> allUsers = (Collection<User>) repo.findAll();
            assertEquals(allUsers.size(), 1);
            BufferedReader br = new BufferedReader(new FileReader("src\\tests\\main.com.map_toysocialnetwork_gui.Repository\\testUserRepo.csv"));
            String line = br.readLine();
            br.close();
            assertNotNull(line);
            String[] attributes = line.split(";");
            assertEquals(attributes[0],"id1");
            assertEquals(attributes[1],"Ion");
            assertEquals(attributes[2],"Pop");
            User existingUser = repo.save(userFactory.createObject("id1", "Ion", "Pop"));
            assertNotNull(existingUser);
            assertEquals(((Collection<User>) repo.findAll()).size(), 1);
            boolean deleted = file.delete();
            assertTrue(deleted);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void delete() {
        File file = new File(".\\src\\tests\\main.com.map_toysocialnetwork_gui.Repository\\testUserRepo.csv");
        try {
            boolean created = file.createNewFile();
            assertTrue(created);
            UserFileRepository repo = new UserFileRepository("src\\tests\\main.com.map_toysocialnetwork_gui.Repository\\testUserRepo.csv");
            UserFactory userFactory= UserFactory.getInstance();
            assertNull(repo.save(userFactory.createObject("id1", "Ion", "Pop")));
            assertNull(repo.save(userFactory.createObject("id2", "Ion2", "Pop2")));
            assertEquals(((Collection<User>) repo.findAll()).size(), 2);
            try {
                repo.delete(null);
            }
            catch (RepositoryException e) {
                assertTrue(true);
            }
            assertNull(repo.delete("id2542"));
            User deleted = repo.delete("id1");
            assertEquals(deleted.getId(), "id1");
            assertEquals(deleted.getFirstName(), "Ion");
            assertEquals(deleted.getLastName(), "Pop");
            assertEquals(((Collection<User>) repo.findAll()).size(), 1);
            BufferedReader br = new BufferedReader(new FileReader("src\\tests\\main.com.map_toysocialnetwork_gui.Repository\\testUserRepo.csv"));
            String line = br.readLine();
            br.close();
            assertNotNull(line);
            String[] attributes = line.split(";");
            assertEquals(attributes[0],"id2");
            assertEquals(attributes[1],"Ion2");
            assertEquals(attributes[2],"Pop2");
            assertTrue(file.delete());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}