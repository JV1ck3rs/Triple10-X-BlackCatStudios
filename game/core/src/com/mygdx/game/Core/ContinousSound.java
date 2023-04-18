package com.mygdx.game.Core;

import com.mygdx.game.soundFrame;
import com.mygdx.game.soundFrame.soundsEnum;

public class ContinousSound
{
  public Long soundID;
  public Boolean ShouldPlay;

  public soundsEnum soundToPlay;
  private boolean stopped = false;

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

  public ContinousSound(soundsEnum name){
    soundToPlay = name;
    soundID = Long.valueOf(-1);
    ShouldPlay = false;
  }
}
