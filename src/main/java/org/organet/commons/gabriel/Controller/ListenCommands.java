package org.organet.commons.gabriel.Controller;

import org.organet.commons.gabriel.App;
import org.organet.commons.inofy.Model.SharedFileHeader;

import java.io.*;
import java.net.Socket;

/**
 * Created by TheDoctor on 10-May-17.
 */
public class ListenCommands extends Thread {

    Socket socket;

    public ListenCommands(Socket socket) {
        this.socket = socket;
    }

    @Override
    public synchronized void start() {
        super.start();
        run();
    }

    @Override
    public void run() {
        try {

            Thread.sleep(1300);
            InputStreamReader inputStreamReader =new InputStreamReader(socket.getInputStream());
            String buffer = new String();
            System.out.println("waiting to Listen command");
            int a=0;
            while ( ( a=inputStreamReader.read()) !='\n'){
               buffer+=(char)a;
            }

//            Thread.sleep(1300);

            System.out.print("command listened: ");
            System.out.println(buffer);
            String fileNDNid = new String(buffer);
            fileNDNid = fileNDNid.split(" - ")[1];
            Integer fileNDNnumber = Integer.parseInt(fileNDNid);

            SharedFileHeader sharedFileHeader = App.localIndex.findIndex(fileNDNnumber);
            FileInputStream fileInputStream = new FileInputStream(sharedFileHeader.getAbsoluteFile());

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            int c;
            while ( (c=fileInputStream.read())!=-1){
                outputStreamWriter.write(c);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
