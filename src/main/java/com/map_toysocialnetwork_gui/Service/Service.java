package com.map_toysocialnetwork_gui.Service;

import com.map_toysocialnetwork_gui.Domain.DTO.Conversation;
import com.map_toysocialnetwork_gui.Domain.DTO.FriendDTO;
import com.map_toysocialnetwork_gui.Domain.Factory.FriendshipFactory;
import com.map_toysocialnetwork_gui.Domain.Factory.UserFactory;
import com.map_toysocialnetwork_gui.Domain.Friendship;
import com.map_toysocialnetwork_gui.Domain.FriendshipRequest;
import com.map_toysocialnetwork_gui.Domain.Message;
import com.map_toysocialnetwork_gui.Domain.User;
import com.map_toysocialnetwork_gui.Domain.Validators.UserValidator;
import com.map_toysocialnetwork_gui.Domain.Validators.ValidatorExceptions.ValidatorException;
import com.map_toysocialnetwork_gui.GraphUtils.GraphTraversal;
import com.map_toysocialnetwork_gui.Repository.Repository;
import com.map_toysocialnetwork_gui.Repository.RepositoryExceptions.RepositoryException;
import com.map_toysocialnetwork_gui.Repository.SQLDataBaseRepository.MessageDBRepository;
import com.map_toysocialnetwork_gui.Service.ServiceExceptions.ServiceException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;

/**
 * Defines the service layer which operates with all repositories.
 */
public class Service {
    private Repository<String, User> userRepository;
    private Repository<Friendship.FriendshipID, Friendship> friendshipRepository;
    private Repository<FriendshipRequest.FriendshipRequestID, FriendshipRequest> friendshipRequestRepository;
    private MessageDBRepository messageRepository;
    private UserFactory userFactory;
    private FriendshipFactory friendshipFactory;
    private UserValidator userValidator;

    /**
     * Creates a main.com.map_toysocialnetwork_gui.Service.com.map_toysocialnetwork_gui.Service object bound to all repositories.
     * @param userRepository UserFileRepository type repository.
     * @param friendshipRepository FriendshipFileRepository type repository.
     * @param messageRepository MessageRepository type repository.
     * @param userValidator UserValidator type Validator.
     */
    public Service(Repository<String, User> userRepository,
                   Repository<Friendship.FriendshipID, Friendship> friendshipRepository,
                   Repository<FriendshipRequest.FriendshipRequestID, FriendshipRequest> friendshipRequestRepository,
                   MessageDBRepository messageRepository, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        this.friendshipRequestRepository = friendshipRequestRepository;
        this.messageRepository = messageRepository;
        this.userValidator = userValidator;
        userFactory = UserFactory.getInstance();
        friendshipFactory = FriendshipFactory.getInstance(userRepository);
    }

    /**
     * Adds a new user in repository.
     * @param username String representing the username of the user.
     * @param firstName String representing the first name of the user.
     * @param lastName String representing the last name of the user.
     * @throws ValidatorException if the user attributes are not valid.
     * @throws ServiceException if the user cannot be added in the repository.
     */
    public void addUser(String username, String firstName, String lastName) {
        User u = userFactory.createObject(username, firstName, lastName);
        userValidator.validate(u);
        if (userRepository.save(u)!=null) {
            throw new ServiceException("User already exists!");
        }
    }

    /**
     * Deletes a user from the repository.
     * @param username String representing the username of the user.
     * @throws ValidatorException if the username is not valid.
     * @throws ServiceException if the user does not exist.
     */
    public void deleteUser(String username) {
        userValidator.validateUsername(username);
        if (userRepository.delete(username) == null) {
            throw new ServiceException("User does not exist!");
        }
        else {
            List<Friendship.FriendshipID> friendshipIDList = new ArrayList<>();
            for (Friendship friendship :
                    friendshipRepository.findAll()) {
                Friendship.FriendshipID id = friendship.getId();
                if (Objects.equals(id.getUserID1(), username) || Objects.equals(id.getUserID2(),
                        username)) {
                    friendshipIDList.add(id);
                }
            }
            for (Friendship.FriendshipID friendshipID: friendshipIDList) {
                friendshipRepository.delete(friendshipID);
            }
        }
    }

