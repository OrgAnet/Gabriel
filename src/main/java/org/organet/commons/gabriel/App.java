package org.organet.commons.gabriel;

import org.organet.commons.gabriel.Controller.Introducer;
import org.organet.commons.gabriel.Model.Connection;
import org.organet.commons.gabriel.Model.Node;
import org.organet.commons.inofy.Index;
import org.organet.commons.inofy.Model.SharedFile;
import org.organet.commons.inofy.Watcher;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class App {
  final static int RANDOM_PORT = 5000;
  final static String SUBNET = "10.253.127";

  public static Index localIndex = null;

  private static ArrayList<Node> nodeList;
  private static Introducer introducer;
  private static ConnectionManager connectionManager; // TODO
  private static int possibleHostsCount;

  public static MainForm mainForm;

  public static void main(String args[]) {
    if (args.length < 1) {
      System.out.println("Shared directory path is missing, first argument must be a valid path.");

      return;
    }

    calculatePossibleHostsCount();

    nodeList = new ArrayList<>();
    introducer = new Introducer(nodeList);
    connectionManager = new ConnectionManager();

    mainForm = new MainForm();
    mainForm.setVisible(true);

    String sharedDirPath = args[0];
    File sharedDir = new File(sharedDirPath);
    localIndex = new Index();

    // Walk shared directory for initial indexing
    try(Stream<Path> paths = Files.walk(Paths.get(sharedDirPath))) {
      paths.forEach(filePath -> {
        if (Files.isRegularFile(filePath)) {
          // FIXME Implement this behaviour in another way (i.e. anywhere else)
            App.mainForm.LocalIndexListModel.addElement(filePath.toFile().getName());
            SharedFile sh = new SharedFile(filePath.toFile().getName());
            localIndex.add(sh);
        }
      });
    } catch (IOException e) {
      e.printStackTrace();

      return;
    }

    // Watch the shared directory directory recursively for changes

      App.startWatcherExecutor( sharedDir);

      App.startServerExecutor();

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
