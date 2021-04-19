package com.github.davinpro.viewmodel;

import com.github.davinpro.model.Snake;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class GameController {

  private String name;
  private Snake snake;

  public void initialize(String name, Color color) {
    this.name = name;
    this.snake = new Snake(color);

  }
}
