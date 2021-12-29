package com.map_toysocialnetwork_gui.Domain;

import com.map_toysocialnetwork_gui.Domain.Factory.UserFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {
    @Test
    void test_message() {
        UserFactory userFactory = UserFactory.getInstance();
        User user1 = userFactory.createObject("mircea2501", "Mircea", "Cozarev");
        User user2 = userFactory.createObject("user1", "Ion1", "Pop1");
        User user3 = userFactory.createObject("user2", "Ion2", "Pop2");
        User user4 = userFactory.createObject("user3", "Ion3", "Pop3");
        List<User> userList = Arrays.asList(user2, user3, user4);
        String message_text = "Salut tuturor!";
        LocalDateTime dateTime = LocalDateTime.of(2021, 9, 30, 13, 25);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Message message = new Message(user1, userList, message_text, dateTime, null);
        assertEquals(message.getFrom().getFirstName(), "Mircea");
        assertEquals(message.getTo().size(), 3);
        assertEquals(message.getText(), "Salut tuturor!");
        assertEquals(message.getDateTime().format(formatter), "2021-09-30 13:25");
        assertNull(message.getRepliesTo());
    }

    @Test
    void test_message_replies() {
        UserFactory userFactory = UserFactory.getInstance();
        User user1 = userFactory.createObject("mircea2501", "Mircea", "Cozarev");
        User user2 = userFactory.createObject("user1", "Ion1", "Pop1");
        User user3 = userFactory.createObject("user2", "Ion2", "Pop2");
        User user4 = userFactory.createObject("user3", "Ion3", "Pop3");
        String message_text = "Salut tuturor!";
        LocalDateTime dateTime = LocalDateTime.of(2021, 9, 30, 13, 25);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Message message1 = new Message(user1, Arrays.asList(user2, user3, user4), message_text, dateTime, null);
        message1.setId(1);
        Message message2 = new Message(user2, List.of(user1), message_text, dateTime, 1);
        message2.setId(2);
        Message message3 = new Message(user3, List.of(user1), message_text, dateTime, 2);
        assertEquals(message2.getRepliesTo(), 1);
        assertEquals(message3.getRepliesTo(), 2);
    }
}