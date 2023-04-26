package piazzapanictests.tests;

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
import com.mygdx.game.Core.Interactions.Interactable;
import com.mygdx.game.Core.MasterChef;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;

import com.mygdx.game.RecipeAndComb.Recipe;
import com.mygdx.game.RecipeAndComb.RecipeDict;
import com.mygdx.game.Stations.ChopStation;
import com.mygdx.game.Stations.FoodCrate;

import java.util.*;

import com.mygdx.game.Core.GameObjectManager;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Tests for the chopping station.
 *
 * @author Jack Vickers
 * @author Azzam Amirul Bahri
 */
@RunWith(GdxTestRunner.class)
public class ChopStationTests extends MasterTestClass {

  /**
   * Tests that an item can be removed from the chopping board when it is not being chopped.
   *
   * @author Jack Vickers
   */
  @Test
  public void testRemoveItemWhileNotChopping() {
    if (GameObjectManager.objManager == null) {
      // creates game object manager which makes sure that the game object manager is not null when it is needed
      new GameObjectManager();
    }
    instantiateWorldAndChoppingStation(); // creates chopping station
    chopStation.GiveItem(new Item(ItemEnum.Lettuce)); // gives lettuce to chopping station
    chopStation.RetrieveItem(); // attempts to retrieve item from chopping station
    assertNull("There should be no item on the chopping station", chopStation.returnItem());
  }

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
    for (ItemEnum test : Arrays.asList(ItemEnum.values())){
      Item testing = new Item(test);
      chopStation.GiveItem(testing);
      if (!((testing.equals(lettuce)) || (testing.equals(tomato)) || (testing.equals(onion)) || (testing.equals(mince)) || (testing.equals(cutTomato)) || (testing.equals(dough)))){
        assertNotNull("These items can be put on chopping station", chopStation.RetrieveItem());
      }

    }
    assertNull("Item cannot be put on chopping station", chopStation.RetrieveItem());

  }


  @Test
  public void testCanGiveCanRetrieveChopping(){
    instantiateWorldAndChoppingStation();
    Item item = new Item(ItemEnum.Lettuce);
    if (item == null){
      assertFalse("Nothing to give chopping station", chopStation.CanGive());
    } else {
      assertTrue("Item can be given to chopping station", chopStation.CanGive());
    }
    if (item == null){
      assertTrue("Nothing to retrieve at chopping station", chopStation.CanRetrieve());
    } else{
      assertFalse("Item cannot be retrieved by chopping station", chopStation.CanRetrieve());
    }
  }

  @Test
  public void testCanInteractChoppingStation(){
    instantiateWorldAndChoppingStation();
    Item item = new Item(ItemEnum.Lettuce);
    Recipe recipe = RecipeDict.recipes.RecipeMap.get(item.name);
    if (recipe == null){
      assertTrue("Item can be interacted with chopping station", chopStation.CanInteract());

    } else {
      assertFalse("Cannot be interacted with chopping station", chopStation.CanInteract());
    }

  }


}
