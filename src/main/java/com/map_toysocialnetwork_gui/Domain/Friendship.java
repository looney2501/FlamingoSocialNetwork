package com.map_toysocialnetwork_gui.Domain;

import com.map_toysocialnetwork_gui.Utils.Constants;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Defines the friendship relation between two Users.
 */
public class Friendship extends Entity<Friendship.FriendshipID> {

    /**
     * Defines the ID of a Friendship object.
     */
    public static class FriendshipID {
        private String userID1, userID2;

        /**
         * FriendshipID constructor that takes the two user's IDs as params.
         * @param userID1 String representing the ID of the first user.
         * @param userID2 String representing the ID of the second user.
         */
        public FriendshipID(String userID1, String userID2) {
            this.userID1 = userID1;
            this.userID2 = userID2;

        }

        /**
         * Gets the ID of the first user.
         * @return String representing the ID of the first user.
         */
        public String getUserID1() {
            return userID1;
        }

        /**
         * Gets the ID of the second user.
         * @return String representing the ID of the second user.
         */
        public String getUserID2() {
            return userID2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FriendshipID that = (FriendshipID) o;
            return Objects.equals(userID1, that.userID1) && Objects.equals(userID2, that.userID2) ||
                    Objects.equals(userID1, that.userID2) && Objects.equals(userID2, that.userID1);
        }

        @Override
        public int hashCode() {
            return Objects.hash(Objects.hash(userID1, userID2)+Objects.hash(userID2, userID1));
        }
    }

    private User user1, user2;
    private LocalDate date;
    /**
     * Creates a Friendship object with the two users give.
     * @param user1 User type object.
     * @param user2 User type object.
     */
    public Friendship(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    /**
     * Gets the first user of the Friendship.
     * @return User type object.
     */
    public User getUser1() {
        return user1;
    }

    /**
     * Gets the second user of the Friendship.
     * @return User type object.
     */
    public User getUser2() {
        return user2;
    }

    /**
     * Sets the first user of the Friendship.
     * @param user1 User type object.
     */
    public void setUser1(User user1) {
        this.user1 = user1;
    }

    /**
     * Sets the second user of the Friendship.
     * @param user2 User type object.
     */
    public void setUser2(User user2) {
        this.user2 = user2;
    }

    @Override
    public String toString() {
        return "Prietenie{" +
                "user1=" + user1 +
                ", user2=" + user2 + "data de la care sunt prieteni: "+date.format(Constants.DATE_FORMATTER)+
                '}';
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }
}
