/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gabriel.models;

import java.net.Inet4Address;
import java.net.Socket;

/**
 *
 * @author EmreDan
 * This class is to manage existing connections between nodes
 */
public class Node implements Runnable{

    Socket connectionSocket;
    Inet4Address connectionIp;

    public Node(Socket connectionSocket) {
        this.connectionSocket = connectionSocket;
    }

    public Node(Inet4Address connectionIp) {
        this.connectionIp = connectionIp;
    }

    
    
    @Override
    public void run() {
        
    }
    
    public Socket getConnectionSocket() {
        return connectionSocket;
    }

    public void setConnectionSocket(Socket connectionSocket) {
        this.connectionSocket = connectionSocket;
    }

    public Inet4Address getConnectionIp() {
        return connectionIp;
    }

    public void setConnectionIp(Inet4Address connectionIp) {
        this.connectionIp = connectionIp;
    }    
}
