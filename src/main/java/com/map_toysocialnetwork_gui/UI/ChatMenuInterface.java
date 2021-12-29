package com.map_toysocialnetwork_gui.UI;

import com.map_toysocialnetwork_gui.Domain.DTO.Conversation;
import com.map_toysocialnetwork_gui.Domain.Entity;
import com.map_toysocialnetwork_gui.Domain.Message;
import com.map_toysocialnetwork_gui.Domain.User;
import com.map_toysocialnetwork_gui.Repository.RepositoryExceptions.RepositoryException;
import com.map_toysocialnetwork_gui.Service.Service;
import com.map_toysocialnetwork_gui.Service.ServiceExceptions.ServiceException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ChatMenuInterface extends NormalUserInterface {
    public ChatMenuInterface(Service service, String username) {
        super(service, username);
    }

    @Override
    protected void showMenu() {
        System.out.println();
        System.out.println("Logat drept: " + loggedUsername);
        System.out.println("1. Dati un mesaj nou");
        System.out.println("2. Vizualizati conversatii");
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
            case 1 -> sendNewMessage();
            case 2 -> viewConversations();
        }
    }

    /**
     * Reads the details of a new message and sends it.
     */
    private void sendNewMessage() {
        Scanner scanner = new Scanner(System.in);
        List<String> receiversList = readReceivers();
        if (receiversList.size()==0) {
            System.out.println("Numar de destinatari invalid!");
        }
        else {
            String messageText = readMessage();
            try {
                service.sendNewMessage(loggedUsername, receiversList, messageText, LocalDateTime.now());
                System.out.println("Mesaj trimis cu succes!");
            } catch (RepositoryException | ServiceException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private List<String> readReceivers() {
        Scanner scanner = new Scanner(System.in);
        List<String> allReceivers = new ArrayList<>();
        String currentReceiver = "";
        System.out.println("Introduceti destinatarii mesajului. Atentie! Trebuie sa fiti prieteni!");
        do {
            System.out.println("Dati username-ul destinatarului (Enter pentru oprire):");
            currentReceiver = scanner.nextLine();
            if (!currentReceiver.equals("")) {
                allReceivers.add(currentReceiver);
            }
        }while(!currentReceiver.equals(""));
        return allReceivers;
    }

    private String readMessage() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduceti textul mesajului:");
        String message = scanner.nextLine();
        return message;
    }

    private void viewConversations() {
        ConversationsInterface conversationsInterface = new ConversationsInterface(service,loggedUsername);
        conversationsInterface.run();
    }

    private class ConversationsInterface extends ChatMenuInterface{

        private Map<Integer, Conversation> optionToMessage;

        public ConversationsInterface(Service service, String username) {
            super(service, username);
        }

        @Override
        protected void showMenu() {
            System.out.println();
            System.out.println("Logat drept: " + loggedUsername);
            setOptionToMessage(service.getAllConversationsFor(service.findUser(loggedUsername)));
            if (optionToMessage.values().size()==0) {
                System.out.println("Nu aveti nicio conversatie!");
            }
            else {
                System.out.println("Aveti urmatoarele conversatii: ");
                optionToMessage.forEach((option, conversation) -> {
                    List<User> allParticipants = conversation.getAllUsers();
                    String toBePrinted = option.toString() + ". ";
                    for (int i = 0; i < allParticipants.size() - 1; i++) {
                        toBePrinted += allParticipants.get(i).getFirstName() + " " + allParticipants.get(i).getLastName();
                        toBePrinted += ", ";
                    }
                    toBePrinted += allParticipants.get(allParticipants.size()-1).getFirstName() + " " + allParticipants.get(allParticipants.size()-1).getLastName();
                    System.out.println(toBePrinted);
                });
            }
            System.out.println("0. Iesire");
        }

        private void setOptionToMessage(List<Conversation> allConversations) {
            int option = 1;
            optionToMessage = new HashMap<>();
            for (Conversation conversation:
                 allConversations) {
                optionToMessage.put(option, conversation);
                option++;
            }
        }

        @Override
        protected int readCommand() {
            int option;
            while (true) {
                System.out.println("Introduceti comanda:");
                try {
                    Scanner in = new Scanner(System.in);
                    option = Integer.parseInt(in.nextLine());
                    if (option < 0 || option > optionToMessage.size()) {
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
            SingleConversationInterface singleConversationInterface = new SingleConversationInterface(service,
                    loggedUsername,
                    inputCommand,
                    optionToMessage);
            singleConversationInterface.run();
        }

        private class SingleConversationInterface extends ConversationsInterface {

            private int option;
            private Map<Integer, Conversation> optionToMessage;

            public SingleConversationInterface(Service service,
                                               String username,
                                               int inputCommand,
                                               Map<Integer, Conversation> optionToMessage) {
                super(service, username);
                this.option = inputCommand;
                this.optionToMessage = optionToMessage;
            }

            @Override
            protected void showMenu() {
                System.out.println();
                System.out.println("Logat drept: " + loggedUsername);
                Conversation currentConversation = optionToMessage.get(option);
                List<User> currentConversationUsers = currentConversation.getAllUsers();
                List<Message> currentConversationMessages = currentConversation.getAllMessages();
                String allUsersPrint = "Useri din conversatie: ";
                for(int i=0; i<currentConversationUsers.size()-1; i++) {
                    allUsersPrint += currentConversationUsers.get(i).getFirstName() + " " + currentConversationUsers.get(i).getLastName();
                    allUsersPrint += ", ";
                }
                allUsersPrint += currentConversationUsers.get(currentConversationUsers.size()-1).getFirstName() + " " + currentConversationUsers.get(currentConversationUsers.size()-1).getLastName();
                System.out.println(allUsersPrint);
                System.out.println();
                currentConversationMessages.forEach(System.out::println);
                System.out.println("1. Raspundeti");
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
                        if (option < 0 || option > 1) {
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
                if (inputCommand==1) {
                    String messageText = ChatMenuInterface.this.readMessage();
                    List<User> receivers = optionToMessage.get(option).getAllUsers();
                    receivers.remove(service.findUser(loggedUsername));
                    List<String> receiversUsernames = receivers.stream()
                            .map(Entity::getId)
                            .toList();
                    Message lastMessage = optionToMessage.get(option).getAllMessages().get(optionToMessage.get(option).getAllMessages().size()-1);
                    try{
                        service.replyToMessage(loggedUsername, receiversUsernames, messageText, LocalDateTime.now(), lastMessage);
                        System.out.println("Mesaj trimis cu succes!");
                        finished = true;
                    }
                    catch (RepositoryException | ServiceException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }
}
