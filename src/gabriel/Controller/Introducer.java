/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gabriel.Controller;

import gabriel.models.Node;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author EmreDan This class is to Introduce new node to the current node. It
 * creates a Connection object and listens for new connection.
 */
public class Introducer implements Runnable {

    ArrayList<Node> nodes;
    ServerSocket serverSocket;

    HostCheckerAll hostCheckerAll;

    public Introducer(ArrayList<Node> connections, ServerSocket serverSocket) {
        this.nodes = connections;
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
                System.out.println("Node " + connectionSocket.getInetAddress()
                        + ":" + connectionSocket.getPort() + " is connected.");

            } catch (IOException ex) {
                Logger.getLogger(Introducer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void checkHostsBruteForce(String subnet) {
        try {
            hostCheckerAll = new HostCheckerAll(subnet, 255);
            hostCheckerAll.run();
            
            hostCheckerAll.getHostIps().forEach((hostIp) -> {
                try {
                    Inet4Address ipAddress = (Inet4Address) Inet4Address.getByName(hostIp);
                    this.nodes.add(new Node(ipAddress));
                } catch (UnknownHostException ex) {
                    System.out.println("Error on Introducer.getConnections()!");
                }
            });

            System.out.println(hostCheckerAll.getHostIps());
        } catch (Exception ex) {
            Logger.getLogger(Introducer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Node> getConnections() {
        return nodes;
    }

    public void setConnections(ArrayList<Node> connections) {
        this.nodes = connections;
    }

}
