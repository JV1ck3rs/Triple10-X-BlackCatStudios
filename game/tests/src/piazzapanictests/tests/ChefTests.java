package piazzapanictests.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Core.GameObjectManager;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;
import com.mygdx.game.RecipeAndComb.CombinationDict;
import com.mygdx.game.Stations.FoodCrate;

import java.util.Stack;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests to do with the Chefs.
 *
 * @author Jack Vickers
 * @author Hubert Solecki
 * @author Azzam Amirul Bahri
 */
@RunWith(GdxTestRunner.class)
public class ChefTests extends MasterTestClass {

  /**
   * Tests that the chef can drop an item.
   *
   * @author Jack Vickers
   * @date 26/03/2023
   */
  @Test
  public void testDropItem() {
    instantiateWorldAndChefs();
    chef[0].GiveItem(new Item(ItemEnum.Mince)); // Give the chef an item
    int inventorySize = chef[0].getInventoryCount(); // Get the size of the inventory
    chef[0].DropItem(); // Drop the item
    assertEquals("The chef should have 1 less item in their inventory after dropping it",
        inventorySize - 1, chef[0].getInventoryCount());

  }

  /**
   * Tests that the chef can't drop an item if they don't have one.
   *
   * @author Jack Vickers
   * @date 26/03/2023
   */
  @Test
  public void testDropEmptyHand() {
    instantiateWorldAndChefs();
    chef[0].DropItem(); // Attempt to drop an item
    assertEquals("The chef should still have 0 items in their hand",
        0, chef[0].getInventoryCount());
  }

  /**
   * Tests that the chef can pick up an item.
   *
   * @author Jack Vickers
   * @date 26/03/2023
   */
  @Test
  public void testPickupItem() {
    instantiateWorldAndChefs();
    Item itemToGive = new Item(ItemEnum.Mince);
    chef[0].GiveItem(itemToGive);
    assertEquals("The chef should have mince at the top of their inventory stack",
        new Item(ItemEnum.Mince),
        chef[0].getInventory().peek());
  }

  /**
   * Tests that the chef can't pick up an item if their inventory is full.
   *
   * @author Jack Vickers
   * @date 26/03/2023
   */
  @Test
  public void testPickupFullInventory() {
    instantiateWorldAndChefs();
    Item item1 = new Item(ItemEnum.Mince);
    Item item2 = new Item(ItemEnum.Lettuce);
    chef[0].GiveItem(item1);
    chef[0].GiveItem(item1);
    chef[0].GiveItem(item1);
    chef[0].GiveItem(item2);
    Stack<Item> Items = new Stack<Item>();
    Items.push(item1);
    Items.push(item1);
    Items.push(item1);
    assertTrue("The chef inventory should contain the first 3 items given to it and not the 4th",
        chef[0].getInventory().equals(Items));
  }

  /**
   * Tests that the chef can pick up an item from a nearby food crate.
   *
   * @author Jack Vickers
   */
  @Test
  public void testChefCanPickupItemFromNearbyFoodCrate() {
    if (GameObjectManager.objManager == null) {
      // creates game object manager which makes sure that the game object manager
      // is not null when it is needed
      new GameObjectManager();
    }
    instantiateMasterChef();
    instantiateWorldAndFoodCrate();

    // The chef's position is set to close to the tomato crate station (which has position (0,0))
    // This is done so that the Fetch item function gets the food crate
    // as the closest interactable object.
    masterChef.getChef(0).gameObject.position = new Vector2(1, 0);
    masterChef.FetchItem(); // The chef should pick up a tomato from the pantry food crate

    assertEquals("The chef should have a tomato at the top of inventory", new Item(ItemEnum.Tomato),
        masterChef.getChef(0).getInventory().peek());

    GameObjectManager.objManager.DestroyGameObject(crate); // Destroys the food crate
  }


