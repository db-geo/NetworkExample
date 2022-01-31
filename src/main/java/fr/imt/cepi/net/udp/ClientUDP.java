package fr.imt.cepi.net.udp;

import java.net.*;

// Un client qui sait juste envoyer des packet à une adresse
public class ClientUDP {

    private static int compteur = 1;

    private final int id;
    private final InetAddress adresse;
    private final int port;

    public ClientUDP(String adresse, int port) throws UnknownHostException, SocketException {
        this.id = compteur++;
        this.adresse = InetAddress.getByName(adresse);
        this.port = port;
    }

    public void start(int nbMessages) {
        new Thread(() -> {
            String message;
            try (DatagramSocket socket = new DatagramSocket()) {
                for (int i = 1; i <= nbMessages; i++) {
                    message = "message " + i + " client " + id;
                    System.out.println("envoi " + message);
                    socket.send(new DatagramPacket(message.getBytes(), message.length(), adresse, port));
                    // Thread.sleep(1); // Lorsqu'il est commenté, il manque nettement plus de paquets à l'arrivée ...
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}