package com.github.davinpro.viewmodel;

import static com.github.davinpro.App.getLoader;
import static com.github.davinpro.App.getScene;
import static com.github.davinpro.App.setRoot;

import com.github.davinpro.App;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class SetupController {

  @FXML
  TextField txtfName;

  @FXML
  ColorPicker bodyColorPicker;

  @FXML
  ColorPicker headColorPicker;

  @FXML
  public void switchToMainMenu() throws IOException {
    App.setRoot("MainMenu");
  }

  @FXML
  public void startGame() throws IOException {
    String name = txtfName.getText();
    Color bodyColor = bodyColorPicker.getValue();
    Color headColor = headColorPicker.getValue();

    FXMLLoader loader = getLoader("Game");
    Parent root = loader.load();
    GameController game = loader.getController();
    game.initialize(name, bodyColor, headColor);
    setRoot(root);
    game.run(getScene());
  }
}
