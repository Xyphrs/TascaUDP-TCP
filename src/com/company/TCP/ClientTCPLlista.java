package com.company.TCP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientTCPLlista extends Thread {
    String hostname;
    int port;
    boolean continueConnected;

    public ClientTCPLlista(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        continueConnected = true;
    }

    public void run() {
        String serverData;
        String request;

        Socket socket;
        BufferedReader in;
        PrintStream out;

        try {
            socket = new Socket(InetAddress.getByName(hostname), port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream());
            while(continueConnected){
                serverData = in.readLine();
                request = getRequest(serverData);
                out.println(request);
                out.flush();

            }
            close(socket);
        } catch (UnknownHostException ex) {
            System.out.println("Error de connexió. No existeix el host: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Error de connexió indefinit: " + ex.getMessage());
        }

    }

    public String getRequest(String serverData) {
        String ret;
        System.out.println(serverData);
        if( serverData.equals("Correcte") ) {
            continueConnected = false;
            ret = "Campió!";
        } else {
            Scanner in = new Scanner(System.in);
            System.out.print("Digues un número: ");
            ret = in.next();
        }

        return ret;

    }

    public boolean mustFinish(String dades) {
        if (dades.equals("exit")) return false;
        return true;

    }

    private void close(Socket socket){
        try {
            if(socket!=null && !socket.isClosed()){
                if(!socket.isInputShutdown()){
                    socket.shutdownInput();
                }
                if(!socket.isOutputShutdown()){
                    socket.shutdownOutput();
                }
                socket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientTCPLlista.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        ClientTCPLlista clientTcp = new ClientTCPLlista("localhost",5558);
        clientTcp.start();
    }
}
