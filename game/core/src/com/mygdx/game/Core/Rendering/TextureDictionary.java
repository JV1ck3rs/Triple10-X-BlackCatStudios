package com.mygdx.game.Core.Rendering;

import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;

/**
 * Tries to reuse and store texture where possible. Otherwise, load in from disk BlackCatStudio's
 * Code
 *
 * @author Felix Seanor Last modified 15/04/2023
 */
public class TextureDictionary {

  /**
   * The constant textures.
   */
  public static TextureDictionary textures;

  private HashMap<String, Texture> storedTextures = new HashMap<>();


  /**
   * instantiates the texture dictionary singleton
   */
  public TextureDictionary() {
    if (textures != null) {
      return;
    }

    textures = this;
  }

  /**
   * Returns a texture from the given path. No knowledge is given if this was already in disk or not
   * to other scripts
   *
   * @param path the path
   * @return texture
   */
  public Texture get(String path) {

    if (storedTextures.containsKey(path)) {
      return storedTextures.get(path);
    }

    Texture tex = new Texture(path);

    storedTextures.put(path, tex);
    return tex;
  }

}