  /**
   * Tests that the chef can't give an item to a food crate.
   *
   * @author Jack Vickers
   */
  @Test
  public void testChefCannotGiveItemToFoodCrate() {
    if (GameObjectManager.objManager == null) {
      // creates game object manager which makes sure that the game object manager
      // is not null when it is needed
      new GameObjectManager();
    }
    instantiateMasterChef();
    instantiateWorldAndFoodCrate();

    // The chef's position is set to close to the tomato crate station (which has position (0,0))
    // This is done so that the Fetch item function gets the food crate
    // as the closest interactable object.
    masterChef.getChef(0).gameObject.position = new Vector2(1, 0);
    Item item = new Item(ItemEnum.Tomato);
    masterChef.getCurrentChef().GiveItem(item);
    assertFalse("Food crate GiveItem() method should return false", FC.GiveItem(item));
    masterChef.GiveItem(); // Try to give the tomato to the food crate
    assertEquals("The chef should still have a tomato at the top of inventory",
        item,
        masterChef.getChef(0).getInventory().peek());
    GameObjectManager.objManager.DestroyGameObject(crate); // Destroys the food crate
  }

  /**
   * Tests that the chef can pick up minced meat from the pantry.
   *
   * @author Jack Vickers
   * @date 26/03/2023
   */
  @Test
  public void testPickUpPantryMince() {
    instantiateWorldAndChefs();
    Item itemToGive = new FoodCrate(
        ItemEnum.Mince).RetrieveItem(); // Creates a mince food crate and gets the mince from it
    chef[0].GiveItem(itemToGive); // Gives the mince to the chef
    assertEquals("The chef should have mince at the top of their inventory stack",
        new Item(ItemEnum.Mince),
        chef[0].getInventory().peek());
  }

  /**
   * Tests that the chef can pick up lettuce from the pantry.
   *
   * @author Jack Vickers
   * @date 26/03/2023
   */
  @Test
  public void testPickupPantryLettuce() {
    instantiateWorldAndChefs();
    Item itemToGive = new FoodCrate(
        ItemEnum.Lettuce).RetrieveItem(); // Creates a lettuce food crate and gets the lettuce from it
    chef[0].GiveItem(itemToGive); // Gives the lettuce to the chef
    assertEquals("The chef should have lettuce at the top of their inventory stack",
        new Item(ItemEnum.Lettuce),
        chef[0].getInventory().peek());
  }

  /**
   * Tests that the chef can pick up bread from the pantry.
   *
   * @author Jack Vickers
   * @date 26/03/2023
   */
  @Test
  public void testPickupPantryBread() {
    instantiateWorldAndChefs();
    Item itemToGive = new FoodCrate(
        ItemEnum.Buns).RetrieveItem(); // Creates a buns food crate and gets the buns from it
    chef[0].GiveItem(itemToGive); // Gives the buns to the chef
    assertEquals("The chef should have bread at the top of their inventory stack",
        new Item(ItemEnum.Buns),
        chef[0].getInventory().peek());
  }

  /**
   * Tests that the chef can pick up a tomato from the pantry.
   *
   * @author Jack Vickers
   * @date 26/03/2023
   */
  @Test
  public void testPickupPantryTomato() {
    instantiateWorldAndChefs();
    Item itemToGive = new FoodCrate(
        ItemEnum.Tomato).RetrieveItem(); // Creates a tomato food crate and gets the tomato from it
    chef[0].GiveItem(itemToGive); // Gives the tomato to the chef
    assertEquals("The chef should have tomato at the top of their inventory stack",
        new Item(ItemEnum.Tomato),
        chef[0].getInventory().peek());
  }

  /**
   * Tests that the chef can pick up cheese from the pantry.
   *
   * @author Jack Vickers
   * @date 26/03/2023
   */
  @Test
  public void testPickupPantryCheese() {
    instantiateWorldAndChefs();
    Item itemToGive = new FoodCrate(
        ItemEnum.Cheese).RetrieveItem(); // Creates a cheese food crate and gets the cheese from it
    chef[0].GiveItem(itemToGive); // Gives the cheese to the chef
    assertEquals("The chef should have cheese at the top of their inventory stack",
        new Item(ItemEnum.Cheese),
        chef[0].getInventory().peek());
  }

  /**
   * Tests that the chef can pick up an onion from the pantry.
   *
   * @author Jack Vickers
   * @date 26/03/2023
   */
  @Test
  public void testPickupPantryOnion() {
    instantiateWorldAndChefs();
    Item itemToGive = new FoodCrate(
        ItemEnum.Onion).RetrieveItem(); // Creates an onion food crate and gets the onion from it
    chef[0].GiveItem(itemToGive); // Gives the onion to the chef
    assertEquals("The chef should have onion at the top of their inventory stack",
        new Item(ItemEnum.Onion),
        chef[0].getInventory().peek());
  }

