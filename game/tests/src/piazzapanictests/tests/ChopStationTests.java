package piazzapanictests.tests;

import com.mygdx.game.Core.Rendering.GameObjectManager;
import com.mygdx.game.Core.GameState.Difficulty;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;

import com.mygdx.game.RecipeAndComb.Recipe;
import com.mygdx.game.RecipeAndComb.RecipeDict;

import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Tests for the chopping station.
 * <p>
 * @Satisfies Requirements for UR_PREP, UR_WORKSTATIONS, UR_INTERACTION, UR_COLLECT_ITEM, UR_REMOVE_ITEM,
 *
 * @author Jack Vickers
 * @author Azzam Amirul Bahri
 * @date 02/05/2023
 */
@RunWith(GdxTestRunner.class)
public class ChopStationTests extends MasterTestClass {

  /**
   * Tests that an item can be removed from the chopping board when it is not being chopped.
   * @Satisfies UR_INTERACTION, UR_COLLECT_ITEM
   * @author Jack Vickers
   * @date 30/03/2023
   */
  @Test
  public void testRemoveItemWhileNotChopping() {
    if (GameObjectManager.objManager == null) {
      // creates game object manager which makes sure that the game object manager is not null when it is needed
      new GameObjectManager();
    }
    instantiateWorldAndChoppingStation(); // creates chopping station
    chopStation.giveItem(new Item(ItemEnum.Lettuce)); // gives lettuce to chopping station
    chopStation.retrieveItem(); // attempts to retrieve item from chopping station
    assertNull("There should be no item on the chopping station", chopStation.returnItem());
  }

  /**
   * Tests which items are allowed to be placed on the chopping station by placing all items on the station and checking
   * the station's white list by seeing if there is a recipe on the station after that item is placed.
   * @Satisfies UR_WORKSTATIONS, UR_PREP
   * @author Azzam Amirul
   * @author Jack Vickers
   * @date 02/05/2023
   */
  @Test
  public void testPlaceItemChoppingInvalid() {
    if (GameObjectManager.objManager == null) {
      // creates game object manager which makes sure that the game object manager is not null when it is needed
      new GameObjectManager();
    }
    instantiateWorldAndChoppingStation(); // creates chopping station
    Item lettuce = new Item(ItemEnum.Lettuce);
    Item tomato = new Item(ItemEnum.Tomato);
    Item onion = new Item(ItemEnum.Onion);
    Item mince = new Item(ItemEnum.Mince);
    Item cutTomato = new Item(ItemEnum.CutTomato);
    Item dough = new Item(ItemEnum.Dough);
    for (ItemEnum test : Arrays.asList(ItemEnum.values())) {
      Item testing = new Item(test);
      chopStation.giveItem(testing);
      if (!((testing.equals(lettuce)) || (testing.equals(tomato)) || (testing.equals(onion))
          || (testing.equals(mince)) || (testing.equals(cutTomato)) || (testing.equals(dough)))) {
        assertNotNull("These items can be put on chopping station", chopStation.retrieveItem());
      }

    }
    assertNull("Item cannot be put on chopping station", chopStation.retrieveItem());

  }


  /**
   * Tests that an item cannot be given to the chopping station when there is already an item on it.
   * @Satisfies UR_INTERACTION, UR_REMOVE_ITEM
   * @author Azzam Amirul
   * @date 26/04/2023
   */
  @Test
  public void testGiveItemWhenChopStationFull() {
    if (GameObjectManager.objManager == null) {
      // creates a game object manager making sure it is not null when it is needed
      new GameObjectManager();
    }
    instantiateWorldAndChoppingStation();
    Item lettuce = new Item(ItemEnum.Lettuce);
    Item cutLettuce = new Item(ItemEnum.CutLettuce);
    chopStation.giveItem(
        lettuce); // gives an item to the hob to test if another item can be placed on it
    assertFalse(
        "The CanGive() method should return false when there is already an item placed on the chop station",
        chopStation.canGive());
    chopStation.giveItem(cutLettuce);
    assertEquals(
        "The item on the chop station should be unchanged if an item is placed on the chop station when there already was an item on there",
        lettuce, chopStation.retrieveItem());
  }


