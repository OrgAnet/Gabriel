/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gabriel.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TheDoctor
 */
public class HostCheckerAll extends Thread {

    ArrayList<String> hostIps = new ArrayList<>();
    String subnet;
    Integer possibleHostCount;

    public HostCheckerAll(String subnet, Integer possibleHostCount) {
        this.subnet = subnet;
        this.possibleHostCount = possibleHostCount;
    }

    @Override
    public void run() {
        HostChecker[] hostCheckers = new HostChecker[possibleHostCount];

        String[] addresses = new String[possibleHostCount];
        for (int i = 0; i < possibleHostCount; i++) {
            addresses[i] = subnet + "." + i;
        }
        hostIps.clear();
        ExecutorService executor = Executors.newFixedThreadPool(possibleHostCount);
        for (int i = 0; i < possibleHostCount; i++) {
            hostCheckers[i] = new HostChecker(addresses[i]);
            executor.execute(hostCheckers[i]);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        for (int i = 0; i < possibleHostCount; i++) {
            if (hostCheckers[i].isValid && !hostIps.contains(hostCheckers[i].hostName)) {
                hostIps.add(hostCheckers[i].hostName);
                System.out.println("Host Ip: " + hostCheckers[i].hostName + " added");
                
            }
        }
    }

    public ArrayList<String> getHostIps() {
        //Collections.sort(hostIps);
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
        return possibleHostCount;
    }

    public void setHostCount(Integer hostCount) {
        this.possibleHostCount = hostCount;
    }
}
