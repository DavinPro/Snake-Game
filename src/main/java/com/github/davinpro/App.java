package com.github.davinpro;

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

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("MainMenu"), 640, 480);
        stage.setScene(scene);
        stage.show();
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

    public static void main(String[] args) {
        launch();
    }

}