    /**
     * Finds the User matching the username given.
     * @param username String representing the username of the user.
     * @return The user with the specified id or null if there is no user with the given id.
     * @throws RepositoryException if username is null.
     */
    public User findUser(String username) {
        return userRepository.findOne(username);
    }

    /**
     * Adds a new friendship between two users.
     * @param username1 String representing the username of the first user.
     * @param username2 String representing the username of the second user.
     * @throws ValidatorException if the usernames are not valid.
     * @throws ServiceException if the friendship cannot be added in the repository.
     */
    public void addFriend(String username1, String username2, LocalDate date) {
        userValidator.validateUsername(username1);
        userValidator.validateUsername(username2);
        String message = "";
        if (userRepository.findOne(username1) == null) {
            message += "User" + username1 + " does not exist! ";
        }
        if (userRepository.findOne(username2) == null) {
            message +=("User " + username2 + " does not exist!");
        }
        if (!message.equals("")) {
            throw new ServiceException(message);
        }
        Friendship friendship = friendshipFactory.createObject(username1, username2);
        friendship.setDate(date);
        if (friendshipRepository.save(friendship) != null) {
            throw new ServiceException("Friendship already exists!");
        }
    }

    /**
     * Deletes a friendship between two users.
     * @param username1 String representing the username of the first user.
     * @param username2 String representing the username of the second user.
     * @throws ValidatorException if the usernames are not valid.
     * @throws ServiceException if the friendship does not exist in the repository.
     */
    public void deleteFriend(String username1, String username2) {
        userValidator.validateUsername(username1);
        userValidator.validateUsername(username2);
        String message = "";
        if (userRepository.findOne(username1) == null) {
            message += "User" + username1 + " does not exist! ";
        }
        if (userRepository.findOne(username2) == null) {
            message +=("User " + username2 + " does not exist!");
        }
        if (!message.equals("")) {
            throw new ServiceException(message);
        }
        Friendship friendship = friendshipFactory.createObject(username1, username2);
        if (friendshipRepository.delete(friendship.getId()) == null) {
            throw new ServiceException("Friendship does not exist!");
        }
    }

    /**
     * Gets the total number of communities in the friendship network.
     * @return integer representing the number of communities in the friendship network.
     */
    public int numberOfCommunities() {
        var adjLists = getAdjListNetwork();
        GraphTraversal<String> graphTraversal = new GraphTraversal<>(adjLists);
        var parents = graphTraversal.AllNodesDFS();
        int components = 0;
        for(var parent:parents.values()) {
            if (parent==null) {
                components++;
            }
        }
        return components;
    }

    /**
     * Gets all the adjacency lists in the network of friendships.
     * @return a map of list of strings representing the adjacency lists.
     */
    private Map<String, List<String>> getAdjListNetwork() {
        Map<String, List<String>> friendshipAdjList = new HashMap<>();
        for (Friendship friendship :friendshipRepository.findAll()) {
            String username1 = friendship.getId().getUserID1();
            String username2 = friendship.getId().getUserID2();
            var prev = friendshipAdjList.get(username1);
            if (prev==null) {
                List<String> adjList = new ArrayList<>();
                adjList.add(username2);
                friendshipAdjList.put(username1, adjList);
            }
            else {
                friendshipAdjList.get(username1).add(username2);
            }
            prev = friendshipAdjList.get(username2);
            if (prev==null) {
                List<String> adjList = new ArrayList<>();
                adjList.add(username1);
                friendshipAdjList.put(username2, adjList);
            }
            else {
                friendshipAdjList.get(username2).add(username1);
            }
        }
        return friendshipAdjList;
    }

    /**
     * Gets the most sociable community in the network.
     * @return List of users representing the most sociable community in the network.
     */
    public List<User> mostSociableCommunity() {
        var adjLists = getAdjListNetwork();
        GraphTraversal<String> graphTraversal = new GraphTraversal<>(adjLists);
        List<User> mostSociableCommunityList = new ArrayList<>();
        if (adjLists.size()==0) {
            return mostSociableCommunityList;
        }
        var longestPath = graphTraversal.computeLongestRoad();
        if (longestPath.size()==0) {
            return null;
        }
        var source = longestPath.get(0);
        var parents = graphTraversal.SingleSourceDFS(source);
        mostSociableCommunityList.add(userRepository.findOne(source));
        parents.forEach((node,parent)->{
            if(parent!=null) {
                mostSociableCommunityList.add(userRepository.findOne(node));
            }
        });
        return mostSociableCommunityList;
    }

