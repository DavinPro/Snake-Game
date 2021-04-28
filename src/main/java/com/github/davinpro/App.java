package com.github.davinpro;

import com.github.davinpro.SoundManager.Sound;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        App.stage = stage;

        scene = new Scene(loadFXML("MainMenu"), 640, 480);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();

        SoundManager.fadeInPlay(Sound.MENU_MUSIC, 10);
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static void setRoot(Parent root) {
      scene.setRoot(root);
    }

    public static FXMLLoader getLoader(String fxml) {
      return new FXMLLoader(App.class.getResource("/com/github/davinpro/view/" + fxml + ".fxml"));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        return getLoader(fxml).load();
    }

    public static Scene getScene() {
        return scene;
    }

    public static Stage getStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch();
    }

}