package piazzapanictests.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.mygdx.game.Core.Rendering.GameObject;
import com.mygdx.game.Core.Rendering.GameObjectManager;
import com.mygdx.game.Core.GameState.Difficulty;
import com.mygdx.game.Core.GameState.GameState;
import com.mygdx.game.Core.GameState.SaveState;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;

import com.mygdx.game.RecipeAndComb.CombinationDict;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for the assembly station.
 *
 * @Satisfies Requirements for UR_WORKSTATIONS, UR_INTERACTION, UR_REMOVE_ITEM, UR_COLLECT_ITEM, UR_PREP,
 * UR_SAVE_GAME, UR_LOAD_GAME
 *
 * @author Jack Vickers
 * @date 01/05/2023
 */
@RunWith(GdxTestRunner.class)
public class AssemblyStationTests extends MasterTestClass {

  /**
   * Tests that an item can be placed on an assembly station.
   * @Satisfies UR_REMOVE_ITEM
   * @author Jack Vickers
   * @date 03/04/2023
   */
  @Test
  public void testPlaceItem() {
    if (GameObjectManager.objManager == null) {
      // creates game object manager which makes sure that the game object manager is not null when it is needed
      new GameObjectManager();
    }
    instantiateWorldAndAssemblyStation(); // world will get overwritten by this but will be the same

    Item item = new Item(ItemEnum.Buns);
    assemblyStation.giveItem(item); // gives a bun to the assembly station
    assertEquals(assemblyStation.getIngredients().get(0),
        item); // checks that the bun is in the inventory
    GameObjectManager.objManager.destroyGameObject(assemble);
  }

  /**
   * Tests that an item can be successfully removed from the station.
   * @Satisfies UR_COLLECT_ITEM
   * @author Jack Vickers
   * @date 03/04/2023
   */
  @Test
  public void testPickupItem() {
    if (GameObjectManager.objManager == null) {
      // creates game object manager which makes sure that the game object manager is not null when it is needed
      new GameObjectManager();
    }
    instantiateWorldAndAssemblyStation(); // world will get overwritten by this but will be the same

    Item item = new Item(ItemEnum.Buns);
    assemblyStation.giveItem(item); // gives a bun to the assembly station
    assertEquals(assemblyStation.getIngredients().get(0),
        item); // checks that the bun is on the assembly station
    Item retrievedItem = assemblyStation.retrieveItem();

    assertEquals("The item retrieved from the station should match the last item put on it", item,
        retrievedItem);

    assertTrue("The station should be empty after retrieving this item",
        assemblyStation.getIngredients().isEmpty());

    assertEquals("There should be no gameOjects on the station",
        assemblyStation.getHeldItems().size(), 0);

    assertNull("There should be no game objects on the station",
        assemblyStation.getHeldItem());
    GameObjectManager.objManager.destroyGameObject(assemble);
  }

  /**
   * Tests that nothing happens when an item is attempted to be removed from an empty assembly
   * station.
   * @Satisfies UR_COLLECT_ITEM
   * @author Jack Vickers
   * @date 03/04/2023
   */
  @Test
  public void testPickupWhenEmpty() {
    if (GameObjectManager.objManager == null) {
      // creates game object manager which makes sure that the game object manager is not null when it is needed
      new GameObjectManager();
    }
    instantiateWorldAndAssemblyStation(); // world will get overwritten by this but will be the same

    assertNull("There should be no game objects on the station to start with",
        assemblyStation.getHeldItem());

    assertNull(
        "Attempting to retrieve an item when there isn't one on the station should return null",
        assemblyStation.retrieveItem());

    assertTrue("The station should still be empty",
        assemblyStation.getIngredients().isEmpty());

    assertEquals("There should be no gameOjects on the station",
        assemblyStation.getHeldItems().size(), 0);

    assertNull("There should be no game objects on the station",
        assemblyStation.getHeldItem());
    GameObjectManager.objManager.destroyGameObject(assemble);
  }

