package org.organet.commons.gabriel.Model;

import org.organet.commons.gabriel.Controller.ListenCommands;
import org.organet.commons.inofy.Index;

import java.io.*;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection {
  Inet4Address connectionIp;

  Socket connectionSocket;

  Index connectionIndex;

  OutputStream os;
  InputStream is;
  ListenCommands listenCommands;


  public Index getConnectionIndex() {
    return connectionIndex;
  }

  public void setConnectionIndex(Index connectionIndex) {
    this.connectionIndex = connectionIndex;
  }

  public Connection(Socket connectionSocket) {
    this.connectionSocket = connectionSocket;
    this.connectionIp = (Inet4Address) connectionSocket.getInetAddress();
    try {
      this.os = connectionSocket.getOutputStream();
      this.is = connectionSocket.getInputStream();
    } catch (IOException e) {
      e.printStackTrace();
    }
    listenCommands = new ListenCommands(connectionSocket);

  }

  public Inet4Address getConnectionIp() {
    return connectionIp;
  }

  public void setConnectionIp(Inet4Address connectionIp) {
    this.connectionIp = connectionIp;
  }

  public Socket getConnectionSocket() {
    return connectionSocket;
  }

  public void setConnectionSocket(Socket connectionSocket) {
    this.connectionSocket = connectionSocket;
  }

  @Override
  public String toString() {
    return "Connection{" + "connectionIp=" + connectionIp + '}';
  }

  public void requestFile(Integer ndnId,String fileName) {
    try {

      System.out.println(connectionSocket);
      OutputStreamWriter os = new OutputStreamWriter(connectionSocket.getOutputStream());
      System.out.println("Sending " +  connectionSocket.getInetAddress() + " the command: GET NDNID:" + fileName);
      os.write(("GET - " + fileName+"\n"));
      os.flush();

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public OutputStream getOutputStream() {
    return os;
  }
  public InputStream getInputStream() {
    return is;
  }


  public ListenCommands getListenCommands() {
    return listenCommands;
  }

  public void setListenCommands(ListenCommands listenCommands) {
    this.listenCommands = listenCommands;
  }

}
