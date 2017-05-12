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
            BufferedReader inputStreamReader =new BufferedReader( new InputStreamReader(socket.getInputStream()));

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

            System.out.println("waiting to Listen command");
            String commandOrFileFirstLine;
            while(true) {
                commandOrFileFirstLine = inputStreamReader.readLine();

                if (commandOrFileFirstLine.startsWith("GET")) {
                    //Send File
                    System.out.print("command listened: " + commandOrFileFirstLine);
                    String fileNDNid = commandOrFileFirstLine.split(" - ")[1];
                    Integer fileNDNnumber = Integer.parseInt(fileNDNid);

                    SharedFileHeader sharedFileHeader = App.localIndex.findIndex(fileNDNnumber);
                    BufferedReader fileInputStream = new BufferedReader(new FileReader(sharedFileHeader.getAbsoluteFile()));

                    String c;
                    while ((c = fileInputStream.readLine()) != null) {
                        outputStreamWriter.write(c+"\n");
                    }
                    outputStreamWriter.flush();
                } else {
                    //get file

                    BufferedWriter fileOutputStream = new BufferedWriter(new PrintWriter(App.mainForm.getNetworkIndexListBox().getSelectedValue().split(" - ")[1]));
                    //BufferedReader bufferedInputStream = new BufferedReader(new InputStreamReader (socket.getInputStream()));

                    String x=commandOrFileFirstLine;
                    do {
                        fileOutputStream.write(x);
                        fileOutputStream.flush();
                        if(inputStreamReader.ready()) {
                            fileOutputStream.newLine();
                            x = inputStreamReader.readLine();
                        }
                        else
                            break;
                    }while(x!=null);

                    fileOutputStream.close();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
