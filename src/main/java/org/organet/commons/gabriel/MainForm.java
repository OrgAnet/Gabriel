package org.organet.commons.gabriel;

import org.organet.commons.gabriel.Controller.Introducer;
import org.organet.commons.gabriel.Model.Connection;
import org.organet.commons.gabriel.Model.Node;
import org.organet.commons.inofy.Index;
import org.organet.commons.inofy.Model.SharedFileHeader;
import sun.security.provider.SHA;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
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

  public JList<String> getNetworkIndexListBox() {
    return NetworkIndexListBox;
  }

  public void setNetworkIndexListBox(JList<String> networkIndexListBox) {
    NetworkIndexListBox = networkIndexListBox;
  }

  private JList<String> NetworkIndexListBox;
  private JPanel panelMain;
  private JButton downloadButton;
    private JList keywords;
    private JButton delete;
    private JButton addKeyword;
    private JTextField keywordsTextField;
    private JButton deleteKeyword;
    private JTextArea fileHeaderInfo;
    private JTextField searchText;
    private JButton clearFilterButton;
    private JButton filterButton;

    private DefaultListModel<String> IpListModel = new DefaultListModel<>();
  private DefaultListModel<String> ConnectionListModel = new DefaultListModel<>();
  public DefaultListModel<String> LocalIndexListModel = new DefaultListModel<>();
    private DefaultListModel<String> NetworkIndexListModel = new DefaultListModel<>();
    public DefaultListModel<String> KeywordsModel = new DefaultListModel<>();

  public DefaultListModel<String> getConnectionListModel() {
    return ConnectionListModel;
  }

  public void setConnectionListModel(DefaultListModel<String> connectionListModel) {
    ConnectionListModel = connectionListModel;
  }

  public DefaultListModel<String> getNetworkIndexListModel() {
    return NetworkIndexListModel;
  }

  // TODO Move these to App
  private Introducer introducer;

  MainForm() {
    IpListModel.setSize(10);
    IpListBox.setModel(IpListModel);
    IpListBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    IpListBox.setPreferredSize(new Dimension(10,700));

    IpListBox.setLayoutOrientation(JList.HORIZONTAL_WRAP);
    IpListBox.setVisibleRowCount(10);

    IpListBox.revalidate();
    IpListBox.repaint();

    ConnectionListBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    ConnectionListBox.setLayoutOrientation(JList.VERTICAL);
    ConnectionListBox.setModel(ConnectionListModel);

    LocalIndexListBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    LocalIndexListBox.setLayoutOrientation(JList.VERTICAL);
    LocalIndexListBox.setModel(LocalIndexListModel);

    NetworkIndexListBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    NetworkIndexListBox.setLayoutOrientation(JList.VERTICAL);
    NetworkIndexListBox.setModel(NetworkIndexListModel);

    ScanNetworkButton.addActionListener(this::ScanNetworkButtonActionPerformed);
    ConnectButton.addActionListener(this::ConnectButtonActionPerformed);
    downloadButton.addActionListener(this::downloadButtonActionPerformed);

    introducer = App.getIntroducer();

    panelMain.setPreferredSize(new Dimension(900, 650));
    panelMain.repaint();
    revalidate();

    keywords.setModel(KeywordsModel);
      keywords.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
              super.mouseClicked(e);
              String a = (String) keywords.getSelectedValue();
              System.out.println("keyword " + a+ " clicked");

              //delete keyword
          }
      });
      LocalIndexListBox.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
              super.mouseClicked(e);
              KeywordsModel.removeAllElements();
              String name = LocalIndexListBox.getSelectedValue();
              System.out.println("Shared File " + name + " clicked ");
              SharedFileHeader sfh = App.localIndex.findIndex(name);
              App.chosenSharedFileHeader = sfh;
              sfh.getKeywords().forEach(p-> KeywordsModel.addElement(p.toString()));
              setKeywordFieldsState(true);
              fillFileHeader(sfh);
          }
      });
      deleteKeyword.addActionListener(e -> {
          String keyword = ( String) keywords.getSelectedValue();
            if(keyword==null) {
                JOptionPane.showMessageDialog(null, "Please choose 1 keyword to delete.");
                return;
            }
          App.chosenSharedFileHeader.getKeywords().remove(keyword);
          KeywordsModel.removeElement(keyword);
      });
      addKeyword.addActionListener(e -> {
          if(App.chosenSharedFileHeader == null){
              JOptionPane.showMessageDialog(null, "Please choose 1 File to add keyword.");
                return;
          }
          if(keywordsTextField == null || keywordsTextField.getText().length()==0 ){
              JOptionPane.showMessageDialog(null, "Please write keyword to add.");
                return;
          }
          App.chosenSharedFileHeader.getKeywords().add(keywordsTextField.getText());
          KeywordsModel.addElement(keywordsTextField.getText());
          keywordsTextField.setText("");
          fillFileHeader(App.chosenSharedFileHeader );
      });

      filterButton.addActionListener(e -> {
            ArrayList<SharedFileHeader> found = (ArrayList) ConnectionManager.networkIndex.search(searchText.getText());
            NetworkIndexListModel.removeAllElements();
            found.forEach(p->NetworkIndexListModel.addElement(p.getScreenName()));
      });
      clearFilterButton.addActionListener(e -> {
          NetworkIndexListModel.removeAllElements();
          ConnectionManager.networkIndex.getSharedFileHeaders().forEach(P->NetworkIndexListModel.addElement(P.getScreenName()));
          searchText.setText("");
      });
      NetworkIndexListBox.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
              super.mouseClicked(e);
              String name = getNetworkIndexListBox().getSelectedValue();
              name = name.split(" - ")[1];
              System.out.println("Shared File " + name + " clicked ");
              SharedFileHeader sfh = ConnectionManager.getNetworkIndex().findIndex(name);
              App.chosenSharedFileHeader = sfh;
              setKeywordFieldsState(false);
              fillFileHeader(App.chosenSharedFileHeader );
          }
      });

      getAndListHosts();
      setContentPane(panelMain);
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      pack();
  }

  private void setKeywordFieldsState(boolean state){
      keywordsTextField.setEnabled(state);
      addKeyword.setEnabled(state);
      deleteKeyword.setEnabled(state);
      keywords.setEnabled(state);
      if(!state){
          keywords.removeAll();
          KeywordsModel.removeAllElements();
      }
  }

    private void fillFileHeader(SharedFileHeader sfh) {
        fileHeaderInfo.setText(sfh.getName() + "\nKeywords:\n" );
        sfh.getKeywords().forEach(p->fileHeaderInfo.append("["+p+"]\n"));
        fileHeaderInfo.append("\nIP: " + sfh.getIp());
    }
  private void downloadButtonActionPerformed(ActionEvent evt) {
      ConnectionManager.downloadFile();
  }

  private void ScanNetworkButtonActionPerformed(ActionEvent evt) {
      clearScreen();

    getAndListHosts();
  }

    private void clearScreen() {

        searchText.setText("");
        keywordsTextField.setText("");
        keywords.removeAll();
    }

    private void getAndListHosts() {
    IpListModel.removeAllElements();
    introducer.checkHostsBruteForce(App.SUBNET);
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
        Connection newConnection = ConnectionManager.createConnection(selectedNode.getConnectionIp());

      } catch (HeadlessException ex) {
        Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
