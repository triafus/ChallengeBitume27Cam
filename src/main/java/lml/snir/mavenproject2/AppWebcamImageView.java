/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lml.snir.mavenproject2;

import java.io.IOException;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AppWebcamImageView extends Application {

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void start(Stage stage) throws IOException {

    // La webcam par défaut
    Webcam webcam = Webcam.getDefault();
    // La taille originale de la webcam
    webcam.setViewSize(WebcamResolution.VGA.getSize());

    // L'afficheur d'image
    ImageView imageView = new ImageView();
    // L'élément racine
    StackPane root = new StackPane(imageView);

    // Ouvre la webcam
    webcam.open();

    Task task = new Task < Void > () {

      @Override
      protected Void call() throws Exception {
        boolean camStart = true;
        while (camStart) {
          // Conversion de l'image en image JavaFX
            WritableImage image = SwingFXUtils.toFXImage(webcam.getImage(), null);
          // Affichage de l'image
          imageView.setImage(image);
        }
        return null;
      }
    };

    // Tâche pour afficher l'image
    Thread thread = new Thread(task);
    thread.setDaemon(true); // Le thread est arrêté si l'application est quittée
    thread.start();

    Scene scene = new Scene(root, 640, 480);
    stage.setScene(scene);
    stage.show();
  }

}