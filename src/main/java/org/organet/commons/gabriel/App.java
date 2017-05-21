package org.organet.commons.gabriel;

import org.organet.commons.gabriel.Controller.Introducer;
import org.organet.commons.gabriel.Model.Node;
import org.organet.commons.inofy.Index;
import org.organet.commons.inofy.Model.SharedFileHeader;
import org.organet.commons.inofy.Watcher;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class App {
  final static String SUBNET = "192.168.1"; // 10.253.74
  static String sharedDirPath;
  public static Index localIndex = null;
  public static String localIp;
  private static ArrayList<Node> nodeList;
  private static Introducer introducer;
  private static ConnectionManager connectionManager; // TODO
  private static int possibleHostsCount;

  public static MainForm mainForm;

  public static SharedFileHeader chosenSharedFileHeader;

  public static void main(String args[]) {
    if (args.length < 2) {
      System.out.println("Shared directory path and/or IP address is missing, first argument must be a valid path.");

      return;
    }
    calculatePossibleHostsCount();
    nodeList = new ArrayList<>();
    introducer = new Introducer(nodeList);
    connectionManager = new ConnectionManager();

    mainForm = new MainForm();
    mainForm.setVisible(true);

//      localIp = InetAddress.getLocalHost().getHostAddress().toString();
      mainForm.IPLabel.setText("Your Ip Address is: "+ localIp + " and server is listening on port: "+ connectionManager.getPORT_NO());
//    } catch (UnknownHostException e) {
//      e.printStackTrace();
//    }

    sharedDirPath = args[1];
    File sharedDir = new File(sharedDirPath);
    localIndex = new Index(true);

    // Watch the shared directory directory recursively for changes

    App.startWatcherExecutor( sharedDir);

    App.startServerExecutor();

    // Walk shared directory for initial indexing
    try(Stream<Path> paths = Files.walk(Paths.get(sharedDirPath))) {
      paths.forEach(filePath -> {
        if (Files.isRegularFile(filePath)) {
          // FIXME Implement this behaviour in another way (i.e. anywhere else)
            App.mainForm.LocalIndexListModel.addElement(filePath.toFile().getName());
            SharedFileHeader sh = SharedFileHeader.fromFile(filePath.toFile());
            localIndex.add(sh);
        }
      });
    } catch (IOException e) {
      e.printStackTrace();

      return;
    }

  }

  private static void startWatcherExecutor(File sharedDir){
    ExecutorService executorService = Executors.newFixedThreadPool(1);
      Watcher w = null;
      try {
          w= new Watcher(sharedDir.getPath());
      } catch (IOException e) {
          e.printStackTrace();
      }
      executorService.submit(w::run);
    executorService.shutdown();
  }

  private static void startServerExecutor() {
    ExecutorService executorService = Executors.newFixedThreadPool(1);
    executorService.submit(ConnectionManager::startServer);
    executorService.shutdown();
  }

  private static void calculatePossibleHostsCount() {
    String[] splittedSUBNET = SUBNET.split("\\.");

    possibleHostsCount = (int) Math.pow(255, (4 - splittedSUBNET.length));
  }

  static Introducer getIntroducer() {
    return introducer;
  }

  static Node getNode(String ipAddress) {
    for (Node node : nodeList) {
      if (node.getConnectionIp().toString().equals(ipAddress)) {
        return node;
      }
    }

    try {
      return new Node((Inet4Address) InetAddress.getByName(ipAddress));
    } catch (UnknownHostException ex) {
      Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);

      return null;
    }
  }

  public static int getPossibleHostsCount() {
    return possibleHostsCount;
  }

  private static String calculateDeviceID() {
    // Start with MAC address of the ad-hoc network interface
    String idBase = Helper.getMACAddress();

    // Remove dashes
    idBase = idBase.replace("-", "");

    // Arbitrarily shuffle the ID
    int idBaseLen = idBase.length();
    char[] characters = idBase.toCharArray();

    // Generate and fill the `charactersOrder` with random numbers
    // NOTE Hard-coded order guarantees that every time same device identifier \
    //      is going to be calculated for the same MAC address.
    List<Integer> charactersOrder = new ArrayList<>(Arrays.asList(10, 1, 5, 8, 0, 2, 6, 4, 7, 11, 3, 9));

    // Finally construct the device identifier by appending it with characters
    StringBuilder idBuilder = new StringBuilder(idBaseLen);
    for (int i = 0; i < idBaseLen; i++) {
      // Try to convert odd numbers to characters in ASCII table if adding 5 (53 in ASCII)
      // will not make them greater than 'f' so this way the device identifier might look
      // more like a random hexadecimal number rather than MAC address-based number.
      if (i % 2 != 0 || ((int) (characters[charactersOrder.get(i)]) - 48) > 5) {
        idBuilder.append(characters[charactersOrder.get(i)]);
      } else {
        idBuilder.append((char) (((int) (characters[charactersOrder.get(i)]) - 48) + 'a'));
      }
    }

    return idBuilder.toString();
  }

  public static String getSharedDirPath() {
    return sharedDirPath;
  }

  public static void setSharedDirPath(String sharedDirPath) {
    App.sharedDirPath = sharedDirPath;
  }
}
