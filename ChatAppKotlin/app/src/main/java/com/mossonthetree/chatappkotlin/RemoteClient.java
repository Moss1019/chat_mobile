package com.mossonthetree.chatappkotlin;

import java.io.Serializable;
import java.net.InetAddress;

public class RemoteClient implements Serializable {
    private final String identifier;

    private final InetAddress ipAddress;

    private final int port;

    public RemoteClient(String identifier, InetAddress ipAddress, int port) {
        this.identifier = identifier;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public String getIdentifier() {
        return identifier;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }
}
