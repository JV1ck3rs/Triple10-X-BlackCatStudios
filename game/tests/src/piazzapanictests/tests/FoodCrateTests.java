package piazzapanictests.tests;

import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for the FoodCrate class that have not already been carried out
 * in ChefTests\.
 *
 * @author Jack Vickers
 */
@RunWith(GdxTestRunner.class)
public class FoodCrateTests extends  MasterTestClass {

  /**
   * Tests that the foodcrate can not be interacted with.
   *
   * @author Jack Vickers
   */
  @Test
  public void testCannotInteract() {
    instantiateWorldAndFoodCrate();
    assertFalse(FC.CanInteract());
    assertFalse(FC.Interact());
  }
}