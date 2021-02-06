package fr.imt.cepi.net.tcp.serveur;

import fr.imt.cepi.net.tcp.protocole.Commande;

import java.io.IOException;
import java.net.ServerSocket;

public class MonServeur {

    // Les deux joueurs géré par le serveur : un pour chaque client
    private MonJoueur joueur1, joueur2;

    public MonServeur(final int port) throws IOException {
        try (ServerSocket ss = new ServerSocket(port)) {
            // Attente de connexion du joueur 1
            joueur1 = new MonJoueur(ss.accept());
            // Le joueur 1 est connecté
            // Attente de connexion du joueur 2
            joueur2 = new MonJoueur(ss.accept());
            // Le joueur 2 est connecté
            // On peut lancer le jeu
            jouer();
        }
    }

    private void jouer() throws IOException {
        joueur1.ecrire("START");
        joueur2.ecrire("START");
        // Exemple avec envoi des commandes selon protocole à définir
        MonJoueur j = joueur1;
        int i = 1;
        // Exemple ici pour 5 séries d'échanges
        while (i <= 5) {
            System.out.println("\nITERATION " + i);
            // Envoi de la commande OPERATION1 au client courant
            j.ecrire(Commande.COMMANDE1.toString());
            // Lecture de la réponse
            String reponse = j.lire();
            // traitement de la réponse
            String res = reponse.equals("content") ? "tant mieux" : "tant pis";
            // Envoi du résultat pour informer le joueur
            j.ecrire(res);
            // Passage au joueur suivant
            j = j == joueur1 ? joueur2 : joueur1;
            i++;
        }
        System.out.println("\nFIN DE LA PARTIE");
        joueur1.ecrire(Commande.END.toString());
        joueur2.ecrire("END");
    }

}
