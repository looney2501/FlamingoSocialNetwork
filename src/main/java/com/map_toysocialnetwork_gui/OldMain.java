package com.map_toysocialnetwork_gui;

import com.map_toysocialnetwork_gui.Domain.Validators.UserValidator;
import com.map_toysocialnetwork_gui.Repository.SQLDataBaseRepository.FriendshipDBRepository;
import com.map_toysocialnetwork_gui.Repository.SQLDataBaseRepository.FriendshipRequestDBRepository;
import com.map_toysocialnetwork_gui.Repository.SQLDataBaseRepository.MessageDBRepository;
import com.map_toysocialnetwork_gui.Repository.SQLDataBaseRepository.UserDBRepository;
import com.map_toysocialnetwork_gui.Service.Service;
import com.map_toysocialnetwork_gui.UI.MainUserInterface;

public class OldMain {
    public static void main(String[] args) {
//        UserFileRepository userFileRepository = new UserFileRepository(".\\data\\users.csv");
//        FriendshipFileRepository friendshipFileRepository =
//                new FriendshipFileRepository(".\\data\\friendships.csv", userFileRepository);
        String url = "jdbc:postgresql://localhost:5432/ToySocialNetwork";
        String username = "postgres";
        String password = "postgres";
        UserDBRepository userDBRepository = new UserDBRepository(url, username, password);
        FriendshipDBRepository friendshipDBRepository = new FriendshipDBRepository(userDBRepository, url, username, password);
        MessageDBRepository messageDBRepository = new MessageDBRepository(userDBRepository, url, username, password);
        UserValidator userValidator = new UserValidator();
        FriendshipRequestDBRepository friendshipRequestDBRepository = new FriendshipRequestDBRepository(userDBRepository,"jdbc:postgresql://localhost:5432/ToySocialNetwork", "postgres", "postgres");
        Service service = new Service(userDBRepository, friendshipDBRepository,friendshipRequestDBRepository, messageDBRepository, userValidator);
        MainUserInterface ui = new MainUserInterface(service);
        ui.run();
    }

}
