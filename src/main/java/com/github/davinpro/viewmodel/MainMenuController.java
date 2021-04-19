package com.github.davinpro.viewmodel;

import com.github.davinpro.App;
import java.io.IOException;
import javafx.fxml.FXML;

public class MainMenuController {

  @FXML
  public void openOptions() {
  }

  @FXML
  public void switchToSetup() throws IOException {
    App.setRoot("Setup");
  }
}
