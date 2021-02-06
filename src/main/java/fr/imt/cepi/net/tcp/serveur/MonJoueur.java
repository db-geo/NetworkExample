package fr.imt.cepi.net.tcp.serveur;

import fr.imt.cepi.net.tcp.helper.InOut;

import java.io.IOException;
import java.net.Socket;

 class MonJoueur {
	private final InOut io;
	private final String nom;

	public MonJoueur(final Socket socket) throws IOException {
		io = new InOut(socket);
		// Envoi du message de bienvenue au client et attente du nom du joueur
		// en réponse
		ecrire("WELCOME");
		nom = lire();
	}

	/**
	 * Méthode présente ici pour permettre l'affichage des échanges sur la
	 * console
	 *
	 * @throws IOException
	 */
	public void ecrire(final String message) throws IOException {
		io.ecrire(message);
		System.out.println("Serveur -> Envoi à " + (nom == null ? "non renseigné" : nom) + " : " + message);
	}

	/**
	 * Méthode présente ici pour permettre l'affichage des échanges sur la
	 * console
	 *
	 * @throws IOException
	 */
	public String lire() throws IOException {
		String message = io.lire();
		System.out.println("Serveur -> Lecture depuis " + (nom == null ? "non renseigné" : nom) + " : " + message);
		return message;
	}
}
