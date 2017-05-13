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
                char[] buff = new char[3];
                int output = inputStreamReader.read(buff,0,3);
                commandOrFileFirstLine = new String(buff);

                if (commandOrFileFirstLine.startsWith("GET")) {
                    //Send File
                    commandOrFileFirstLine = "GET" + inputStreamReader.readLine();
                    System.out.print("command listened: " + commandOrFileFirstLine);
                    String fileNDNid = commandOrFileFirstLine.split(" - ")[1];
                    Integer fileNDNnumber = Integer.parseInt(fileNDNid);

                    SharedFileHeader sharedFileHeader = App.localIndex.findIndex(fileNDNnumber);
                    BufferedReader fileInputStream = new BufferedReader(new FileReader(sharedFileHeader.getAbsoluteFile()));
                    outputStreamWriter.write("SEN");
                    Thread.sleep(300);
                    int c;
                    while ((c = fileInputStream.read()) != -1) {
                        outputStreamWriter.write(c);
                        outputStreamWriter.flush();
                    }
                    fileInputStream.close();
                } else {
                    //get file

                    BufferedWriter fileOutputStream = new BufferedWriter(new PrintWriter(App.mainForm.getNetworkIndexListBox().getSelectedValue().split(" - ")[1]));
                    //BufferedReader bufferedInputStream = new BufferedReader(new InputStreamReader (socket.getInputStream()));
                    int c;
                    do {
                        if(inputStreamReader.ready()) {
                            c = inputStreamReader.read();
                            fileOutputStream.write(c);
                            fileOutputStream.flush();
                        }
                        else
                            break;
                    }while(c!=-1);
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
