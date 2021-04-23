package com.github.davinpro.viewmodel;

import com.github.davinpro.App;
import com.github.davinpro.SoundManager;
import com.github.davinpro.SoundManager.Sound;
import com.github.davinpro.SoundManager.SoundType;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;

public class OptionsController {

  @FXML
  Slider musicSlider;

  @FXML
  Slider sFXSlider;

  @FXML
  protected void initialize() {
    musicSlider.setValue(SoundType.MUSIC.getVolume());
    sFXSlider.setValue(SoundType.SFX.getVolume());
  }

  public void updateMusicVolume() {
    SoundManager.changeVolume(SoundType.MUSIC, musicSlider.getValue());
  }

  public void updateSFXVolume() {
    SoundManager.changeVolume(SoundType.SFX, sFXSlider.getValue());
  }

  public void switchToMainMenu() throws IOException {
    SoundManager.play(Sound.BUTTON_PRESS);
    App.setRoot("MainMenu");
  }
}
