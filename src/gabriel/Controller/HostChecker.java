/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gabriel.Controller;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TheDoctor
 */
public class HostChecker extends Thread {
    String hostName = null;
    Boolean isValid = false;
    public HostChecker(String hostName) {
        this.hostName = hostName;
    }
    @Override
    public void run() {
        int timeout = 2000;
        try {
            if (InetAddress.getByName(hostName).isReachable(timeout)) {
                System.out.println(hostName + " is reachable");
                isValid = true;
            }else
                isValid=false;
        } catch (IOException ex) {
            Logger.getLogger(Introducer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
