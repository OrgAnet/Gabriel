package org.organet.commons.gabriel.Model;

import org.organet.commons.inofy.Index;

import java.net.Inet4Address;
import java.net.Socket;

public class Connection {
  Inet4Address connectionIp;
  Socket connectionSocket;
  String connectionId;
  Index connectionIndex;

  public Index getConnectionIndex() {
    return connectionIndex;
  }

  public void setConnectionIndex(Index connectionIndex) {
    this.connectionIndex = connectionIndex;
  }

  public Connection(Socket connectionSocket) {
    this.connectionSocket = connectionSocket;
    this.connectionIp = (Inet4Address) connectionSocket.getInetAddress();
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
}