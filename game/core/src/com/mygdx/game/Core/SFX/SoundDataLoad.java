package com.mygdx.game.Core.SFX;

import com.mygdx.game.Core.SFX.soundFrame.soundsEnum;


/**
 * Stores data about how to load in a sound and where to store it to BlackCatStudio's code
 *
 * @author Felix Seanor
 * @date 18/04/23
 */
public class SoundDataLoad {

  public String path;
  public soundsEnum name;

  public SoundDataLoad(String Path, soundsEnum name) {
    this.path = Path;
    this.name = name;
  }

}
