package com.mygdx.game.Core.Rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * an abstraction for texture region BlackCatStudio's Code
 *
 * @author Felix Seanor
 * @date 24/04/23
 */
public class BlackTexture extends Renderable {

  public Texture texture;
  public TextureRegion textureRegion;

  //Width and height of image
  public int imageWidth, imageHeight;
  //width and height of texture region
  public int width, height;


  /**
   * Generates a new texture from file path (data safe)
   *
   * @param tex
   * @author Felix Seanor
   */
  public BlackTexture(String tex) {

    changeTextureFromPath(tex);
  }


  /**
   * change the current texture on this texture region
   *
   * @param tex
   * @author Sam Toner
   */
  public void changeTexture(Texture tex) {
    texture = tex;
    textureRegion.setTexture(texture);
  }

  /**
   * Given a file path set up the texture, tries to reuse textures
   *
   * @param path
   * @author Felix Seanor
   */
  public void changeTextureFromPath(String path) {

    texture = TextureDictionary.textures.get(path);

    imageWidth = texture.getWidth();
    imageHeight = texture.getHeight();

    width = imageWidth;
    height = imageHeight;

    if (textureRegion == null) {
      textureRegion = new TextureRegion(texture, width, height);
    }

  }

  /**
   * sets the images width and height parameters
   *
   * @param _w
   * @param _h
   * @author Felix Seanor
   */
  public void setImageSize(int _w, int _h) {
    imageWidth = _w;
    imageHeight = _h;
  }

  /**
   * sets the texture regions width and height. If you want to change the size in game use this
   * one.
   *
   * @param _w
   * @param _h
   * @author Felix Seanor
   */
  public void setSize(int _w, int _h) {
    width = _w;
    height = _h;


  }

  /**
   * Sets wraps
   *
   * @param U
   * @param V
   * @author Felix Seanor
   */
  public void setWrap(Texture.TextureWrap U, Texture.TextureWrap V) {

    texture.setWrap(U, V);
  }

  /**
   * Set the wraps from TextureWrap
   *
   * @param UV
   * @author Felix Seanor
   */
  public void setWrap(Texture.TextureWrap UV) {
    setWrap(UV, UV);
  }

  /**
   * Render this texture region
   *
   * @param batch draw batch
   * @param x     pos x
   * @param y     pos y
   * @author Felix Seanor
   */
  @Override
  public void render(SpriteBatch batch, float x, float y) {
    batch.draw(textureRegion, x, y, width, height);
  }

  /**
   * Destroy this texture
   *
   * @author Felix Seanor
   */
  @Override
  public void destroy() {
    texture = null;
    textureRegion = null;
  }

  /**
   * @return texture regions width
   * @author Felix Seanor
   */
  public int getWidth() {
    return width;
  }

  /**
   * @return texture region height
   */
  public int getHeight() {
    return height;
  }


}
