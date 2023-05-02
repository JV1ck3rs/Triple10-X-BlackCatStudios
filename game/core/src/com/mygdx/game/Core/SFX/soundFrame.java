package com.mygdx.game.Core.SFX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Uses an enum containing all the names of the sound effects Uses an array of all the sounds,
 * must have the same index as the enum Uses a hashmap to store the IDs for each sound
 * BlackCatStudio's code
 * @author Sam Toner
 * @author Felix Seanor
 * @date 18/04/23
 */
public class soundFrame {


   public enum soundsEnum {
    CustomerArrivedBell, FoodReadyBell, Frying, GasCooker, DropItem, EquipItem, KnifeChop, StepAchieved,BuyItem,end;
  }

  public static soundFrame SoundEngine;
  Sound[] Sounds = new Sound[soundsEnum.end.ordinal()];

  HashMap<soundsEnum, LinkedList<Long>> soundIDsMap = new HashMap<>();
 public float currentSystemVolume = 1.0f;
 public float Volume = 1.0f;


  public soundFrame(){
    LoadSounds.load(this);
    SoundEngine = this;
  }

  /**
   * @param ring - Enum value as the name of the sound
   * @return Returns the ID given when a sound is played Retrieves the sound from the array based on
   * the enum Plays the sound getting the ID If the sound already has previous IDs, add the ID to
   * the list If the sound doesnt already have IDs then create a new list and add that to the
   * hashmap under an enum key
   * @author Sam Toner
   */
  public long playSound(soundsEnum ring) {
    Sound toPlay = Sounds[ring.ordinal()];
    long soundID = toPlay.play(Volume);
    LinkedList<Long> soundIDs = new LinkedList<>();

    if (soundIDsMap.get(ring) == null) {
      soundIDs.push(soundID);
      soundIDsMap.put(ring, soundIDs);
    } else {
      soundIDs = soundIDsMap.get(ring);
      soundIDs.push(soundID);
      soundIDsMap.put(ring, soundIDs);
    }
    return soundID;
  }


  /**
   * @param ring - Enum value of the name of the sound
   * @param ID   - The ID required to interact with the sound instance
   *             <p>
   *             Find the sound from the enum, and pause it using the sound instance ID
   * @author Sam Toner
   */
  public void pauseSound(soundsEnum ring, long ID) {
    Sounds[ring.ordinal()].pause(ID);
  }

  /**
   * @param ring - Enum value of the name of the sound
   * @param ID   - The ID required to interact with the sound instance
   *             <p>
   *             Find the sound from the enum, and resume it using the sound instance ID
   * @author Sam Toner
   */

  public void resumeSound(soundsEnum ring, long ID) {
    Sounds[ring.ordinal()].resume(ID);
  }


  /**
   * @param ring  - Enum value of the name of the sound
   * @param ID    - The ID required to interact with the sound instance
   * @param state - The looping state, True = loop - False = stop looping
   *             <p>
   *             Find the sound from the enum, and set it to loop using the sound instance ID
   * @author Sam Toner
   */
  public void setLooping(soundsEnum ring, long ID, boolean state) {
    Sounds[ring.ordinal()].setLooping(ID, state);
  }


  /**
   * @param filepath - The filepath of the sound wanted to be played
   * @param ring     - Enum value of the name of the sound
   *                 <p>
   *                 Checks if the sound doesnt exist in the Sound array If it doesnt, create a
   *                 sound and add it to the array
   * @author Sam Toner
   */
  public void addSound(String filepath, soundsEnum ring) {
    if (Sounds[ring.ordinal()] == null) {
      Sound effect = Gdx.audio.newSound(Gdx.files.internal(filepath));
      Sounds[ring.ordinal()] = effect;
    }
  }

  /**
   * @param ring - Enum value of the name of the sound
   *
   *             Removes the sound from the Sound array in the position of the enum value
   * @author Sam Toner
   */
  public void removeSound(soundsEnum ring) {
    if (Sounds[ring.ordinal()] != null) {
      Sounds[ring.ordinal()] = null;
    }
  }

  public void setSystemVolume(float volume) {
    /**
     * @param volume - The volume wanted to set all the sounds to
     *
     * Sets the current system volume variable to the volume set
     * Iterate through all the sounds, and get the sound
     * Iterate through all the IDs of each sound, and set each instace of each sound to the required volume
     * @author Sam Toner
     * */
    if (volume != 0.0f) {
      currentSystemVolume = volume;
    }

    Volume = volume;
    for (soundsEnum key : soundIDsMap.keySet()) {
      LinkedList<Long> value = soundIDsMap.get(key);
      Sound currentSound = Sounds[key.ordinal()];
      for (int i = 0; i < value.size(); i++) {
        currentSound.setVolume(value.get(i), volume);
      }
    }
  }

  /**
   * Runs setSystemVolume with a sound of 0
   * @author Sam Toner
   */
  public void muteSound() {
    setSystemVolume(0.0f);
  }

  /**
   * Sets the volume back to the volume previous to mute, allows return to previous volume after a
   * mute
   * @author Sam Toner
   */
  public void unmuteSound() {
    setSystemVolume(currentSystemVolume);
  }
}