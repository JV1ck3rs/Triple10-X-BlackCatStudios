package com.mygdx.game.Core.SFX;

import java.util.LinkedList;
import java.util.List;

/**
 * Loads in sound into the game BlackCatStudios code
 *
 * @author Felix Seanor Last Modified 18/04/23
 */
public class LoadSounds {

  /**
   * Load.
   *
   * @param frame the frame
   */
  public static void load(SoundFrame frame) {
    List<SoundDataLoad> sounds = new LinkedList<>();

    sounds.add(
        new SoundDataLoad("customer_arrived_bell.wav", SoundFrame.soundsEnum.CustomerArrivedBell));
    sounds.add(new SoundDataLoad("food_ready_bell.wav", SoundFrame.soundsEnum.FoodReadyBell));
    sounds.add(new SoundDataLoad("frying.wav", SoundFrame.soundsEnum.Frying));
    sounds.add(new SoundDataLoad("gas_cooker.wav", SoundFrame.soundsEnum.GasCooker));
    sounds.add(new SoundDataLoad("ItemDrop.wav", SoundFrame.soundsEnum.DropItem));
    sounds.add(new SoundDataLoad("ItemEquip.mp3", SoundFrame.soundsEnum.EquipItem));
    sounds.add(new SoundDataLoad("knife_chop.wav", SoundFrame.soundsEnum.KnifeChop));
    sounds.add(new SoundDataLoad("step_achieved.wav", SoundFrame.soundsEnum.StepAchieved));
    sounds.add(new SoundDataLoad("CoinJingle.wav", SoundFrame.soundsEnum.BuyItem));

    for (SoundDataLoad sound : sounds) {
      frame.addSound("Sound/" + sound.path, sound.name);
    }

  }

}
