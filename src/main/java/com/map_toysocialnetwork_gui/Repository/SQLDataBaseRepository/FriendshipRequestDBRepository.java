package com.map_toysocialnetwork_gui.Repository.SQLDataBaseRepository;

import com.map_toysocialnetwork_gui.Domain.FriendshipRequest;
import com.map_toysocialnetwork_gui.Domain.User;
import com.map_toysocialnetwork_gui.Repository.Repository;
import com.map_toysocialnetwork_gui.Repository.RepositoryExceptions.RepositoryException;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class FriendshipRequestDBRepository implements Repository<FriendshipRequest.FriendshipRequestID,FriendshipRequest> {
    private UserDBRepository userRepo;
    private String url;
    private String username;
    private String password;

    public FriendshipRequestDBRepository(UserDBRepository userRepo, String url, String username, String password) {
        this.userRepo = userRepo;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public FriendshipRequest findOne(FriendshipRequest.FriendshipRequestID friendshipRequestID) throws RepositoryException {
        if(friendshipRequestID==null)
        {
            throw new RepositoryException("ID must not be null");
        }
        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            String username1 = friendshipRequestID.getUsernameSender();
            String username2 = friendshipRequestID.getUsernameReceiver();
            PreparedStatement statement = connection.prepareStatement
                    ("select count(*) as friendship_requests_number from friendship_requests where username1=? and username2=? and status='pending'");
            statement.setString(1,username1);
            statement.setString(2,username2);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int users_number = resultSet.getInt("friendship_requests_number");
            if(users_number==0){
                return null;
            }
            User user1 = userRepo.findOne(username1);
            User user2 = userRepo.findOne(username2);
            FriendshipRequest friendshipRequest = new FriendshipRequest(user1,user2);
            friendshipRequest.setId(friendshipRequestID);
            return friendshipRequest;

        }
        catch (SQLException e)
        {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Iterable<FriendshipRequest> findAll() {
        Set<FriendshipRequest> allFriendshipRequests = new HashSet<>();
        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement
                    ("select * from friendship_requests where status='pending'");
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                String username1 = resultSet.getString("username1");
                String username2 = resultSet.getString("username2");
                String status = resultSet.getString("status");
                Date date = resultSet.getDate("date_friendship_request");
                User user1 = userRepo.findOne(username1);
                User user2 = userRepo.findOne(username2);
                FriendshipRequest friendshipRequest = new FriendshipRequest(user1,user2);
                friendshipRequest.setId(new FriendshipRequest.FriendshipRequestID(username1,username2));
                friendshipRequest.setStatus(status);
                friendshipRequest.setDate(date.toLocalDate());
                allFriendshipRequests.add(friendshipRequest);
            }
            return allFriendshipRequests;
        }
        catch (SQLException ex){
            throw new RepositoryException(ex.getMessage());
        }
    }

    @Override
    public FriendshipRequest save(FriendshipRequest entity) throws RepositoryException {
        if(entity==null){
            throw new RepositoryException("Entity must not be null");
        }
        try {
            Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement
                    ("select count(*) as friendship_requests_number from friendship_requests where username1=? and username2=? and status='pending'");
            statement.setString(1,entity.getUserSender().getId());
            statement.setString(2,entity.getUserReceiver().getId());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int friendshipRequests_number = resultSet.getInt("friendship_requests_number");
            if(friendshipRequests_number!=0){
                return entity;
            }
            PreparedStatement statement1 = connection.prepareStatement
                    ("insert into friendship_requests (username1, username2, status, date_friendship_request) values (?,?,?,?)");
            statement1.setString(1,entity.getId().getUsernameSender());
            statement1.setString(2,entity.getId().getUsernameReceiver());
            statement1.setString(3,entity.getStatus());
            statement1.setDate(4,Date.valueOf(entity.getDate()));
            statement1.executeUpdate();
            return null;
        }
        catch(SQLException e){
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public FriendshipRequest delete(FriendshipRequest.FriendshipRequestID friendshipRequestID) throws RepositoryException {
        return null;
    }

    @Override
    public FriendshipRequest update(FriendshipRequest entity) {
        if(entity==null){
            throw new RepositoryException("Entity must not be null");
        }
        FriendshipRequest friendshipRequestFound = this.findOne(entity.getId());
        if(friendshipRequestFound==null){
            return null;
        }
        else{
            try {
                Connection connection = DriverManager.getConnection(url,username,password);
                PreparedStatement statement = connection.prepareStatement
                        (
                        "update friendship_requests set status=? where username1=? and username2=? and status='pending' and date_friendship_request=?"
                        );
                statement.setString(1,entity.getStatus());
                statement.setString(2,entity.getUserSender().getId());
                statement.setString(3,entity.getUserReceiver().getId());
                statement.setDate(4,Date.valueOf(entity.getDate()));
                statement.executeUpdate();
            }
            catch(SQLException e){
                throw new RepositoryException(e.getMessage());
            }
            return friendshipRequestFound;
        }
    }
}
