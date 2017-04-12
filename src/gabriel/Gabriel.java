/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gabriel;

import gabriel.Controller.Sender;
import gabriel.Controller.Receiver;
import gabriel.Controller.Introducer;
import gabriel.models.Node;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author EmreDan This class is to: Listen for connection, send & receive
 * messages from & to nodes, update locale & remote index tables
 *
 */
public class Gabriel {

    final static int RANDOM_PORT = 5000;
    String subnet = "192.168.1";
    
    private ArrayList<Node> connections;
    private Introducer mainIntroducer;
    private ServerSocket serverSocket;
    private Receiver receiver;
    private Sender sender;

    public Gabriel() {
        try {
            connections = new ArrayList<>();
            serverSocket=new ServerSocket();
            sender = new Sender();
            receiver = new Receiver();
            mainIntroducer=new Introducer(connections, serverSocket);
        } catch (IOException ex) {
            Logger.getLogger(Gabriel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   
    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
//    public void main() {
//
//        try {
//            serverSocket = new ServerSocket(RANDOM_PORT);
//            mainIntroducer = new Introducer(connections, serverSocket);
//            receiver = new Receiver();
//            sender = new Sender();
//            //mainIntroducer.checkHostsBruteForce();;
//            
//
//            //TODO: A gui for user to choose request file. 
//        } catch (IOException ex) {
//            Logger.getLogger(Gabriel.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    public ArrayList<Node> getConnections() {
        return connections;
    }

    public void setConnections(ArrayList<Node> connections) {
        this.connections = connections;
    }

    public Introducer getMainIntroducer() {
        return mainIntroducer;
    }
    public void setMainIntroducer(Introducer mainIntroducer) {
        this.mainIntroducer = mainIntroducer;
    }
    public ServerSocket getServerSocket() {
        return serverSocket;
    }
    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    public Receiver getReceiver() {
        return receiver;
    }
    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }
    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }
    
    public String getSubnet() {
        return subnet;
    }

    public void setSubnet(String subnet) {
        this.subnet = subnet;
    }

    
}
