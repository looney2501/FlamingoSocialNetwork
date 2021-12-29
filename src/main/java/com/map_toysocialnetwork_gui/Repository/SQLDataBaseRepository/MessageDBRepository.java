package com.map_toysocialnetwork_gui.Repository.SQLDataBaseRepository;

import com.map_toysocialnetwork_gui.Domain.Message;
import com.map_toysocialnetwork_gui.Domain.User;
import com.map_toysocialnetwork_gui.Repository.Repository;
import com.map_toysocialnetwork_gui.Repository.RepositoryExceptions.RepositoryException;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MessageDBRepository implements Repository<Integer, Message> {
    private Repository<String, User> userRepo;
    private String url;
    private String username;
    private String password;

    /**
     * Creates a new repository that saves Messages in a PostgreSQL database.
     * @param userRepo Reference to a UserDBRepository.
     * @param url String representing the url of the connection.
     * @param username String representing the username of the PostgreSQL server.
     * @param password String representing the password of the PostgreSQL server.
     */
    public MessageDBRepository(Repository<String, User> userRepo, String url, String username, String password) {
        this.userRepo = userRepo;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * Finds the Message with the given id.
     * @param messageID The id of the message to be returned. Must not be null.
     * @return The message with the specified id or null if there is no message with the given id.
     * @throws RepositoryException if id is null.
     */
    @Override
    public Message findOne(Integer messageID) throws RepositoryException {
        if (messageID==null) {
            throw new RepositoryException("ID must not be null!");
        }
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement messagesStatement = connection.prepareStatement(
                    "select * from Messages where id=?"
            );
            messagesStatement.setInt(1,messageID);
            ResultSet messagesResultSet = messagesStatement.executeQuery();
            if (messagesResultSet.next()) {
                User sender = userRepo.findOne(messagesResultSet.getString("from_user"));
                String text = messagesResultSet.getString("message");
                LocalDateTime timestamp = messagesResultSet.getTimestamp("date_sent").toLocalDateTime();
                Integer repliesTo = messagesResultSet.getInt("reply_id");
                if (repliesTo==0) {
                    repliesTo = null;
                }
                PreparedStatement messagesReceiversStatement = connection.prepareStatement(
                        "select user_id from messages_receivers where message_id=?"
                );
                messagesReceiversStatement.setInt(1, messageID);
                ResultSet messagesReceiversResultSet = messagesReceiversStatement.executeQuery();
                List<User> receivers = new ArrayList<>();
                while(messagesReceiversResultSet.next()) {
                    User receiver = userRepo.findOne(messagesReceiversResultSet.getString("user_id"));
                    receivers.add(receiver);
                }
                Message message = new Message(sender, receivers, text, timestamp, repliesTo);
                message.setId(messageID);
                return message;
            }
            else {
                return null;
            }
        } catch (SQLException throwables) {
            throw new RepositoryException(throwables.getMessage());
        }
    }

    @Override
    public Iterable<Message> findAll() {
        return null;
    }

    /**
     * Saves a given message into the repository.
     * @param message Message object to be saved into the repository. Must not be null.
     * @return null if the given message is saved. Otherwise, returns the message.
     * @throws RepositoryException if the message is null.
     */
    @Override
    public Message save(Message message) throws RepositoryException {
        if (message==null) {
            throw new RepositoryException("Message must not be null!");
        }
        if(message.getId()!=null && message==findOne(message.getId())) {
            return message;
        }
        User fromUser = message.getFrom();
        List<User> toUsers = message.getTo();
        String text = message.getText();
        LocalDateTime timestamp = message.getDateTime();
        Integer repliesTo = message.getRepliesTo();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            PreparedStatement insertMessagesStatement = connection.prepareStatement(
                    "insert into messages (from_user, message, date_sent, reply_id) values (?,?,?,?) returning id"
            );
            insertMessagesStatement.setString(1, fromUser.getId());
            insertMessagesStatement.setString(2, text);
            insertMessagesStatement.setObject(3, timestamp);
            if(repliesTo==null) {
                insertMessagesStatement.setNull(4, Types.INTEGER);
            }
            else {
                insertMessagesStatement.setInt(4, repliesTo);
            }
            ResultSet idResultSet = insertMessagesStatement.executeQuery();
            idResultSet.next();
            Integer messageID = idResultSet.getInt("id");

            for (User user:toUsers){
                PreparedStatement insertMessageReceiversStatement = connection.prepareStatement(
                        "insert into messages_receivers values (?,?)"
                );
                insertMessageReceiversStatement.setInt(1, messageID);
                insertMessageReceiversStatement.setString(2, user.getId());
                insertMessageReceiversStatement.executeUpdate();
            }
        } catch (SQLException throwables) {
            throw new RepositoryException(throwables.getMessage());
        }
        return null;
    }

    @Override
    public Message delete(Integer integer) throws RepositoryException {
        return null;
    }

    @Override
    public Message update(Message message) {
        return null;
    }

    /**
     * Gets all messages that start a conversation in which a user given is involved.
     * @param user User instance
     * @return list of Message objects.
     */
    //poate fi facuta filtrarea direct din baza de date
    public List<Message> getAllFirstMessagesOfUser(User user) {
        List<Message> allFirstMessages = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement allFirstMessagesStatement = connection.prepareStatement(
                    "select * from messages where reply_id IS NULL"
            );
            ResultSet allFirstMessagesResultSet = allFirstMessagesStatement.executeQuery();
            while (allFirstMessagesResultSet.next()) {
                Integer messageID = allFirstMessagesResultSet.getInt("id");
                User sender = userRepo.findOne(allFirstMessagesResultSet.getString("from_user"));
                String text = allFirstMessagesResultSet.getString("message");
                LocalDateTime timestamp = allFirstMessagesResultSet.getTimestamp("date_sent").toLocalDateTime();

                PreparedStatement allReceiversForMessage = connection.prepareStatement(
                        "select user_id from messages_receivers where message_id=?"
                );
                allReceiversForMessage.setInt(1,messageID);
                ResultSet allReceiversResultSet = allReceiversForMessage.executeQuery();
                List<User> receivers = new ArrayList<>();
                while(allReceiversResultSet.next()) {
                    receivers.add(userRepo.findOne(allReceiversResultSet.getString("user_id")));
                }
                Message message = new Message(sender, receivers, text, timestamp, null);
                message.setId(messageID);
                allFirstMessages.add(message);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        List<Message> allFirstMessagesOfUser;
        return allFirstMessagesOfUser = allFirstMessages.stream()
                    .filter(x->(x.getFrom().equals(user) || x.getTo().contains(user)))
                    .toList();
    }

    /**
     * Searches for the reply of a message.
     * @param messageID Integer representing the id of the message for which the reply is searched for.
     * @return Message representing the reply or null if there is no reply for the given message.
     * @throws RepositoryException if the messageID is null.
     */
    public Message findReplyFor(Integer messageID) {
        if (messageID == null) {
            throw new RepositoryException("ID must not be null!");
        }
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement selectReplyMessageStatement = connection.prepareStatement(
                    "select * from messages where reply_id=?"
            );
            selectReplyMessageStatement.setInt(1, messageID);
            ResultSet replyMessageResultSet = selectReplyMessageStatement.executeQuery();
            if(replyMessageResultSet.next()) {
                Integer replyMessageID = replyMessageResultSet.getInt("id");
                User sender = userRepo.findOne(replyMessageResultSet.getString("from_user"));
                String text = replyMessageResultSet.getString("message");
                LocalDateTime timestamp = replyMessageResultSet.getTimestamp("date_sent").toLocalDateTime();

                PreparedStatement selectReplyMessageReceiversStatement = connection.prepareStatement(
                        "select user_id from messages_receivers where message_id=?"
                );
                selectReplyMessageReceiversStatement.setInt(1,replyMessageID);
                ResultSet replyMessageReceiversResultSet = selectReplyMessageReceiversStatement.executeQuery();
                List<User> receivers = new ArrayList<>();
                while (replyMessageReceiversResultSet.next()) {
                    receivers.add(userRepo.findOne(replyMessageReceiversResultSet.getString("user_id")));
                }
                Message reply = new Message(sender, receivers, text, timestamp, messageID);
                reply.setId(replyMessageID);
                return reply;
            }
            else {
                return null;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
