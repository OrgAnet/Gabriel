package org.organet.commons.gabriel;

import org.organet.commons.gabriel.Model.Connection;
import org.organet.commons.inofy.Index;
import org.organet.commons.inofy.Model.SharedFile;

import java.io.*;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionManager {
  private static ArrayList<Connection> connections = new ArrayList<>();
  private final static Integer PORT_NO = 5000;

  static Index networkIndex = new Index();
  Map<String, Index> remoteIndeces = new HashMap<>(); // TODO Emre will implement this
  //  connectionId, (de-serialized) remote Index class

  public Integer getPORT_NO() {
    return PORT_NO;
  }

  public static void  startServer(){
    try {
      //Listening for a connection to be made
        System.out.println("server started");
      ServerSocket serverSocket = new ServerSocket(PORT_NO);
      System.out.println("TCPServer Waiting for client on port "+PORT_NO);
      while (true) {
        Socket connectionSocket = serverSocket.accept();

        Connection newIncomingConnection = new Connection(connectionSocket);

        getRemoteIndex(newIncomingConnection);

        connections.add(newIncomingConnection);

       // App.mainForm.getConnectionListModel().addElement(newIncomingConnection.getConnectionIp().toString());
      }
    } catch (IOException ex) {
      System.out.println("Input Output Exception on Listen Connection Action Performed");
      Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public static void sendIndex(Connection connection, Index myIndex) {
    try {
      try (OutputStream os = connection.getConnectionSocket().getOutputStream()) {
        ObjectOutputStream objectOS = new ObjectOutputStream(new BufferedOutputStream(os));
        objectOS.writeObject(myIndex);
        objectOS.close();
      }
    } catch (IOException ex) {
      Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public static void getRemoteIndex(Connection conn) {
    try {
      ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(conn.getConnectionSocket().getInputStream()));
      Index remoteIndex=new Index();
      boolean flag = true;
      while(flag){
          try{
              remoteIndex = (Index) in.readObject();
              flag = false;
          }catch(EOFException ex){
              flag=true;
          }
      }
        System.out.println("Index read successfully: " + remoteIndex.toString());
        conn.setConnectionIndex(remoteIndex);
        networkIndex.addAllSharedFiles(remoteIndex);
        for (SharedFile sh :networkIndex.getSharedFiles()) {
            conn.getConnectionIndex().add(sh);
            App.mainForm.getNetworkIndexListModel().addElement(sh.getScreenName());
        }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }
  }

  public static void broadcastLocalIndex() {
    //TODO send local index to all connections
  }


  public Index getNetworkIndex() {
    return networkIndex;
  }

  public void setNetworkIndex(Index networkIndex) {
    this.networkIndex = networkIndex;
  }

  public static Connection createConnection(Inet4Address connectionIp) {
    Connection newConnection = null;
    try {

      newConnection = new Connection(new Socket(connectionIp.getHostAddress(),PORT_NO) );

      sendIndex(newConnection, App.localIndex);
      connections.add(newConnection);
      return newConnection;
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Couldn't create connection");
      return null;
    }
  }
}
