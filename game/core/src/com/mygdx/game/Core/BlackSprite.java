package com.mygdx.game.Core;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * an abstraction for GDX sprite
 * BlackCatStudio's Code
 * @author Felix Seanor
 * @date 23/04/23
 */
public class BlackSprite extends Renderable {

  public Sprite sprite;


  /**
   * Render the sprite
   * @param batch batch draw
   * @param x x pos
   * @param y y pos
   * @author Felix Seanor
   */

  @Override
  public void Render(SpriteBatch batch, float x, float y) {
    sprite.setPosition(x, y);
    sprite.draw(batch);


  }

  /**
   * Destroy the sprite
   * @author Felix Seanor
   */
  @Override
  public void Destroy() {
    sprite = null;
  }

  /**
   * Set the size of the sprite
   * @param x
   * @param y
   * @author Felix Seanor
   */
  @Override
  public void setSize(int x,int y) {
    sprite.setSize(x, y);
  }

  /**
   * set the sprite to be any GDX sprite
   * @param sprite
   * @author Felix Seanor
   */
  public void setSprite(Sprite sprite) {
    if (this.sprite == null) {
      this.sprite = sprite;
      return;
    }
    this.sprite.set(sprite);
  }

  /**
   * returns the width
   * @return
   * @author Felix Seanor
   */
  public int GetWidth()
  {
    return (int)(sprite.getWidth());
  }

  /**
   * returns the height
   * @return
   * @author Felix Seanor
   */
  public int GetHeight(){
    return  (int)(sprite.getHeight());
  }


}
