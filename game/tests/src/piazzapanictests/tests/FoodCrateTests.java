package piazzapanictests.tests;

import static org.junit.Assert.*;


import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for the FoodCrate class that have not already been carried out
 * in ChefTests.
 * @satisfies  UR_PANTRY  FR_PANTRY UR_INTERACTIONS
 * @author Jack Vickers
 */
@RunWith(GdxTestRunner.class)
public class FoodCrateTests extends  MasterTestClass {

  /**
   * Tests that the foodcrate can not be interacted with.
   *
   * @satisfies  UR_PANTRY FR_PANTRY UR_INTERACTIONS
   *
   * @author Jack Vickers
   * @author Hubert Solecki
   * @date 29/04/2023
   */
  @Test
  public void testCannotInteract() {
    instantiateWorldAndFoodCrate();
    assertFalse(FC.CanInteract());
    assertEquals(0.0, FC.Interact(), 0.1);
  }
}
