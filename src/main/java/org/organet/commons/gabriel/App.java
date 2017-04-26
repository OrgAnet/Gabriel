package org.organet.commons.gabriel;

import org.organet.commons.gabriel.Controller.Introducer;
import org.organet.commons.gabriel.Controller.Receiver;
import org.organet.commons.gabriel.Controller.Sender;
import org.organet.commons.gabriel.Model.Node;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
  final static int RANDOM_PORT = 5000;

  private static ArrayList<Node> nodeList;
  private static Introducer mainIntroducer;
  private static Receiver receiver;
  private static Sender sender;
  private static ConnectionManager connectionManager;
  private static MainForm mainForm;

  static String subnet = "192.168.1";

  public static void main(String args[]) {
    nodeList = new ArrayList<>();
    sender = new Sender();
    receiver = new Receiver();
    mainIntroducer = new Introducer(nodeList);
//    connectionManager = new ConnectionManager();
    mainForm = new MainForm();

    // ...

    mainForm.setVisible(true);
  }

  public ArrayList<Node> getNodeList() {
    return nodeList;
  }

  public void setNodeList(ArrayList<Node> nodeList) {
    App.nodeList = nodeList;
  }

  static ConnectionManager getConnectionManager() {
    return connectionManager;
  }

  public void setConnectionManager(ConnectionManager connectionManager) {
    App.connectionManager = connectionManager;
  }

  static Node getNode(String ipAddress) {
    for (Node node : nodeList) {
      if (node.getConnectionIp().toString().equals(ipAddress)) {
        return node;
      }
    }

    try {
      System.out.println("gelen ip `" + ipAddress + "`");
      return new Node((Inet4Address) InetAddress.getByName(ipAddress));
    } catch (UnknownHostException ex) {
      Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
      return null;
    }
  }

  public ArrayList<Node> getNodes() {
    return nodeList;
  }

  public void setNodes(ArrayList<Node> nodeList) {
    App.nodeList = nodeList;
  }

  static Introducer getMainIntroducer() {
    return mainIntroducer;
  }

  public void setMainIntroducer(Introducer mainIntroducer) {
    App.mainIntroducer = mainIntroducer;
  }

  public Receiver getReceiver() {
    return receiver;
  }

  public void setReceiver(Receiver receiver) {
    App.receiver = receiver;
  }

  public Sender getSender() {
    return sender;
  }

  public void setSender(Sender sender) {
    App.sender = sender;
  }
}
