/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gabriel.Controller;

import gabriel.models.Connection;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
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

                //TODO: Search for why we should use logger.
                System.out.println("Node " + connectionSocket.getInetAddress() +
                        ":" + connectionSocket.getPort() + " is connected.");

            } catch (IOException ex) {
                Logger.getLogger(Introducer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void checkHostsBruteForce(String subnet) {
        //FIXME: This method is too slow.
        int timeout = 1000;
        for (int i = 1; i < 255; i++) {
            try {
                String host = subnet + "." + i;
                if (InetAddress.getByName(host).isReachable(timeout)) {
                    System.out.println(host + " is reachable");
                }
            } catch (IOException ex) {
                Logger.getLogger(Introducer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void checkHostsx() {
        // faster solution but not exactly what we need.
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                
                Enumeration ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    InetAddress ia = (InetAddress) ias.nextElement();
                       
                    System.out.println(ia.getHostAddress());
                }

            }
        } catch (SocketException ex) {
            Logger.getLogger(Introducer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Connection> getConnections() {
        return connections;
    }

    public void setConnections(ArrayList<Connection> connections) {
        this.connections = connections;
    }

}
