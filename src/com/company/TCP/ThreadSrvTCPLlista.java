package com.company.TCP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ThreadSrvTCPLlista implements Runnable{
    Socket clientSocket;
    BufferedReader in;
    PrintStream out;
    String msgEntrant, msgSortint;
    boolean acabat;

    public ThreadSrvTCPLlista(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        acabat = false;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out= new PrintStream(clientSocket.getOutputStream());

    }

    @Override
    public void run() {
        try {
            while(!acabat) {
                msgSortint = generaResposta(msgEntrant);
                out.println(msgSortint);
                out.flush();
                msgEntrant = in.readLine();
            }
        }catch(IOException e){
            System.out.println(e.getLocalizedMessage());
        }
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String generaResposta(String en) {

        return en;
    }
}
