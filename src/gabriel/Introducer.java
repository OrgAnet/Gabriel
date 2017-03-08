/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gabriel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author EmreDan This class is to Introduce new node to the current node. It
 * creates a Connection object and listens for new connection.
 */
public class Introducer implements Runnable {

    ArrayList<Connection> connections;
    ServerSocket serverSocket;

    public Introducer(ArrayList<Connection> connections, ServerSocket serverSocket) {
        this.connections = connections;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        // When new connection comes add it to connections
        while (true) {
            try {
                // Actively listen for new connections to serverSocket.
                Socket connectionSocket = serverSocket.accept();
                //TODO: Search for why Logger class is better than println.
                System.out.println("Node " + connectionSocket.getInetAddress() + ":" + connectionSocket.getPort() + " is connected.");

            } catch (IOException ex) {
                Logger.getLogger(Introducer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public ArrayList<Connection> getConnections() {
        return connections;
    }

    public void setConnections(ArrayList<Connection> connections) {
        this.connections = connections;
    }

}
