package com.map_toysocialnetwork_gui.UI;

import com.map_toysocialnetwork_gui.Domain.User;
import com.map_toysocialnetwork_gui.Service.Service;

import java.util.Scanner;

/**
 * Defines the console user interface.
 */
public class MainUserInterface extends AbstractUserInterface {
    protected Service service;

    public MainUserInterface(Service service) {
        this.service = service;
    }

    @Override
    protected void showMenu() {
        System.out.println();
        System.out.println("Meniu principal:");
        System.out.println("1. Login");
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
                if (option < 0 || option > 6) {
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
        if(inputCommand==1) {
            logIn();
        }
    }

    /**
     * Performs the login operation.
     */
    private void logIn() {
        AbstractUserInterface userInterface;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Login drept:");
        String loggedUsername = scanner.nextLine();
        if (loggedUsername.equals("admin")) {
            userInterface = new AdminUserInterface(service);
        }
        else {
            User user = service.findUser(loggedUsername);
            if (user == null) {
                userInterface = null;
            } else {
                userInterface = new NormalUserInterface(service,loggedUsername);
            }
        }
        if (userInterface != null) {
            userInterface.run();
        }
        else {
            System.out.println("User inexistent!");
        }
    }
}
