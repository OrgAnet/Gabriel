package org.organet.commons.gabriel;

import org.organet.commons.gabriel.Controller.Introducer;
import org.organet.commons.gabriel.Model.Connection;
import org.organet.commons.gabriel.Model.Index;
import org.organet.commons.gabriel.Model.Node;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainForm extends JFrame {
  private JList IpListBox;
  private JButton ScanNetworkButton;
  private JButton ConnectButton;
  private JList ConnectionListBox;
  private JButton listenConnection;
  private JList LocalIndexListBox;
  private JList NetworkIndexListBox;

  // TODO Move these to App
  private Introducer mainIntroducer;
  private ConnectionManager connectionManager;

  MainForm() {
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    IpListBox.addListSelectionListener(this::IpListBoxActionPerformed);
    ScanNetworkButton.addActionListener(this::ScanNetworkButtonActionPerformed);
    ConnectButton.addActionListener(this::ConnectButtonActionPerformed);
    listenConnection.addActionListener(this::listenConnectionActionPerformed);

    connectionManager = App.getConnectionManager();
    mainIntroducer = App.getMainIntroducer();
    mainIntroducer.checkHostsBruteForce(App.subnet);
//    mainIntroducer.getHostCheckerAll().getHostIps().forEach((node) -> { FIXME
//      ListModel IpListBoxModel = IpListBox.getModel();
//      ((DefaultListModel<String>)IpListBoxModel).addElement(node);
//      IpListBox.setModel(IpListBoxModel);
////      IpListBox.add(node);
//    });
  }

  private void IpListBoxActionPerformed(ListSelectionEvent evt) {
    throw new UnsupportedOperationException("Not supported yet."); // FIXME
  }

  private void ScanNetworkButtonActionPerformed(ActionEvent evt) {
    Executors.newSingleThreadExecutor().execute(() -> {
      try {
        IpListBox.removeAll();
        //FIXME: Up Iplerin gelmesi uzun suruyor. Ve sonradan gelenler oluyor ??!
        mainIntroducer = App.getMainIntroducer();
        mainIntroducer.setNodes(new ArrayList<>());
        mainIntroducer.checkHostsBruteForce(App.subnet);
        Thread.sleep(1);
        mainIntroducer.getHostCheckerAll().getHostIps().forEach((node) -> {
          ListModel IpListBoxModel = IpListBox.getModel();
          ((DefaultListModel<String>)IpListBoxModel).addElement(node);
          IpListBox.setModel(IpListBoxModel);
//          IpListBox.add(node)
        });
      } catch (InterruptedException ex) {
        Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
      }
    });
  }

  private void ConnectButtonActionPerformed(ActionEvent evt) {
    String selectedIp = IpListBox.getSelectedValue().toString();
//    String selectedIp = IpListBox.getSelectedItem();
    if (selectedIp == null) {
      JOptionPane.showMessageDialog(null, "Please choose 1 IP to connect to.");
    } else {
      try {
        String ip = selectedIp.split(" -")[0].replaceAll("/", "");
        Node selectedNode = App.getNode(ip);
        Connection newConnection = new Connection((Inet4Address) selectedNode.getConnectionIp());

        connectionManager.addConnection(newConnection);
        if (connectionManager.startConnection(newConnection)) {
          ListModel ConnectionListBoxModel = ConnectionListBox.getModel();
          ((DefaultListModel<String>)ConnectionListBoxModel).addElement(newConnection.getConnectionIp().toString());
          ConnectionListBox.setModel(ConnectionListBoxModel);
//          ConnectionListBox.add(newConnection.getConnectionIp().toString());
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
      Index nodeIndex = connectionManager.getIndex(connectionSocket);

      connectionManager.addToNetworkIndex(nodeIndex);

      // connectionManager.networkIndex.getFileHeaders().addAll(incomingData.getFileHeaders());
      System.out.println(nodeIndex.getFileHeaders().get(0).getName());

      ListModel ConnectionListBoxModel = ConnectionListBox.getModel();
      ((DefaultListModel<String>)ConnectionListBoxModel).addElement(newIncomingConnection.getConnectionIp().toString());
      ConnectionListBox.setModel(ConnectionListBoxModel);
//      ConnectionListBox.add(newIncomingConnection.getConnectionIp().toString());

    } catch (IOException ex) {
      System.out.println("Input Output Exception on Listen Connection Action Performed");
      Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
