package com.mygdx.game.Core.SFX;

import com.mygdx.game.Core.SFX.SoundFrame.soundsEnum;


/**
 * Stores data about how to load in a sound and where to store it to BlackCatStudio's code
 *
 * @author Felix Seanor
 * @date 18 /04/23
 */
public class SoundDataLoad {

  /**
   * The Path.
   */
  public String path;
  /**
   * The Name.
   */
  public soundsEnum name;

  /**
   * Instantiates a new Sound data load.
   *
   * @param path the path
   * @param name the name
   */
  public SoundDataLoad(String path, soundsEnum name) {
    this.path = path;
    this.name = name;
  }

}