  /**
   * Tests that items can be placed on an assembly station after other items have been combined on
   * this station. Also tests that they can all be successfully removed from the station.
   * @Satisfies UR_REMOVE_ITEM, UR_COLLECT_ITEM
   * @author Jack Vickers
   * @date 03/04/2023
   */
  @Test
  public void testPlaceItemAfterCombining() {
    if (GameObjectManager.objManager == null) {
      // creates game object manager which makes sure that the game object
      // manager is not null when it is needed
      new GameObjectManager();
    }
    new CombinationDict();
    CombinationDict.combinations.implementItems(); // creates combination dictionary
    instantiateWorldAndAssemblyStation();

    Item item1 = new Item(ItemEnum.CutLettuce);
    Item item2 = new Item(ItemEnum.CutTomato);
    Item item3 = new Item(ItemEnum.CutOnion);
    assemblyStation.giveItem(item1);
    assemblyStation.giveItem(item2);
    assemblyStation.combine(); // combines the cut lettuce and tomato into a lettuce tomato salad
    assemblyStation.giveItem(
        item3); // gives a cut onion to the assembly station which can be combined with the current
    assemblyStation.giveItem(item3);
    assemblyStation.giveItem(item3);
    assertEquals("The first item on the station should be the combined food",
        assemblyStation.getIngredients().get(0),
        new Item(ItemEnum.LettuceTomatoSalad)); // checks that the lettuce is in the inventory
    assertEquals(assemblyStation.getIngredients().get(1),
        item3); // checks that the onion is the 2nd item in the inventory
    assertEquals(assemblyStation.getIngredients().get(2),
        item3); // checks that the onion is in the inventory
    assertEquals(assemblyStation.getIngredients().get(3),
        item3); // checks that the onion is in the inventory

    for (int i = 0; i < 4; i++) { // retrieves all items from the assembly station
      assemblyStation.retrieveItem();
    }
    assertEquals("There should be no gameOjects on the station",
        assemblyStation.getHeldItems().size(), 0);

    assertNull("There should be no game objects on the station",
        assemblyStation.getHeldItem());
    GameObjectManager.objManager.destroyGameObject(assemble);
  }

  /**
   * Tests that complete meals can be placed on an assembly station and then successfully removed
   * from it.
   * @Satisfies UR_COLLECT_ITEM, UR_REMOVE_ITEM
   * @author Jack Vickers
   * @date 03/04/2023
   */
  @Test
  public void placeFinishedMealsThenRemoveThem() {
    if (GameObjectManager.objManager == null) {
      // creates game object manager which makes sure that the game object
      // manager is not null when it is needed
      new GameObjectManager();
    }
    instantiateWorldAndAssemblyStation();

    Item item = new Item(ItemEnum.TomatoOnionLettuceSalad);
    assemblyStation.giveItem(item);
    assemblyStation.giveItem(item);
    assemblyStation.giveItem(item);
    assemblyStation.giveItem(item);
    assertEquals("There should be 4 items on the station",
        assemblyStation.getHeldItems().size(), 4);
    for (int i = 0; i < 4; i++) { // retrieves all items from the assembly station
      assemblyStation.retrieveItem();
    }
    assertEquals("There should be no ingredients on the station",
        assemblyStation.getIngredients().size(), 0);
    assertEquals("There should be no gameOjects on the station",
        assemblyStation.getHeldItems().size(), 0);
    assertNull("There should be no game objects on the station",
        assemblyStation.getHeldItem());
    GameObjectManager.objManager.destroyGameObject(assemble);
  }

  /**
   * Tests that an assembly station can hold a maximum of 4 items.
   * @Satisfies UR_WORKSTATIONS
   * @author Jack Vickers
   * @date 03/04/2023
   */
  @Test
  public void testItemLimit() {
    if (GameObjectManager.objManager == null) {
      // creates game object manager which makes sure that the game object manager is not null when it is needed
      new GameObjectManager();
    }
    instantiateWorldAndAssemblyStation();

    Item item = new Item(ItemEnum.Buns);
    for (int i = 0; i < 4; i++) {
      assemblyStation.giveItem(item); // gives 4 buns to the assembly station
    }
    assertEquals(assemblyStation.getIngredients().size(),
        4); // checks that the assembly station has 12 buns
    assemblyStation.giveItem(item); // attempts to give a 5th bun to the assembly station
    assertEquals("There should be no more than 4 items allowed on an assembly station",
        assemblyStation.getIngredients().size(),
        4); // checks that the assembly station still has 4 buns
    GameObjectManager.objManager.destroyGameObject(assemble);
  }

  /**
   * Tests that items from different recipes cannot be combined on an assembly station.
   * @Satisfies UR_PREP, UR_WORKSTATIONS
   * @author Jack Vickers
   * @date 03/04/2023
   */
  @Test
  public void testInvalidCombination() {
    if (GameObjectManager.objManager == null) {
      // creates game object manager which makes sure that the game object
      // manager is not null when it is needed
      new GameObjectManager();
    }
    new CombinationDict();
    CombinationDict.combinations.implementItems(); // creates combination dictionary
    instantiateWorldAndAssemblyStation();

    Item item1 = new Item(ItemEnum.CutLettuce);
    Item item2 = new Item(ItemEnum.CookedPatty);
    assemblyStation.giveItem(item1);
    assemblyStation.giveItem(item2);
    assemblyStation.combine(); // attempts to combine the cut lettuce
    // and cooked patty which is not a valid combination
    assertEquals("The items should still be separate on the assembly station",
        assemblyStation.getIngredients().get(0), item1);
    assertEquals("The items should still be separate on the assembly station",
        assemblyStation.getIngredients().get(1), item2);
    GameObjectManager.objManager.destroyGameObject(assemble);
  }

