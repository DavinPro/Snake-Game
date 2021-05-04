package com.github.davinpro.viewmodel;

import static com.github.davinpro.App.getLoader;

import com.github.davinpro.App;
import com.github.davinpro.SoundManager;
import com.github.davinpro.SoundManager.Sound;
import com.github.davinpro.model.Fruit;
import com.github.davinpro.model.Fruit.Type;
import com.github.davinpro.model.Snake;
import com.github.davinpro.model.Snake.Direction;
import com.github.davinpro.model.Wall;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class GameController {

  @FXML
  Pane gamePane;

  @FXML
  Label score;

  @FXML
  Label timeLabel;

  public static boolean WALLS_ENABLED = false;
  public static double BOUND_X = 0.0;
  public static double BOUND_Y = 0.0;
  public static final int GRID_SIZE = 20;
  private static final int NUM_FRUITS = 4;
  private static final int NUM_WALLS = 30;

  // Percent chance a special fruit will spawn
  private static final int SPECIAL_FRUIT_CHANCE = 10;

  private Timeline timeline;
  private AnimationTimer timer;

  private String name;
  private Snake snake;
  private ArrayList<Fruit> fruits;
  private ArrayList<Wall> walls;

  public void initialize(String name, Color bodyColor, Color headColor) {
    BOUND_X = gamePane.getPrefWidth();
    BOUND_Y = gamePane.getPrefHeight();
    gamePane.setStyle("-fx-background-color: #0F0F0F;");

    this.name = name;

    this.snake = new Snake(bodyColor, headColor);
    snake.draw(gamePane);

    this.fruits = new ArrayList<>();
    for(int i = 0; i < NUM_FRUITS; i++) {
      Fruit fruit = new Fruit(((int) (Math.random() * (BOUND_X / GRID_SIZE))) * GRID_SIZE + GRID_SIZE/2,
                              ((int) (Math.random() * (BOUND_Y / GRID_SIZE))) * GRID_SIZE + GRID_SIZE/2,
                                 Type.NORMAL);

      // Random chance Fruit becomes special
      if ((Math.random() * 100) < SPECIAL_FRUIT_CHANCE) {
        fruit.setType(Type.SPECIAL);
      } else {
        fruit.setType(Type.NORMAL);
      }

      fruits.add(fruit);
      fruit.draw(gamePane);
    }

    if (WALLS_ENABLED) {
      System.out.println("Creating walls...");
      this.walls = new ArrayList<>();
      for (int i = 0; i < NUM_WALLS; i++) {
        // Find a location to start wall
        double x, y;
        do {
          x = ((int) (Math.random() * (BOUND_X / GRID_SIZE))) * GRID_SIZE + GRID_SIZE/2f;
          y = ((int) (Math.random() * (BOUND_Y / GRID_SIZE))) * GRID_SIZE + GRID_SIZE/2f;
        } while (snake.onPoint(x, y) || onFruit(x, y) || onWall(x, y));
        Wall w = new Wall(x, y);
        walls.add(w);
        w.draw(gamePane);

        // Find locations around start location for wall segments
        int numWallSegs = (int) (Math.random()*5);

        for(int j = 0; j < numWallSegs; j++) {

          // Choose location adjacent to current location
          double direction = Math.random();
          if (direction < 0.25) {
            x -= GRID_SIZE;
          }
          else if (direction < 0.5) {
            x += GRID_SIZE;
          }
          else if (direction < 0.75) {
            y -= GRID_SIZE;
          }
          else if (direction < 1.0) {
            y += GRID_SIZE;
          }

          if (inBounds(x, y) && !snake.onPoint(x, y) && !onFruit(x, y) && !onWall(x, y)) {
            w = new Wall(x, y);
            walls.add(w);
            w.draw(gamePane);
          }
        }
      }
    }

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

      // Check collisions with Fruits
      for (Fruit fruit : fruits) {
        if (snake.onPoint(fruit.getX(), fruit.getY())) {
          // Increase Score
          score.setText("Score: " + (Integer.parseInt(score.getText().substring(7)) + fruit.getValue()));

          // Grow the Snake
          snake.grow();

          // Play sound effect
          SoundManager.play(Sound.EAT_FRUIT);

          // Move fruit, ensuring the new location is not occupied by the snake
          do {
            fruit.setPosition(((int) (Math.random() * (BOUND_X / GRID_SIZE))) * GRID_SIZE + GRID_SIZE/2.0,
                              ((int) (Math.random() * (BOUND_Y / GRID_SIZE))) * GRID_SIZE + GRID_SIZE/2.0);
          }
          while (snake.onPoint(fruit.getX(), fruit.getY()));

          // Random chance Fruit becomes special
          if ((Math.random() * 100) < SPECIAL_FRUIT_CHANCE) {
            fruit.setType(Type.SPECIAL);
          } else {
            fruit.setType(Type.NORMAL);
          }
        }
      }

      // Check collisions with walls
      if (WALLS_ENABLED) {
        for (Wall wall : walls) {
          if (snake.onPoint(wall.getX(), wall.getY())) {
            try {
              endGame();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
      }
    }));

    // Game Timer
    timer = new AnimationTimer() {
      private final long startTime = System.currentTimeMillis();

      @Override
      public void handle(long timestamp) {
        long curTime = System.currentTimeMillis() - startTime;
        timeLabel.setText(String.format("%dm %.1fs", ((curTime / 1000) / 60), (curTime / 1000.0) % 60));
      }
    };
    timer.start();
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
    timer.stop();

    SoundManager.play(Sound.GAME_OVER);
    SoundManager.fadeInPlay(Sound.MENU_MUSIC, 15);

    FXMLLoader loader = getLoader("EndScreen");
    Stage popupStage = new Stage();
    popupStage.initModality(Modality.APPLICATION_MODAL);
    popupStage.initOwner(App.getStage());
    popupStage.initStyle(StageStyle.UNDECORATED);
    popupStage.setScene(new Scene(loader.load()));

    String scoreStr = score.getText().substring(7);

    EndScreenController endScreen = loader.getController();
    endScreen.setName(name.isBlank() ? "Player 1" : name);
    endScreen.setScore(scoreStr);
    endScreen.setTime(timeLabel.getText());

    popupStage.show();

    String path;
    // Save score
    try {
      path = new File("./src/main/java/com/github/davinpro").getCanonicalPath();
      BufferedWriter bufferedWriter =
          new BufferedWriter(new FileWriter(path + "/Scores.txt", true));

      bufferedWriter.write((name.isBlank() ? "Player 1" : name) + ":" + scoreStr + ":" + timeLabel.getText());
      bufferedWriter.newLine();

      bufferedWriter.close();
    }
    catch(IOException ex) {
        ex.printStackTrace();
    }
  }

  private boolean inBounds(double x, double y) {
    return x > 0 && x < BOUND_X && y > 0 && y < BOUND_Y;
  }

  private boolean onWall(double x, double y) {
    for (Wall w : walls) {
      double a = Math.abs(w.getX() - x);
      double b = Math.abs(w.getY() - y);
      double c = Math.sqrt((a*a + b*b));

      if (c < GRID_SIZE) {
        return true;
      }
    }
    return false;
  }

  private boolean onFruit(double x, double y) {
    for (Fruit f : fruits) {
      double a = Math.abs(f.getX() - x);
      double b = Math.abs(f.getY() - y);
      double c = Math.sqrt((a*a + b*b));

      if (c <= Fruit.RADIUS) {
        return true;
      }
    }
    return false;
  }
}