  /**
   * Tests that the chef can pick up a potato from the pantry.
   *
   * @author Jack Vickers
   * @date 26/03/2023
   */
  @Test
  public void testPickupPantryPotato() {
    instantiateWorldAndChefs();
    Item itemToGive = new FoodCrate(
        ItemEnum.Potato).RetrieveItem(); // Creates a potato food crate and gets the potato from it
    chef[0].GiveItem(itemToGive); // Gives the potato to the chef
    assertEquals("The chef should have potato at the top of their inventory stack",
        new Item(ItemEnum.Potato),
        chef[0].getInventory().peek());
  }

  /**
   * Tests that the chef can pick up dough from the pantry.
   *
   * @author Jack Vickers
   * @date 26/03/2023
   */
  @Test
  public void testPickupPantryDough() {
    instantiateWorldAndChefs();
    Item itemToGive = new FoodCrate(
        ItemEnum.Dough).RetrieveItem(); // Creates a dough food crate and gets the dough from it
    chef[0].GiveItem(itemToGive); // Gives the dough to the chef
    assertEquals("The chef should have dough at the top of their inventory stack",
        new Item(ItemEnum.Dough),
        chef[0].getInventory().peek());
  }

  /**
   * Tests that the chef cannot pick up from an empty tile with no items present.
   *
   * @author Hubert Solecki
   * @date 31/03/2023
   */
  @Test
  public void testPickupEmptyTile() {
    instantiateWorldAndChefs();
    int chefInventoryCountBefore = chef[0].getInventoryCount();
    chef[0].FetchItem();
    int chefInventoryCountAfter = chef[0].getInventoryCount();
    assertEquals(
        "The chef's inventory is still empty after attempting to pick up an item from an empty tile",
        chefInventoryCountBefore, chefInventoryCountAfter);
  }

  /**
   * Tests that the chef being controlled can pick up an item from the assembly station.
   */
  @Test
  public void testPickupItemFromAssemblyStation() {
    if (GameObjectManager.objManager == null) {
      // creates game object manager which makes sure that the game object manager
      // is not null when it is needed
      new GameObjectManager();
    }
    instantiateMasterChef();
    instantiateWorldAndAssemblyStation(); // world will get overwritten by this but will be the same

    // The chef's position is set to close to the assembly station (which has position (0,0))
    // This is done so that the Fetch item function gets the assembly
    // station as the closest interactable object.
    masterChef.getChef(0).gameObject.position = new Vector2(1, 0);

    assertEquals("The assembly station should have no ingredients on it",
        0, assemblyStation.getIngredients().size());

    assemblyStation.GiveItem(new Item(ItemEnum.Mince));

    assertEquals("The assembly station should have mince on it", new Item(ItemEnum.Mince),
        assemblyStation.getIngredients().get(0));

    assertEquals("The chef should have no ingredients on their inventory",
        0, masterChef.getChef(0).getInventory().size());

    masterChef.FetchItem(); // The chef should pick up the mince from the assembly station

    assertEquals("The chef should have mince at the top of inventory", new Item(ItemEnum.Mince),
        masterChef.getChef(0).getInventory().peek());

    assertEquals("The assembly station should have no ingredients on it",
        0, assemblyStation.getIngredients().size());
    GameObjectManager.objManager.DestroyGameObject(assemble);
  }

  /**
   * Tests that the chef being controlled can place an item on the assembly station.
   *
   * @author Jack Vickers
   */
  @Test
  public void testPlaceItemOnAssemblyStation() {
    if (GameObjectManager.objManager == null) {
      // creates game object manager which makes sure that the game object manager
      // is not null when it is needed
      new GameObjectManager();
    }
    instantiateMasterChef();
    instantiateWorldAndAssemblyStation(); // world will get overwritten by this but will be the same

    // The chef's position is set to close to the assembly station (which has position (0,0))
    // This is done so that the Fetch item function gets the assembly
    // station as the closest interactable object.
    masterChef.getChef(0).gameObject.position = new Vector2(1, 0);
    Item item = new Item(ItemEnum.Mince);
    masterChef.getChef(0).GiveItem(item);
    assertEquals("The chef should have mince at the top of inventory", item,
        masterChef.getChef(0).getInventory().peek());

    // Gives the chef's held item to the closest interactable object (assembly station)
    masterChef.GiveItem();

    assertEquals("The assembly station should have mince on it", item,
        assemblyStation.getIngredients().get(0));

    assertEquals("The chef should no longer have mince in their inventory",
        0, masterChef.getChef(0).getInventory().size());
    GameObjectManager.objManager.DestroyGameObject(assemble);
  }

