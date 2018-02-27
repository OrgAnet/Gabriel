/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gabriel;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import gabriel.Controller.Introducer;
import gabriel.models.Node;

/**
 *
 * @author TheDoctor This class is to: Listen for connection, send & receive
 * messages from & to nodes, update locale & remote index tables
 *
 */
public class Gabriel {

    final static int RANDOM_PORT = 5000;
    String subnet = "192.168.1";

    private ArrayList<Node> nodeList;
    private Introducer mainIntroducer;
    private ConnectionManager connectionManager;

    public Node getNode(String ipAddress) {
        for (Node node : nodeList) {
            if (node.getConnectionIp().toString().equals(ipAddress)) {
                return node;
            }
        }
        try {
            System.out.println("gelen ip `" + ipAddress + "`");
            return new Node((Inet4Address) InetAddress.getByName(ipAddress));
        } catch (UnknownHostException ex) {
            Logger.getLogger(Gabriel.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public ArrayList<Node> getNodeList() {
        return nodeList;
    }

    public void setNodeList(ArrayList<Node> nodeList) {
        this.nodeList = nodeList;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public Gabriel() {
        nodeList = new ArrayList<>();
        mainIntroducer = new Introducer(nodeList);
        connectionManager = new ConnectionManager();
    }

    public ArrayList<Node> getNodes() {
        return nodeList;
    }

    public void setNodes(ArrayList<Node> nodeList) {
        this.nodeList = nodeList;
    }

    public Introducer getMainIntroducer() {
        return mainIntroducer;
    }

    public void setMainIntroducer(Introducer mainIntroducer) {
        this.mainIntroducer = mainIntroducer;
    }

    public String getSubnet() {
        return subnet;
    }

    public void setSubnet(String subnet) {
        this.subnet = subnet;
    }
}
