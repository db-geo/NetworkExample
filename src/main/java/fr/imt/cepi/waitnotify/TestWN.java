package fr.imt.cepi.waitnotify;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Exemple d'application avec un process en attente d'un clic :
 */
public class TestWN extends Application {

    private String idRectClic; // identifiant du rectangle cliqué

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {

        // Un booleen (dans un environnement multi processus) qui indique
        // si l'on est en attente d'un clic ou non
        final AtomicBoolean attenteClic = new AtomicBoolean();
        // Un objet servant de verrou
        final Object verrou = new Object();

        BorderPane bp = new BorderPane();
        // Zone de texte pour afficher des messages
        TextArea ta = new TextArea("");
        ta.setEditable(false);
        // Création d'un groupe de rectangles qui sera mis dans la partie
        // centrale du border pane
        Group group = new Group();
        Rectangle r;
        Text t;
        for (int i = 0; i < 4; i++) {
            r = new Rectangle(i % 2 * 100, i / 2 * 100, 100, 100);
            r.setFill(Color.WHITE);
            r.setStroke(Color.BLACK);
            // On grise le rectangle au survol
            r.setOnMouseEntered((e) -> ((Rectangle) e.getSource()).setFill(Color.GRAY));
            r.setOnMouseExited((e) -> ((Rectangle) e.getSource()).setFill(Color.WHITE));
            // On sauvegarde des infos dans le rectangle pour l'identifier : il
            // vaudrait mieux avoir une sous-classe de Group qui contiendrait un
            // Rectangle (voire aussi une Image ou un Text...) et qui comme
            // attribut les coordonnées de la case représentée. Selon l'état de
            // la case, la couleur de fond du Rectangle (ou l'image affichée, ou
            // le texte) changerait...
            r.setId(String.valueOf(i));
            // Gestion du clic sur le rectangle
            r.setOnMouseClicked(e -> {
                // ici on est dans le FxApplicationThread :
                // (Platform.isFxApplicationThread() renvoie true)
                if (attenteClic.get()) {
                    // ici on est en attente du clic : on récupère donc l'id du
                    // rectangle
                    ta.appendText("Clic sur un rectangle traité car en attente d'un clic\n");
                    idRectClic = ((Rectangle) e.getSource()).getId();
                    attenteClic.set(false);
                    // on colorie le rectangle en rouge pour visualiser la prise
                    // en compte du clic
                    ((Rectangle) e.getSource()).setFill(Color.RED);
                    // On débloque le verrou
                    synchronized (verrou) {
                        verrou.notify();
                    }
                } else {
                    // on n'est pas en attente du clic : on ne le traite
                    ta.appendText("Clic sur un rectangle non traité car pas en attente d'un clic\n");
                }
            });
            // Ajout du rectangle au groupe
            group.getChildren().add(r);
            // Ajout d'un text pour visualiser le numéro du rectangle
            t = new Text(r.getX() + 10, r.getY() + 20, String.valueOf(i));
            group.getChildren().add(t);

        }
        bp.setCenter(group);
        bp.setRight(ta);

        // A cause de la boucle infinie ci-dessous, on termine
        // "brutalement" l'application lorsque l'on ferme la fenêtre
        primaryStage.setOnCloseRequest(e -> System.exit(0));

        primaryStage.setScene(new Scene(bp, 800, 600));
        primaryStage.show();

        // On lance le jeu une fois que toutes les opérations en attente dans le
        // FxApplicationThread sont terminée, en gros quand la fenêtre est
        // complètement configurée et visible
        Platform.runLater(() -> {
            // ici on est dans le FxApplicationThread :
            // (Platform.isFxApplicationThread() renvoie true)
            // Le traitement (jeu) doit se faire dans un processus différent de
            // celui qui s'occupe de l'affichage (FxApplicationThread) afin de
            // ne pas le bloquer.
            // On crée et on démarre donc un autre Thread pour notre jeu
            new Thread(() -> {

                while (true) {
                    // traitement avant l'attente du clic
                    // ...
                    // ici on n'est pas dans le FxApplicationThread :
                    // (Platform.isFxApplicationThread() renvoie false)
                    // Toute mise à jour de l'interface graphique doit se faire
                    // dans le FxApplicationThread. Du fait qu'ici ce n'est pas
                    // le cas, les mises à jour de l'interface graphique doivent
                    // se faire en appelant Platform->runLater() qui lui fera le
                    // traitement dans le Thread de javaFX.
                    attenteClic.set(true);
                    // Mise en attente du Thread courant (sans bloquer le
                    // TFxApplicationThread). Il sera débloqué par un appel à
                    // notify sur l'objet verrou (lors du clic sur un rectangle)
                    Platform.runLater(() -> ta.appendText(
                            "Traitement mis en attente : cliquez sur un rectangle et le traitement récupérera son numéro\n"));
                    synchronized (verrou) {
                        try {
                            verrou.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Platform.runLater(() -> ta.appendText("=> clic sur rectangle " + idRectClic + "\n"));
                    // traitement après le clic : on simule ici une attente de
                    // 10 secondes.
                    Platform.runLater(() -> ta.appendText("Le traitement se poursuit...\n"));
                    try {
                        Platform.runLater(() -> ta.appendText(
                                "Le clic n'est plus géré pendant 10 secondes : essayez de cliquer pour voir !\n"));
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {

                    }

                }
            }).start();
        });

    }
}
