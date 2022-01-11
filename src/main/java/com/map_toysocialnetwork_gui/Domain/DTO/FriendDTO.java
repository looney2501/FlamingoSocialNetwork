package com.map_toysocialnetwork_gui.Domain.DTO;

import com.map_toysocialnetwork_gui.Domain.User;
import com.map_toysocialnetwork_gui.Utils.Constants;

import java.time.LocalDate;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendDTO friendDTO = (FriendDTO) o;
        return Objects.equals(user, friendDTO.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }
}
