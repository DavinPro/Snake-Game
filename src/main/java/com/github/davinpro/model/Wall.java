package com.github.davinpro.model;

import static com.github.davinpro.viewmodel.GameController.GRID_SIZE;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Wall {

  private static final Color COLOR = Color.GREY;
  private static final int WIDTH = GRID_SIZE;

  private final Rectangle shape;

  /**
   * Creates a Wall centered at (x,y)
   *
   * @param x The x coordinate of the Wall's position
   * @param y The y coordinate of the Wall's position
   */
  public Wall(double x, double y) {
    // Adjust (x,y) to be the center of the Rectangle
    this.shape = new Rectangle(x-WIDTH/2f, y-WIDTH/2f, WIDTH, WIDTH);
    this.shape.setFill(COLOR);
  }

  /**
   * Draws this Wall on the given Pane
   *
   * @param pane The pane on which to draw the Wall
   */
  public void draw(Pane pane) { pane.getChildren().add(shape); }

  /**
   * Get the x coordinate of this Wall's position
   */
  public double getX() { return this.shape.getX()+WIDTH/2f; }

  /**
   * Get the y coordinate of this Wall's position
   */
  public double getY() { return this.shape.getY()+WIDTH/2f; }
}
