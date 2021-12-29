package com.map_toysocialnetwork_gui.UI;

import com.map_toysocialnetwork_gui.Domain.FriendshipRequest;
import com.map_toysocialnetwork_gui.Service.Service;
import com.map_toysocialnetwork_gui.Service.ServiceExceptions.ServiceException;

import java.util.List;
import java.util.Scanner;

public class FriendRequestsUserInterface extends MainUserInterface {

    String username;
    public FriendRequestsUserInterface(Service service, String usernameParam) {
        super(service);
        this.username=usernameParam;
    }

    @Override
    protected void showMenu() {
        System.out.println();
        System.out.println("Logat drept: " + username);
        System.out.println("1. Trimite cerere de prietenie");
        System.out.println("2. Accepta cerere de prietenie");
        System.out.println("3. Respinge cerere primita");
        System.out.println("4. Vizualizare cererile mele de prietenie");
        System.out.println("0. Iesire");
    }

    @Override
    protected int readCommand() {
        int option;
        while (true) {
            System.out.println("Introduceti comanda: ");
            try {
                Scanner in = new Scanner(System.in);
                option = Integer.parseInt(in.nextLine());
                if (option < 0 || option > 5) {
                    throw new NumberFormatException();
                }
                break;
            } catch (NumberFormatException ex) {
                System.out.println("Comanda invalida!");
            }
        }
        return option;
    }


    @Override
    protected void executeCommand(int inputCommand) {
        switch(inputCommand){
            case 1 -> sendFriendRequest();
            case 2 -> acceptFriendRequest();
            case 3 -> rejectFriendRequest();
            case 4 -> printFriendRequests();
        }
    }

    private void sendFriendRequest(){
        System.out.println("Catre:");
        Scanner in = new Scanner(System.in);
        String usernameFriend = in.nextLine();
        try {
            service.sendFriendRequest(this.username,usernameFriend);
            System.out.println("Cerere trimisa!");
        }
        catch (ServiceException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void acceptFriendRequest(){
        System.out.println("Username friend request de acceptat:");
        Scanner in = new Scanner(System.in);
        String usernameFriend = in.nextLine();
        try {
            service.acceptFriendRequest(usernameFriend, this.username);
            System.out.println("Cerere acceptata!");
        }
        catch (ServiceException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void rejectFriendRequest(){
        System.out.println("Username friend request de sters:");
        Scanner in = new Scanner(System.in);
        String usernameFriend = in.nextLine();
        try {
            service.rejectFriendRequest(usernameFriend, this.username);
            System.out.println("Cerere respinsa!");
        }
        catch (ServiceException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void printFriendRequests(){
        try {
            List<FriendshipRequest> friendshipRequestList = service.getAllFriendRequestsFor(this.username);
            if (friendshipRequestList.size() == 0) {
                System.out.println("Nu sunt cereri de prietenie de afisat.");
            }
            else {
                friendshipRequestList.forEach(System.out::println);
            }
        }
        catch (ServiceException ex){
            System.out.println(ex.getMessage());
        }
    }
}
