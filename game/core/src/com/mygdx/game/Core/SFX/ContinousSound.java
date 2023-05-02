package com.mygdx.game.Core.SFX;

import com.mygdx.game.Core.SFX.soundFrame.soundsEnum;

/**
 * Creates a continous looping sound that contains the logic to start and stop on any frame
 * BlackCatStudio's Code
 * @author Felix Seanor
 * @date 18/04/23
 */
public class ContinousSound
{
  public Long soundID;
  public Boolean ShouldPlay;

  public soundsEnum soundToPlay;
  private boolean stopped = false;

  /**
   * Run logic to see if the sound will stop, start or unpause this frame
   * @author Felix Seanor
   */
  public void DoSoundCheck(){

    if(!ShouldPlay && soundID != -1)
    {
      //Stop sound
      soundFrame.SoundEngine.pauseSound(soundToPlay,soundID);
      stopped = true;
    } else if (ShouldPlay && soundID == -1){
      //Play sound
     soundID = soundFrame.SoundEngine.playSound(soundToPlay);
     stopped = false;
       soundFrame.SoundEngine.setLooping(soundToPlay,soundID,true);
    } else if (soundID != -1 && ShouldPlay && stopped){
      soundFrame.SoundEngine.resumeSound(soundToPlay,soundID);
      stopped = false;
    }

    ShouldPlay = false;
  }

  public boolean getStopped(){return stopped;}

  /**
   * Create a new sound given a sound enum
   * @param name
   * @author Felix Seanor
   */
  public ContinousSound(soundsEnum name){
    soundToPlay = name;
    soundID = Long.valueOf(-1);
    ShouldPlay = false;
  }
}