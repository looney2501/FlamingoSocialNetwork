package com.map_toysocialnetwork_gui.Repository.SQLDataBaseRepository;

import com.map_toysocialnetwork_gui.Domain.Friendship;
import com.map_toysocialnetwork_gui.Domain.User;
import com.map_toysocialnetwork_gui.Repository.Repository;
import com.map_toysocialnetwork_gui.Repository.RepositoryExceptions.RepositoryException;

import java.sql.*;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class FriendshipDBRepository implements Repository<Friendship.FriendshipID, Friendship> {
    private UserDBRepository userRepo;
    private String url;
    private String username;
    private String password;

    /**
     * Creates a new repository that saves Friendships in a PostgreSQL database.
     * @param userRepo Reference to a UserDBRepository.
     * @param url String representing the url of the connection.
     * @param username String representing the username of the PostgreSQL server.
     * @param password String representing the password of the PostgreSQL server.
     */
    public FriendshipDBRepository(UserDBRepository userRepo, String url, String username, String password) {
        this.userRepo = userRepo;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Friendship findOne(Friendship.FriendshipID friendshipID) throws RepositoryException {
        if (friendshipID==null) {
            throw new RepositoryException("ID must not be null!");
        }
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String username1 = friendshipID.getUserID1();
            String username2 = friendshipID.getUserID2();
            PreparedStatement statement = connection.prepareStatement
                    ("select * from friendships where (username1=? and username2=?) or (username1=? and username2=?)");
            statement.setString(1,username1);
            statement.setString(2,username2);
            statement.setString(3,username2);
            statement.setString(4,username1);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            User user1 = userRepo.findOne(username1);
            User user2 = userRepo.findOne(username2);
            Date date = resultSet.getDate("data_crearii_prieteniei");
            Friendship friendship = new Friendship(user1, user2);
            friendship.setId(friendshipID);
            friendship.setDate(date.toLocalDate());
            return friendship;
        } catch (SQLException throwables) {
            throw new RepositoryException(throwables.getMessage());
        }
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> allFriendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement
                    ("select * from friendships");
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                String username1 = resultSet.getString("username1");
                String username2 = resultSet.getString("username2");
                Date date = resultSet.getDate("data_crearii_prieteniei");
                User user1 = userRepo.findOne(username1);
                User user2 = userRepo.findOne(username2);
                Friendship friendship = new Friendship(user1, user2);
                friendship.setId(new Friendship.FriendshipID(username1, username2));
                friendship.setDate(date.toLocalDate());
                allFriendships.add(friendship);
            }
            return allFriendships;
        } catch (SQLException throwables) {
            throw new RepositoryException(throwables.getMessage());
        }
    }

    @Override
    public Friendship save(Friendship entity) throws RepositoryException {
        if (entity==null) {
            throw new RepositoryException("Entity must not be null!");
        }
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement1 = connection.prepareStatement
                    ("select count(*) as friendships_number from friendships where (username1=? and username2=?) or (username1=? and username2=?)");
            statement1.setString(1, entity.getUser1().getId());
            statement1.setString(2, entity.getUser2().getId());
            Date date = Date.valueOf(LocalDate.of(entity.getDate().getYear(),entity.getDate().getMonth(),entity.getDate().getDayOfMonth()));
            statement1.setString(3, entity.getUser2().getId());
            statement1.setString(4, entity.getUser1().getId());
            ResultSet resultSet = statement1.executeQuery();
            resultSet.next();
            int frienships_number = resultSet.getInt("friendships_number");
            if (frienships_number!=0) {
                return entity;
            }
            PreparedStatement statement2 = connection.prepareStatement
                    ("insert into friendships values (?,?,?)");
            statement2.setString(1, entity.getId().getUserID1());
            statement2.setString(2, entity.getId().getUserID2());
            statement2.setDate(3,date);
            statement2.executeUpdate();
            return null;
        } catch (SQLException throwables) {
            throw new RepositoryException(throwables.getMessage());
        }
    }

    @Override
    public Friendship delete(Friendship.FriendshipID friendshipID) throws RepositoryException {
        if (friendshipID==null) {
            throw new RepositoryException("ID must not be null!");
        }
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement1 = connection.prepareStatement
                    ("select count(*) as friendships_number from friendships where (username1=? and username2=?) or (username1=? and username2=?)");
            statement1.setString(1, friendshipID.getUserID1());
            statement1.setString(2, friendshipID.getUserID2());
            statement1.setString(3, friendshipID.getUserID2());
            statement1.setString(4, friendshipID.getUserID1());
            ResultSet resultSet = statement1.executeQuery();
            resultSet.next();
            int frienships_number = resultSet.getInt("friendships_number");
            if (frienships_number==0) {
                return null;
            }
            PreparedStatement statement2 = connection.prepareStatement
                    ("delete from friendships where (username1=? and username2=?) or (username1=? and username2=?)");
            statement2.setString(1, friendshipID.getUserID1());
            statement2.setString(2, friendshipID.getUserID2());
            statement2.setString(3, friendshipID.getUserID2());
            statement2.setString(4, friendshipID.getUserID1());
            statement2.executeUpdate();
            User user1 = userRepo.findOne(friendshipID.getUserID1());
            User user2 = userRepo.findOne(friendshipID.getUserID2());
            return new Friendship(user1, user2);
        } catch (SQLException throwables) {
            throw new RepositoryException(throwables.getMessage());
        }
    }

    @Override
    public Friendship update(Friendship entity) {return null;}
}