    /** Function that helps me to transform a list of friendships from Iterable to List
     * @param friendships - the initial list
     * @return - the modified list
     */
    private List<Friendship> fromIterableToListFriendship(Iterable<Friendship> friendships)
    {
        List<Friendship> friendshipList = new ArrayList<>();
        friendships.forEach(friendshipList::add);
        return friendshipList;
    }

    /**
     * This method gets the list of friendships for a given user
     * @param username - id of user for whom we want to find friends
     * @return - list of friendships
     */
    public List<Friendship> getFriends(String username) {
        Predicate<Friendship> friendshipPredicate1 = x->x.getUser2().getId().equals(username);
        Predicate<Friendship> friendshipPredicate = x->x.getUser1().getId().equals(username);
        List<Friendship> friendships= fromIterableToListFriendship(friendshipRepository.findAll());
        return friendships.stream()
                .filter(friendshipPredicate1.or(friendshipPredicate))
                .distinct()
                .toList();
    }

    /**
     * This method creates a list of friends for the user, the dates when the friendship
     * started also included
     * @param username - the name of user
     * @return - list of friends
     */
    private List<FriendDTO> getDateForFriendship(String username){
        List<FriendDTO> friendDTOS = new ArrayList<>();
        List<Friendship> friendshipList = getFriends(username);
        for(Friendship friendship:friendshipList){
            if(friendship.getUser1().getId().equals(username)){
                FriendDTO friendDTO = new FriendDTO(friendship.getUser2());
                friendDTO.setDate(friendship.getDate());
                friendDTOS.add(friendDTO);
            }
            if(friendship.getUser2().getId().equals(username)){
                FriendDTO friendDTO = new FriendDTO(friendship.getUser1());
                friendDTO.setDate(friendship.getDate());
                friendDTOS.add(friendDTO);
            }
        }
        return friendDTOS;
    }

    /**
     * This method creates a list of friends for the given user. The list is sorted by last name and then by first name
     * @param username - name of user
     *                 It throws exception if the username is not found in repository
     * @return - list of friends
     */
    public List<FriendDTO> getFriendsOfUser(String username){
        if(userRepository.findOne(username)==null)
        {
            throw new ServiceException ("User does not exist!");
        }
        List<FriendDTO> friendDTOList = getDateForFriendship(username);
        Comparator<FriendDTO> userComparatorLastName = Comparator.comparing(x -> x.getUser().getLastName());
        Comparator<FriendDTO> userComparatorFirstName = Comparator.comparing(x -> x.getUser().getFirstName());
        return friendDTOList.stream()
                .sorted(userComparatorLastName.thenComparing(userComparatorFirstName))
                .toList();
    }

    /**
     * This method creates a list of friends for a user,
     * if the friendship was created in a given month
     * @param username - name of user (String)
     * @param month - month of the year (Integer)
     * @return - List of friends
     */
    public List<FriendDTO> getFriendsMonth(String username,Integer month){
        if(userRepository.findOne(username)==null)
        {
            throw new ServiceException ("User does not exist!");
        }
        List<FriendDTO> friendDTOList = getDateForFriendship(username);
        Comparator<FriendDTO> userComparatorLastName = Comparator.comparing(x -> x.getUser().getLastName());
        Comparator<FriendDTO> userComparatorFirstName = Comparator.comparing(x -> x.getUser().getFirstName());
        Predicate<FriendDTO> friendDTOPredicate = x->x.getDate().getMonth().getValue()==month;
        return friendDTOList.stream()
                .filter(friendDTOPredicate)
                .sorted(userComparatorLastName.thenComparing(userComparatorFirstName))
                .toList();
    }

