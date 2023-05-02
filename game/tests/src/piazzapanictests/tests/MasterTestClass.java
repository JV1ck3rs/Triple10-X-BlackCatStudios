package piazzapanictests.tests;

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
import com.mygdx.game.Core.CustomerController;
import com.mygdx.game.Core.GameObject;

import com.mygdx.game.Core.GameObjectManager;
import com.mygdx.game.Core.GameState.Difficulty;
import com.mygdx.game.Core.GameState.DifficultyMaster;
import com.mygdx.game.Core.GameState.DifficultyState;
import com.mygdx.game.Core.MasterChef;
import com.mygdx.game.Core.Pathfinding;
import com.mygdx.game.Core.TextureDictionary;
import com.mygdx.game.Core.ValueStructures.CustomerControllerParams;
import com.mygdx.game.Core.ValueStructures.EndOfGameValues;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;
import com.mygdx.game.RecipeAndComb.RecipeDict;
import com.mygdx.game.Stations.AssemblyStation;
import com.mygdx.game.Stations.ChopStation;
import com.mygdx.game.Stations.HobStation;
import com.mygdx.game.Stations.TrashCan;

import com.mygdx.game.Stations.*;
import com.mygdx.game.soundFrame;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * The master test class is used to instantiate the world, stations and chefs for testing in each of its child classes.
 * Each child class inherits from this class in order to use its methods for testing.
 *
 * @author Jack Vickers
 * @author Hubert Solecki
 * @author Azzam Amirul
 * @date 02/05/2023
 */
class MasterTestClass {

  GameObjectManager manager;
  CustomerController customerController;
  Pathfinding pathfinding;
  EndOfGameValues vals;
  CustomerControllerParams params = new CustomerControllerParams();

  // Array of chefs
  private static ArrayList<TextureAtlas> chefAtlasArray = new ArrayList<TextureAtlas>();
  Chef[] chef;
  MasterChef masterChef;
  World world;
  ChopStation chopStation;

  CustomerCounters customerCounter;

  TrashCan trashCan;

  OvenStation ovenStation;
  HobStation hobStation;

  ToasterStation toasterStation;
  AssemblyStation assemblyStation;
  GameObject assemble;
  GameObject crate;
  FoodCrate FC;

  TextureDictionary textureDictionary = new TextureDictionary();
  GameObject Fry;

  GameObject Oven;
  GameObject Toast;

  GameObject Chop;

  GameObject CustCount;

  void SetUpPathfinding(){
    pathfinding = new Pathfinding(GameScreen.TILE_WIDTH/4,GameScreen.viewportWidth,GameScreen.viewportWidth);
  }

  /**
   * Instantiates the world.
   *
   * @author Jack Vickers
   * @date 03/04/2023
   */
  void instantiateWorld() {
    world = new World(new Vector2(0, 0), true);
  }

  void instantiateCustomerScripts() {
    instantiateCustomerScripts(Difficulty.Stressful);
  }

  void instantiateCustomerScripts(Difficulty difficulty) {

    GameObjectManager.objManager = null;
    TextureDictionary dico = new TextureDictionary();

    DifficultyState difficultyState = DifficultyMaster.getDifficulty(difficulty);
    pathfinding = new Pathfinding(GameScreen.TILE_WIDTH / 4, GameScreen.viewportWidth,
        GameScreen.viewportWidth);

    manager = new GameObjectManager();
    params = difficultyState.ccParams;
    params.NoCustomers = 5;
    customerController = new CustomerController(new Vector2(0, 0), new Vector2(32, 0), pathfinding,
        (EndOfGameValues a) -> EndGame(a), params, new Vector2(190, 390), new Vector2(190, 290),
        new Vector2(290, 290),
        new Vector2(290, 295));
  }

  void EndGame(EndOfGameValues val){
    vals =val;
  }


