package com.map_toysocialnetwork_gui.Repository.SQLDataBaseRepository;

import com.map_toysocialnetwork_gui.Domain.Factory.UserFactory;
import com.map_toysocialnetwork_gui.Domain.Message;
import com.map_toysocialnetwork_gui.Domain.User;
import com.map_toysocialnetwork_gui.Repository.Repository;
import com.map_toysocialnetwork_gui.Repository.RepositoryExceptions.RepositoryException;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageDBRepositoryTest {

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
    void findOne() {
        clearDB();
        Repository<String, User> userRepository = new UserDBRepository(url, username, password);
        UserFactory userFactory = UserFactory.getInstance();
        userRepository.save(userFactory.createObject("1", "Ion1", "Pop1"));
        userRepository.save(userFactory.createObject("2", "Ion2", "Pop2"));
        userRepository.save(userFactory.createObject("3", "Ion3", "Pop3"));
        Integer messageID = null;
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement insertMessages = connection.prepareStatement(
                    "insert into messages (from_user, message, date_sent, reply_id) values (?,?,?,?)"
            );
            insertMessages.setString(1,"1");
            insertMessages.setString(2,"mesaj");
            insertMessages.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            insertMessages.setNull(4, Types.INTEGER);
            insertMessages.executeUpdate();

            PreparedStatement getIDStatement = connection.prepareStatement("select id from messages");
            ResultSet rs = getIDStatement.executeQuery();
            if (rs.next()) {
                messageID = rs.getInt("id");
            }

            String insertMessagesReceiversQuery = "insert into messages_receivers values (?,?)";
            PreparedStatement insertMessagesReceivers1 = connection.prepareStatement(insertMessagesReceiversQuery);
            insertMessagesReceivers1.setInt(1,messageID);
            insertMessagesReceivers1.setString(2, "1");
            PreparedStatement insertMessagesReceivers2 = connection.prepareStatement(insertMessagesReceiversQuery);
            insertMessagesReceivers2.setInt(1,messageID);
            insertMessagesReceivers2.setString(2, "2");
            PreparedStatement insertMessagesReceivers3 = connection.prepareStatement(insertMessagesReceiversQuery);
            insertMessagesReceivers3.setInt(1,messageID);
            insertMessagesReceivers3.setString(2, "3");
            insertMessagesReceivers1.executeUpdate();
            insertMessagesReceivers2.executeUpdate();
            insertMessagesReceivers3.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Repository<Integer, Message> messagesRepository = new MessageDBRepository(userRepository, url, username, password);
        try {
            messagesRepository.findOne(null);
            fail();
        }
        catch (RepositoryException e) {
            assertTrue(true);
        }
        assertNull(messagesRepository.findOne(-1));
        try (Connection connection = DriverManager.getConnection(url, username, password)) {

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Message message = messagesRepository.findOne(messageID);
        assertEquals(message.getId(), messageID);
        assertEquals(message.getFrom().getId(), "1");
        List<User> receivers = message.getTo();
        assertEquals(receivers.size(), 3);
        assertEquals(receivers.get(0).getId(), "1");
        assertEquals(receivers.get(2).getId(), "3");
    }

    @Test
    void save() {
        clearDB();
        Repository<String, User> userRepository= new UserDBRepository(url, username, password);
        Repository<Integer, Message> messageRepository = new MessageDBRepository(userRepository, url, username, password);
        UserFactory userFactory = UserFactory.getInstance();

        User user1 = userFactory.createObject("1", "Ion1", "Pop1");
        User user2 = userFactory.createObject("2", "Ion2", "Pop2");
        User user3 = userFactory.createObject("3", "Ion3", "Pop3");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        Message message1 = new Message(user1, List.of(user2, user3), "mesaj1", LocalDateTime.now(), null);
        messageRepository.save(message1);
        Message message2 = new Message(user3, List.of(user2), "mesaj2", LocalDateTime.now(), null);
        assertNull(messageRepository.save(message2));

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement selectMessages = connection.prepareStatement(
                    "select * from messages"
            );
            ResultSet messagesResultSet = selectMessages.executeQuery();
            Integer currentMessageId;
            messagesResultSet.next();
            currentMessageId = messagesResultSet.getInt("id");
            assertEquals(messagesResultSet.getString("from_user"), "1");
            assertEquals(messagesResultSet.getString("message"), "mesaj1");
            assertEquals(messagesResultSet.getInt("reply_id"), 0);
            PreparedStatement selectMessagesReceivers = connection.prepareStatement(
                    "select user_id from messages_receivers where message_id=?"
            );
            selectMessagesReceivers.setInt(1, currentMessageId);
            ResultSet messagesReceiversResultSet = selectMessagesReceivers.executeQuery();
            messagesReceiversResultSet.next();
            assertEquals(messagesReceiversResultSet.getString("user_id"), "2");
            messagesReceiversResultSet.next();
            assertEquals(messagesReceiversResultSet.getString("user_id"), "3");

            messagesResultSet.next();
            currentMessageId = messagesResultSet.getInt("id");
            assertEquals(messagesResultSet.getString("from_user"), "3");
            assertEquals(messagesResultSet.getString("message"), "mesaj2");
            assertEquals(messagesResultSet.getInt("reply_id"),0);
            selectMessagesReceivers.setInt(1, currentMessageId);
            messagesReceiversResultSet = selectMessagesReceivers.executeQuery();
            messagesReceiversResultSet.next();
            assertEquals(messagesReceiversResultSet.getString("user_id"), "2");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    void findReplyFor() {
        clearDB();
        Repository<String, User> userRepository = new UserDBRepository(url, username, password);
        MessageDBRepository messageRepository = new MessageDBRepository(userRepository, url, username, password);
        UserFactory userFactory = UserFactory.getInstance();

        User user1 = userFactory.createObject("1", "Ion1", "Pop1");
        User user2 = userFactory.createObject("2", "Ion2", "Pop2");
        User user3 = userFactory.createObject("3", "Ion3", "Pop3");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        Message message1 = new Message(user1, List.of(user2), "mesaj1", LocalDateTime.now(), null);
        messageRepository.save(message1);
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement getIDStatement = connection.prepareStatement(
                    "select id from messages order by id desc"
            );
            ResultSet lastIDResultSet = getIDStatement.executeQuery();
            lastIDResultSet.next();
            message1.setId(lastIDResultSet.getInt("id"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Message message2 = new Message(user2, List.of(user1), "mesaj2", LocalDateTime.now(), message1.getId());
        messageRepository.save(message2);
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement getIDStatement = connection.prepareStatement(
                    "select id from messages order by id desc"
            );
            ResultSet lastIDResultSet = getIDStatement.executeQuery();
            lastIDResultSet.next();
            message2.setId(lastIDResultSet.getInt("id"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Message message3 = new Message(user1, List.of(user2), "mesaj1", LocalDateTime.now(), message2.getId());
        messageRepository.save(message3);
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement getIDStatement = connection.prepareStatement(
                    "select id from messages order by id desc"
            );
            ResultSet lastIDResultSet = getIDStatement.executeQuery();
            lastIDResultSet.next();
            message3.setId(lastIDResultSet.getInt("id"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            messageRepository.findReplyFor(null);
            fail();
        }
        catch (RepositoryException e) {
            assertTrue(true);
        }

        assertEquals(messageRepository.findReplyFor(message1.getId()), message2);
        assertEquals(messageRepository.findReplyFor(message2.getId()), message3);
        assertNull(messageRepository.findReplyFor(message3.getId()));
    }

    @Test
    void testFindReplyFor() {
        clearDB();
        Repository<String, User> userRepository = new UserDBRepository(url, username, password);
        MessageDBRepository messageDBRepository = new MessageDBRepository(userRepository, url, username, password);
        UserFactory userFactory = UserFactory.getInstance();

        User user1 = userFactory.createObject("1", "Ion1", "Pop1");
        User user2 = userFactory.createObject("2", "Ion2", "Pop2");
        User user3 = userFactory.createObject("3", "Ion3", "Pop3");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        List<Message> allMessagesForUser = null;
        allMessagesForUser = messageDBRepository.getAllFirstMessagesOfUser(user1);
        assertEquals(allMessagesForUser.size(), 0);

        messageDBRepository.save(new Message(user1, List.of(user2, user3), "mesaj1", LocalDateTime.now(), null));
        allMessagesForUser = messageDBRepository.getAllFirstMessagesOfUser(user1);
        assertEquals(allMessagesForUser.size(),1);
        allMessagesForUser = messageDBRepository.getAllFirstMessagesOfUser(user2);
        assertEquals(allMessagesForUser.size(),1);
        Message currentMessage = allMessagesForUser.get(0);

        messageDBRepository.save(new Message(user2, List.of(user1, user3), "mesaj2", LocalDateTime.now(), currentMessage.getId()));
        allMessagesForUser = messageDBRepository.getAllFirstMessagesOfUser(user1);
        assertEquals(allMessagesForUser.size(),1);

        messageDBRepository.save(new Message(user2, List.of(user1), "mesaj3", LocalDateTime.now(), null));
        allMessagesForUser = messageDBRepository.getAllFirstMessagesOfUser(user1);
        assertEquals(allMessagesForUser.size(),2);

        assertEquals(allMessagesForUser.get(0).getText(), "mesaj1");
        assertEquals(allMessagesForUser.get(1).getText(), "mesaj3");
    }
}