package piazzapanictests.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.Key;

import static com.mygdx.game.Core.Inputs.*;
import static org.junit.Assert.assertEquals;

/**
 * This class tests the input keys for the game regarding chefs.
 *
 * @author Azzam Amirul
 * @satisfies UR_INTERACTION UR_COOK_MOVEMENT FR_CONTROL_CHEF
 * @date 28/04/2023
 */
@RunWith(GdxTestRunner.class)
public class InputKeysTests {


  /**
   * Test to for check the correct input keys to cycle stack on the chef
   *
   * @satisfies UR_INTERACTION FR_CONTROL_CHEF
   * @author Azzam Amirul
   * @date 28/04/2023
   */
  @Test
  public void CycleStackInputsTests() {
    assertEquals("Should be W key", CYCLE_STACK, Keys.W);

  }


  /**
   * Test to for check the correct input keys to interact using the chef
   *
   * @satisfies UR_INTERACTION FR_CONTROL_CHEF
   * @author Azzam Amirul
   * @date 28/04/2023
   */
  @Test
  public void ChefInteractInputs() {
    assertEquals("Should be Q key", GIVE_ITEM, Keys.Q);
    assertEquals("Should be E key", FETCH_ITEM, Keys.E);
    assertEquals("Should be F key", DROP_ITEM, Keys.F);
    assertEquals("Should be spacebar", INTERACT, Keys.SPACE);
  }

}