  /**
   * Instantiates the world and two chefs so that these can be used in the tests.
   *
   * @author Jack Vickers
   * @date 01/05/2023
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
      chef[i] = new Chef(i, getChefAtlasArray().get(chefControl));
      chefsGameObject.attachScript(chef[i]);
      chefsGameObject.image.setSize(18, 40); // set size of sprite
      chef[i].updateSpriteFromInput("idlesouth");
    }
  }

  /**
   * Instantiates the masterchef class. This class generates multiple chefs. This will be used to
   * test interactions between a chef and interactable game object.
   *
   * @author Jack Vickers
   * @author Felix Seanor
   * @date 01/05/2023
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
    masterChef = new MasterChef(2, camera, pathfinding, difficultyState.chefParams, difficultyState.cookingParams);
    GameObjectManager.objManager.AppendLooseScript(masterChef);
  }


  /**
   * Generates a chef array which can be used to get random chef sprites from the chef class. Needed
   * to instantiate the chefs.
   *
   * @author Jack Vickers
   * @date 03/04/2023
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
   * @date 31/03/2023
   */
  private static ArrayList<TextureAtlas> getChefAtlasArray() {
    return chefAtlasArray;
  }

  /**
   * Instantiates a tomato food crate.
   *
   * @author Jack Vickers
   * @date 18/04/2023
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
   * @author Azzam Amirul
   * @author Felix Seanor
   * @date 26/04/2023
   */
  void instantiateWorldAndChoppingStation() {
    world = new World(new Vector2(0, 0), true);
    DifficultyState state = DifficultyMaster.getStressful();
    soundFrame soundFrame = new soundFrame();
    TiledMap map;
    map = new TmxMapLoader().load("PiazzaPanicMap.tmx"); // loads map
    MapLayer chopping = map.getLayers().get(5); // gets chopping layer
    MapObject object = chopping.getObjects().getByType(RectangleMapObject.class)
        .get(0); // gets chopping object
    Rectangle rect = ((RectangleMapObject) object).getRectangle(); // gets chopping rectangle
    Chop = new GameObject(null); // creates chopping game object
    Chop.setPosition(0,
        0); // sets chopping position (this must be done to avoid null pointer exception)
    Chop.setWidthAndHeight(rect.getWidth(),
        rect.getHeight()); // sets chopping width and height (this must be done to avoid null pointer exception)
    chopStation = new ChopStation(state.cookingParams); // creates chopping station
    Chop.attachScript(chopStation); // attaches chopping station to chopping game object
    new RecipeDict(); // creates recipe dictionary
    RecipeDict.recipes.implementRecipes(); // implements recipes
    chopStation.init(); // initialises chopping station
  }

  /**
   * Creates the world and assembly station. Also creates the recipe dictionary.
   *
   * @author Jack Vickers
   * @author Felix Seanor
   * @date 25/04/2023
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
    Rectangle rect = ((RectangleMapObject) object).getRectangle(); // gets assembly rectangle
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
   * @author Hubert Solecki
   * @author Jack Vickers
   * @date 25/04/2023
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
    Fry = new GameObject(null); // creates frying game object
    Fry.setPosition(0,
        0); // sets frying position (this must be done to avoid null pointer exception)
    Fry.setPosition(0,
        0); // sets frying position (this must be done to avoid null pointer exception)
    Fry.setWidthAndHeight(rect.getWidth(),
        rect.getHeight()); // sets frying width and height (this must be done to avoid null pointer exception)
    hobStation = new HobStation(state.cookingParams); // creates frying station

    Fry.attachScript(hobStation); // attaches frying station to frying game object
    hobStation.init();
    new RecipeDict(); // creates recipe dictionary
    RecipeDict.recipes.implementRecipes(); // implements recipes
  }

  /**
   * Creates the world and toaster station. Also creates the recipe dictionary.
   *
   * @author Hubert Solecki
   * @author Jack Vickers
   * @date 25/04/2023
   */

