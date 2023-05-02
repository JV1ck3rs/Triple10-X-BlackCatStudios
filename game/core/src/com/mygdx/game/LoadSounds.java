package com.mygdx.game;

import java.util.LinkedList;
import java.util.List;

/**
 * Loads in sound into the game
 * BlackCatStudios code
 * @author Felix Seanor
 * Last Modified 18/04/23
 */
public class LoadSounds {

  static void load(soundFrame frame){
    List<SoundDataLoad> sounds = new LinkedList<>();

    sounds.add(new SoundDataLoad("customer_arrived_bell.wav",soundFrame.soundsEnum.CustomerArrivedBell));
    sounds.add(new SoundDataLoad("food_ready_bell.wav",soundFrame.soundsEnum.FoodReadyBell));
    sounds.add(new SoundDataLoad("frying.wav",soundFrame.soundsEnum.Frying));
    sounds.add(new SoundDataLoad("gas_cooker.wav",soundFrame.soundsEnum.GasCooker));
    sounds.add(new SoundDataLoad("ItemDrop.wav",soundFrame.soundsEnum.DropItem));
    sounds.add(new SoundDataLoad("ItemEquip.mp3",soundFrame.soundsEnum.EquipItem));
    sounds.add(new SoundDataLoad("knife_chop.wav",soundFrame.soundsEnum.KnifeChop));
    sounds.add(new SoundDataLoad("step_achieved.wav",soundFrame.soundsEnum.StepAchieved));
    sounds.add(new SoundDataLoad("CoinJingle.wav",soundFrame.soundsEnum.BuyItem));

    for (SoundDataLoad sound:sounds)
      frame.addSound("Sound/"+sound.Path,sound.name);

  }

}
