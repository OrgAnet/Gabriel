package org.organet.commons.gabriel;

import org.organet.commons.gabriel.Model.Connection;
import org.organet.commons.inofy.Index;

import java.io.*;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionManager {
  private ArrayList<Connection> connections = new ArrayList<>();
  private final Integer PORT_NO = 5000;

  Index networkIndex = new Index();

  public Integer getPORT_NO() {
    return PORT_NO;
  }

  public ConnectionManager() {
    // TODO
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void addConnection(Connection conn) {
    this.connections.add(conn);
  }

  void startServer(){

    try {
      //Listening for a connection to be made
      ServerSocket serverSocket = new ServerSocket(PORT_NO);
      System.out.println("TCPServer Waiting for client on port "+PORT_NO);
      while (true) {
        Socket connectionSocket = serverSocket.accept();

        Connection newIncomingConnection = new Connection(connectionSocket);
        addConnection(newIncomingConnection);

//      Index nodeIndex = connectionManager.getIndex(connectionSocket); // FIXME
//
//      connectionManager.addToNetworkIndex(nodeIndex);
//
//      // connectionManager.networkIndex.getFileHeaders().addAll(incomingData.getFileHeaders());
//      System.out.println(nodeIndex.getFileHeaders().get(0).getName());

        App.mainForm.getConnectionListModel().addElement(newIncomingConnection.getConnectionIp().toString());
      }
    } catch (IOException ex) {
      System.out.println("Input Output Exception on Listen Connection Action Performed");
      Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
    }



  }


  Boolean startConnection(Connection newConnection) {
    try {
      newConnection.setConnectionSocket(new Socket(newConnection.getConnectionIp(), PORT_NO));

      sendData(newConnection, App.localIndex);
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

  public Index getNetworkIndex() {
    return networkIndex;
  }

  public void setNetworkIndex(Index networkIndex) {
    this.networkIndex = networkIndex;
  }

//  void addToNetworkIndex(Index incomingIndex) { // FIXME
//    this.networkIndex.getFileHeaders().addAll(incomingIndex.getFileHeaders());
//  }

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

  public Connection createConnection(Inet4Address connectionIp) {

//todooo
    return new Connection(new Socket());
  }
}
