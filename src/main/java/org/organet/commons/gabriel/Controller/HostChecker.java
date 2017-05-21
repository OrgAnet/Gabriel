package org.organet.commons.gabriel.Controller;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HostChecker extends Thread {
  String hostName = null;
  Boolean isValid = false;
  final int timeout = 1200;

  public HostChecker(String hostName) {
    this.hostName = hostName;
  }
  @Override
  public void run() {
      try {
        if (InetAddress.getByName(hostName).isReachable(timeout)) {
          System.out.println(hostName + " is reachable ");
          this.isValid = true;
        } else {
          this.isValid = false;
        }
      } catch (IOException ex) {
        Logger.getLogger(Introducer.class.getName()).log(Level.SEVERE, null, ex);
      }
  }

}
