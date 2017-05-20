package org.organet.commons.gabriel;

import org.organet.commons.gabriel.Model.Connection;
import org.organet.commons.inofy.Index;
import org.organet.commons.inofy.Model.SharedFileHeader;

import java.io.*;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionManager {
  private static ArrayList<Connection> connections = new ArrayList<>();
  private final static Integer PORT_NO = 5001;
  private static String localIp;

  static Index networkIndex = new Index(false);

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
      networkIndex.addAllSharedFiles(remoteIndex);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
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
    String []data = selectedString.split(" - ");
    String selectedFileName = data[1];

    System.out.println("chosen file to download: "+ selectedFileName);
    if (selectedFileName == null) {
      System.out.println("Error file not specified");
      return;
    }
    getConnection(data[2]).requestFile(selectedFileName);   //first find IP to decide who to ask.
  }

  public static void sendNewSharedFiletoNetwork(SharedFileHeader sh) {

    // Create a shared file and add to the local index
    for (Connection c : getConnections()) {
      System.out.println("Comparing *" +c.getConnectionIp().toString() + "* ?= *" + sh.getIp()+"*");
      if(c.getConnectionIp().toString().equals("/"+sh.getIp())){
        continue;
      }

      String command = "NEW";
      OutputStreamWriter os = null;
      try {
        //Sending command "NEW" to inform peers that a new file is added.
        os = new OutputStreamWriter(c.getConnectionSocket().getOutputStream());
        System.out.println("Sending " +  c.getConnectionIp() + " the command: " + command);
        os.write(command);
        os.flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
      try {
        Thread.sleep(300);
        System.out.println("Sending to " +  c.getConnectionIp() + " the Shared File Object: " + sh.getFileName());

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(c.getConnectionSocket().getOutputStream() ));

        objectOutputStream.writeObject(sh);
        objectOutputStream.flush();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public static Connection getConnection(String sourceIp) {
      for(Connection c : getConnections()){
        if(c.getConnectionIp().toString().equals(sourceIp));
          return c;
      }
      return  null;
  }

  public static Integer getPortNo() {
    return PORT_NO;
  }

  public static String getLocalIp() {
    return localIp;
  }

  public static void setLocalIp(String localIp) {
    ConnectionManager.localIp = localIp;
  }

  public static ArrayList<Connection> getConnections() {
    return connections;
  }

  public static void setConnections(ArrayList<Connection> connections) {
    ConnectionManager.connections = connections;
  }

  public Integer getPORT_NO() {
    return PORT_NO;
  }

  public static Index getNetworkIndex() {
    return networkIndex;
  }

  public void setNetworkIndex(Index networkIndex) {
    this.networkIndex = networkIndex;
  }

}
