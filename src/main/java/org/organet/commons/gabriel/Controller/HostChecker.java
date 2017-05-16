package org.organet.commons.gabriel.Controller;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HostChecker extends Thread {
  String hostName = null;
  Boolean isValid = false;

  public HostChecker(String hostName) {
    this.hostName = hostName;
  }

  @Override
  public void run() {
    int timeout = 1200;

    try {
      if (InetAddress.getByName(hostName).isReachable(timeout)) {
        System.out.println(hostName + " is reachable ");
        isValid = true;
      } else {
        isValid = false;
      }
    } catch (IOException ex) {
      Logger.getLogger(Introducer.class.getName()).log(Level.SEVERE, null, ex);
    }
  }



}
