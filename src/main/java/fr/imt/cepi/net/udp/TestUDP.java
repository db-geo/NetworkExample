package fr.imt.cepi.net.udp;

import java.io.IOException;

public class TestUDP {

    public static void main(String[] args) throws IOException {
        ServeurUDP serveur = new ServeurUDP(12322);
        serveur.start();
        ClientUDP client1 = new ClientUDP("localhost", 12322);
        ClientUDP client2 = new ClientUDP("localhost", 12322);
        // 2 client qui envoi 1000 messages chacun
        client1.start(1000);
        client2.start(1000);
    }
}
