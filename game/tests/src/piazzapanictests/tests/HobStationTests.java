package piazzapanictests.tests;

import com.badlogic.gdx.utils.ObjectMap;
import com.mygdx.game.Core.GameObjectManager;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;

import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Tests for the frying stations. Hobs only interact with raw patties and cooked patties so no other
 * items need to be used or checked.
 *
 * Satisfies requirements for UR_PREP, UR_WORKSTATIONS and UR_INTERACTIONS
 *
 * @author Hubert Solecki
 */

@RunWith(GdxTestRunner.class)
public class HobStationTests extends MasterTestClass {

  /**
   * Tests that an item can be removed from the frying station whether frying is complete or not.
   *
   * @author Hubert Solecki
   * @date 14/04/2023
   */

  @Test
  public void testRemoveItemFromFryer() {  // This also tests if the correct items can be placed on the hob
    if (GameObjectManager.objManager == null) {
      // creates a game object manager making sure it is not null when it is needed
      new GameObjectManager();
    }
    instantiateWorldAndHobsStation(); // creates hob station
    hobStation.GiveItem(new Item(ItemEnum.RawPatty)); // gives a raw patty to the frying station
    hobStation.RetrieveItem(); // attempts to retrieve the raw item from the hob
    assertNull("There should be no item on the hob station", hobStation.RetrieveItem());
    hobStation.GiveItem(new Item(
        ItemEnum.CookedPatty)); // gives a new raw patty to the hob to check if it can be retrieved when cooked
    hobStation.RetrieveItem(); // Attempts to retrieve a cooked patty from the hob to check if it can be retrieved
    assertNull("There should be no item on the hob station", hobStation.RetrieveItem());
    hobStation.GiveItem(new Item(
        ItemEnum.Cinder)); // gives a burnt patty to the hob to check if it can be retrieved
    hobStation.RetrieveItem(); // attempts to retrieve a cooked patty from the hob to heck if it can be retrieved
    assertNull("There should be no item on the hob station", hobStation.RetrieveItem());
    // test that a burnt patty can also be taken off

  }

  /**
   * Tests that the hob station can burn a burger if maximum progress for both cooking and
   * interaction is reached.
   *
   * @author Hubert Solecki
   * @date 17/04/2023
   */

  @Test
  public void testBurnItemOnHob() {
    if (GameObjectManager.objManager == null) {
      // creates a game object manager making sure it is not null when it is needed
      new GameObjectManager();
    }
    instantiateWorldAndHobsStation(); // creates ohb station
    hobStation.GiveItem(
        new Item(ItemEnum.RawPatty)); // gives a raw patty to the hob to test if it can be burnt
    hobStation.Cook(10);
    hobStation.Interact(); // sets the interaction variable to true and paired with the line below flips the patty
    hobStation.Cook(5);
    hobStation.Cook(
        10); // cooks the patty to completion for 10 delta times (final time step) to set progress to 0 afterwards
    hobStation.Cook(10); // burns the patty after one time step too many
    Item test = hobStation.RetrieveItem();
    assertEquals(
        "Tests that the retrieved burnt patty from the hob is the same as a cinder burnt item",
        test, new Item(ItemEnum.Cinder));
  }

  /**
   * Tests that incorrect items cannot be fried as current recipe on toaster station is null when
   * placed on the toaster.
   *
   * @author Hubert Solecki
   * @date 23/04/2023
   */
  @Test
  public void testIncorrectItemOnHob() {
    if (GameObjectManager.objManager == null) {
      // creates a game object manager making sure it is not null when it is needed
      new GameObjectManager();
    }
    Item rawPatty = new Item(
        ItemEnum.RawPatty); // creates the valid items that can be cooked on the hob for testing
    Item cookedPatty = new Item(ItemEnum.CookedPatty);
    instantiateWorldAndHobsStation(); // create hob station
    for (ItemEnum test : Arrays.asList(
        ItemEnum.values())) { // creates a list of all items in the game and steps through them to check which can interact with the hob
      Item testing = new Item(test);
      hobStation.GiveItem(testing);
      if (!((testing.equals(rawPatty)) || (testing.equals(
          cookedPatty)))) { // if the items are not a raw or cooked patty (i.e. items that can't interact with the hob), the current recipe should be null showing they cannot interact with the hob
        assertNull(
            "Current recipe should be null if the item is not on the white list of the hob station and therefore cannot be cooked",
            hobStation.currentRecipe);
      } else {
        assertNotNull(
            "Tests that if there are raw patties or cooked patties, they can be placed and be processed by the hob as they are in the white list",
            hobStation.currentRecipe);
      }
      hobStation.RetrieveItem(); // items on the hob must be removed so that the next items can be tested
    }
  }

