package com.map_toysocialnetwork_gui.Domain.DTO;

import com.map_toysocialnetwork_gui.Domain.User;
import com.map_toysocialnetwork_gui.Utils.Constants;

import java.time.LocalDate;

public class FriendDTO {
    private User user;
    private LocalDate date;

    public FriendDTO(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Friend:" +
                user.getLastName() +
                user.getFirstName() + date.format(Constants.DATE_FORMATTER);
    }
}
