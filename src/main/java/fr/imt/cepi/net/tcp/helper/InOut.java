package fr.imt.cepi.net.tcp.helper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/** Classe de gestion des envois/réceptions de messages */
public class InOut {

	private final InputStream in;
	private final OutputStream out;

	public InOut(final Socket s) throws IOException {
		// Récupération des flux d'entrée et de sortie pour la lecture et
		// l'écriture des commandes
		in = s.getInputStream();
		out = s.getOutputStream();
	}

	/**
	 * Lecture d'une commande se terminant par \n\n
	 *
	 * @return la commande lue (sans les \n de fin)
	 * @throws IOException
	 */
	public String lire() throws IOException {
		StringBuilder sb = new StringBuilder();
		int c;
		int cPrev = -1;
		while ((c = in.read()) != -1) {
			sb.append((char) c);
			if (cPrev == '\n' && c == '\n') {
				break;
			}
			cPrev = c;
		}
		sb.setLength(sb.length() - 2);
		return sb.toString();
	}

	/**
	 * Ecriture d'une commande se terminant par \n\n
	 *
	 * @param cmd
	 *            commande à écrire
	 * @throws IOException
	 */
	public void ecrire(final String cmd) throws IOException {
		for (int i = 0; i < cmd.length(); i++) {
			out.write(cmd.charAt(i));
		}
		// ajout des \n en fin si nécessaire
		if (!cmd.endsWith("\n\n")) {
			out.write('\n');
			if (!cmd.endsWith("\n")) {
				out.write('\n');
			}
		}
	}
}
