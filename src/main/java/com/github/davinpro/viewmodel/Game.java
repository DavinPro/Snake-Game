package com.github.davinpro.viewmodel;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class Game {

  String name;
  Color color;

  public void initialize(String name, Color color) {
    this.name = name;
    this.color = color;

  }
}
