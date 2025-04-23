package com.meloman.project.communications;

import com.meloman.project.service_model.User;

import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Client-side class handling user interactions and communication
 */

@Getter
@Setter

public class Client {
    private User currentUser;
    //private String serverAddress;

    private int serverPortTCP;
    private int serverPortUDP;

    /**
     * Initializes client
     */
    public Client(int portTCP, int portUDP, User newUser) {
        // this.serverAddress = serverAddress;
        this.serverPortTCP = portTCP;
        this.serverPortUDP = portUDP;
        this.currentUser = newUser;
    }

    public Response sendRequestTCP(Request request)
            throws IOException, ClassNotFoundException {

        try (Socket socket = new Socket("localhost", serverPortTCP);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in  = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(request);
            out.flush();

            return (Response) in.readObject();
        }
    }

    public Response sendRequestUDP(Request request)
            throws IOException, ClassNotFoundException {

        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(2500);

        // Serialize request to byte array
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
        objOut.writeObject(request);
        objOut.flush();
        byte[] requestBytes = byteOut.toByteArray();

        // Send the request to the server
        InetAddress serverAddress = InetAddress.getByName("localhost");
        DatagramPacket requestPacket = new DatagramPacket(requestBytes, requestBytes.length, serverAddress, serverPortUDP);
        socket.send(requestPacket);

        // Prepare to receive the response
        byte[] buffer = new byte[65535]; // Max UDP size
        DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
        socket.receive(responsePacket); // Blocking call

        // Deserialize response
        ByteArrayInputStream byteIn = new ByteArrayInputStream(responsePacket.getData(), 0, responsePacket.getLength());
        ObjectInputStream objIn = new ObjectInputStream(byteIn);
        Response response = (Response) objIn.readObject();

        socket.close();
        return response;
    }

}