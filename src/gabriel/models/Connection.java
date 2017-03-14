/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gabriel.models;

import java.net.Socket;

/**
 *
 * @author EmreDan
 * This class is to manage existing connections between nodes
 */
public class Connection implements Runnable{

    Socket connectionSocket;

    public Connection(Socket connectionSocket) {
        this.connectionSocket = connectionSocket;
    }

    
    
    
    
    @Override
    public void run() {
        
    }
    
    
    
}