    /**
     * Sends a new message to all receivers.
     * @param senderUsername String representing the username of the sender. Must be an existent user in the database.
     * @param listOfReceiversUsernames List of strings representing the
     * @param messageText String representing the text of the message.
     * @param dateTime LocalDateTime representing the timestamp of the message.
     * @throws ServiceException
     *              if the parameters are null,
     *              if the users do not exist,
     *              if there is no friendship between sender and receivers,
     *              if there is only one receiver and another conversation between the sender and the receiver already exists.
     */
    public void sendNewMessage(String senderUsername,
                            List<String> listOfReceiversUsernames,
                            String messageText,
                            LocalDateTime dateTime) {
        userValidator.validateUsername(senderUsername);
        for (String receiverUsername:listOfReceiversUsernames) {
            userValidator.validateUsername(receiverUsername);
        }
        if (dateTime == null) {
            throw new ServiceException("Invalid timestamp!");
        }
        if (messageText == null) {
            throw new ServiceException("Invalid message text!");
        }
        User senderUser = userRepository.findOne(senderUsername);
        if (senderUser == null) {
            throw new ServiceException("User " + senderUsername + " does not exist!");
        }
        List<User> receiverUsersList = new ArrayList<>();
        for (String receiverUsername:listOfReceiversUsernames) {
            User receiverUser = userRepository.findOne(receiverUsername);
            if (receiverUser==null) {
                throw new ServiceException("User " + receiverUsername + " does not exist!");
            }
            if (findFriendship(senderUsername, receiverUsername)==null) {
                throw new ServiceException("Friendship between " + senderUsername + " and "
                        + receiverUsername + " does not exist!");
            }
            receiverUsersList.add(receiverUser);
        }
        if(receiverUsersList.size()==1) {
            sendNewPrivateMessage(senderUser, receiverUsersList, messageText, dateTime);
        }
        else {
            sendNewGroupMessage(senderUser, receiverUsersList, messageText, dateTime);
        }
    }

    /**
     * Sends a new private message to a single user, unless there is a conversation between them already.
     * @param senderUser User instance
     * @param receiverUsers List of User instances. Must contain only one User object.
     * @param messageText String representing the message.
     * @param dateTime LocalDateTime instance.
     * @throws ServiceException if there is a converations between two users already.
     */
    private void sendNewPrivateMessage(User senderUser, List<User> receiverUsers, String messageText, LocalDateTime dateTime) {
        User receiverUser = receiverUsers.get(0);
        messageRepository.getAllFirstMessagesOfUser(senderUser).forEach(msg->{
            if(
                    msg.getTo().size()==1 && (
                    (msg.getFrom().equals(senderUser) && msg.getTo().contains(receiverUser)) ||
                    (msg.getFrom().equals(receiverUser) && msg.getTo().contains(senderUser))
                    )
            )
            {
                throw new ServiceException("Conversation already exists!");
            }
        });
        messageRepository.save(new Message(senderUser, receiverUsers, messageText, dateTime, null));
    }

    /**
     * Sends a new group message to a group of users.
     * @param senderUser User instance
     * @param receiverUsers List of User instances
     * @param messageText String representing the message
     * @param dateTime LocalDateTime instance
     */
    private void sendNewGroupMessage(User senderUser, List<User> receiverUsers, String messageText, LocalDateTime dateTime) {
        messageRepository.save(new Message(senderUser, receiverUsers, messageText, dateTime, null));
    }