  /**
   * Tests that the chopping station can be given to and retrieved from using its CanGive() and CanRetrieve() methods
   * based on whether there is an item on the station.
   * @Satisfies UR_INTERACTION, UR_COLLECT_ITEM, UR_REMOVE_ITEM
   * @author Azzam Amirul
   * @date 26/04/2023
   */
  @Test
  public void testCanGiveCanRetrieveChopping() {
    instantiateWorldAndChoppingStation();
    Item item = new Item(ItemEnum.Lettuce);
    if (item == null) {
      assertFalse("Nothing to give chopping station", chopStation.canGive());
    } else {
      assertTrue("Item can be given to chopping station", chopStation.canGive());
    }
    if (item == null) {
      assertTrue("Nothing to retrieve at chopping station", chopStation.canRetrieve());
    } else {
      assertFalse("Item cannot be retrieved by chopping station", chopStation.canRetrieve());
    }
  }

  /**
   * Tests that the chopping station can be interacted with through its CanInteract() method.
   * @Satisfies UR_INTERACTION
   * @author Azzam Amirul
   * @date 26/04/2023
   */
  @Test
  public void testCanInteractChoppingStation() {
    instantiateWorldAndChoppingStation();
    Item item = new Item(ItemEnum.Lettuce);
    Recipe recipe = RecipeDict.recipes.RecipeMap.get(item.name);
    if (recipe == null) {
      assertTrue("Item can be interacted with chopping station", chopStation.canInteract());

    } else {
      assertFalse("Cannot be interacted with chopping station", chopStation.canInteract());
    }

  }

  /**
   * Tests the update method on the chop station checking that it can be interacted with and modifies items on it.
   * @Satisfies UR_WORKSTATIONS, UR_INTERACTION, UR_PREP
   * @author Azzam Amirul
   * @author Jack Vickers
   * @date 02/05/2023
   */
  @Test
  public void testUpdateMethodChopStation() {
    if (GameObjectManager.objManager == null) {
      new GameObjectManager();
      // creates a new game object manager making sure it is not null when needed
    }
    instantiateWorldAndChoppingStation();
    chopStation.giveItem(new Item(ItemEnum.Lettuce));
    chopStation.update(5);
    chopStation.interact();
    assertTrue("The interaction attribute is true when interacted with in the update function",
        chopStation.getInteracted());
    assertNotNull("The recipe on the hob station is not null when there is an item on it",
        chopStation.currentRecipe);
    chopStation.cut(5);
    chopStation.update(10);
    Item test = chopStation.retrieveItem();
    assertFalse(
        "The interaction attribute is set to false after the item is retrieved from the hob",
        chopStation.getInteracted());
  }

  /**
   * Tests that the chopping station cannot be used when it is locked.
   * @Satisfies UR_INTERACTION
   * @author Jack Vickers
   * @date 02/05/2023
   */
  @Test
  public void testCannotUseWhileLocked() {
    if (GameObjectManager.objManager == null) {
      new GameObjectManager();
      // creates a new game object manager making sure it is not null when needed
    }
    instantiateWorldAndChoppingStation();
    chopStation.setLocked(true);
    assertFalse("Cannot use chopping station when locked", chopStation.giveItem(new Item(ItemEnum.Lettuce)));
    assertNull("There should be nothing on the chopping station so retrieving an item should return null", chopStation.retrieveItem());
  }

  /**
   * Tests that the chopping station can be used when it is unlocked.
   * @Satisfies UR_INTERACTION
   * @Author Jack Vickers
   * @date 02/05/2023
   */
  @Test
  public void testCanUseWhenUnlocked() {
    if (GameObjectManager.objManager == null) {
      new GameObjectManager();
      // creates a new game object manager making sure it is not null when needed
    }
    instantiateCustomerScripts(Difficulty.Relaxing);
    // ensures that there is definitely enough money to unlock the oven (100 coins)
    for (int i = 0; i < 100; i++) {
      customerController.changeMoney(100);
    }
    instantiateWorldAndChoppingStation();
    chopStation.setLocked(true);
    chopStation.giveItem(new Item(ItemEnum.RepairTool)); // should unlock the station
    assertFalse("The chopping station should be unlocked", chopStation.getLocked());
    assertTrue("Can use chopping station when unlocked", chopStation.giveItem(new Item(ItemEnum.Lettuce)));
    assertNotNull("There should be an item on the chopping station so retrieving an item should not return null", chopStation.retrieveItem());
  }
}
