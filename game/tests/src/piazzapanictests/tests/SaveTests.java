package piazzapanictests.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.game.CameraFunctions;
import com.mygdx.game.Core.Rendering.BlackTexture;
import com.mygdx.game.Core.Rendering.GameObject;
import com.mygdx.game.Core.Rendering.GameObjectManager;
import com.mygdx.game.Core.GameState.CookingParams;
import com.mygdx.game.Core.GameState.GameState;
import com.mygdx.game.Core.GameState.ItemState;
import com.mygdx.game.Core.GameState.SaveState;
import com.mygdx.game.Core.Rendering.RenderManager;
import com.mygdx.game.Core.Rendering.TextureDictionary;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;
import com.mygdx.game.Stations.HobStation;
import com.mygdx.game.Core.SFX.SoundFrame;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for saving the game.
 *
 * @satisfies UR_SAVE_GAME, NFR_SAVE_GAME, FR_SAVE_STATE UR_LOAD_GAME FR_LOAD_STATE
 * @author Felix Seanor
 * @date 25/04/23
 */
@RunWith(GdxTestRunner.class)

public class SaveTests extends MasterTestClass{



  public SpriteBatch batch;
  public TiledMap map;
  public SoundFrame soundFrame;
  public TextureDictionary textureDictionary;
  public CameraFunctions cameraFunctions = new CameraFunctions();

  /**
   * creates maps and sprites
   * @author Felix Seanor
   * @date 25/04/23
   */
  public void create() {
  //  batch = new SpriteBatch();
    map = new TmxMapLoader().load("PiazzaPanicMap.tmx");
    textureDictionary = new TextureDictionary();
    soundFrame = new SoundFrame();


    if (GameObjectManager.objManager == null) {
      new GameObjectManager();
    } else {
      GameObjectManager.objManager.reset();
    }

    if (RenderManager.renderer == null) {
      new RenderManager();
    }
  }

  /**
   * Tries to load in the game and is not null
   * @satisfies UR_LOAD_GAME FR_LOAD_STATE
   * @author Felix Seanor
   * @date 25/04/23
   */
  @Test
  public void loadSave(){
    SaveState state = new SaveState();



    GameState gstate = state.loadState("../assets/TestingData.ser");

    assertNotNull(gstate);


  }

  /**
   * Tries to save the game and verfies the save is the same as the load
   * @satisfies  UR_SAVE_GAME, NFR_SAVE_GAME, FR_SAVE_STATE
   * @author Felix Seanor
   * @date 25/04/23
   */

  @Test
  public void TestSave(){
    SaveState state = new SaveState();



    GameState gstate = state.loadState("../assets/TestingData.ser");

    state.saveState(gstate,"../assets/TestingData.ser");

    GameState lstate = state.loadState("../assets/TestingData.ser");


    assertTrue("Saved chefs must equals",gstate.isChefPartEquals(lstate));
    assertTrue("Saved Customers must equals",gstate.isCustomerPartEquals(lstate));

  }

  /**
   * Tries to build the game from a save file
   * @satisfies UR_LOAD_GAME FR_LOAD_STATE
   * @author Felix Seanor
   * @date 25/04/23
   */
  @Test
  public void BuildGame(){
    create();

    SaveState state = new SaveState();
    GameState gstate = state.loadState("../assets/TestingData.ser");

    instantiateCustomerScripts();
    instantiateMasterChef();

    CookingParams params = new CookingParams();
    HobStation station = new HobStation(params);
    GameObject hobObj = new GameObject(new BlackTexture("Black.png"));
    hobObj.attachScript(station);
    station.init();


    ItemState itemState = new ItemState(new Item(ItemEnum.Lettuce));

    List<ItemState> states = new LinkedList<>();
    states.add(itemState);
    station.loadState(states,false);


    assertFalse("Must be unlocked", station.getLocked());
    assertTrue("Must have lettuce on it", station.item.name == ItemEnum.Lettuce);


    customerController.loadState(gstate);
    masterChef.LoadState(gstate);

    assertNotNull(customerController.getCurrentWaitingCustomerGroup());

    GameState nState = new GameState();

    customerController.saveState(nState);



    assertTrue("Must have the same customer parameters", nState.isCustomerPartEquals(gstate));

    masterChef.SaveState(nState);


    assertTrue("Must have the same chef parameters", nState.isChefPartEquals(gstate));








    // CustomerController controller = new CustomerController(new Vector2(0,0),new Vector2(0,0), null,null,gstate.ChefHoldingStacks)
  }
}
