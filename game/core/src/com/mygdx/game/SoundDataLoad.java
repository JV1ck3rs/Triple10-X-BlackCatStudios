package com.mygdx.game;

import com.mygdx.game.soundFrame.soundsEnum;


/**
 * Stores data about how to load in a sound and where to store it to
 * BlackCatStudio's code
 * @author Felix Seanor
 */
public class SoundDataLoad {

  public String Path;
  public soundsEnum name;

  public SoundDataLoad(String Path, soundsEnum name){
    this.Path = Path;
    this.name = name;
  }

}
