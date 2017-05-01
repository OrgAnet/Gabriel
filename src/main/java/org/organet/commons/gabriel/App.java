package org.organet.commons.gabriel;

import org.organet.commons.gabriel.Controller.Introducer;
import org.organet.commons.gabriel.Model.Node;
import org.organet.commons.inofy.Inofy;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
  final static int RANDOM_PORT = 5000;
  final static String SUBNET = "192.168.1";

  public static MainForm mainForm;

  private static ArrayList<Node> nodeList;
  private static Introducer introducer;
  private static ConnectionManager connectionManager; // TODO
  private static int possibleHostsCount;

  public static void main(String args[]) {
    calculatePossibleHostsCount();

    nodeList = new ArrayList<>();
    introducer = new Introducer(nodeList);
//    connectionManager = new ConnectionManager();

    mainForm = new MainForm();
    mainForm.setVisible(true);

    Inofy.start(args[0]);
  }

  private static void calculatePossibleHostsCount() {
    String[] splittedSUBNET = SUBNET.split("\\.");

    possibleHostsCount = (int) Math.pow(255, (4 - splittedSUBNET.length));
  }

  static Introducer getIntroducer() {
    return introducer;
  }

  static ConnectionManager getConnectionManager() {
    return connectionManager;
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

  public static int getPossibleHostsCount() {
    return possibleHostsCount;
  }
}
