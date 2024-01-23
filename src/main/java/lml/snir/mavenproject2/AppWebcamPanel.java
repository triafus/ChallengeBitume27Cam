/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lml.snir.mavenproject2;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AppWebcamPanel extends Application {

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void start(Stage stage) {

    // La webcam par défaut
    Webcam webcam = Webcam.getDefault();
    // La taille originale de la webcam
    webcam.setViewSize(WebcamResolution.VGA.getSize());

    // Le panneau contenant la webcam
    WebcamPanel panel = new WebcamPanel(webcam);

    panel.setImageSizeDisplayed(true);

    // Le noeud swing pour afficher un élément swing dans un noeud JavaFX
    SwingNode swingNode = new SwingNode();
    swingNode.setContent(panel);

    Scene scene = new Scene(new StackPane(swingNode), 640, 480);
    stage.setScene(scene);

    // Ferme l'application quand la fenêtre est fermée
    stage.setOnCloseRequest((event) -> {
      System.exit(0);
    });

    //panel.getImage()
    stage.show();
  }

}