package piazzapanictests.tests;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Chef;
import com.badlogic.gdx.maps.MapLayer;
import com.mygdx.game.Core.BlackSprite;
import com.mygdx.game.Core.GameObject;

import com.mygdx.game.Core.GameObjectManager;
import com.mygdx.game.Core.GameState.GameState;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;
import com.mygdx.game.RecipeAndComb.RecipeDict;
import com.mygdx.game.Stations.ChopStation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.runner.RunWith;
import piazzapanictests.tests.GdxTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Tests for the toaster station.
 * Toaster only interacts with the bun item so no other items need to be used or tested apart from testing for progress for incorrect items.
 *
 * @author Hubert Solecki
 */

@RunWith(GdxTestRunner.class)
public class ToasterStationTests extends MasterTestClass {

  /**
   * Tests that an item can be removed from the toaster station whether it is complete or not.
   *
   * @author Hubert Solecki
   * @date 23/04/2023
   */

  @Test
  public void testRemoveItemFromToaster() { // tests whether valid and invalid items can be retrieved from the toaster station
    if (GameObjectManager.objManager == null) {
      // creates a game object manager making sure it is not null when needed.
      new GameObjectManager();
    }
    instantiateWorldAndToasterStation(); //creates world and toaster station
    toasterStation.GiveItem(new Item(ItemEnum.Buns)); // gives the toaster the valid bun item
    assertNotNull("The toaster station should have an item on it", toasterStation.item);
    toasterStation.RetrieveItem(); //attempts to retrieve the bun from the toaster for testing
    assertNull("There should be no item on the toaster station", toasterStation.RetrieveItem());
    toasterStation.GiveItem(new Item(ItemEnum.ToastedBuns));
    assertNotNull("The toaster station should have an item on it", toasterStation.item);
    toasterStation.RetrieveItem();
    assertNull("There should be no item on the toaster station", toasterStation.RetrieveItem());
    toasterStation.GiveItem(new Item(ItemEnum.Burger));
    assertNotNull("The toaster station should have an item on it", toasterStation.item);
    toasterStation.RetrieveItem();
    assertNull("There should be no item on the toaster station", toasterStation.RetrieveItem());
  }

  /**
   * Tests that the toaster station can burn an item if maximum progress for cooking and/or interaction is reached.
   *
   * @author Hubert Solecki
   * @date 23/04/2023
   */

  /**
   * Tests that incorrect items cannot be toasted by checking that the current recipe on the toaster is null when incorrect items are placed on it.
   *
   * @author Hubert Solecki
   * @date 23/04/2023
   */

  @Test
  public void testIncorrectItemOnToaster() {
    if (GameObjectManager.objManager == null) {
      new GameObjectManager();
      // creates a game object manager making sure it is not null when needed
    }
    instantiateWorldAndToasterStation(); // creates world and hob station
    Item buns = new Item(ItemEnum.Buns); // creates the valid item for testing
    for (ItemEnum test : Arrays.asList(ItemEnum.values())) { // creates a list of all items in the game and steps through them to check which can interact with the toaster
      Item testing = new Item(test);
      toasterStation.GiveItem(testing); // gives the current test item to the toaster
      if (!(testing.equals(buns))) { // if the item currently being tested is not a valid item
        assertNull("Current recipe on toaster should be null if an incorrect item is on the toaster (i.e. not in toaster white list) and therefore cannot be toasted", toasterStation.currentRecipe);
      } else {
        assertNotNull("There is a valid item on the toaster (buns) the current recipe should not be null showing it can be toasted", toasterStation.currentRecipe);
      }
      toasterStation.RetrieveItem(); // items on the toaster must be removed for testing later in the loop
    }
  }

  /**
   * Tests whether an item can be removed from the toaster when it has nothing on it; should not allow and return false.
   *
   * @author Hubert Solecki
   * @date 23/04/2023
   */

  @Test
  public void testRemoveItemWhenToasterEmpty() {
    if (GameObjectManager.objManager == null) {
      new GameObjectManager();
      // creates a game object manager making sure it is not null when needed
    }
    instantiateWorldAndToasterStation();
    assertFalse("The CanRetrieveItem() method should return false when no item is on the toaster", toasterStation.CanRetrieve());
    assertNull("The RetrieveItem() method should return null when there is nothing on the toaster", toasterStation.RetrieveItem());
  }

  /**
   *Tests whether an item can be placed on the toaster when there is already an item placed on the toaster; should return false.
   * Placing a different item on the toaster should not change the item currently on the toaster.
   *
   * @author Hubert Solecki
   * @date 23/04/2023
   */

  @Test
  public void testGiveItemWhenToasterFull() {
    if (GameObjectManager.objManager == null) {
      new GameObjectManager();
      // creates a game object manager making sure it is not null when needed
    }
    instantiateWorldAndToasterStation();
    Item buns = new Item(ItemEnum.Buns);
    Item toastedBuns = new Item(ItemEnum.ToastedBuns);
    toasterStation.GiveItem(buns);
    assertFalse("The CanGive() method should return false when there is already an item placed on the toaster", toasterStation.CanGive());
    toasterStation.GiveItem(toastedBuns);
    System.out.println(toastedBuns);
    System.out.println(toasterStation.item);
    assertEquals("The item on the toaster should be unchanged if an item is placed on the toaster when there already was an item on the toaster", buns, toasterStation.RetrieveItem());
  }

  /**
   * Tests that an item can be picked up during toasting and that the progress of the item is saved in its progress attribute.
   *
   * @author Hubert Solecki
   * @date 23/04/2023
   */

  @Test
  public void testItemRetrievedDuringToastingAndProgress() {
    if (GameObjectManager.objManager == null){
      // creates a game object manager making sure it is not null when needed
      new GameObjectManager();
    }
    instantiateWorldAndToasterStation(); // creates world and toaster station
    toasterStation.GiveItem(new Item(ItemEnum.Buns)); // gives valid item to toaster
    toasterStation.Cook(5); // toasts item for 5 delta time steps to increase progress but not fully cook item
    Integer testProgress = (int)toasterStation.getCookingTime();
    Item test = toasterStation.RetrieveItem();
    assertNotEquals("The value of the progress of the item should not be 0 when it is removed from the toaster after it has been cooked for some dt", 0, (int)test.progress);
    assertNull("The toaster should not contain an item when an in progress item is removed from it", toasterStation.RetrieveItem());
    assertNotEquals("The progress of the item should not be 0 when it has been cooking for some dt and is removed", 0, test.progress);
    assertEquals("The progress of the item being cooked should be the same before and after it is removed from the toaster station", (int)testProgress, (int)test.progress);
  }

  /**
   * Tests that the update function updates the toaster station and checks that the toaster cannot be interacted with.
   *
   * @author Hubert Solecki
   * @date 23/04/2023
   */

  @Test
  public void testUpdateMethodToasterStation() {
    if (GameObjectManager.objManager == null){
      new GameObjectManager();
      // creates a game object manager making sure it is not null when needed
    }
    instantiateWorldAndToasterStation();
    assertFalse("The chef should not be able to interact with the toaster", toasterStation.CanInteract());
    assertFalse("The chef should not be able to interact with the toaster", toasterStation.Interact());
    toasterStation.GiveItem(new Item(ItemEnum.Buns));
    toasterStation.Update(4);
    Item test = toasterStation.RetrieveItem();
    assertNotEquals("The progress of the item retrieved from the toaster should not be 0 if the update function works as it will cook the item", 0, test.progress);

  }
}
