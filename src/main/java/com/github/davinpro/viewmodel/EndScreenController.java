package com.github.davinpro.viewmodel;

import com.github.davinpro.App;
import com.github.davinpro.SoundManager;
import com.github.davinpro.SoundManager.Sound;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class EndScreenController {

  @FXML
  Label name;

  @FXML
  Label score;

  @FXML
  Label time;

  public void setName(String name) {
    this.name.setText(name);
  }

  public void setScore(String score) {
    this.score.setText(score);
  }

  public void setTime(String time) { this.time.setText(time); }

  @FXML
  public void switchToMainMenu(ActionEvent event) throws IOException {
    SoundManager.play(Sound.BUTTON_PRESS);
    App.setRoot("MainMenu");
    ((Node) event.getSource()).getScene().getWindow().hide();
  }
}
