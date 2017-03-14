/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gabriel;

import gabriel.Controller.Sender;
import gabriel.Controller.Receiver;
import gabriel.Controller.Introducer;
import gabriel.models.Connection;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author EmreDan
 * This class is to: Listen for connection, 
 *      send & receive messages from & to nodes,
 *      update locale & remote index tables,
 *      
 */
public class Gabriel {

    final static int RANDOM_PORT = 5000;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        ArrayList<Connection> connections = new ArrayList<>();
        Introducer mainIntroducer ;
        ServerSocket serverSocket;
        Receiver receiver;
        Sender sender;
        
        try {
            
           serverSocket= new ServerSocket(RANDOM_PORT);
           mainIntroducer = new Introducer(connections,serverSocket);
           receiver = new Receiver();
           sender = new Sender();
           mainIntroducer.checkHostsBruteForce("192.168.1");
           
           //TODO: A gui for user to choose request file. 
             
        } catch (IOException ex) {
            Logger.getLogger(Gabriel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
