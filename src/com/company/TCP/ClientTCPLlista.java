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
        ObjectOutputStream out;
        ObjectInputStream in;

        try {
            socket = new Socket(InetAddress.getByName(hostname), port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            request = getRequest();
            out.writeObject(request);
            out.flush();
            System.out.println(request.getNom() + " antes del servidor => " + request.getNumberList());

            serverData = (Llista) in.readObject();
            System.out.println(serverData.getNom() + " despues del servidor => " + serverData.getNumberList());
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
        nom = scanner.nextLine();

        for (int i = 0; i < 10; i++) {
            System.out.println("Introduce un numero");
            integerList.add(scanner.nextInt());
        }
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
