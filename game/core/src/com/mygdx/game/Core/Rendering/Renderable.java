package com.mygdx.game.Core.Rendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Abstraction of renderable images BlackCatStudio's Code
 *
 * @author Felix Seanor
 * @date 20/03/23
 */
public abstract class Renderable {

  /**
   * rendering layer higher means rendered on top
   */
  public int layer;

  public abstract void Render(SpriteBatch batch, float x, float y);

  public abstract void Destroy();

  public abstract void setSize(int x, int y);

  public Renderable(int layer) {
    this.layer = layer;
  }

  public Renderable() {
    layer = 0;
  }


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

  public abstract int GetWidth();

  public abstract int GetHeight();

}
