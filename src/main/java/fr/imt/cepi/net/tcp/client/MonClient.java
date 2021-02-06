package fr.imt.cepi.net.tcp.client;

import fr.imt.cepi.net.tcp.protocole.Commande;
import fr.imt.cepi.net.tcp.helper.InOut;

import java.io.IOException;
import java.net.Socket;

public class MonClient {

	private final String nom;
	private InOut io;

	public MonClient(final String nom, final String address, final int port) throws IOException {
		this.nom = nom;
		try (Socket socket = new Socket(address, port)) {
			io = new InOut(socket);
			// Boucle de lecture tant que la méthode traiter renvoie vrai
			while (traiter(lire())) {
			}

		} catch (Exception e) {
			// ...
		}
	}

	public boolean traiter(final String pCommande) throws IOException {
		// traitement des différente commandes
		switch (Commande.valueOf(pCommande)) {
		case WELCOME:
			welcome();
			break;
		case START:
			break;
		case COMMANDE1:
			commande1();
			break;
		case COMMANDE2:
			commande2();
			break;
		case END:
			return false;
		}
		return true;
	}

	private void welcome() throws IOException {
		// en réponse à cette commande, on envoie son nom
		ecrire(nom);
	}

	public void commande1() throws IOException {
		ecrire(Math.random() > 0.5 ? "content" : "pas content");
		// récupération éventuelle du résultat dont on ne fait rien ici mais
		// qu'on pourrait traiter
		/* String message = */lire();
		// ...
	}

	public void commande2() throws IOException {
		// ...
	}

	/**
	 * Méthode présente ici uniquement pour permettre l'affichage des échanges sur la
	 * console
	 *
	 * @throws IOException
	 */
	private void ecrire(final String message) throws IOException {
		io.ecrire(message);
		System.out.println(nom + "-> ENVOI : " + message);
	}

	/**
	 * Méthode présente ici pour permettre l'affichage des échanges sur la
	 * console
	 *
	 * @throws IOException
	 */
	private String lire() throws IOException {
		String message = io.lire();
		System.out.println(nom + "-> LECTURE : " + message);
		return message;
	}

}
