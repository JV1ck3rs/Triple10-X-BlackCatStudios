package com.mygdx.game.Core.SFX;

import com.mygdx.game.Core.SFX.SoundFrame.soundsEnum;

/**
 * Creates a continous looping sound that contains the logic to start and stop on any frame
 * BlackCatStudio's Code
 *
 * @author Felix Seanor
 * @date 18 /04/23
 */
public class ContinousSound {

  /**
   * The Sound id.
   */
  public Long soundID;
  /**
   * The Should play.
   */
  public Boolean shouldPlay;

  /**
   * The Sound to play.
   */
  public soundsEnum soundToPlay;
  private boolean stopped = false;

  /**
   * Run logic to see if the sound will stop, start or unpause this frame
   *
   * @author Felix Seanor
   */
  public void doSoundCheck() {

    if (!shouldPlay && soundID != -1) {
      //Stop sound
      SoundFrame.SoundEngine.pauseSound(soundToPlay, soundID);
      stopped = true;
    } else if (shouldPlay && soundID == -1) {
      //Play sound
      soundID = SoundFrame.SoundEngine.playSound(soundToPlay);
      stopped = false;
      SoundFrame.SoundEngine.setLooping(soundToPlay, soundID, true);
    } else if (soundID != -1 && shouldPlay && stopped) {
      SoundFrame.SoundEngine.resumeSound(soundToPlay, soundID);
      stopped = false;
    }

    shouldPlay = false;
  }

  /**
   * Gets stopped.
   *
   * @return the stopped
   */
  public boolean getStopped() {
    return stopped;
  }

  /**
   * Create a new sound given a sound enum
   *
   * @param name the name
   * @author Felix Seanor
   */
  public ContinousSound(soundsEnum name) {
    soundToPlay = name;
    soundID = Long.valueOf(-1);
    shouldPlay = false;
  }
}
