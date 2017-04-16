/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gabriel;

import gabriel.models.Connection;
import java.util.ArrayList;

/**
 *
 * @author TheDoctor
 */
public class ConnectionManager {

    private ArrayList<Connection> connections = new ArrayList<>();

    public ConnectionManager() {

    }

    public void addConnection(Connection conn) {
        this.connections.add(conn);
    }

    void startConnection(Connection newConnection) {

    }

}
