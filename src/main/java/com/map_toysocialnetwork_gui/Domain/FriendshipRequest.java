package com.map_toysocialnetwork_gui.Domain;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Defines class representing a friend request
 * It extends the Entity class
 * The ID is represented by a class that has two id string of users
 * Fields: id, two users, and a string that represents a status (pending, approved, rejected)
 */
public class FriendshipRequest extends Entity<FriendshipRequest.FriendshipRequestID>{
    public static class FriendshipRequestID{
        private String usernameSender;
        private String usernameReceiver;

        public FriendshipRequestID(String user1, String user2) {
            this.usernameSender = user1;
            this.usernameReceiver = user2;
        }

        /**
         * Get the first user
         * @return - String representig id of user
         */
        public String getUsernameSender() {
            return usernameSender;
        }

        /**
         * Set string for second user
         * @param usernameSender - User
         */
        public void setUsernameSender(String usernameSender) {
            this.usernameSender = usernameSender;
        }

        /**
         * Get second user
         * @return - String representing id of user
         */
        public String getUsernameReceiver() {
            return usernameReceiver;
        }

        /**
         * Set string for second user
         * @param usernameReceiver - User
         */
        public void setUsernameReceiver(String usernameReceiver) {
            this.usernameReceiver = usernameReceiver;
        }

        /**
         * Defines when two Friendship Requests are equal:
         *          when they have the same id
         * @param o - other Object of of same type
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FriendshipRequestID that = (FriendshipRequestID) o;
            return Objects.equals(usernameSender, that.usernameSender) && Objects.equals(usernameReceiver, that.usernameReceiver);
        }

        @Override
        public int hashCode() {
            return Objects.hash(usernameSender, usernameReceiver);
        }
    }
    private User userSender, userReceiver;
    private String status;
    private LocalDate date;

    public FriendshipRequest(User user1, User user2) {
        this.userSender = user1;
        this.userReceiver = user2;
    }

    /**
     * Gets first user
     * User that sent the friendship request
     * @return - of type User
     */
    public User getUserSender() {
        return userSender;
    }

    /**
     * Sets first user - user that sent a friend request
     * @param userSender - of type User
     */
    public void setUserSender(User userSender) {
        this.userSender = userSender;
    }

    /**
     * Gets second user
     * User that received the friendship request
     * @return - of type User
     */
    public User getUserReceiver() {
        return userReceiver;
    }

    /**
     * Sets second user - user that received a friend request
     * @param userReceiver - of type User
     */
    public void setUserReceiver(User userReceiver) {
        this.userReceiver = userReceiver;
    }

    /**
     * Gets status of friendship request
     * (pending, approved, rejected)
     * @return - String
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets status of friendship request
     * (pending, approved, rejected)
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "FriendshipRequest{" +
                "user1=" + userSender +
                ", user2=" + userReceiver +
                ", status='" + getStatus() + '\'' +
                '}';
    }
}
