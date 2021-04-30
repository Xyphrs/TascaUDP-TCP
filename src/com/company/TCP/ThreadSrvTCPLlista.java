package com.company.TCP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ThreadSrvTCPLlista implements Runnable{

    Socket clientSocket = null;
    BufferedReader in = null;
    PrintStream out = null;
    String msgEntrant, msgSortint;
    boolean acabat;
    int intentsJugador;

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
                intentsJugador = Integer.parseInt(in.readLine());


            }
        }catch(IOException e){
            System.out.println(e.getLocalizedMessage());
        }
        System.out.println(msgEntrant + " - intents: " + intentsJugador);
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