    /**
     * Sends a reply to a message.
     * @param senderUsername String representing the username of the sender. Must be an existent user in the database.
     * @param listOfReceiversUsernames List of strings representing the
     * @param messageText String representing the text of the message.
     * @param dateTime LocalDateTime representing the timestamp of the message.
     * @throws ServiceException
     *              if the parameters are null,
     *              if the users do not exist,
     *              if there is no friendship between sender and receivers,
     *              if there is only one receiver and another conversation between the sender and the receiver already exists,
     *              if the message to be replied does not exist.
     */
    public Message replyToMessage(String senderUsername, List<String> listOfReceiversUsernames, String messageText, LocalDateTime dateTime, Message messageToBeReplied) {
        userValidator.validateUsername(senderUsername);
        for (String receiverUsername:listOfReceiversUsernames) {
            userValidator.validateUsername(receiverUsername);
        }
        if (dateTime == null) {
            throw new ServiceException("Invalid timestamp!");
        }
        if (messageText == null) {
            throw new ServiceException("Invalid message text!");
        }
        User senderUser = userRepository.findOne(senderUsername);
        if (senderUser == null) {
            throw new ServiceException("User " + senderUsername + " does not exist!");
        }
        List<User> receiverUsersList = new ArrayList<>();
        for (String receiverUsername:listOfReceiversUsernames) {
            User receiverUser = userRepository.findOne(receiverUsername);
            if (receiverUser==null) {
                throw new ServiceException("User " + receiverUsername + " does not exist!");
            }
            receiverUsersList.add(receiverUser);
        }
        Integer messageToBeRepliedID = messageToBeReplied.getId();
        if (messageRepository.findOne(messageToBeRepliedID) == null) {
            throw new ServiceException("Message to be replied must not be null!");
        }
        Message replyMessage = new Message(senderUser, receiverUsersList, messageText, dateTime, messageToBeRepliedID);
        messageRepository.save(replyMessage);
        return messageRepository.findReplyFor(messageToBeRepliedID);
    }

    /**
     * Gets all conversations for a given user.
     * @param user User instance.
     * @return List of Conversation instances.
     * @throws ServiceException if the user is null or if the user does not exist in the user repository.
     */
    //merge refactorizat
    public List<Conversation> getAllConversationsFor(User user) {
        if (user == null || userRepository.findOne(user.getId()) == null) {
            throw new ServiceException("User must not be null!");
        }
        List<Message> allFirstMessagesFor = messageRepository.getAllFirstMessagesOfUser(user);
        List<Conversation> allConversationsFor = new ArrayList<>();
        allFirstMessagesFor.forEach(msg->{
            Conversation currentConversation = getConversationFromMessage(msg);
            allConversationsFor.add(currentConversation);
        });
        return allConversationsFor;
    }

    /**
     * Gets the full conversation starting from a given message.
     * @param firstMessage Message instance. Must not be null.
     * @return list of messages representing the conversation. First entry in list corresponds to the first message in conversation.
     */
    private Conversation getConversationFromMessage(Message firstMessage) {
        List<Message> currentConversationMessages = new ArrayList<>();
        List<User> currentConversationUsers = new ArrayList<>();
        Message replyMessage = firstMessage;
        currentConversationUsers.add(firstMessage.getFrom());
        currentConversationUsers.addAll(firstMessage.getTo());
        currentConversationUsers.sort((user1, user2)->{
            String fullName1 = user1.getFirstName() + " " + user1.getLastName();
            String fullName2 = user2.getFirstName() + " " + user2.getLastName();
            return fullName1.compareTo(fullName2);
        });

        boolean hasNext = true;
        while(hasNext) {
            currentConversationMessages.add(replyMessage);
            replyMessage = messageRepository.findReplyFor(replyMessage.getId());
            if (replyMessage==null) {
                hasNext = false;
            }
        }
        return new Conversation(currentConversationMessages, currentConversationUsers);
    }

    /**
     * Finds the friendship between the users with the given usernames.
     * @param username1 String representing the username of the first user.
     * @param username2 String representing the username of the second user.
     * @return Friendship object representing the friendship between the two users,
     *          or null if there is no friendship between them.
     * @throws RepositoryException if username1 or username2 are null.
     */
    public Friendship findFriendship(String username1, String username2) {
        return friendshipRepository.findOne(new Friendship.FriendshipID(username1, username2));
    }

