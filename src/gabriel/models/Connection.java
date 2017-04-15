/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gabriel.models;

import java.net.Inet4Address;

/**
 * @author EmreDanisan
 */
public class Connection {
    
    Inet4Address connectionIp;

    public Connection(Inet4Address connectionIp) {
        this.connectionIp = connectionIp;
    }

    public Inet4Address getConnectionIp() {
        return connectionIp;
    }

    public void setConnectionIp(Inet4Address connectionIp) {
        this.connectionIp = connectionIp;
    }
    
    
    
}
