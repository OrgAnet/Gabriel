/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gabriel.Controller;


import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TheDoctor
 */
public class HostCheckerAll extends Thread {

    ArrayList<String> hostIps = new ArrayList<>();
    String subnet;
    Integer hostCount;


    public HostCheckerAll(String subnet, Integer hostCount) {
        this.subnet = subnet;
        this.hostCount = hostCount;
    }

    @Override
    public void run() {

        String[] addresses = new String[hostCount];
        for (int i = 0; i < hostCount; i++) {
            addresses[i] = subnet + "." + i;
        }

        while (true) {

            HostChecker[] hostCheckers = new HostChecker[hostCount];

            for (int i = 0; i < hostCount; i++) {
                hostCheckers[i] = new HostChecker(addresses[i]);
            }
            for (int i = 0; i < hostCount; i++) {
                hostCheckers[i].start();
            }
            for (int i = 0; i < hostCount; i++) {
                try {
                    hostCheckers[i].join();
                    if (hostCheckers[i].isValid && !hostIps.contains(hostCheckers[i].hostName)) {
                        hostIps.add(hostCheckers[i].hostName);
                        System.out.println("Host Ip: " + hostCheckers[i].hostName + " added");
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Introducer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            for (int i = 0; i < hostCount; i++) {
                hostCheckers[i].interrupt();
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(HostCheckerAll.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    
    
    
    public ArrayList<String> getHostIps() {
        return hostIps;
    }

    public void setHostIps(ArrayList<String> hostIps) {
        this.hostIps = hostIps;
    }

    public String getSubnet() {
        return subnet;
    }

    public void setSubnet(String subnet) {
        this.subnet = subnet;
    }

    public Integer getHostCount() {
        return hostCount;
    }

    public void setHostCount(Integer hostCount) {
        this.hostCount = hostCount;
    }
}
