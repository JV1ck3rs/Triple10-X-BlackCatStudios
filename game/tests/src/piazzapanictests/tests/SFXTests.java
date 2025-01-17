package piazzapanictests.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.mygdx.game.Core.SFX.ContinousSound;
import com.mygdx.game.Core.SFX.SoundFrame;
import com.mygdx.game.Core.SFX.SoundFrame.soundsEnum;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(GdxTestRunner.class)

/**
 * Tests for SFX to verify that
 * they are able to played/function correctly and are able to be loaded in
 * @satisfies FR_SOUND
 * @author Felix Seanor
 * @date 25/04/23
 */
public class SFXTests {

  /**
   * Tests if the sound frame loads in
   * @satisfies FR_SOUND
   * @author Felix Seanor
   * @date 25/04/23
   */
  @Test
  public void LoadInSounds(){
    SoundFrame frame = new SoundFrame();

    assertNotNull(frame);
  }
  /**
   * Tests if the sounds can play
   * @satisfies FR_SOUND
   * @author Felix Seanor
   * @date 25/04/23
   */
  @Test
  public void PlaySounds(){
    SoundFrame frame = new SoundFrame();

    long id = frame.playSound(soundsEnum.DropItem);

    assertTrue("Mustve created a sound id reference", id>=0);
  }
  /**
   * Tests if the continous sounds work correctly. Play Pause Loop
   * @satisfies FR_SOUND
   * @author Felix Seanor
   * @date 25/04/23
   */
  @Test
  public void PlayLoopingSounds(){
    SoundFrame frame = new SoundFrame();

    ContinousSound sounds = new ContinousSound(soundsEnum.Frying);
    assertTrue("Should start without a reference", sounds.soundID == -1);

    assertTrue("should start off", !sounds.shouldPlay);
    assertTrue("should start off", !sounds.getStopped());

    sounds.shouldPlay = true;

    sounds.doSoundCheck();
    assertTrue("Should have a reference", sounds.soundID >=0);

    assertTrue("should be playing", !sounds.getStopped());
    sounds.doSoundCheck();

    sounds.doSoundCheck();
    assertTrue("should be stopped", sounds.getStopped());

    sounds.shouldPlay = true;
    sounds.doSoundCheck();
    assertTrue("should be playing", !sounds.getStopped());
  }
  /**
   * Verifies that sounds change in volume when muted and a volume is set
   * @satisfies FR_SOUND
   * @author Felix Seanor
   * @date 25/04/23
   */
  @Test
  public void changeVolumes(){
    SoundFrame frame = new SoundFrame();

    frame.setSystemVolume(100);

    assertTrue("Volume must be 100", frame.currentSystemVolume==100);

    frame.muteSound();
  //  assertTrue("Volume must be 0", );

    frame.unmuteSound();

    assertTrue("Volume must be 100", frame.currentSystemVolume==100);


  }
}
