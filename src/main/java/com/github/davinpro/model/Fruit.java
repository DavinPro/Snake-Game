package com.github.davinpro.model;

import static com.github.davinpro.viewmodel.GameController.GRID_SIZE;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Fruit {

  public enum Type {
    NORMAL(1, Color.RED),
    SPECIAL(2, Color.GOLD);

    private final int value;
    private final Color color;

    Type(int value, Color color) {
      this.value = value;
      this.color = color;
    }
  }

  public static final int RADIUS = GRID_SIZE * 3/8;

  private final Circle shape;
  private Type type;

  public Fruit(int x, int y, Type type) {
    this.shape = new Circle(x, y, RADIUS, type.color);
    this.type = type;
  }

  public void draw(Pane pane) {
    pane.getChildren().add(shape);
  }

  public void setType(Type type) {
    this.type = type;
    this.shape.setFill(type.color);
  }

  public double getX() { return shape.getCenterX(); }
  public double getY() { return shape.getCenterY(); }

  public int getValue() { return this.type.value; }

  public void setPosition(double x, double y) {
    this.shape.setCenterX(x);
    this.shape.setCenterY(y);
  }
}
