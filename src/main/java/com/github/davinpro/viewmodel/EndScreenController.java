package com.github.davinpro.viewmodel;

import com.github.davinpro.App;
import com.github.davinpro.SoundManager;
import com.github.davinpro.SoundManager.Sound;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EndScreenController {

  @FXML
  Label name;

  @FXML
  Label score;

  public void setName(String name) {
    this.name.setText(name);
  }

  public void setScore(String score) {
    this.score.setText(score);
  }

  @FXML
  public void switchToMainMenu() throws IOException {
    SoundManager.play(Sound.BUTTON_PRESS);
    App.setRoot("MainMenu");
  }
}
