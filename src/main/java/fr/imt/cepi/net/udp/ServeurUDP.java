package fr.imt.cepi.net.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

// Un serveur sui sait juste recevoir des messages
public class ServeurUDP {

    private final int port;

    public ServeurUDP(int port) {
        this.port = port;
    }

    public void start() {
        new Thread(() ->
        {
            try (DatagramSocket socket = new DatagramSocket(port)) {
                // La méthode receive soulèvera une exception de type SocketTimeoutExceptionsans
                // sans message reçu pendant 5 secondes
                socket.setSoTimeout(5000);
                socket.setReceiveBufferSize(100000); // Si plus petit et pas de sleep dans le client => perte de paquets
                DatagramPacket data;
                // Boucle de lecture
                int nbMessagesRecus = 0;
                while (true) {
                    System.out.println("Serveur en attente...");
                    data = new DatagramPacket(new byte[128], 128);
                    socket.receive(data);
                    System.out.println("Serveur - Réception (" + ++nbMessagesRecus + ") : " + new String(data.getData())
                            + " depuis  " + data.getAddress());
                }
            } catch (SocketTimeoutException e) {
                System.out.println("Arrêt du serveur car pas de message reçu depuis 5 secondes.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}