    /**
     * Sends a friend request
     * @param fromUsername - String representing username who wants to send a friend request
     * @param toUsername - String representing the receiver of the friend request
     *                   The status of friend request will be set to "pending"
     */
    public FriendshipRequest sendFriendRequest(String fromUsername, String toUsername) throws ServiceException{
        if(fromUsername.equals(toUsername)) {
            throw new ServiceException("Nu puteti trimite o cerere voua insiva!");
        }
        if(userRepository.findOne(toUsername)==null){
            throw new ServiceException("User does not exist");
        }
        if (friendshipRepository.findOne(new Friendship.FriendshipID(fromUsername, toUsername))!=null) {
            throw new ServiceException("Prietenie deja existenta!");
        }
        if (friendshipRequestRepository.findOne(new FriendshipRequest.FriendshipRequestID(fromUsername, toUsername)) != null) {
            throw new ServiceException("Cerere deja trimisa catre user " + toUsername + " !");
        }
        if (friendshipRequestRepository.findOne(new FriendshipRequest.FriendshipRequestID(toUsername, fromUsername))!=null) {
            throw new ServiceException("Aveti deja o cerere de prietenie de la userul " + toUsername);
        }
        User userFrom = userRepository.findOne(fromUsername);
        User userTo = userRepository.findOne(toUsername);
        if(userTo==null){
            throw new ServiceException("User not found!");
        }
        FriendshipRequest friendshipRequest = new FriendshipRequest(userFrom,userTo);
        FriendshipRequest.FriendshipRequestID friendshipRequestID = new FriendshipRequest.FriendshipRequestID(fromUsername,toUsername);
        friendshipRequest.setId(friendshipRequestID);
        friendshipRequest.setStatus("pending");
        friendshipRequest.setDate(LocalDate.now());
        friendshipRequestRepository.save(friendshipRequest);
        return friendshipRequest;
    }

    /**
     * Accepts friend request
     * @param senderUsername - String representing the user who sent the request
     * @param receiverUsername - String representing the user who needs to accept the request
     * @throws ServiceException if the friendship does not exist in the repository
     */
    public void acceptFriendRequest(String senderUsername, String receiverUsername){
        if(userRepository.findOne(senderUsername)==null){
            throw new ServiceException("User not found!");
        }
        User sender = userRepository.findOne(senderUsername);
        User receiver = userRepository.findOne(receiverUsername);

        FriendshipRequest friendshipRequest = new FriendshipRequest(sender,receiver);
        FriendshipRequest.FriendshipRequestID friendshipRequestID = new FriendshipRequest.FriendshipRequestID(senderUsername,receiverUsername);
        if(friendshipRequestRepository.findOne(friendshipRequestID)==null){
            throw new ServiceException("Cerere inexistenta!");
        }
        friendshipRequest.setId(friendshipRequestID);
        friendshipRequest.setStatus("approved");
        friendshipRequest.setDate(LocalDate.now());
        friendshipRequestRepository.update(friendshipRequest);
        Friendship friendship = new Friendship(sender,receiver);
        Friendship.FriendshipID friendshipID = new Friendship.FriendshipID(sender.getId(),receiver.getId());
        friendship.setId(friendshipID);
        friendship.setDate(LocalDate.now());
        friendshipRepository.save(friendship);
    }

    /**
     * Rejects friend request
     * @param username1 - String representing the user who sent the request
     * @param username2 - String representing the user who needs to accept the request
     * @throws ServiceException if the friendship does not exist in the repository
     */
    public void rejectFriendRequest(String username1, String username2){
        if(userRepository.findOne(username2)==null){
            throw new ServiceException("User not found!");
        }
        User user1 = userRepository.findOne(username1);
        User user2 = userRepository.findOne(username2);
        FriendshipRequest.FriendshipRequestID friendshipRequestID2 = new FriendshipRequest.FriendshipRequestID(username1,username2);
        if(friendshipRequestRepository.findOne(friendshipRequestID2)==null){
            throw new ServiceException("Cerere inexistenta!");
        }
        FriendshipRequest friendshipRequest = new FriendshipRequest(user1,user2);
        FriendshipRequest.FriendshipRequestID friendshipRequestID = new FriendshipRequest.FriendshipRequestID(username1, username2);
        friendshipRequest.setId(friendshipRequestID);
        friendshipRequest.setStatus("rejected");
        friendshipRequest.setDate(LocalDate.now());
        friendshipRequestRepository.update(friendshipRequest);
    }

