package com.map_toysocialnetwork_gui.Repository.SQLDataBaseRepository;

import com.map_toysocialnetwork_gui.Domain.User;
import com.map_toysocialnetwork_gui.Repository.Repository;
import com.map_toysocialnetwork_gui.Repository.RepositoryExceptions.RepositoryException;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UserDBRepository implements Repository<String, User> {

    private String url;
    private String username;
    private String password;

    /**
     * Creates a new repository that saves Users in a PostgreSQL database.
     * @param url String representing the url of the connection.
     * @param username String representing the username of the PostgreSQL connection.
     * @param password String representing the password of the PostgreSQL connection.
     */
    public UserDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public User findOne(String user_id) throws RepositoryException {
        if (user_id==null) {
            throw new RepositoryException("ID must not be null!");
        }
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement
                    ("select count(*) as users_number from users where username=?");)
        {
            statement.setString(1, user_id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int users_number = resultSet.getInt("users_number");
            if (users_number==0) {
                return null;
            }
            PreparedStatement statement2 = connection.prepareStatement
                    ("select first_name, last_name from users where username=?");
            statement2.setString(1,user_id);
            ResultSet resultSet2 = statement2.executeQuery();
            resultSet2.next();
            String first_name = resultSet2.getString("first_name");
            String last_name = resultSet2.getString("last_name");
            User user = new User(first_name, last_name);
            user.setId(user_id);
            return user;
        } catch (SQLException throwables) {
            throw new RepositoryException(throwables.getMessage());
        }
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> allUsers = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, username, password);) {
            PreparedStatement statement = connection.prepareStatement
                    ("select * from users");
            ResultSet resultSet = statement.executeQuery()  ;
            while (resultSet.next()) {
                String user_id = resultSet.getString("username");
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");
                User user = new User(first_name, last_name);
                user.setId(user_id);
                allUsers.add(user);
            }
            return allUsers;
        } catch (SQLException throwables) {
            throw new RepositoryException(throwables.getMessage());
        }
    }

    @Override
    public User save(User user) throws RepositoryException {
        if (user==null) {
            throw new RepositoryException("Entity must not be null!");
        }
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement
                    ("select count(*) as users_number from users where username=?");
            statement.setString(1,user.getId());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int users_number = resultSet.getInt("users_number");
            if (users_number!=0) {
                return user;
            }
            PreparedStatement statement2 = connection.prepareStatement
                    ("insert into users values (?,?,?)");
            statement2.setString(1,user.getId());
            statement2.setString(2,user.getFirstName());
            statement2.setString(3,user.getLastName());
            statement2.executeUpdate();
            return null;
        } catch (SQLException throwables) {
            throw new RepositoryException(throwables.getMessage());
        }
    }

    @Override
    public User delete(String user_id) throws RepositoryException {
        if(user_id==null) {
            return null;
        }
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement
                    ("select count(*) as users_number from users where username=?");
            statement.setString(1,user_id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int users_number = resultSet.getInt("users_number");
            if (users_number==0) {
                return null;
            }
            PreparedStatement statement3 = connection.prepareStatement
                    ("select first_name, last_name from users where username=?");
            statement3.setString(1,user_id);
            ResultSet resultSet3 = statement3.executeQuery();
            resultSet3.next();
            String old_first_name = resultSet3.getString("first_name");
            String old_last_name = resultSet3.getString("last_name");
            User old_user = new User(old_first_name, old_last_name);
            old_user.setId(user_id);
            PreparedStatement statement2 = connection.prepareStatement
                    ("delete from users where username=?");
            statement2.setString(1,user_id);
            statement2.executeUpdate();
            return old_user;
        } catch (SQLException throwables) {
            throw new RepositoryException(throwables.getMessage());
        }
    }

    @Override
    public User update(User user) {
        if (user==null) {
            throw new RepositoryException("Entity must not be null!");
        }
        try (Connection connection=DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement
                    ("select count(*) as users_number from users where username=?");
            statement.setString(1,user.getId());
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            int users_number = resultSet.getInt("users_number");
            if (users_number==0) {
                return null;
            }
            PreparedStatement statement3 = connection.prepareStatement
                    ("select first_name, last_name from users where username=?");
            statement3.setString(1,user.getId());
            ResultSet resultSet3 = statement3.getResultSet();
            String old_first_name = resultSet3.getString("first_name");
            String old_last_name = resultSet3.getString("last_name");
            User old_user = new User(old_first_name, old_last_name);
            old_user.setId(user.getId());
            PreparedStatement statement2 = connection.prepareStatement
                    ("update users set first_name=?, last_name=? where username=?");
            statement2.setString(3,user.getId());
            statement2.setString(1,user.getFirstName());
            statement2.setString(2,user.getLastName());
            statement2.executeUpdate();
            return old_user;
        } catch (SQLException throwables) {
            throw new RepositoryException(throwables.getMessage());
        }
    }
}
