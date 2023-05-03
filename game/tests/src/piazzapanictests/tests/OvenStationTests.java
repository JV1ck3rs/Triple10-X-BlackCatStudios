package piazzapanictests.tests;

import com.mygdx.game.Core.Rendering.GameObjectManager;
import com.mygdx.game.Core.GameState.Difficulty;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;

import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Tests for the oven stations. Ovens only interact with pizzas and jacket potatoes.
 * <p>
 * @satisfies UR_PREP UR_WORKSTATIONS UR_INTERACTIONS UR_COLLECT_ITEM UR_BURN_FOOD UR_SPEND_EARNINGS
 * @author Hubert Solecki
 * @author Jack Vickers
 * @date 02/05/2023
 */

@RunWith(GdxTestRunner.class)
public class OvenStationTests extends MasterTestClass {

  /**
   * Tests that an item can be removed from the oven station whether cooking is complete or not.
   * @satisfies UR_PREP UR_INTERACTIONS UR_COLLECT_ITEM
   * @author Hubert Solecki
   * @date 30/04/2023
   */

  @Test
  public void testRemoveItemFromOven() {
    if (GameObjectManager.objManager == null) {
      new GameObjectManager();
      // creates a game object manager making sure it is not null when needed
    }
    instantiateWorldAndOvenStation(); // creates oven station
    ovenStation.giveItem(new Item(ItemEnum.CheesePizza)); // gives the oven station a cheese pizza
    ovenStation.retrieveItem(); // attempts to retrieve the uncooked pizza from the oven
    assertNull("The oven station should return null when attempted to retrieve nothing from it",
        ovenStation.retrieveItem());
  }

  /**
   * Tests that the oven station can burn an item if maximum progress is exceeded.
   * @satisfies UR_PREP UR_INTERACTIONS UR_BURN_FOOD
   * @author Hubert Solecki
   * @date 30/04/2023
   */

  @Test
  public void testBurnItemOnOven() {
    if (GameObjectManager.objManager == null) {
      new GameObjectManager();
      // creates a new game object manager making sure it is not null when needed.
    }
    instantiateWorldAndOvenStation();
    ovenStation.giveItem(new Item(ItemEnum.CheesePizza)); // gives a cheese pizza to the oven
    ovenStation.cook(10);
    ovenStation.cook(10); // cooks item till max progress is exceeded
    Item test = ovenStation.retrieveItem(); // retrieves the burnt item from the oven
    assertEquals("The retrieved item should be equal to cinder signalling burnt",
        new Item(ItemEnum.Cinder), test);
  }

  /**
   * Tests that incorrect items cannot be cooked in the oven as current recipe on oven station is
   * null when incorrect items are placed on it.
   * @satsifies UR_INTERACTIONS
   * @author Hubert Solecki
   * @date 02/05/2023
   */

  @Test
  public void testIncorrectItemOnOven() {
    if (GameObjectManager.objManager == null) {
      // creates a game object manager making sure it is not null when needed
      new GameObjectManager();
    }

    List whitelist = new ArrayList();
    whitelist.add(new Item(ItemEnum.CheesePizza));
    whitelist.add(new Item(ItemEnum.CheesePizzaCooked));
    whitelist.add(new Item(ItemEnum.MeatPizza));
    whitelist.add(new Item(ItemEnum.MeatPizzaCooked));
    whitelist.add(new Item(ItemEnum.VegPizza));
    whitelist.add(new Item(ItemEnum.VegPizzaCooked));
    whitelist.add(new Item(ItemEnum.Potato));
    whitelist.add(new Item(ItemEnum.BakedPotato));
    whitelist.add(new Item(ItemEnum.MeatPotato));
    whitelist.add(new Item(ItemEnum.MeatBake));
    whitelist.add(new Item(ItemEnum.CheesePotato));
    whitelist.add(new Item(ItemEnum.CheeseBake));
    instantiateWorldAndOvenStation();
    for (ItemEnum test : Arrays.asList(
        ItemEnum.values())) { // creates a list of all items in the game and steps through them to check which can interact with the oven
      Item testing = new Item(test);
      ovenStation.giveItem(testing);
      if (!(whitelist.contains(
          testing))) { // if the valid item is not being tested, the current recipe on the oven station should be null
        assertNull("Current recipe should be null if an incorrect item is placed on the oven",
            ovenStation.currentRecipe);
        assertFalse("Giving an invalid item to the oven should return false",
            ovenStation.giveItem(testing));
      } else {
        assertNotNull(
            "The current recipe on the oven should not be null if there is a correct item in the oven",
            ovenStation.currentRecipe);
      }
      ovenStation.retrieveItem();
    }
  }

  /**
   * Tests whether an item can be removed from the oven when there is nothing on it; should not
   * allow.
   * @satisfies UR_INTERACTIONS UR_COLLECT_ITEM
   * @author Hubert Solecki
   * @date 30/04/2023
   */

  @Test
  public void testRemoveItemWhenOvenEmpty() {
    if (GameObjectManager.objManager == null) {
      new GameObjectManager();
      // creates a game object manager making sure it is not null when needed
    }
    instantiateWorldAndOvenStation();
    assertFalse("The CanRetrieve() method should return false if there is nothing in the oven",
        ovenStation.canRetrieve());
    assertNull("The RetrieveItem() method should return null when no item is on the hob",
        ovenStation.retrieveItem());
  }

