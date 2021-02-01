package com.mossonthetree.chatappkotlin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpClient implements Closeable, AutoCloseable {
    private InetAddress remoteHost;

    private short remotePort;

    private boolean inError;

    private DatagramSocket sock;

    public boolean getInError() {
        return inError;
    }

    public UdpClient(String ipAddress, short port) {
        try {
            remoteHost = InetAddress.getByName(ipAddress);
            remotePort = port;
            sock = new DatagramSocket(8082);
            inError = false;
        } catch (Exception ex) {
            inError = true;
            System.out.println(String.format("mossonthetreeapp %s", ex.getMessage()));
        }
    }

    public void sendData(String msg) {
        sendData(new AppMessage(1, msg));
    }

    public void sendData(AppMessage appMessage) {
        try {
            ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream(2048);
            ObjectOutputStream objOutStream = new ObjectOutputStream(byteOutStream);
            objOutStream.writeObject(appMessage);
            objOutStream.close();
            byte[] data = byteOutStream.toByteArray();
            DatagramPacket req = new DatagramPacket(data, 0, data.length, remoteHost, remotePort);
            byteOutStream.close();
            try {
                sock.send(req);
            } catch (Exception ex) {
                System.out.println("mossonthetreeapp " + ex);
            }
        } catch (Exception ex) {
            System.out.println("mossonthetreeapp " + ex);
        }
    }

    public AppMessage receivedData() {
        System.out.println("mossonthetreeapp Receiving...");
        try {
            byte[] buffer = new byte[2048];
            DatagramPacket res = new DatagramPacket(buffer, 0, buffer.length);
            sock.receive(res);
            ByteArrayInputStream byteInStream = new ByteArrayInputStream(res.getData(), 0, res.getLength());
            ObjectInputStream objInStream = new ObjectInputStream(byteInStream);
            AppMessage appMessage = (AppMessage)objInStream.readObject();
            objInStream.close();
            byteInStream.close();
            return appMessage;
        } catch (Exception ex) {
            System.out.println("mossonthetreeapp " + ex.toString());
        }
        return new AppMessage(0, null);
    }

    @Override
    public void close() {
        try {
            sock.close();
        } catch (Exception ex) {
        }
        inError = true;
    }
}
