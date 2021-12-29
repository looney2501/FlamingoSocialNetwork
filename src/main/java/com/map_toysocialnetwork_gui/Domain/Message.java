package com.map_toysocialnetwork_gui.Domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Defines the message entity.
 */
public class Message extends Entity<Integer>{
    private User from;
    private List<User> to;
    private String text;
    private LocalDateTime dateTime;
    private Integer repliesTo;

    /**
     * Creates a new message object.
     * @param from User object representing the sender of the message.
     * @param to List of User objects representing the receivers of the message.
     * @param message String representing the text of the message
     * @param dateTime timestamp of the message
     * @param repliesTo Reference to another Message object representing the message to which the one created replies to;
     *                  null if the message does not reply to anything
     */
    public Message(User from, List<User> to, String message, LocalDateTime dateTime, Integer repliesTo) {
        this.from = from;
        this.to = to;
        this.text = message;
        this.dateTime = dateTime;
        this.repliesTo = repliesTo;
    }

    /**
     * Gets the sender of the message.
     * @return User representing the sender of the message.
     */
    public User getFrom() {
        return from;
    }

    /**
     * Gets the receivers of the message.
     * @return List of Users representing the receivers of the message.
     */
    public List<User> getTo() {
        return to;
    }

    /**
     * Gets the text of the message.
     * @return String representing the text of the message.
     */
    public String getText() {
        return text;
    }

    /**
     * Gets the timestamp of the message.
     * @return LocalDateTime object representing the timestamp of the message.
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Gets the message to which this one replies to.
     * @return Message object representing the message to which this one replies to.
     */
    public Integer getRepliesTo() {
        return repliesTo;
    }

    /**
     * Sets the message to which the current one is a reply to.
     * @param repliesTo Message object.
     */
    public void setRepliesTo(Integer repliesTo) {
        this.repliesTo = repliesTo;
    }

    @Override
    public String toString() {
        String toBePrinted = "";
        toBePrinted += "(" + dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + ") ";
        toBePrinted += from + ": ";
        toBePrinted += text;
        return toBePrinted;
    }
}
