package com.map_toysocialnetwork_gui.UI;

import com.map_toysocialnetwork_gui.Service.Service;

import java.util.Scanner;

public class NormalUserInterface extends MainUserInterface{
    protected String loggedUsername;
    public NormalUserInterface(Service service, String username) {
        super(service);
        this.loggedUsername = username;
    }

    @Override
    protected void showMenu() {
        System.out.println();
        System.out.println("Logat drept: " + loggedUsername);
        System.out.println("1. Cereri prietenie");
        System.out.println("2. Mesaje");
        System.out.println("0. Iesire");
    }

    @Override
    protected int readCommand() {
        int option;
        while (true) {
            System.out.println("Introduceti comanda:");
            try {
                Scanner in = new Scanner(System.in);
                option = Integer.parseInt(in.nextLine());
                if (option < 0 || option > 2) {
                    throw new NumberFormatException();
                }
                break;
            }
            catch (NumberFormatException e) {
                System.out.println("Comanda invalida!");
            }
        }
        return option;
    }

    @Override
    protected void executeCommand(int inputCommand) {
        switch (inputCommand) {
            case 1 -> runFriendRequestsMenu();
            case 2 -> runChatMenu();
        }
    }

    private void runFriendRequestsMenu  () {
        FriendRequestsUserInterface friendRequestsUserInterface = new FriendRequestsUserInterface(service,this.loggedUsername);
        friendRequestsUserInterface.run();
    }

    private void runChatMenu() {
        ChatMenuInterface chatMenuInterface = new ChatMenuInterface(service, loggedUsername);
        chatMenuInterface.run();
    }

}
