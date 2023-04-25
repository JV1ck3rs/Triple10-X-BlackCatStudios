package piazzapanictests.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.game.CameraFunctions;
import com.mygdx.game.Core.CustomerController;
import com.mygdx.game.Core.DistanceTest;
import com.mygdx.game.Core.GameObjectManager;
import com.mygdx.game.Core.GameState.Difficulty;
import com.mygdx.game.Core.GameState.GameState;
import com.mygdx.game.Core.GameState.SaveState;
import com.mygdx.game.Core.Pathfinding;
import com.mygdx.game.Core.RenderManager;
import com.mygdx.game.Core.TextureDictionary;
import com.mygdx.game.GameScreen;
import com.mygdx.game.MenuScreen;
import com.mygdx.game.soundFrame;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.badlogic.gdx.math.Vector2;

@RunWith(GdxTestRunner.class)

public class SaveTests {



  public SpriteBatch batch;
  public TiledMap map;
  public com.mygdx.game.soundFrame soundFrame;
  public TextureDictionary textureDictionary;
  public CameraFunctions cameraFunctions = new CameraFunctions();

  /**
   * creates maps and sprites
   */
  public void create() {
  //  batch = new SpriteBatch();
    map = new TmxMapLoader().load("PiazzaPanicMap.tmx");
    textureDictionary = new TextureDictionary();
    soundFrame = new soundFrame();


    if (GameObjectManager.objManager == null) {
      new GameObjectManager();
    } else {
      GameObjectManager.objManager.reset();
    }

    if (RenderManager.renderer == null) {
      new RenderManager();
    }
  }

  @Test
  public void loadSave(){
    SaveState state = new SaveState();



    GameState gstate = state.LoadState("../assets/TestingData.ser");

    assertNotNull(gstate);


  }


  @Test
  public void BuildGame(){
    create();

    SaveState state = new SaveState();
    GameState gstate = state.LoadState("../assets/TestingData.ser");

    CustomerController controller = new CustomerController(new Vector2(0,0),new Vector2(0,0), null,null,gstate.ChefHoldingStacks)
  }
}
