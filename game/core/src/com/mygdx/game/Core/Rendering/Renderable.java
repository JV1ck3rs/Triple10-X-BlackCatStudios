package com.mygdx.game.Core.Rendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Abstraction of renderable images BlackCatStudio's Code
 *
 * @author Felix Seanor
 * @date 20 /03/23
 */
public abstract class Renderable {

  /**
   * rendering layer higher means rendered on top
   */
  public int layer;

  /**
   * Render.
   *
   * @param batch the batch
   * @param x     the x
   * @param y     the y
   */
  public abstract void render(SpriteBatch batch, float x, float y);

  /**
   * Destroy.
   */
  public abstract void destroy();

  /**
   * Sets size.
   *
   * @param x the x
   * @param y the y
   */
  public abstract void setSize(int x, int y);

  /**
   * Instantiates a new Renderable.
   *
   * @param layer the layer
   */
  public Renderable(int layer) {
    this.layer = layer;
  }

  /**
   * Instantiates a new Renderable.
   */
  public Renderable() {
    layer = 0;
  }


  /**
   * Compare int.
   *
   * @param o1 the o 1
   * @param o2 the o 2
   * @return the int
   */
  public int compare(Renderable o1, Renderable o2) {
    if (o1.layer < o2.layer) {
      return -1;
    }
    if (o1.layer == o2.layer) {
      return 0;
    }

    //o1 > o2
    return 1;
  }

  /**
   * Gets width.
   *
   * @return the width
   */
  public abstract int getWidth();

  /**
   * Gets height.
   *
   * @return the height
   */
  public abstract int getHeight();

}
