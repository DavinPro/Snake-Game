package com.github.davinpro.viewmodel;

import static com.github.davinpro.App.getLoader;
import static com.github.davinpro.App.setRoot;

import com.github.davinpro.SoundManager;
import com.github.davinpro.SoundManager.Sound;
import com.github.davinpro.model.Fruit;
import com.github.davinpro.model.Snake;
import com.github.davinpro.model.Snake.Direction;
import java.io.IOException;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class GameController {

  @FXML
  Pane gamePane;

  @FXML
  Label score;

  public static final int GRID_SIZE = 20;
  private static final int NUM_FRUITS = 4;

  //private final SoundManager soundManager = SoundManager.getInstance();
  private Timeline timeline;

  private String name;
  private Snake snake;
  private ArrayList<Circle> fruits;

  public void initialize(String name, Color bodyColor, Color headColor) {
    this.name = name;

    this.snake = new Snake(bodyColor, headColor, gamePane.getPrefWidth(), gamePane.getPrefHeight());
    snake.draw(gamePane);

    this.fruits = new ArrayList<>();
    for (int i = 0; i < NUM_FRUITS; i++) {
      int x = ((int) (Math.random() * (gamePane.getPrefWidth() / GRID_SIZE))) * GRID_SIZE + GRID_SIZE/2;
      int y = ((int) (Math.random() * (gamePane.getPrefHeight() / GRID_SIZE))) * GRID_SIZE + GRID_SIZE/2;

      this.fruits.add(new Circle(x, y, Fruit.RADIUS, Fruit.COLOR));
    }
    gamePane.getChildren().addAll(fruits);

    gamePane.setStyle("-fx-background-color: #0F0F0F;");

    timeline = new Timeline();
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.getKeyFrames().add(new KeyFrame(Duration.millis(125), event -> {
      snake.move();

      // Check if Snake collides with itself or pane bounds
      if (snake.collided()) {
        try {
          endGame();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      // Check if Snake collides with fruits
      for (Circle fruit : fruits) {
        if (snake.ateFruit(fruit)) {
          // Increase Score
          score.setText("Score: " + (Integer.parseInt(score.getText().substring(7)) + 1));

          // Grow the Snake
          snake.grow();

          // Play sound effect
          SoundManager.play(Sound.EAT_FRUIT);

          // Move fruit, ensuring the new location is not occupied by the snake
          do {
            fruit.setCenterX(((int) (Math.random() * (gamePane.getPrefWidth() / GRID_SIZE))) * GRID_SIZE + GRID_SIZE/2f);
            fruit.setCenterY(((int) (Math.random() * (gamePane.getPrefHeight() / GRID_SIZE))) * GRID_SIZE + GRID_SIZE/2f);
          }
          while (snake.onPoint(fruit.getCenterX(), fruit.getCenterY()));

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

    SoundManager.play(Sound.GAME_OVER);
    SoundManager.fadeInPlay(Sound.MENU_MUSIC, 15);

    FXMLLoader loader = getLoader("EndScreen");
    Parent root = loader.load();
    setRoot(root);

    EndScreenController endScreen = loader.getController();
    endScreen.setName(name.isBlank() ? "Player 1" : name);
    endScreen.setScore(score.getText());
  }
}
