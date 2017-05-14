package org.organet.commons.gabriel;

import org.organet.commons.gabriel.Model.Connection;
import org.organet.commons.inofy.Index;
import org.organet.commons.inofy.Model.SharedFileHeader;

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
  private final static Integer PORT_NO = 5001;

  public static ArrayList<Connection> getConnections() {
    return connections;
  }

  public static void setConnections(ArrayList<Connection> connections) {
    ConnectionManager.connections = connections;
  }

  static Index networkIndex = new Index(false);

  public Integer getPORT_NO() {
    return PORT_NO;
  }

  public static void startServer() {
    try {
      //Listening for a connection to be made
      System.out.println("server started");
      ServerSocket serverSocket = new ServerSocket(PORT_NO);
      System.out.println("TCPServer Waiting for client on port " + PORT_NO);
      while (true) {
        Socket neighbourConnectionSocket = serverSocket.accept();
        Connection newIncomingConnection = new Connection(neighbourConnectionSocket);

        sendIndex(newIncomingConnection, App.localIndex);
        getRemoteIndex(newIncomingConnection);

        connections.add(newIncomingConnection);

        App.mainForm.getConnectionListModel().addElement(newIncomingConnection.getConnectionIp().toString());



        new Thread(newIncomingConnection.getListenCommands()).start();

        System.out.println("started listening");

      }
    } catch (IOException ex) {
      System.out.println("Input Output Exception on Listen Connection Action Performed");
      Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public static void sendIndex(Connection connection, Index myIndex) {
    ObjectOutputStream objectOS = null;
    try {
      objectOS = new ObjectOutputStream(new BufferedOutputStream(connection.getConnectionSocket().getOutputStream()));
      objectOS.writeObject(myIndex);
      objectOS.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void getRemoteIndex(Connection conn) {
    try {
      ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(conn.getConnectionSocket().getInputStream()));
      Index remoteIndex = new Index(false);
      boolean flag = true;
      while (flag) {
        try {
          remoteIndex = (Index) in.readObject();
          flag = false;
        } catch (EOFException ex) {
          flag = true;
        }
      }
      System.out.println("Index read successfully: " + remoteIndex.toString());
      conn.setConnectionIndex(remoteIndex);
      networkIndex.addAllSharedFiles(remoteIndex);
      for (SharedFileHeader sh : networkIndex.getSharedFileHeaders()) {
        conn.getConnectionIndex().add(sh);
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
    try {

      final Connection newConnection = new Connection(new Socket(connectionIp.getHostAddress(), PORT_NO));

      getRemoteIndex(newConnection);
      sendIndex(newConnection, App.localIndex);

      connections.add(newConnection);
      App.mainForm.getConnectionListModel().addElement(newConnection.getConnectionIp().toString());

      new Thread(newConnection.getListenCommands()).start();

      System.out.println("started listening");

      return newConnection;
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Couldn't create connection");
      return null;
    }
  }

  public static void downloadFile() {
    String selectedString = App.mainForm.getNetworkIndexListBox().getSelectedValue();
    Integer selectedNDNid = Integer.parseInt(selectedString.split(" - ")[0]);

    System.out.println("chosen file to download: "+ selectedNDNid);
    if (selectedNDNid == null) {
      System.out.println("Error file not specified");
      return;
    }
    String fileName = selectedString.split(" - ")[1];
    //TODO: secili dosyanin oldugu nodedan dosyayi iste
    connections.get(0).requestFile(selectedNDNid, fileName);   //first find IP to decide who to ask.
  }
}