  /**
   * Tests whether an item can be placed on the hob when there is an item currently on it; should
   * return false. Placing a different item on the hob should not change what is currently on the
   * hob.
   * @satisfies UR_INTERACTIONS UR_REMOVE_ITEM
   * @author Hubert Solecki
   * @date 30/04/2023
   */

  @Test
  public void testGiveItemWhenHobFull() {
    if (GameObjectManager.objManager == null) {
      new GameObjectManager();
      // creates a game object manager making sure it is not null when needed
    }
    instantiateWorldAndOvenStation();
    Item cheesePizza = new Item(ItemEnum.CheesePizza);
    Item cheesePizzaCooked = new Item(ItemEnum.CheesePizzaCooked);
    ovenStation.giveItem(cheesePizza);
    assertFalse(
        "The CanGive() method should return false when there is an item already in the oven",
        ovenStation.canGive());
    ovenStation.giveItem(cheesePizzaCooked);
    assertEquals(
        "The item in the oven should be unchanged if an item is placed in the oven when there already was an item in the oven",
        new Item(ItemEnum.CheesePizza), ovenStation.retrieveItem());
  }

  /**
   * Tests that an item can be picked up during cooking and that the progress of the item being
   * cooked is saved in its progress attribute.
   * UR_INTERACTIONS UR_COLLECT_ITEM UR_PREP
   * @author Hubert Solecki
   * @date 30/04/2023
   */

  @Test
  public void testItemRetrievedDuringCookingAndProgress() {
    if (GameObjectManager.objManager == null) {
      new GameObjectManager();
      // creates a new game object manager making sure it is not null when needed
    }
    instantiateWorldAndOvenStation();
    Item cheesePizza = new Item(ItemEnum.CheesePizza);
    ovenStation.giveItem(cheesePizza); // gives a pizza to the oven for testing
    ovenStation.cook(5); // cooks the item halfway for the test
    assertEquals("The progress of the station and the item should be the same",
        (int) cheesePizza.progress, (int) ovenStation.getProgress());
    Integer testProgress = ovenStation.getProgress();
    Item test = ovenStation.retrieveItem();
    assertNotEquals(
        "The value of the progress of the cooking item should not be 0 when it is removed from the oven",
        (int) testProgress, 0);
    assertNull("The oven should not contain an item when an in progress item is removed from it",
        ovenStation.retrieveItem());
    assertNotEquals(
        "The progress of the item should not be 0 when it has been cooking for some dt and is removed",
        test.progress, 0);
    assertEquals(
        "The progress of the item being cooked should be the same before and after it is retrieved from the oven",
        (int) testProgress, (int) test.progress);
  }

  /**
   * Tests that the update function updates the oven station and sets interaction to false.
   * @satisfies UR_PREP
   * @author Hubert Solecki
   * @date 30/04/2023
   */

  @Test
  public void testUpdateMethodOvenStation() {
    if (GameObjectManager.objManager == null) {
      new GameObjectManager();
      // creates a new game object manager making sure it is not null when needed
    }
    instantiateWorldAndOvenStation();
    ovenStation.giveItem(new Item(ItemEnum.CheesePizza));
    ovenStation.update(10);
    ovenStation.interact();
    assertFalse("The chef should not be able to interact with the oven", ovenStation.canInteract());
    assertEquals("The chef should not be able to interact with the oven", 0.0,
        ovenStation.interact(), 0.1);
    Item test = ovenStation.retrieveItem();
    assertNotEquals(
        "The progress of the item retrieved from the toaster should not be 0 if the update function works as it will cook the item",
        0, test.progress);
  }

  /**
   * Tests that the oven station cannot be used while locked.
   * @satisfies UR_PREP
   * @author Jack Vickers
   * @date 02/05/2023
   */
  @Test
  public void testCannotUseWhileLocked() {
    if (GameObjectManager.objManager == null) {
      new GameObjectManager();
      // creates a new game object manager making sure it is not null when needed
    }
    instantiateWorldAndOvenStation();
    ovenStation.setLocked(true);
    assertFalse("You should not be able to give anything to an oven while it is locked",
        ovenStation.giveItem(new Item(ItemEnum.CheesePizza)));
    assertNull("There should be nothing in the oven", ovenStation.retrieveItem());
  }

  /**
   * Tests that the oven station can be unlocked and that a valid item can be given to it
   * afterwards.
   * @satisfies UR_PREP UR_SPEND_EARNINGS
   * @author Jack Vickers
   * @date 02/05/2023
   */
  @Test
  public void testCanUnlockOven() {
    if (GameObjectManager.objManager == null) {
      new GameObjectManager();
      // creates a new game object manager making sure it is not null when needed
    }
    instantiateWorldAndOvenStation();
    instantiateCustomerScripts(Difficulty.Relaxing);
    // ensures that there is enough money to unlock the oven
    for (int i = 0; i < 100; i++) {
      customerController.changeMoney(100);
    }
    ovenStation.setLocked(true);
    ovenStation.giveItem(new Item(ItemEnum.RepairTool)); // should unlock the oven
    assertFalse("The oven should not be locked", ovenStation.getLocked());
    Item itemToGive = new Item(ItemEnum.CheesePizza);
    assertTrue("The oven should be able to be given an item", ovenStation.giveItem(itemToGive));
    assertEquals("The item in the oven should be the same as the item given", itemToGive,
        ovenStation.retrieveItem());
  }
}
