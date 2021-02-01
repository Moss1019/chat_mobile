package com.mossonthetree.chatappkotlin;

import java.io.Serializable;

public class ClientMessage implements Serializable {
    private final String recipient;

    private final String msg;

    public ClientMessage(String recipient, String msg) {
        this.recipient = recipient;
        this.msg = msg;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMsg() {
        return msg;
    }
}
