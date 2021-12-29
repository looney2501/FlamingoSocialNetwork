package com.map_toysocialnetwork_gui.Repository.FileRepository;

import com.map_toysocialnetwork_gui.Domain.Factory.FriendshipFactory;
import com.map_toysocialnetwork_gui.Domain.Factory.UserFactory;
import com.map_toysocialnetwork_gui.Domain.Friendship;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class FriendshipRepositoryTest {

    @Test
    void findOne() {
    }

    @Test
    void findAll() {
    }

    @Test
    void save() {
        File file1 = new File(".\\src\\tests\\main.com.map_toysocialnetwork_gui.Repository\\testFriendshipRepo.csv");
        File file2 = new File(".\\src\\tests\\main.com.map_toysocialnetwork_gui.Repository\\testUserRepo.csv");
        try {
            assertTrue(file1.createNewFile());
            assertTrue(file2.createNewFile());
            UserFileRepository userFileRepository = new UserFileRepository("src\\tests\\main.com.map_toysocialnetwork_gui.Repository\\testUserRepo.csv");
            UserFactory userFactory = UserFactory.getInstance();
            userFileRepository.save(userFactory.createObject("id1", "prenume1", "nume1"));
            userFileRepository.save(userFactory.createObject("id2", "prenume2", "nume1"));
            FriendshipFileRepository friendshipFileRepository =
                    new FriendshipFileRepository("src\\tests\\main.com.map_toysocialnetwork_gui.Repository\\testFriendshipRepo.csv",
                            userFileRepository);
            FriendshipFactory friendshipFactory = FriendshipFactory.getInstance(userFileRepository);
            friendshipFileRepository.save(friendshipFactory.createObject("id1", "id2"));
            BufferedReader br = new BufferedReader(new FileReader("src\\tests\\main.com.map_toysocialnetwork_gui.Repository\\testFriendshipRepo.csv"));
            String line = br.readLine();
            br.close();
            assertNotNull(line);
            String[] attributes = line.split(";");
            assertEquals(attributes[0], "id1");
            assertEquals(attributes[1], "id2");
            assertNotNull(friendshipFileRepository.save(friendshipFactory.createObject("id1",
                    "id2")));
            assertEquals(((Collection<Friendship>) friendshipFileRepository.findAll()).size(), 1);
            assertTrue(file1.delete());
            assertTrue(file2.delete());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void delete() {
    }
}