  /**
   * Tests that the chef being controlled can combine two items on the assembly station.
   *
   * @author Jack Vickers
   */
  @Test
  public void testCombineItemsOnAssemblyStation() {
    if (GameObjectManager.objManager == null) {
      // creates game object manager which makes sure that the game object manager
      // is not null when it is needed
      new GameObjectManager();
    }
    new CombinationDict();
    CombinationDict.combinations.implementItems(); // creates combination dictionary
    instantiateMasterChef();
    instantiateWorldAndAssemblyStation(); // world will get overwritten by this but will be the same

    // The chef's position is set to close to the assembly station (which has position (0,0))
    // This is done so that the Fetch item function gets the assembly
    // station as the closest interactable object.
    masterChef.getChef(0).gameObject.position = new Vector2(1, 0);
    Item item1 = new Item(ItemEnum.CutLettuce);
    Item item2 = new Item(ItemEnum.CutTomato);
    assemblyStation.GiveItem(item1);
    assemblyStation.GiveItem(item2);
    masterChef.Interact();
    assertEquals("The lettuce and tomato should be combined into a LettuceTomatoSalad",
        new Item(ItemEnum.LettuceTomatoSalad), assemblyStation.getIngredients().get(0));
    GameObjectManager.objManager.DestroyGameObject(assemble);
  }

  /**
   * Tests that the chef cannot place item onto empty tile if not stood next to any interaction
   * stations.
   *
   * @author Azzam Amirul
   * @date 02/04/2023
   */
  @Test
  public void testPlaceEmptyTile() {
    instantiateWorldAndChefs(); // creates world and chefs
    int chefInventoryCountBefore = chef[0].getInventoryCount();
    chef[0].DropItem();
    int chefInventoryCountAfter = chef[0].getInventoryCount();
    assertEquals(
        "The chef's inventory is still the same after attempting to place an item on an empty tile",
        chefInventoryCountBefore, chefInventoryCountAfter);
  }

  @Test
  public void testItemRemoveFromChefInventory() {
    instantiateWorldAndChefs();
    chef[0].GiveItem(new Item(ItemEnum.Lettuce)); // Give the chef an item
    int inventorySize = chef[0].getInventoryCount(); // Get the size of the inventory
    chef[0].DropItem(); // Drop the item
    assertEquals("The chef should have 1 less item in their inventory after dropping it",
        inventorySize - 1, chef[0].getInventoryCount());
  }

  /**
   * Tests that the chef can interact with the hob station to flip the items being cooked.
   *
   * @author Hubert Solecki
   * @date 18/04/2023
   */

  @Test
  public void testItemInteractionHobStation() {
    if (GameObjectManager.objManager == null) {
      // creates object manager for use making sure it is not null
      new GameObjectManager();
    }
    instantiateMasterChef();
    instantiateWorldAndHobsStation();
    masterChef.getChef(0).gameObject.position = new Vector2(1, 0); // sets chef position to that of next to the hob for interaction
    masterChef.getChef(0).GiveItem(new Item(ItemEnum.RawPatty)); // gives a raw patty to the chef to give to the hob
    masterChef.GiveItem(); // gives currently held item to hob
    hobStation.Cook(10);
    masterChef.Interact();
    hobStation.Cook(5);
    hobStation.Cook(10);
    masterChef.FetchItem();
    assertEquals("The item in the chef's inventory should be a cooked patty", masterChef.getChef(0).getInventory().peek(), new Item(ItemEnum.CookedPatty));
    masterChef.getChef(0).ClearInventory();
    masterChef.getChef(0).GiveItem(new Item(ItemEnum.RawPatty));
    masterChef.GiveItem();
    hobStation.Update(10);
    hobStation.Update(15);
    masterChef.FetchItem();
    assertEquals("The item on top of the chef's inventory stack should be cinder", masterChef.getChef(0).getInventory().peek(), new Item(ItemEnum.Cinder));

  }
}
