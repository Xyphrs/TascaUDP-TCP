package com.company.TCP;

import java.io.*;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ThreadSrvTCPLlista implements Runnable{
    Socket clientSocket;
    ObjectInputStream in;
    ObjectOutputStream out;
    Llista msgEntrant, msgSortint;
    boolean acabat;

    public ThreadSrvTCPLlista(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        acabat = false;
        in = new ObjectInputStream(clientSocket.getInputStream());
        out = new ObjectOutputStream(clientSocket.getOutputStream());

    }

    @Override
    public void run() {
        try {
            while(!acabat) {
                msgEntrant = (Llista) in.readObject();
                msgSortint = generaResposta(msgEntrant);
                out.writeObject(msgSortint);
                out.flush();
            }
        }catch(IOException | ClassNotFoundException e){
            System.out.println(e.getLocalizedMessage());
        }
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Llista generaResposta(Llista en) {
        System.out.println("Not Sorted: " + en.getNumberList());

        List<Integer> sorted = en.getNumberList();
        Collections.sort(sorted);
        System.out.println("Sorted: " + sorted);

        List<Integer> noDuplicates = sorted.stream().distinct().collect(Collectors.toList());
        System.out.println("No Duplicates: " + noDuplicates);

        en.setNumberList(noDuplicates);

        acabat = true;

        return en;
    }
}
