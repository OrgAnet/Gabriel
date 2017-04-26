package org.organet.commons.gabriel.Model;

import java.net.Inet4Address;

/**
 * This class is to manage existing connections between nodes
 */
public class Node {
  Inet4Address connectionIp;

  public Node(Inet4Address connectionIp) {
    this.connectionIp = connectionIp;
  }

  public Inet4Address getConnectionIp() {
    return connectionIp;
  }

  public void setConnectionIp(Inet4Address connectionIp) {
    this.connectionIp = connectionIp;
  }
}