package com.github.davinpro.model;

import static com.github.davinpro.viewmodel.GameController.GRID_SIZE;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * A Snake is an entity that can be drawn on a {@link Pane}.
 * A Snake is made of a number of body segments, and can move across the pane in
 * a specified direction.
 */
public class Snake {

  private static final int SEGMENT_RADIUS = GRID_SIZE / 2;
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

  private boolean grow = false;

  /**
   * Creates a new instance of Snake with the specifed body color and head color.
   *
   * @param bodyColor The {@link Color} to use when drawing the Snake body
   * @param headColor The {@link Color} to use when drawing the Snake head
   * @param boundX    The bound in the x direction this Snake can move to without colliding
   * @param boundY    The bound in the y direction this Snake can move to without colliding
   */
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
    Circle newHead;
    if (grow) {
      newHead = new Circle(SEGMENT_RADIUS);
      ((Pane)body.get(0).getParent()).getChildren().add(newHead);
      grow = false;
    } else {
      newHead = body.remove(body.size() - 1);
    }

    newHead.setFill(headColor);
    newHead.setCenterX(centerX);
    newHead.setCenterY(centerY);
    body.add(0, newHead);

    // Reset for next direction change
    changingDir = false;
  }

  /**
   * Set this {@link Snake} to increase length by one segment next time the move method is called.
   */
  public void grow() { grow = true; }

  /**
   * A method to determine if a this {@link Snake} has collided with it's bounds or with itself.
   *
   * @return true if this {@link Snake} has collided, otherwise false
   */
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

  /**
   * A method to determine if a segment of this {@link Snake} is centered at coordinates (x, y).
   *
   * @param x The x coordinate of the point to check
   * @param y The y coordinate of the point to check
   * @return true if this {@link Snake} is on the point, otherwise false
   */
  public boolean onPoint(double x, double y) {
    for (int i = 1; i < body.size() - 1; i++) {
      if (body.get(i).getCenterX() == x && body.get(i).getCenterY() == y) {
        return true;
      }
    }
    return false;
  }

  /**
   * A method to determine if the head of this {@link Snake} is on a fruit.
   *
   * @param fruit The fruit to check
   * @return true if the head of this {@link Snake} is on the fruit.
   */
  public boolean ateFruit(Circle fruit) {
    return body.get(0).getCenterX() == fruit.getCenterX() && body.get(0).getCenterY() == fruit.getCenterY();
  }

  public boolean isChangingDir() {
    return changingDir;
  }

  public void setDirection(Direction direction) {
    this.direction = direction;
    changingDir = true;
  }

  public Direction getDirection() {
    return this.direction;
  }
}
