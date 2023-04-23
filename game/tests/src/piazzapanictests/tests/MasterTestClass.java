package piazzapanictests.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.mygdx.game.Core.GameState.Difficulty;
import com.mygdx.game.Core.GameState.DifficultyMaster;
import com.mygdx.game.Core.GameState.DifficultyState;
import com.mygdx.game.Core.MasterChef;
import com.mygdx.game.Core.Pathfinding;
import com.mygdx.game.Core.TextureDictionary;
import com.mygdx.game.Items.ItemEnum;
import com.mygdx.game.RecipeAndComb.RecipeDict;
import com.mygdx.game.Stations.AssemblyStation;
import com.mygdx.game.Stations.ChopStation;
import com.mygdx.game.Stations.FoodCrate;
import com.mygdx.game.Stations.HobStation;
import com.mygdx.game.Stations.TrashCan;
import com.mygdx.game.soundFrame;
import java.util.ArrayList;

class MasterTestClass {

  // Array of chefs
  private static ArrayList<TextureAtlas> chefAtlasArray = new ArrayList<TextureAtlas>();
  Chef[] chef;
  MasterChef masterChef;
  World world;
  ChopStation chopStation;

  TrashCan trashCan;

  HobStation hobStation;
  AssemblyStation assemblyStation;
  GameObject assemble;
  GameObject crate;
  FoodCrate FC;

  TextureDictionary textureDictionary = new TextureDictionary();

  /**
   * Instantiates the world.
   *
   * @author Jack Vickers
   * @date 03/04/2023
   */
  void instantiateWorld() {
    world = new World(new Vector2(0, 0), true);
  }

  /**
   * Instantiates the world and two chefs so that these can be used in the tests.
   *
   * @author Jack Vickers
   */
  void instantiateWorldAndChefs() {
    world = new World(new Vector2(0, 0), true);
    generateChefArray();
    soundFrame soundFrame = new soundFrame();
    chef = new Chef[2];
    int chefControl = 0;
    for (int i = 0; i < chef.length; i++) {
      GameObject chefsGameObject = new GameObject(
          new BlackSprite()); // passing in null since chef will define it later
      chef[i] = new Chef(world, i, getChefAtlasArray().get(chefControl));
      chefsGameObject.attachScript(chef[i]);
      chefsGameObject.image.setSize(18, 40); // set size of sprite
      chef[i].updateSpriteFromInput("idlesouth");
    }
  }

  /**
   * Instantiates the masterchef class. This class generates multiple chefs. This will be used to
   * test interactions between a chef and interactable game object.
   */
  void instantiateMasterChef() {
    world = new World(new Vector2(0, 0), true);
    OrthographicCamera camera = new OrthographicCamera(); // create camera. Needed to instantiate MasterChef
    camera.setToOrtho(false, 1024, 576); // set camera to orthographic mode using values
    // taken from GameScreen class
    camera.update();
    DifficultyState difficultyState = DifficultyMaster.getDifficulty(Difficulty.Stressful);
    // Sets up the pathfinding using values taken from GameScreen class
    Pathfinding pathfinding = new Pathfinding(32 / 4, 32 * 32, 18 * 32);
    // Instantiates the MasterChef class
    masterChef = new MasterChef(2, world, camera, pathfinding,difficultyState.chefParams);
    GameObjectManager.objManager.AppendLooseScript(masterChef);
  }


  /**
   * Generates a chef array which can be used to get random chef sprites from the chef class. Needed
   * to instantiate the chefs.
   *
   * @author Jack Vickers
   */
  private void generateChefArray() {
    String filename;
    TextureAtlas chefAtlas;
    for (int i = 1; i < 4; i++) {
      filename = "Chefs/Chef" + i + "/chef" + i + ".txt";
      chefAtlas = new TextureAtlas(Gdx.files.internal(filename));
      chefAtlasArray.add(chefAtlas);
    }
  }

  /**
   * Returns the chef atlas array. Needed to instantiate the chefs.
   *
   * @return chefAtlasArray
   * @author Jack Vickers
   */
  private static ArrayList<TextureAtlas> getChefAtlasArray() {
    return chefAtlasArray;
  }

  /**
   * Instantiates a tomato food crate.
   *
   * @author Jack Vickers
   */
  void instantiateWorldAndFoodCrate() {
    world = new World(new Vector2(0, 0), true);
    TiledMap map;
    map = new TmxMapLoader().load("PiazzaPanicMap.tmx"); // loads map
    MapLayer tomotoLayer = map.getLayers().get(9); // gets tomato crate layer
    MapObject object = tomotoLayer.getObjects().getByType(RectangleMapObject.class)
        .get(0); // gets tomato crate object
    Rectangle rect = ((RectangleMapObject) object).getRectangle(); // gets tomato crate rectangle
    crate = new GameObject(null);
    crate.setPosition(0, 0);
    crate.setWidthAndHeight(rect.getWidth(), rect.getHeight());
    FC = new FoodCrate(ItemEnum.Tomato);
    crate.attachScript(FC);
  }