  /**
   * Tests that all valid combinations of items can be combined on an assembly station.
   * @Satisfies UR_PREP, UR_WORKSTATIONS
   * @author Jack Vickers
   * @date 03/04/2023
   */
  @Test
  public void testValidCombinations() {
    String item1;
    String item2;
    int indexOfSpace;
    if (GameObjectManager.objManager == null) {
      // creates game object manager which makes sure that the game object
      // manager is not null when it is needed
      new GameObjectManager();
    }
    new CombinationDict();
    CombinationDict.combinations.implementItems(); // creates combination dictionary
    instantiateWorldAndAssemblyStation();

    for (String combination : CombinationDict.combinations.combinationMap.keySet()) {
      indexOfSpace = combination.indexOf(
          " "); // gets the index of the space between the two words in the key of the combination map
      item1 = combination.substring(0, indexOfSpace); // gets the first item in the combination
      item2 = combination.substring(indexOfSpace + 1); // gets the second item in the combination
      Item item1Object = new Item(ItemEnum.valueOf(item1));
      Item item2Object = new Item(ItemEnum.valueOf(item2));
      assemblyStation.giveItem(item1Object);
      assemblyStation.giveItem(item2Object);
      assemblyStation.combine(); // combines the two items

      // checks that the first item on the station is the combined food
      assertTrue("The first item on the station should be the combined food",
          assemblyStation.getIngredients().get(0)
              .equals(new Item(CombinationDict.combinations.combinationMap.get(combination))));

      assertTrue("There should only be one item on the station",
          assemblyStation.getIngredients().size() == 1);

      assemblyStation.retrieveItem(); // retrieves the combined food
      assertTrue("There should be no items on the station",
          assemblyStation.getIngredients().isEmpty());

      assertEquals("There should be no gameOjects on the station",
          assemblyStation.getHeldItems().size(), 0);

      assertNull("There should be no game objects on the station",
          assemblyStation.getHeldItem());
      GameObjectManager.objManager.destroyGameObject(assemble);
    }
  }


  /**
   * Tests that the assembly station data can be saved and loaded correctly.
   * @Satisfies UR_SAVE_GAME, UR_LOAD_GAME
   * @author Jack Vickers
   * @author Felix Seanor
   * @date 01/05/2023
   */
  @Test
  public void testSaveAndLoad() {
    if (GameObjectManager.objManager == null) {
      // creates game object manager which makes sure that the game object
      // manager is not null when it is needed
      new GameObjectManager();
    }
    instantiateWorldAndAssemblyStation();
    // A masster chef must be passed to the save state class
    instantiateMasterChef();
    // A customer controller must be passed to the save state class
    instantiateCustomerScripts();

    // Creates items and gives them to the assembly station
    Item item1 = new Item(ItemEnum.CutLettuce);
    Item item2 = new Item(ItemEnum.CookedPatty);
    Item item3 = new Item(ItemEnum.CutTomato);
    Item item4 = new Item(ItemEnum.CutOnion);
    assemblyStation.giveItem(item1);
    assemblyStation.giveItem(item2);
    assemblyStation.giveItem(item3);
    assemblyStation.giveItem(item4);

    List<GameObject> assemblyStations = new ArrayList<>();
    // Adds the assembly station to the list of assembly stations
    assemblyStations.add(assemble);
    // Creates empty lists of stations and customer counters
    List<GameObject> stations = new ArrayList<>();
    List<GameObject> customerCounters = new ArrayList<>();

    // Gets the items on the assembly station before saving
    ArrayList<Item> itemsOnStationBeforeSave = assemblyStation.getIngredients();

    SaveState saveState = new SaveState();
    // Saves the state of the game
    GameState saveContents = saveState.saveState("testAssemblySave.ser", masterChef,
        customerController, Difficulty.Stressful, 0, 0f, stations, customerCounters, assemblyStations);
    // Loads the state of the game
    GameState loadedState = saveState.loadState("testAssemblySave.ser");
    assertEquals("The items on the station after loading should be the same as the items on the station before saving",
        itemsOnStationBeforeSave, assemblyStation.getIngredients());
  }
}
