package com.github.davinpro.model;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Snake {

  private static final int SEGMENT_RADIUS = 10;
  private static final int NUM_STARTING_SEGMENTS = 6;

  public enum Direction {UP, DOWN, LEFT, RIGHT}
  private Direction direction = Direction.RIGHT;

  // Used to prevent multiple changes in direction during one animation frame
  private boolean changingDir = false;

  private final Color bodyColor;
  private final Color headColor;
  private final List<Circle> body;

  private final double boundX;
  private final double boundY;

  public Snake(Color bodyColor, Color headColor, double boundX, double boundY) {
    this.bodyColor = bodyColor;
    this.headColor = headColor;
    this.boundX = boundX;
    this.boundY = boundY;
    this.body = new ArrayList<>();

    // Calculate starting position, adjust for body segment radius if needed
    double x = boundX / 2;
    if (x % 20 == 0) { x += SEGMENT_RADIUS; }

    double y = boundY / 2;
    if (y % 20 == 0) { y += SEGMENT_RADIUS; }


    // Element 0 of the body is the snake's head
    body.add(new Circle(x, y, SEGMENT_RADIUS, headColor));

    for (int i = 1; i < NUM_STARTING_SEGMENTS; i++) {
      x -= 2* SEGMENT_RADIUS;
      body.add(new Circle(x, y, SEGMENT_RADIUS, bodyColor));
    }
  }

  public void draw(Pane pane) {
    pane.getChildren().addAll(body);
  }

  public void move() {
    Circle oldHead = body.get(0);
    oldHead.setFill(bodyColor);

    double centerX = oldHead.getCenterX();
    double centerY = oldHead.getCenterY();

    switch (direction) {
      case UP:
        centerY = oldHead.getCenterY() - 2 * SEGMENT_RADIUS;
        break;
      case DOWN:
        centerY = oldHead.getCenterY() + 2 * SEGMENT_RADIUS;
        break;
      case LEFT:
        centerX = oldHead.getCenterX() - 2 * SEGMENT_RADIUS;
        break;
      case RIGHT:
        centerX = oldHead.getCenterX() + 2 * SEGMENT_RADIUS;
        break;
    }

    // Remove tail and make it the new head, rather than move every body segment
    Circle newHead = body.remove(body.size() - 1);
    newHead.setFill(headColor);
    newHead.setCenterX(centerX);
    newHead.setCenterY(centerY);
    body.add(0, newHead);

    // Reset for next direction change
    changingDir = false;
  }

  public void setDirection(Direction direction) {
    this.direction = direction;
    changingDir = true;
  }

  public Direction getDirection() {
    return this.direction;
  }

  public boolean isChangingDir() {
    return changingDir;
  }

  public boolean collided() {
    double x = body.get(0).getCenterX();
    double y = body.get(0).getCenterY();

    if (x < SEGMENT_RADIUS || x > boundX-SEGMENT_RADIUS ||
        y < SEGMENT_RADIUS || y > boundY-SEGMENT_RADIUS) {
      return true;
    }

    for (int i = 4; i < body.size() - 1; i++) {
      if (body.get(i).getCenterX() == x && body.get(i).getCenterY() == y) {
        return true;
      }
    }
    return false;
  }
}
