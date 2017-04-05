/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gabriel.Controller;

import gabriel.models.Connection;
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

    HostCheckerAll hostCheckerAll;

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
                System.out.println("Node " + connectionSocket.getInetAddress()
                        + ":" + connectionSocket.getPort() + " is connected.");

            } catch (IOException ex) {
                Logger.getLogger(Introducer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void checkHostsBruteForce(String subnet) throws InterruptedException {
        hostCheckerAll = new HostCheckerAll(subnet, 255);
        hostCheckerAll.start();

        Thread.sleep(5000);
        System.out.println("Introducer sleeped 5 seconds");

        hostCheckerAll.getHostIps();
        System.out.println(hostCheckerAll.getHostIps());
    }

    public void checkHostTrial3() {
        try {
            int timeout = 2000;
            InetAddress[] addresses = InetAddress.getAllByName("localhost");
            for (InetAddress address : addresses) {
                if (address instanceof Inet4Address) {
                    if (address.isReachable(timeout)) {
                        System.out.printf("%s is reachable%n", address);
                    } else {
                        System.out.printf("%s could not be contacted%n", address);
                    }
                }
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(Introducer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
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
