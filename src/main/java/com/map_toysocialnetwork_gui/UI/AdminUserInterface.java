package com.map_toysocialnetwork_gui.UI;

import com.map_toysocialnetwork_gui.Domain.DTO.FriendDTO;
import com.map_toysocialnetwork_gui.Domain.Validators.ValidatorExceptions.ValidatorException;
import com.map_toysocialnetwork_gui.Service.Service;
import com.map_toysocialnetwork_gui.Service.ServiceExceptions.ServiceException;
import com.map_toysocialnetwork_gui.Utils.Constants;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class AdminUserInterface extends MainUserInterface {
    public AdminUserInterface(Service service) {
        super(service);
    }

    protected void showMenu() {
        System.out.println();
        System.out.println("Meniu admin:");
        System.out.println("1. Adauga user");
        System.out.println("2. Sterge user");
        System.out.println("3. Adauga prieten");
        System.out.println("4. Sterge prieten");
        System.out.println("5. Afisare numar comunitati");
        System.out.println("6. Afisare cea mai sociabila comunitate");
        System.out.println("7. Afisare prietenii unui user");
        System.out.println("8. Afisare prietenii unui user dintr-o anumita luna");
        System.out.println("0. Iesire");
    }

    protected int readCommand() {
        int option;
        while (true) {
            System.out.println("Introduceti comanda:");
            try {
                Scanner in = new Scanner(System.in);
                option = Integer.parseInt(in.nextLine());
                if (option < 0 || option > 8) {
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
            case 1 -> addUser();
            case 2 -> deleteUser();
            case 3 -> addFriend();
            case 4 -> deleteFriend();
            case 5 -> printNumberOfCommunities();
            case 6 -> printMostSociableCommunity();
            case 7 -> printFriendsForUser();
            case 8 -> printFriendsForUserMonth();
        }
    }


    private void addUser() {
        Scanner in = new Scanner(System.in);
        System.out.println("Dati username:");
        String username = in.nextLine();
        System.out.println("Dati prenume:");
        String firstName = in.nextLine();
        System.out.println("Dati nume:");
        String lastName = in.nextLine();
        try {
            service.addUser(username, firstName, lastName);
            System.out.println("User adaugat cu succes!");
        }
        catch (ValidatorException | ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteUser() {
        Scanner in = new Scanner(System.in);
        System.out.println("Dati username:");
        String username = in.nextLine();
        try {
            service.deleteUser(username);
            System.out.println("User sters cu succes!");
        }
        catch (ValidatorException | ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    private void addFriend() {
        Scanner in = new Scanner(System.in);
        System.out.println("Dati username-ul primului user:");
        String username1 = in.nextLine();
        System.out.println("Dati username-ul celui de-al doilea user:");
        String username2 = in.nextLine();
        System.out.println("Introduceti data, sub format YYYY-MM-DD");
        String data = in.nextLine();
        LocalDate date;
        try {
            date = LocalDate.parse(data);
            service.addFriend(username1, username2,  date);
            System.out.println("Prietenie adaugata cu succes!");
        }
        catch (ValidatorException | ServiceException| DateTimeException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void deleteFriend() {
        Scanner in = new Scanner(System.in);
        System.out.println("Dati username-ul primului user:");
        String username1 = in.nextLine();
        System.out.println("Dati username-ul celui de-al doilea user:");
        String username2 = in.nextLine();
        try {
            service.deleteFriend(username1, username2);
            System.out.println("Prietenie stearsa cu succes!");
        }
        catch (ValidatorException | ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    private void printNumberOfCommunities() {
        int noCommunities = service.numberOfCommunities();
        System.out.println("In retea exista " + noCommunities + " comunitati.");
    }

    private void printMostSociableCommunity() {
        var mostSociableCommunityList = service.mostSociableCommunity();
        if (mostSociableCommunityList.size()==0) {
            System.out.println("Nu exista comunitati in retea!");
        }
        else {
            System.out.println("Cea mai sociabila comunitate:");
            System.out.println(mostSociableCommunityList);
        }
    }

    private void printFriendsForUserMonth() {
        Scanner in = new Scanner(System.in);
        System.out.println("Dati username:");
        String username = in.nextLine();
        try {

            validateInput(username);
            System.out.println("Dati luna:");
            String lunaString = in.nextLine();
            try {
                validateInput(lunaString);
                try {
                    Integer month = Integer.parseInt(lunaString);
                    if (month < 1 || month > 12) {
                        System.out.println("Luna invalida!");
                    } else {
                        try {
                            List<FriendDTO> friendDTOS = service.getFriendsMonth(username, month);
                            if (friendDTOS.size() == 0) {
                                System.out.println("Nu sunt prietenii create in aceasta luna.");
                            } else {
                                for (FriendDTO friendDTO : service.getFriendsMonth(username, month)) {
                                    System.out.println(friendDTO.getUser().getLastName() + " " + friendDTO.getUser().getFirstName() + " " + friendDTO.getDate().format(Constants.DATE_FORMATTER));
                                }
                            }
                        } catch (ServiceException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }

                } catch (NumberFormatException ex) {
                    System.out.println(ex.getMessage());
                }
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
            }

        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }
    private void printFriendsForUser()
    {
        Scanner in = new Scanner(System.in);
        System.out.println("Dati username:");
        String username = in.nextLine();
        try{
            validateInput(username);
        }
        catch (IllegalArgumentException ex){
            System.out.println(ex.getMessage());
        }
        try{
            List<FriendDTO> friends = service.getFriendsOfUser(username);
            if(friends.size()==0)
            {
                System.out.println("Acest user nu are prieteni.");
            }
            else{
                for(FriendDTO friendDTO: service.getFriendsOfUser(username))
                {
                    System.out.println(friendDTO.getUser().getLastName()+" "+friendDTO.getUser().getFirstName()+" "+friendDTO.getDate().format(Constants.DATE_FORMATTER));
                }
            }
        }
        catch (ServiceException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
    private void validateInput(String input){
        if(input==""){
            throw new IllegalArgumentException("Nu ati introdus corect.");
        }
    }
}