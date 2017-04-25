/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gabriel;

import gabriel.models.Connection;
import gabriel.models.Index;
import gabriel.models.SharedFileHeader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
    private final Integer PORT_NO = 5000;

    public Integer getPORT_NO() {
        return PORT_NO;
    }

    public ConnectionManager() {

    }

    public void addConnection(Connection conn) {
        this.connections.add(conn);
    }

    Boolean startConnection(Connection newConnection) {
        try {
            newConnection.setConnectionSocket(new Socket(newConnection.getConnectionIp(), PORT_NO));
            Index myIndex = new Index();
            //falsely filled
            SharedFileHeader fakeSharedFile = new SharedFileHeader();
            fakeSharedFile.setName("ilk file header");

            myIndex.getFileHeaders().add(fakeSharedFile);
            sendData(newConnection, myIndex);
            addConnection(newConnection);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void sendData(Connection connection, Index myIndex) {
        try {
            try (OutputStream os = connection.getConnectionSocket().getOutputStream()) {
                ObjectOutputStream objectOS = new ObjectOutputStream(os);
                objectOS.writeObject(myIndex);
            }
        } catch (IOException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