  /**
   * Creates the world and chopping station. Also creates the recipe dictionary.
   *
   * @author Jack Vickers
   */
  void instantiateWorldAndChoppingStation() {
    world = new World(new Vector2(0, 0), true);
    DifficultyState state = DifficultyMaster.getStressful();
    TiledMap map;
    map = new TmxMapLoader().load("PiazzaPanicMap.tmx"); // loads map
    MapLayer chopping = map.getLayers().get(5); // gets chopping layer
    MapObject object = chopping.getObjects().getByType(RectangleMapObject.class)
        .get(0); // gets chopping object
    Rectangle rect = ((RectangleMapObject) object).getRectangle(); // gets chopping rectangle
    GameObject Chop = new GameObject(null); // creates chopping game object
    Chop.setPosition(0, 0); // sets chopping position (this must be done to avoid null pointer exception)
    Chop.setWidthAndHeight(rect.getWidth(),
        rect.getHeight()); // sets chopping width and height (this must be done to avoid null pointer exception)
    chopStation = new ChopStation(state.cookingParams); // creates chopping station
    Chop.attachScript(chopStation); // attaches chopping station to chopping game object
    new RecipeDict(); // creates recipe dictionary
    RecipeDict.recipes.implementRecipes(); // implements recipes
  }

  /**
   * Creates the world and assembly station. Also creates the recipe dictionary.
   *
   * @author Jack Vickers
   */
  AssemblyStation instantiateWorldAndAssemblyStation() {
    world = new World(new Vector2(0, 0), true);
    DifficultyState state = DifficultyMaster.getStressful();
    soundFrame soundFrame = new soundFrame();
    TiledMap map;
    map = new TmxMapLoader().load("PiazzaPanicMap.tmx"); // loads map
    MapLayer counter = map.getLayers().get(3); // gets counter layer (layer 3 of the map)
    MapObject object = counter.getObjects().getByType(RectangleMapObject.class)
        .get(0); // gets counter object
    Rectangle rect = ((RectangleMapObject) object).getRectangle(); // gets chopping rectangle
    assemble = new GameObject(null);
    assemble.setPosition(0, 0);
    assemble.setWidthAndHeight(rect.getWidth(), rect.getHeight());
    assemblyStation = new AssemblyStation(state.cookingParams);
    assemble.attachScript(assemblyStation);
    new RecipeDict(); // creates recipe dictionary
    RecipeDict.recipes.implementRecipes(); // implements recipes
    return assemblyStation;
  }

  /**
   * Creates the world and hobs station. Also creates the recipe dictionary.
   *
   * @author Azzam Amirul Bahri
   */
  void instantiateWorldAndHobsStation() {
    world = new World(new Vector2(0, 0), true);
    TiledMap map;
    DifficultyState state = DifficultyMaster.getStressful();
    soundFrame soundFrame = new soundFrame();
    map = new TmxMapLoader().load("PiazzaPanicMap.tmx"); // loads map
    MapLayer frying = map.getLayers().get(4); // gets fryer layer
    MapObject object = frying.getObjects().getByType(RectangleMapObject.class)
        .get(0); // gets frying object
    Rectangle rect = ((RectangleMapObject) object).getRectangle(); // gets frying rectangle
    GameObject Fry = new GameObject(null); // creates frying game object
    Fry.setPosition(0,0); // sets frying position (this must be done to avoid null pointer exception)
    Fry.setWidthAndHeight(rect.getWidth(),
        rect.getHeight()); // sets frying width and height (this must be done to avoid null pointer exception)
    hobStation = new HobStation(state.cookingParams); // creates frying station
    Fry.attachScript(hobStation); // attaches frying station to frying game object
    hobStation.init();
    new RecipeDict(); // creates recipe dictionary
    RecipeDict.recipes.implementRecipes(); // implements recipes
  }

  void instantiateWorldAndTrashCan() {
    world = new World(new Vector2(0, 0), true);
    TiledMap map;
    map = new TmxMapLoader().load("PiazzaPanicMap.tmx"); // loads map
    MapLayer trashcan = map.getLayers().get(2); // gets trashcan layer
    MapObject object = trashcan.getObjects().getByType(RectangleMapObject.class)
        .get(0); // gets trashcan object
    Rectangle rect = ((RectangleMapObject) object).getRectangle(); // gets trashcan rectangle
    GameObject Trash = new GameObject(null); // creates trashcan game object
    Trash.setPosition(rect.getX(),
        rect.getY()); // sets trashcan position (this must be done to avoid null pointer exception)
    Trash.setWidthAndHeight(rect.getWidth(),
        rect.getHeight()); // sets frying width and height (this must be done to avoid null pointer exception)
    trashCan = new TrashCan(); // creates trashcan
    Trash.attachScript(trashCan); // attaches trashcan to trashcan game object
    new RecipeDict(); // creates recipe dictionary
    RecipeDict.recipes.implementRecipes(); // implements recipes
  }


}
