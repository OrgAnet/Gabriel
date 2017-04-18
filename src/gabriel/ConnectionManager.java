/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gabriel;

import gabriel.models.Connection;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TheDoctor
 */
public class ConnectionManager {

    private ArrayList<Connection> connections = new ArrayList<>();
    private final Integer PORT_NO=5000;
    public ConnectionManager() {

    }

    public void addConnection(Connection conn) {
        this.connections.add(conn);
    }

    void startConnection(Connection newConnection) {
        try {
            newConnection.setConnectionSocket(new Socket(newConnection.getConnectionIp(), PORT_NO));
        } catch (IOException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
