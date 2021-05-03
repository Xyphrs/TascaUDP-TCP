package com.company.TCP;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientTCPLlista extends Thread {
    Scanner scanner = new Scanner(System.in);
    String hostname;
    int port;

    public ClientTCPLlista(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void run() {
        Llista serverData;
        Llista request;
        Socket socket;
        ObjectInputStream in;
        ObjectOutputStream out;

        try {
            socket = new Socket(InetAddress.getByName(hostname), port);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            while(true){
                serverData = (Llista) in.readObject();
                if (serverData != null) break;
                request = getRequest();
                out.writeObject(request);
                out.flush();
            }
            close(socket);
        } catch (UnknownHostException ex) {
            System.out.println("Error de connexió. No existeix el host: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Error de connexió indefinit: " + ex.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public Llista getRequest() {
        List<Integer> integerList = new ArrayList<>();
        String nom;

        System.out.println("Introduce un nombre para la lista");
        nom = scanner.next();

        for (int i = 0; i < 10; i++) {
            System.out.println("Introduce un numero");
            integerList.add(scanner.nextInt());
        }

        System.out.println(integerList);

        return new Llista(nom, integerList);
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
