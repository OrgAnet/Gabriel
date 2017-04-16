/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gabriel.Controller;

import gabriel.models.Connection;
import gabriel.models.Node;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author EmreDan This class is to Introduce new node to the current node. It
 * creates a Connection object and listens for new connection.
 */
public class Introducer implements Runnable {

    ArrayList<Node> nodes = new ArrayList<>();
    HostCheckerAll hostCheckerAll;

    public Introducer(ArrayList<Node> connections) {
        this.nodes = connections;
    }

    @Override
    public void run() {
        // When new connection comes add it to connections
//        while (true) {
//            //TODO: Search for why we should use logger.
//            System.out.println("Node " + connectionSocket.getInetAddress()
//                    + ":" + connectionSocket.getPort() + " is connected.");
//        }
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

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<Node> nodes) {
        this.nodes = nodes;
    }

    public HostCheckerAll getHostCheckerAll() {
        return hostCheckerAll;
    }

    public void setHostCheckerAll(HostCheckerAll hostCheckerAll) {
        this.hostCheckerAll = hostCheckerAll;
    }

}
