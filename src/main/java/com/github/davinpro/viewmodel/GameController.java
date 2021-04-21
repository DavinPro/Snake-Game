package com.github.davinpro.viewmodel;

import static com.github.davinpro.App.getLoader;
import static com.github.davinpro.App.setRoot;

import com.github.davinpro.model.Snake;
import com.github.davinpro.model.Snake.Direction;
import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class GameController {

  @FXML
  Pane gamePane;

  private Timeline timeline;
  private String name;
  private Snake snake;

  public void initialize(String name, Color bodyColor, Color headColor) {
    this.name = name;

    gamePane.setStyle("-fx-background-color: #0F0F0F;");

    this.snake = new Snake(bodyColor, headColor, gamePane.getPrefWidth(), gamePane.getPrefHeight());
    snake.draw(gamePane);

    timeline = new Timeline();
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.getKeyFrames().add(new KeyFrame(Duration.millis(125), event -> {
      // Move snake
      snake.move();

      if (snake.collided()) {
        try {
          endGame();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }));
  }

  public void run(Scene scene) {

    // Change the direction the snake is moving based on the keys pressed
    // only if the snake is not moving in the opposite direction and the
    // snake's direction has not yet been changed this animation frame
    scene.setOnKeyPressed(event -> {
      if (!snake.isChangingDir()) {
        switch (event.getCode()) {
          case UP:
          case W:
            if (snake.getDirection() != Direction.DOWN) {
              snake.setDirection(Direction.UP);
            }
            break;
          case DOWN:
          case S:
            if (snake.getDirection() != Direction.UP) {
              snake.setDirection(Direction.DOWN);
            }
            break;
          case LEFT:
          case A:
            if (snake.getDirection() != Direction.RIGHT) {
              snake.setDirection(Direction.LEFT);
            }
            break;
          case RIGHT:
          case D:
            if (snake.getDirection() != Direction.LEFT) {
              snake.setDirection(Direction.RIGHT);
            }
            break;
        }
      }
    });

    timeline.play();
  }

  private void endGame() throws IOException {
    timeline.stop();

    FXMLLoader loader = getLoader("EndScreen");
    Parent root = loader.load();
    setRoot(root);

    EndScreenController endScreen = loader.getController();
    endScreen.setName(name.isBlank() ? "Player 1" : name);
  }
}
