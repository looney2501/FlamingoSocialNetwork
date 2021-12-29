package com.map_toysocialnetwork_gui.Service;

import com.map_toysocialnetwork_gui.Domain.DTO.Conversation;
import com.map_toysocialnetwork_gui.Domain.Factory.UserFactory;
import com.map_toysocialnetwork_gui.Domain.Friendship;
import com.map_toysocialnetwork_gui.Domain.Message;
import com.map_toysocialnetwork_gui.Domain.User;
import com.map_toysocialnetwork_gui.Domain.Validators.UserValidator;
import com.map_toysocialnetwork_gui.Domain.Validators.ValidatorExceptions.ValidatorException;
import com.map_toysocialnetwork_gui.Repository.FileRepository.FriendshipFileRepository;
import com.map_toysocialnetwork_gui.Repository.FileRepository.UserFileRepository;
import com.map_toysocialnetwork_gui.Repository.SQLDataBaseRepository.MessageDBRepository;
import com.map_toysocialnetwork_gui.Repository.SQLDataBaseRepository.UserDBRepository;
import com.map_toysocialnetwork_gui.Service.ServiceExceptions.ServiceException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {

    private String url = "jdbc:postgresql://localhost:5432/ToySocialNetworkTests";
    private String username = "postgres";
    private String password = "postgres";

    private void clearDB() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement deleteFriendships = connection.prepareStatement("delete from friendships");
            deleteFriendships.executeUpdate();
            PreparedStatement deleteFriendshipsRequests = connection.prepareStatement("delete from friendship_requests");
            deleteFriendshipsRequests.executeUpdate();
            PreparedStatement deleteMessagesReceivers = connection.prepareStatement("delete from messages_receivers");
            deleteMessagesReceivers.executeUpdate();
            PreparedStatement deleteMessages = connection.prepareStatement("delete from messages");
            deleteMessages.executeUpdate();
            PreparedStatement deleteUsers = connection.prepareStatement("delete from users");
            deleteUsers.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    void addUser() {
        File file1 = new File(".\\src\\tests\\usersTest.csv");
        File file2 = new File(".\\src\\tests\\friendshipTest.csv");
        try {
            assertTrue(file1.createNewFile());
            assertTrue(file2.createNewFile());
            UserFileRepository userFileRepository;
            userFileRepository = new UserFileRepository(".\\src\\tests\\usersTest.csv");
            FriendshipFileRepository friendshipFileRepository;
            friendshipFileRepository = new FriendshipFileRepository(".\\src\\tests\\friendshipTest.csv",
                    userFileRepository);
            UserValidator userValidator = new UserValidator();
            Service service = new Service(userFileRepository, friendshipFileRepository,null, null,  userValidator);
            try {
                service.addUser("asa", "%*Maria", "Popa");
            } catch (ValidatorException e) {
                assertTrue(true);
            }
            service.addUser("ionel2545", "Ion-Marian", "Pop");
            service.addUser("ana_maria24", "Ana-Maria", "Pop");
            service.addUser("darius$$", "Darius", "Ionescu");
            assertEquals(((Collection<User>) userFileRepository.findAll()).size(), 3);
            service.addUser("andrei51", "Andrei George", "Ionescu");
            assertEquals(((Collection<User>) userFileRepository.findAll()).size(), 4);
            assertTrue(file1.delete());
            assertTrue(file2.delete());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteUser() {
        File file1 = new File(".\\src\\tests\\usersTest.csv");
        File file2 = new File(".\\src\\tests\\friendshipTest.csv");
        try {
            assertTrue(file1.createNewFile());
            assertTrue(file2.createNewFile());
            UserFileRepository userFileRepository;
            userFileRepository = new UserFileRepository(".\\src\\tests\\usersTest.csv");
            FriendshipFileRepository friendshipFileRepository;
            friendshipFileRepository = new FriendshipFileRepository(".\\src\\tests\\friendshipTest.csv",
                    userFileRepository);
            UserValidator userValidator = new UserValidator();
            Service service = new Service(userFileRepository, friendshipFileRepository, null, null, userValidator);
            service.addUser("ionel2545", "Ion-Marian", "Pop");
            service.addUser("ana_maria24", "Ana-Maria", "Pop");
            service.addUser("darius$$", "Darius", "Ionescu");
            String date="2021-12-12";
            String date1="2021-10-10";
            LocalDate localDate = LocalDate.parse(date);
            LocalDate localDate1 = LocalDate.parse(date1);
            service.addFriend("ionel2545", "darius$$",localDate);
            service.addFriend("ionel2545", "ana_maria24",localDate1);
            try {
                service.deleteUser("wargegr");
            } catch (ServiceException e) {
                assertTrue(true);
            }
            service.deleteUser("ionel2545");
            assertEquals(((Collection<User>) userFileRepository.findAll()).size(), 2);
            assertEquals(((Collection<Friendship>) friendshipFileRepository.findAll()).size(), 0);
            assertTrue(file1.delete());
            assertTrue(file2.delete());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addFriend() {
        File file1 = new File(".\\src\\tests\\usersTest.csv");
        File file2 = new File(".\\src\\tests\\friendshipTest.csv");
        try {
            assertTrue(file1.createNewFile());
            assertTrue(file2.createNewFile());
            UserFileRepository userFileRepository;
            userFileRepository = new UserFileRepository(".\\src\\tests\\usersTest.csv");
            FriendshipFileRepository friendshipFileRepository;
            friendshipFileRepository = new FriendshipFileRepository(".\\src\\tests\\friendshipTest.csv",
                    userFileRepository);
            UserValidator userValidator = new UserValidator();
            Service service = new Service(userFileRepository, friendshipFileRepository,null, null, userValidator);
            service.addUser("ionel2545", "Ion-Marian", "Pop");
            service.addUser("ana_maria24", "Ana-Maria", "Pop");
            service.addUser("darius$$", "Darius", "Ionescu");
            String date="2021-12-12";
            LocalDate localDate = LocalDate.parse(date);
            try {
                service.addFriend("gigi", "ionel2545",localDate);
            } catch (ServiceException e) {
                assertTrue(true);
            }
            service.addFriend("ionel2545", "darius$$",localDate);
            assertEquals(((Collection<Friendship>) friendshipFileRepository.findAll()).size(), 1);
            try {
                service.addFriend("darius$$", "ionel2545",localDate);
                fail();
            } catch (ServiceException e) {
                assertTrue(true);
            }
            assertEquals(((Collection<Friendship>) friendshipFileRepository.findAll()).size(), 1);
            var iterator = friendshipFileRepository.findAll().iterator();
            Friendship friendship = iterator.next();
            assertEquals(friendship.getUser1().getFirstName(), "Ion-Marian");
            assertEquals(friendship.getUser2().getFirstName(), "Darius");
            assertTrue(file1.delete());
            assertTrue(file2.delete());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteFriend() {
        File file1 = new File(".\\src\\tests\\usersTest.csv");
        File file2 = new File(".\\src\\tests\\friendshipTest.csv");
        try {
            assertTrue(file1.createNewFile());
            assertTrue(file2.createNewFile());
            UserFileRepository userFileRepository;
            userFileRepository = new UserFileRepository(".\\src\\tests\\usersTest.csv");
            FriendshipFileRepository friendshipFileRepository;
            friendshipFileRepository = new FriendshipFileRepository(".\\src\\tests\\friendshipTest.csv",
                    userFileRepository);
            UserValidator userValidator = new UserValidator();
            Service service = new Service(userFileRepository, friendshipFileRepository,null,null, userValidator);
            service.addUser("ionel2545", "Ion-Marian", "Pop");
            service.addUser("ana_maria24", "Ana-Maria", "Pop");
            service.addUser("darius$$", "Darius", "Ionescu");
            String date="2021-12-12";
            LocalDate localDate = LocalDate.parse(date);
            String date2="2021-08-14";
            LocalDate localDate2 = LocalDate.parse(date2);
            try {
                service.addFriend("gigi", "ionel2545",localDate);
            } catch (ServiceException e) {
                assertTrue(true);
            }
            service.addFriend("ionel2545", "darius$$",localDate);
            service.addFriend("ionel2545", "ana_maria24",localDate2);
            assertEquals(((Collection<Friendship>) friendshipFileRepository.findAll()).size(), 2);
            try {
                service.deleteFriend("isbai", "darius$$");
                fail();
            } catch (ServiceException e) {
                assertTrue(true);
            }
            assertTrue(file1.delete());
            assertTrue(file2.delete());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void findUser() {
        File file1 = new File(".\\src\\tests\\main.com.map_toysocialnetwork_gui.Repository\\usersTest.csv");
        File file2 = new File(".\\src\\tests\\main.com.map_toysocialnetwork_gui.Repository\\friendshipTest.csv");
        try {
            assertTrue(file1.createNewFile());
            assertTrue(file2.createNewFile());
            UserFileRepository userFileRepository;
            userFileRepository = new UserFileRepository(".\\src\\tests\\main.com.map_toysocialnetwork_gui.Repository\\usersTest.csv");
            FriendshipFileRepository friendshipFileRepository;
            friendshipFileRepository = new FriendshipFileRepository(".\\src\\tests\\main.com.map_toysocialnetwork_gui.Repository\\friendshipTest.csv",
                    userFileRepository);
            UserValidator userValidator = new UserValidator();Service service = new Service(userFileRepository, friendshipFileRepository,null, null, userValidator);
            service.addUser("gigi10", "George", "Ion");
            assertNotNull(service.findUser("gigi10"));
            assertEquals(service.findUser("gigi10").getFirstName(), "George");
            assertNull(service.findUser("asdas"));
            assertTrue(file1.delete());
            assertTrue(file2.delete());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void findFriendship() {
        File file1 = new File(".\\src\\tests\\usersTest.csv");
        File file2 = new File(".\\src\\tests\\friendshipTest.csv");
        try {
            assertTrue(file1.createNewFile());
            assertTrue(file2.createNewFile());
            UserFileRepository userFileRepository;
            userFileRepository = new UserFileRepository(".\\src\\tests\\usersTest.csv");
            FriendshipFileRepository friendshipFileRepository;
            friendshipFileRepository = new FriendshipFileRepository(".\\src\\tests\\friendshipTest.csv",
                    userFileRepository);
            UserValidator userValidator = new UserValidator();
            Service service = new Service(userFileRepository, friendshipFileRepository, null, null, userValidator);
            service.addUser("ionel2545", "Ion-Marian", "Pop");
            service.addUser("ana_maria24", "Ana-Maria", "Pop");
            service.addUser("darius$$", "Darius", "Ionescu");
            service.addFriend("ionel2545", "darius$$", LocalDate.now());
            var existentFriendship = service.findFriendship("darius$$", "ionel2545");
            assertNotNull(existentFriendship);
            assertEquals(existentFriendship.getUser1().getId(),"ionel2545");
            assertEquals(existentFriendship.getUser2().getId(), "darius$$");
            assertTrue(file1.delete());
            assertTrue(file2.delete());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getAllConversationsFor() {
        clearDB();
        UserDBRepository userRepository = new UserDBRepository(url, username, password);
        MessageDBRepository messageRepository = new MessageDBRepository(userRepository, url, username, password);
        Service service = new Service(userRepository, null, null, messageRepository, new UserValidator());
        UserFactory userFactory = UserFactory.getInstance();

        User user1 = userFactory.createObject("1", "Ion1", "Pop1");
        User user2 = userFactory.createObject("2", "Ion2", "Pop2");
        User user3 = userFactory.createObject("3", "Ion3", "Pop3");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        try {
            service.getAllConversationsFor(null);
            fail();
        }
        catch (ServiceException e) {
            assertTrue(true);
        }

        try {
            service.getAllConversationsFor(userFactory.createObject("aef12", null, null));
            fail();
        }
        catch (ServiceException e) {
            assertTrue(true);
        }

        List<Conversation> allConversationsFor = service.getAllConversationsFor(user1);
        assertEquals(allConversationsFor.size(),0);

        messageRepository.save(new Message(user1, List.of(user2, user3), "mesaj1", LocalDateTime.now(), null));
        allConversationsFor = service.getAllConversationsFor(user1);
        assertEquals(allConversationsFor.size(),1);
        assertEquals(allConversationsFor.get(0).getAllMessages().size(),1);
        messageRepository.save(new Message(user2, List.of(user1, user3), "mesaj2", LocalDateTime.now(), allConversationsFor.get(0).getAllMessages().get(0).getId()));
        allConversationsFor = service.getAllConversationsFor(user1);
        assertEquals(allConversationsFor.size(),1);
        assertEquals(allConversationsFor.get(0).getAllMessages().size(),2);
        assertEquals(allConversationsFor.get(0).getAllMessages().get(1).getText(), "mesaj2");

        messageRepository.save(new Message(user2, List.of(user3), "mesaj3", LocalDateTime.now(), null));
        allConversationsFor = service.getAllConversationsFor(user2);
        assertEquals(allConversationsFor.size(),2);
        assertEquals(allConversationsFor.get(0).getAllMessages().size(),2);
        assertEquals(allConversationsFor.get(1).getAllMessages().size(),1);
    }
}