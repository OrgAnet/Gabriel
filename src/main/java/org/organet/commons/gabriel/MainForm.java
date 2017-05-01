package org.organet.commons.gabriel;

import org.organet.commons.gabriel.Controller.Introducer;
import org.organet.commons.gabriel.Model.Connection;
import org.organet.commons.gabriel.Model.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public class MainForm extends JFrame {
  private JList<String> IpListBox;
  private JButton ScanNetworkButton;
  private JButton ConnectButton;
  private JList<String> ConnectionListBox;
  private JButton listenConnection;
  private JList<String> LocalIndexListBox;
  private JList<String> NetworkIndexListBox;
  private JPanel panelMain;

  private DefaultListModel<String> IpListModel = new DefaultListModel<>();
  private DefaultListModel<String> ConnectionListModel = new DefaultListModel<>();
  public DefaultListModel<String> LocalIndexListModel = new DefaultListModel<>();
//  private DefaultListModel<String> NetworkIndexListModel = new DefaultListModel<>();

  // TODO Move these to App
  private Introducer introducer;
  private ConnectionManager connectionManager;

  MainForm() {
    IpListBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    IpListBox.setLayoutOrientation(JList.VERTICAL);
    IpListBox.setModel(IpListModel);

    ConnectionListBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    ConnectionListBox.setLayoutOrientation(JList.VERTICAL);
    ConnectionListBox.setModel(ConnectionListModel);

    LocalIndexListBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    LocalIndexListBox.setLayoutOrientation(JList.VERTICAL);
    LocalIndexListBox.setModel(LocalIndexListModel);

    NetworkIndexListBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    NetworkIndexListBox.setLayoutOrientation(JList.VERTICAL);
//    NetworkIndexListBox.setModel(NetworkIndexListModel);

    ScanNetworkButton.addActionListener(this::ScanNetworkButtonActionPerformed);
    ConnectButton.addActionListener(this::ConnectButtonActionPerformed);
    listenConnection.addActionListener(this::listenConnectionActionPerformed);

    connectionManager = App.getConnectionManager();
    introducer = App.getIntroducer();
    introducer.checkHostsBruteForce(App.SUBNET);
    getAndListHosts();

    setContentPane(panelMain);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    pack();
  }

  private void ScanNetworkButtonActionPerformed(ActionEvent evt) {
    getAndListHosts();
  }

  private void getAndListHosts() {
    IpListModel.removeAllElements();
    introducer.getHostCheckerAll().getHostIps().forEach((node) -> IpListModel.addElement(node));
  }

  private void ConnectButtonActionPerformed(ActionEvent evt) {
    String selectedIp = IpListModel.getElementAt(IpListBox.getSelectedIndex());

    // TODO If there is no selection then disable the button before here
    if (selectedIp == null) {
      JOptionPane.showMessageDialog(null, "Please choose 1 IP to connect to.");
    } else {
      try {
        String ip = selectedIp.split(" -")[0].replaceAll("/", "");
        Node selectedNode = App.getNode(ip);
        Connection newConnection = new Connection(selectedNode.getConnectionIp());

        connectionManager.addConnection(newConnection);
        if (connectionManager.startConnection(newConnection)) {
          ConnectionListModel.addElement(newConnection.getConnectionIp().toString());
        } else {
          System.out.println("Sorry, could not connect to: " + newConnection.getConnectionIp().toString());
        }
      } catch (HeadlessException ex) {
        Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  private void listenConnectionActionPerformed(ActionEvent evt) {
    try {
      //Listening for a connection to be made
      ServerSocket serverSocket = new ServerSocket(connectionManager.getPORT_NO());
      System.out.println("TCPServer Waiting for client on port 5000\n");
      Socket connectionSocket = serverSocket.accept();

      Connection newIncomingConnection = new Connection((Inet4Address) connectionSocket.getInetAddress());
      connectionManager.addConnection(newIncomingConnection);
//      Index nodeIndex = connectionManager.getIndex(connectionSocket); // FIXME
//
//      connectionManager.addToNetworkIndex(nodeIndex);
//
//      // connectionManager.networkIndex.getFileHeaders().addAll(incomingData.getFileHeaders());
//      System.out.println(nodeIndex.getFileHeaders().get(0).getName());

      ConnectionListModel.addElement(newIncomingConnection.getConnectionIp().toString());
    } catch (IOException ex) {
      System.out.println("Input Output Exception on Listen Connection Action Performed");
      Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