    /**
     * Cancel a friend request previously sent
     * @param sender - username of user that sent the friendship request
     * @param receiver - username of user to whom the friendship request was sent
     * @throws com.map_toysocialnetwork_gui.Service.ServiceExceptions.ServiceException if the users don't exist in repository
     */
    public void cancelFriendshipRequest(String sender, String receiver){
        if(userRepository.findOne(sender)==null || userRepository.findOne(receiver)==null){
            throw new ServiceException("User not found!");
        }
        User senderUser = userRepository.findOne(sender);
        User receiverUser = userRepository.findOne(receiver);
        FriendshipRequest friendshipRequest = new FriendshipRequest(senderUser,receiverUser);
        FriendshipRequest.FriendshipRequestID friendshipRequestID = new FriendshipRequest.FriendshipRequestID(sender,receiver);
        friendshipRequest.setId(friendshipRequestID);
        friendshipRequest.setStatus("cancelled");
        friendshipRequest.setDate(LocalDate.now());
        friendshipRequestRepository.update(friendshipRequest);

    }

    /**
     * Function that helps to transfrom a list of iterable friendship requests to List of friendship requests
     * @param friendships - list of iterable friendship requests
     * @return - List of friendship requests
     */
    private List<FriendshipRequest> fromIterableToListFriendshipRequests(Iterable<FriendshipRequest> friendships)
    {
        List<FriendshipRequest> friendshipList = new ArrayList<>();
        friendships.forEach(friendshipList::add);
        return friendshipList;
    }

    /**
     * Gets all friend requests for a user (pending)
     * @throws com.map_toysocialnetwork_gui.Service.ServiceExceptions.ServiceException if repository is empty
     * @return list of Friend Requests, sorted by status
     */
    public List<FriendshipRequest> getAllFriendRequestsFor(String username){
        List<FriendshipRequest> friendshipRequests;
        if(friendshipRequestRepository==null){
            throw new ServiceException("No friend requests");
        }
        friendshipRequests = fromIterableToListFriendshipRequests(friendshipRequestRepository.findAll());
        if(friendshipRequests.size()==0){
            return new ArrayList<>();
        }
        else{
            return filterFriendRequestsForUser(username,friendshipRequests);
        }
    }

    /**
     * Gets all friendship requests from a user
     * @param usernameSender - username of user who sent the friendship requests
     * @throws com.map_toysocialnetwork_gui.Service.ServiceExceptions.ServiceException if repository is empty
     * @return list of Friendship Requests, filtered by sender
     */
    public List<FriendshipRequest> getAllFriendRequestsFrom(String usernameSender){
        List<FriendshipRequest> friendshipRequests;
        if(friendshipRepository==null){
            throw new ServiceException("No friend requests!");
        }
        friendshipRequests = fromIterableToListFriendshipRequests(friendshipRequestRepository.findAll());
        if(friendshipRequests.size()==0){
            return new ArrayList<>();
        }
        else{
            return filterFriendRequestsFromUser(usernameSender,friendshipRequests);
        }
    }

    /**
     * Method that filters a list of friendship requests by username of receiver
     * @param username - String representing the username of person for whom
     *                 we want to find friends
     * @param friendshipRequests - list of Friend Requests
     * @return - sorted, filtered list of friend requests
     */
    private List<FriendshipRequest> filterFriendRequestsForUser(String username, List<FriendshipRequest> friendshipRequests) {
        Predicate<FriendshipRequest> notNull = Objects::nonNull;
        Predicate<FriendshipRequest> predicateReceiver = x -> x.getUserReceiver().getId().equals(username);
        Predicate<FriendshipRequest> predicatePending = x->x.getStatus().equals("pending");
        return friendshipRequests.stream()
                .filter((notNull.and(predicateReceiver.and(predicatePending))))
                .toList();
    }

    /**
     * Method that filters a list of friendship requests by username of sender
     * @param sender - username of sender
     * @param friendshipRequests - list of Friend Requests
     * @return - sorted, filtered list of friend requests
     */
    private List<FriendshipRequest> filterFriendRequestsFromUser(String sender, List<FriendshipRequest> friendshipRequests){
        Predicate<FriendshipRequest> notNull = Objects::nonNull;
        Predicate<FriendshipRequest> predicateSender = x->x.getUserSender().getId().equals(sender);
        Predicate<FriendshipRequest> predicatePending = x->x.getStatus().equals("pending");
        return friendshipRequests.stream()
                .filter(notNull.and(predicateSender.and(predicatePending)))
                .toList();
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}

