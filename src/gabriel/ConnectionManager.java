/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gabriel;

import gabriel.models.Connection;
import gabriel.models.Index;
import gabriel.models.SharedFileHeader;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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

    Index myIndex = new Index();
    Index networkIndex = new Index();

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

    public Index getMyIndex() {
        return myIndex;
    }

    public void setMyIndex(Index myIndex) {
        this.myIndex = myIndex;
    }

    public Index getNetworkIndex() {
        return networkIndex;
    }

    public void setNetworkIndex(Index networkIndex) {
        this.networkIndex = networkIndex;
    }

    void addToNetworkIndex(Index incomingIndex) {
        this.networkIndex.getFileHeaders().addAll(incomingIndex.getFileHeaders());
    }

    Index getIndex(Socket connectionSocket) {
        Index incomingIndex = null;
        try {
            //Getting the ObjectInputStream to retrive file index
            ObjectInputStream incomingStream = new ObjectInputStream(new BufferedInputStream(connectionSocket.getInputStream()));
       
            incomingIndex = (Index) incomingStream.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return incomingIndex;
    }

}
