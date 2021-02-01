package com.mossonthetree.chatappkotlin;

import java.io.Serializable;

public class AppMessage implements Serializable {
    private final int type;

    private final Object data;

    public AppMessage(int type, Object data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return String.format("%d: %s", type, data.toString());
    }
}