  void instantiateWorldAndToasterStation() {
    world = new World(new Vector2(0, 0), true);
    TiledMap map;
    DifficultyState state = DifficultyMaster.getStressful();
    soundFrame soundFrame = new soundFrame();
    map = new TmxMapLoader().load("PiazzaPanicMap.tmx"); // loads map
    MapLayer toasting = map.getLayers().get(6); // gets toasting layer
    MapObject object = toasting.getObjects().getByType(RectangleMapObject.class)
        .get(0); // gets toasting object
    Rectangle rect = ((RectangleMapObject) object).getRectangle(); // gets toasting rectangle
    Toast = new GameObject(null); // creates toasting game object
    Toast.setPosition(0, 0); // sets toasting position (done to avoid NullPointerException)
    Toast.setWidthAndHeight(rect.getWidth(),
        rect.getHeight()); // sets toasting width and height (done to avoid NullPointerException)
    toasterStation = new ToasterStation(state.cookingParams); // creates toaster station
    Toast.attachScript(toasterStation); // attaches toaster station to toaster game object
    toasterStation.init();
    new RecipeDict(); // creates recipe dictionary
    RecipeDict.recipes.implementRecipes(); // implements recipes
    toasterStation.init(); // initialises toaster station
  }

  /**
   * Creates the world and the customer counter for testing. Also creates the recipe dictionary.
   *
   * @author Azzam Amirul
   * @date 01/05/2023
   */
  void instantiateWorldAndCustomerCounter() {
    world = new World(new Vector2(0, 0), true);
    TiledMap map;
    DifficultyState state = DifficultyMaster.getStressful();
    soundFrame soundFrame = new soundFrame();
    map = new TmxMapLoader().load("PiazzaPanicMap.tmx"); // loads map
    MapLayer customerCounterLayer = map.getLayers().get(8); // gets customer counter layer
    MapObject object = customerCounterLayer.getObjects().getByType(RectangleMapObject.class)
            .get(0); // gets customerCounter object
    Rectangle rect = ((RectangleMapObject) object).getRectangle(); // gets customer counter rectangle
    CustCount = new GameObject(null); // creates customer counter game object
    CustCount.setPosition(0, 0); // sets customer counter position (done to avoid NullPointerException)
    CustCount.setWidthAndHeight(rect.getWidth(),
            rect.getHeight()); // sets Customer counter width and height (done to avoid NullPointerException)
    customerCounter = new CustomerCounters((Item a) -> (customerController.tryGiveFood(a)), state.cookingParams); // creates customer counter station
    CustCount.attachScript(customerCounter); // attaches customer counter station to customer counter game object
    customerCounter.init();
    new RecipeDict(); // creates recipe dictionary
    RecipeDict.recipes.implementRecipes(); // implements recipes
    customerCounter.init(); // initialises customer counter
  }

  /**
   * Creates the world and the Trash can for testing.
   *
   * @author Azzam Amirul
   * @date 14/04/2023
   */
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

  /**
   * Creates the world and oven station. Also creates the recipe dictionary.
   *
   * @author Hubert Solecki
   * @date 01/05/2023
   */
  void instantiateWorldAndOvenStation() {
    world = new World(new Vector2(0, 0), true);
    TiledMap map;
    DifficultyState state = DifficultyMaster.getStressful();
    soundFrame soundFrame = new soundFrame();
    map = new TmxMapLoader().load("PiazzaPanicMap.tmx"); // loads map
    MapLayer ovening = map.getLayers().get(7); // gets oven layer
    MapObject object = ovening.getObjects().getByType(RectangleMapObject.class).get(0); // gets oven object
    Rectangle rect = ((RectangleMapObject) object).getRectangle(); // gets oven rectangle
    Oven = new GameObject(null); // creates oven game object
    Oven.setPosition(0, 0); // sets oven station position (done to avoid NullPointerException)
    Oven.setWidthAndHeight(rect.getWidth(), rect.getHeight()); // sets oven station width and height (done to avoid NullPointerException)
    Consumer<Boolean> custController = (Boolean a) -> customerController.updateMenu(a);
    ovenStation = new OvenStation(state.cookingParams, custController); // creates oven station
    Oven.attachScript(ovenStation); // attaches oven station to oven game object
    ovenStation.init();
    new RecipeDict(); // creates recipe dictionary
    RecipeDict.recipes.implementRecipes(); // implements recipes
//    ovenStation.init(); // initialises oven station
  }


}
