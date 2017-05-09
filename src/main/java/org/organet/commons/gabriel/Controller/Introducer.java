package org.organet.commons.gabriel.Controller;

import org.organet.commons.gabriel.App;
import org.organet.commons.gabriel.Model.Node;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is to Introduce new node to the current node. It
 * creates a Connection object and listens for new connection.
 */
public class Introducer implements Runnable {
  private ArrayList<Node> nodes = new ArrayList<>();
  private HostCheckerAll hostCheckerAll;

  public Introducer(ArrayList<Node> connections) {
    this.nodes = connections;
  }

  @Override
  public void run() {
    throw new UnsupportedOperationException("Not supported yet."); // FIXME

//    // When new connection comes add it to connections
//    while (true) {
//      //TODO: Search for why we should use logger.
//      System.out.println("Node " + connectionSocket.getInetAddress() + ":" + connectionSocket.getPort() + " is connected.");
//    }
  }

  public void checkHostsBruteForce(String subnet) {
    try {
      hostCheckerAll = new HostCheckerAll(subnet, App.getPossibleHostsCount());
      hostCheckerAll.run();

      hostCheckerAll.getHostIps().forEach((hostIp) -> {
        try {
          Inet4Address ipAddress = (Inet4Address) Inet4Address.getByName(hostIp);
          this.nodes.add(new Node(ipAddress));
        } catch (UnknownHostException ex) {
          System.out.println("Error on Introducer.getConnections()!");
        }
      });
      System.out.println(hostCheckerAll.getHostIps());
    } catch (Exception ex) {
      Logger.getLogger(Introducer.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public ArrayList<Node> getNodes() {
    return nodes;
  }

  public void setNodes(ArrayList<Node> nodes) {
    this.nodes = nodes;
  }

  public HostCheckerAll getHostCheckerAll() {
    return hostCheckerAll;
  }

  public void setHostCheckerAll(HostCheckerAll hostCheckerAll) {
    this.hostCheckerAll = hostCheckerAll;
  }
}