  /**
   * Tests whether an item can be removed from the hob when it has nothing on it; should not allow
   *
   * @author Hubert Solecki
   * @date 17/04/2023
   */
  @Test
  public void testRemoveItemWhenHobEmpty() {
    if (GameObjectManager.objManager == null) {
      // creates a game object manager making sure it is not null when it is needed
      new GameObjectManager();
    }
    instantiateWorldAndHobsStation();
    assertFalse("The CanRetrieveItem() method should return false when there is nothing on the hob",
        hobStation.CanRetrieve());
    assertNull("The RetrieveItem() method should return null when no item is on the hob",
        hobStation.RetrieveItem());
  }

  /**
   * Tests whether an item can be placed on the hob when there is already an item on it; should
   * return false. Placing a different item on the hob should not change what is currently on the
   * hob.
   *
   * @author Hubert Solecki
   * @date 23/04/2023
   */
  @Test
  public void testGiveItemWhenHobFull() {
    if (GameObjectManager.objManager == null) {
      // creates a game object manager making sure it is not null when it is needed
      new GameObjectManager();
    }
    instantiateWorldAndHobsStation();
    Item patty = new Item(ItemEnum.RawPatty);
    Item cookedPatty = new Item(ItemEnum.CookedPatty);
    hobStation.GiveItem(
        patty); // gives an item to the hob to test if another item can be placed on it
    assertFalse(
        "The CanGive() method should return false when there is already an item placed on the hob",
        hobStation.CanGive());
    hobStation.GiveItem(cookedPatty);
    assertEquals(
        "The item on the hob should be unchanged if an item is placed on the hob when there already was an item on the hob",
        patty, hobStation.RetrieveItem());
  }


  /**
   * Tests that an item can be picked up during frying and that the progress of the item being
   * cooked is saved in its progress attribute.
   *
   * @author Hubert Solecki
   * @date 18/04/2023
   */

  @Test
  public void testItemRetrievedDuringFryingAndProgress() {
    if (GameObjectManager.objManager == null) {
      // creates a game object manager making sure it is not null when needed
      new GameObjectManager();
    }
    instantiateWorldAndHobsStation();
    hobStation.GiveItem(new Item(ItemEnum.RawPatty)); // gives a raw patty to the hob for the test
    hobStation.Cook(5);
    Integer testProgress = hobStation.getProgress();
    Item test = hobStation.RetrieveItem();
    assertNotEquals(
        "The value of the progress of the cooking item should not be 0 when it is removed from the hob",
        (int) testProgress, 0);
    assertNull("The hob should not contain an item when an in progress item is removed from it",
        hobStation.RetrieveItem());
    assertNotEquals(
        "The progress of the item should not be 0 when it has been cooking for some time and is removed",
        test.progress, 0);
    assertEquals(
        "The progress of the item being cooked should be the same before and after it is retrieved from the hob",
        (int) testProgress, (int) test.progress);
  }

  /**
   * Tests that the update function updates the hob station and sets interaction to false.
   *
   * @author Hubert Solecki
   * @date 21/04/2023
   */

  @Test
  public void testUpdateMethodHobStation() {
    if (GameObjectManager.objManager == null) {
      new GameObjectManager();
    }
    instantiateWorldAndHobsStation();
    hobStation.GiveItem(new Item(ItemEnum.RawPatty));
    hobStation.Update(10);
    hobStation.Interact();
    assertTrue("The interaction attribute is true when interacted with in the update function",
        hobStation.GetInteracted());
    assertNotNull("The recipe on the hob station is not null when there is an item on it",
        hobStation.currentRecipe);
    hobStation.Cook(5);
    hobStation.Update(10);
    Item test = hobStation.RetrieveItem();
    assertFalse(
        "The interaction attribute is set to false after the item is retrieved from the hob",
        hobStation.GetInteracted());
  }


}
