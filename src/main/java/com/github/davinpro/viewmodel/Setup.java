package com.github.davinpro.viewmodel;

import static com.github.davinpro.App.getLoader;
import static com.github.davinpro.App.setRoot;

import com.github.davinpro.App;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class Setup {

  @FXML
  TextField txtfName;

  @FXML
  ColorPicker clrpSnakeColor;

  @FXML
  public void switchToMainMenu() throws IOException {
    App.setRoot("MainMenu");
  }

  @FXML
  public void startGame() throws IOException {
    String name = txtfName.getText();
    Color color = clrpSnakeColor.getValue();

    FXMLLoader loader = getLoader("Game");
    Parent root = loader.load();
    Game game = loader.getController();
    game.initialize(name, color);
    setRoot(root);
  }
}
