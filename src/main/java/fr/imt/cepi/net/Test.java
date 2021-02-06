package fr.imt.cepi.net;

import fr.imt.cepi.net.tcp.client.MonClient;
import fr.imt.cepi.net.tcp.serveur.MonServeur;

import java.io.IOException;

public class Test {

	public static void main(final String[] args) {

		String adresseServeur = "localhost"; // adresse de la machine locale
		int portServeur = 35842; // port utilisé par le serveur
		// Dans un vrai jeu, il y a trois programmes à lancer séparément : un
		// sur le serveur, un sur un client et un autre sur un autre client. Le
		// serveur et les deux clients peuvent aussi être sur la même machine
		// Ici pour les tests on lance tout cela à partir d'un même programme
		// donc on est obligé de les lancer dans des processus (Thread)
		// différents
		// Un processus pour le serveur
		new Thread(() -> {
			try {
				new MonServeur(portServeur);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();

		// un processus pour chaque client
		for (String nom : new String[] { "Marcelle", "Roger" }) {
			new Thread(() -> {
				try {
					new MonClient(nom, adresseServeur, portServeur);
				} catch (IOException e) {
				}
			}).start();
		}
		// On a donc 3 processus qui s'exécutent
	}
}
