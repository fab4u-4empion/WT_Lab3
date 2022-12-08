package by.bsuir.ufpnjh.connection;

import java.io.Serializable;

public class Message implements Serializable {

    private final MessageType typeMessage;

    private String textMessage;

    public Message(MessageType typeMessage) {
        this.typeMessage = typeMessage;
    }

    public Message(MessageType typeMessage, String textMessage) {
        this.typeMessage = typeMessage;
        this.textMessage = textMessage;
    }

    public MessageType getTypeMessage() {
        return typeMessage;
    }

    public String getTextMessage() {
        return textMessage;
    }
}
