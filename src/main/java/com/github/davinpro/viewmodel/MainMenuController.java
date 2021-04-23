package com.github.davinpro.viewmodel;

import com.github.davinpro.App;
import com.github.davinpro.SoundManager;
import com.github.davinpro.SoundManager.Sound;
import java.io.IOException;
import javafx.fxml.FXML;

public class MainMenuController {

  @FXML
  public void openOptions() throws IOException {
    SoundManager.play(Sound.BUTTON_PRESS);
    App.setRoot("Options");
  }

  @FXML
  public void switchToSetup() throws IOException {
    SoundManager.play(Sound.BUTTON_PRESS);
    App.setRoot("Setup");
  }
}
