package org.organet.commons.gabriel.Model;

import java.net.Inet4Address;
import java.net.Socket;

public class Connection {
  Inet4Address connectionIp;
  Socket connectionSocket;

  public Connection(Inet4Address connectionIp) {
    this.connectionIp = connectionIp;
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