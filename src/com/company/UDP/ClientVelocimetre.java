package com.company.UDP;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public class ClientVelocimetre {
    private int medianNumber;
    private int counter;
    private MulticastSocket multisocket;
    boolean continueRunning = true;
    InetSocketAddress groupMulticast;
    NetworkInterface netIf;

    public ClientVelocimetre() {
        try {
            multisocket = new MulticastSocket(5557);
            InetAddress multicastIP = InetAddress.getByName("224.0.34.124");
            groupMulticast = new InetSocketAddress(multicastIP, 5557);
            netIf = NetworkInterface.getByName("wlp0s20f3");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runClient() throws IOException {
        byte[] receivedData = new byte[4];
        multisocket.joinGroup(groupMulticast, netIf);
        while (continueRunning) {
            DatagramPacket mpacket = new DatagramPacket(receivedData, 4);
            multisocket.receive(mpacket);
            medianVelocity(mpacket.getData());
        }
        multisocket.leaveGroup(groupMulticast, netIf);
        multisocket.close();
    }

    private void medianVelocity(byte[] data) {
        System.out.println(ByteBuffer.wrap(data).getInt());
        medianNumber += ByteBuffer.wrap(data).getInt();
        counter++;
        if (counter == 5) {
            System.out.println("Media: " + medianNumber / 5);
//          ESTO LO HE PUESTO PARA QUE NO SEA UN BUCLE INFINITO Y PUEDA ACABAR LA CONEXION
            if (medianNumber / 5 > 60) {
                continueRunning = false;
            }
            medianNumber = 0;
            counter = 0;
        }
    }

    public static void main(String[] args) {
        ClientVelocimetre cVelocimetre = new ClientVelocimetre();

        try {
            cVelocimetre.runClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
