package org.organet.commons.gabriel.Controller;

import org.organet.commons.gabriel.App;
import org.organet.commons.gabriel.ConnectionManager;
import org.organet.commons.gabriel.Helper;
import org.organet.commons.gabriel.Model.Connection;
import org.organet.commons.inofy.Model.SharedFileHeader;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
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
            BufferedReader bufferedInputStreamReader =new BufferedReader( new InputStreamReader(socket.getInputStream()));

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

            String commandOrFileFirstLine;
            while(true) {
                System.out.println("waiting to Listen command");
                char[] buff = new char[3];
                int output = bufferedInputStreamReader.read(buff,0,3);
                commandOrFileFirstLine = new String(buff);
                System.out.println("command received " + commandOrFileFirstLine);

                if (commandOrFileFirstLine.equals("\0\0\0")) {
                    System.out.format("Connection lost (%s)\n", socket.getInetAddress());
                    break;
                }

                if (commandOrFileFirstLine.startsWith("GET")) {
                    //Send File
                    sendFile(bufferedInputStreamReader, outputStreamWriter);
                } else if(commandOrFileFirstLine.startsWith("SEN")){
                    //Receiving file from other node.
                    receiveNewFile(bufferedInputStreamReader);
                }
                else if(commandOrFileFirstLine.startsWith("NEW")){
                    recieveNewSharedFileHeader();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendFile(BufferedReader bufferedInputStreamReader, OutputStreamWriter outputStreamWriter) throws IOException, InterruptedException {
        String commandOrFileFirstLine;
        commandOrFileFirstLine = "GET" + bufferedInputStreamReader.readLine();
        System.out.print("command listened: " + commandOrFileFirstLine);
        String fileName = commandOrFileFirstLine.split(" - ")[1];

        outputStreamWriter.write("SEN");
        outputStreamWriter.flush();
        Thread.sleep(300);

        //check requested file locally.
        SharedFileHeader sharedFileHeader = App.localIndex.findIndex(fileName);
        if(sharedFileHeader!=null){ //file found locally.
            // retrieve from file.
            BufferedInputStream bufferedFileInputStream = new  BufferedInputStream(new FileInputStream(sharedFileHeader.getAbsoluteFile()),1024);

            int c;
            int x =0;
            while ((c = bufferedFileInputStream.read()) != -1) {
                outputStreamWriter.write(c);
                x++;
            }
            System.out.println("bytes sent : "+x);
            outputStreamWriter.flush();
            bufferedFileInputStream.close();
        }else{
            //if file is not local, check network Index.
            sharedFileHeader = ConnectionManager.getNetworkIndex().findIndex(fileName);
            //if file still not found. show error and exit.
            if(sharedFileHeader == null){
                JOptionPane.showMessageDialog(null, "Requested file not found.");
                return;
            }
            String sourceIp= sharedFileHeader.getIp();
            Connection sourceConnection = ConnectionManager.getConnection(sourceIp); /// find file source and receive from there and send to requested node.
            sourceConnection.requestFile(sharedFileHeader.fileName);
            redirectFile(sharedFileHeader, sourceConnection.getConnectionSocket(), socket);
        }
    }

    private void redirectFile(SharedFileHeader sh, Socket sourceSocket, Socket destinationSocket) {
       try {
           System.out.println("\nredirecting file :"+sh+" from " + sourceSocket.getInetAddress() + " to " + destinationSocket.getInetAddress());
           //Thread.sleep(1000);
            char[] buff = new char[3];
            BufferedReader bufferedInputStreamReader = new BufferedReader( new InputStreamReader(sourceSocket.getInputStream()));
            int output = bufferedInputStreamReader.read(buff,0,3);
            System.out.println(new String (buff) + " command to send file is received. Sending file now.");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(destinationSocket.getOutputStream());

            int c;
            int x=0;
            do {
                c = bufferedInputStreamReader.read();
                outputStreamWriter.write(c);
                x++;
            }while(x < sh.getSize());
            outputStreamWriter.flush();
           System.out.println(x + " bytes redirected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void receiveNewFile(BufferedReader bufferedInputStreamReader) throws IOException {
        String chosenName = App.mainForm.getNetworkIndexListBox().getSelectedValue();
        //create the file
        String [] text=chosenName.split(" - ");
        File file = new File(App.getSharedDirPath()+text[1]);
        SharedFileHeader sharedFileHeader = ConnectionManager.getNetworkIndex().findIndex(text[1]);
        //start receiving.
        BufferedOutputStream bufferedFileOutputStream = new BufferedOutputStream(new FileOutputStream(file),1024) ;
        int c;
        int x=0;
        do {
            c = bufferedInputStreamReader.read();
            bufferedFileOutputStream.write(c);
            x++;
        }while(x < sharedFileHeader.getSize());

        System.out.println("bytes read : "+x);
        bufferedFileOutputStream.flush();
        bufferedFileOutputStream.close();
    }

    private void recieveNewSharedFileHeader() throws IOException {
        //new object is created on neighbour. And updating this node.
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            SharedFileHeader sh;
            boolean flag = true;
            while (flag) {
                try {
                    sh = (SharedFileHeader) objectInputStream.readObject();
                    if(!App.localIndex.isContainsHash(sh.getHash())) {  //to prevent flooding, if exists, dont Add

                        System.out.println("NEW SharedFileHeader added to index successfully: " + sh.toString());
                        sh.setNDNid(ConnectionManager.getNetworkIndex().getNDNcount());
                        String ipd = socket.getInetAddress().toString();
                        if (ipd.startsWith("/")) ipd = ipd.substring(1);

                        sh.setIp(ipd);  //Set Ip to received node.

                        ConnectionManager.getNetworkIndex().add(sh);
                        App.mainForm.getNetworkIndexListModel().addElement(sh.getScreenName());

                        ConnectionManager.sendNewSharedFiletoNetwork(sh);

                    }else{
                        System.out.println("NEW SharedFileHeader already exists: " + sh.toString());
                    }
                    flag = false;
                } catch (EOFException ex) {
                    flag = true;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
