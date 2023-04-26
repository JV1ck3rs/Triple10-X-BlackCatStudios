package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Core.*;
import com.mygdx.game.Core.GameState.Difficulty;
import com.mygdx.game.Core.GameState.DifficultyMaster;
import com.mygdx.game.Core.GameState.DifficultyState;
import com.mygdx.game.Core.GameState.GameState;
import com.mygdx.game.Core.GameState.ItemState;
import com.mygdx.game.Core.GameState.SaveState;
import com.mygdx.game.Core.ValueStructures.CustomerControllerParams;
import com.mygdx.game.Core.ValueStructures.EndOfGameValues;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.RecipeAndComb.CombinationDict;
import com.mygdx.game.RecipeAndComb.RecipeDict;
import com.mygdx.game.Stations.*;
import java.util.Objects;
import jdk.internal.org.jline.utils.DiffHelper.Diff;

/**
 * This is the main class of the game which runs all the logic and rendering Here all the outside
 * objects are created and drawn as well as interactions registered
 *
 * @author Robin Graham
 * @author Riko Puusepp
 * @author Kelvin Chen
 * @author Amy Cross
 * @author Labib Zabeneh
 */
public class GameScreen implements Screen {

  // game attributes
  private final MyGdxGame game;

  // camera
  private final OrthographicCamera camera;
  private Pathfinding pathfinding;
  public static final int TILE_WIDTH = 32;
  public static final int TILE_HEIGHT = 32;

  // box2d
  static World world;

  public CustomerController customerController;

  public Powerup powerup;




  // map
  private final TiledMapRenderer mapRenderer;

  public MasterChef masterChef;



  public GameObject exitLogo = new GameObject(new BlackTexture("Exit.png"));

  // game timer and displayTimer
  private float seconds = 0f;
  private int timer = 0;
  private final Label timerLabel;
  private final Label moneyLabel;
  private final BitmapFont timerFont;

  boolean Paused = false;
  DifficultyState difficultyState;

  ConstructMachines constructMachines;
  Difficulty difficulty;
  public static final int viewportWidth = 32 * TILE_WIDTH;
  public static final int viewportHeight = 18 * TILE_HEIGHT;
  Music gameMusic;

  public showRecipeInstructions recipeScreen;
  Label modeLabel;

  Stage pauseStage; // stage for the pause menu
  Stage gameUIStage; // stage for the game UI
  float scaleX;
  float scaleY;
  boolean isEndlessMode;
  FitViewport viewport;

  /**
   * Constructor class which initialises all the variables needed to draw the sprites and also
   * manage the logic of the render as well as setting the camera and map
   *
   * @param game base Object which is used to draw on
   * @author Amy Cross
   * @author Felix Seanor
   * @author Sam Toner
   * @author Jack Vickers
   * @author Jack Hinton
   */
  public GameScreen(MyGdxGame game, TiledMap map, boolean Test, int numCustomers, boolean loadSave,
      Difficulty difficultyLevel) {
    this.game = game;
    camera = new OrthographicCamera();
    recipeScreen = new showRecipeInstructions();
    //recipeScreen.showRecipeInstruction();
    CameraFunctions camera1 = CameraFunctions.camera;
    camera1.updateCamera(camera);
    viewport = new FitViewport(720, 1280, camera);


    camera.setToOrtho(false, viewportWidth, viewportHeight);
    camera.update();

    gameMusic = Gdx.audio.newMusic(Gdx.files.internal("gameMusic.mp3"));
    gameMusic.setLooping(true);

    recipeScreen.createInstructionPage("Empty");

    world = new World(new Vector2(0, 0), true);
    exitLogo.isVisible = false;
    exitLogo.getBlackTexture().height = 30;
    exitLogo.getBlackTexture().width = 30;
    exitLogo.position = new Vector2(713, 454);

    // add map
    mapRenderer = new OrthogonalTiledMapRenderer(map);
    mapRenderer.setView(camera);

    difficultyState = DifficultyMaster.getDifficulty(difficultyLevel);

    pathfinding = new Pathfinding(TILE_WIDTH / 4, viewportWidth, viewportWidth);

    masterChef = new MasterChef(2, world, camera, pathfinding, difficultyState.chefParams);
    GameObjectManager.objManager.AppendLooseScript(masterChef);

    CustomerControllerParams CCParams = difficultyState.ccParams;

    CCParams.NoCustomers = numCustomers;
    customerController = new CustomerController(new Vector2(200, 100), new Vector2(360, 180),
        pathfinding, (EndOfGameValues vals) -> EndGame(vals), CCParams, new Vector2(190, 390),
        new Vector2(190, 290), new Vector2(290, 290));
    // customerController.SetWaveAmount(1);//Demonstration on how to do waves, -1 for endless

    GameObjectManager.objManager.AppendLooseScript(customerController);

    powerup = new Powerup(masterChef, customerController); // powerup object

    constructMachines = new ConstructMachines(customerController,difficultyState,pathfinding);

    new CombinationDict();
    CombinationDict.combinations.implementItems();
    new RecipeDict();
    RecipeDict.recipes.implementRecipes();

    createCollisionListener();
    int[] objectLayers = {3, 4, 6, 9, 11, 13, 16, 18, 20, 22, 24, 25, 26, 27, 28, 29, 30, 31, 32,
        33, 34, 35, 36, 37, 38, 39};

    //Fixed the hideous mechanism for creating collidable objects
    for (int n = 0; n < 17; n++) {
      MapLayer layer = map.getLayers().get(n);
      String name = layer.getName();

      for (MapObject object : layer.getObjects()
          .getByType(RectangleMapObject.class)) {

        Rectangle rect = ((RectangleMapObject) object).getRectangle();
        constructMachines.buildObject(world, rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), "Static",
            name);

        switch (name) {
          case "bin":
            constructMachines.CreateBin(rect);
            break;
          case "counter":
            constructMachines.CreateAssembly(rect);
            break;
          case "frying":
            constructMachines.CreateHobs(rect);
            break;
          case "chopping board":
            constructMachines.CreateChopping(rect);
            break;
          case "toaster":
            constructMachines.CreateToaster(rect);
            break;
          case "oven":
            constructMachines.CreateOven(rect);
            break;
          case "customer counter":
            constructMachines.CreateCustomerCounters(rect);
            break;
          case "tomato":
            constructMachines.CreateFoodCrates(rect, ItemEnum.Tomato);
            break;
          case "lettuce":
            constructMachines.CreateFoodCrates(rect, ItemEnum.Lettuce);
            break;
          case "onion":
            constructMachines.CreateFoodCrates(rect, ItemEnum.Onion);
            break;
          case "mince":
            constructMachines. CreateFoodCrates(rect, ItemEnum.Mince);
            break;
          case "buns":
            constructMachines. CreateFoodCrates(rect, ItemEnum.Buns);
            break;
          case "dough":
            constructMachines.CreateFoodCrates(rect, ItemEnum.Dough);
            break;
          case "cheese":
            constructMachines.CreateFoodCrates(rect, ItemEnum.Cheese);
            break;
          case "potato":
            constructMachines.CreateFoodCrates(rect, ItemEnum.Potato);
            break;
        }
      }
    }

    timerLabel = new Label("TIME: " + timer,
        new Label.LabelStyle(new BitmapFont(), Color.WHITE));

    moneyLabel = new Label("Money: ¥" + timer,
        new Label.LabelStyle(new BitmapFont(), Color.WHITE));

    timerFont = new BitmapFont();
    pauseStage = new Stage();

    // Calculates the scale of the screen to the original size of the game
    scaleX = Gdx.graphics.getWidth() / 640f;
    scaleY = Gdx.graphics.getHeight() / 480f;
    if (loadSave) { // if the game is being loaded from a save
      if(!Test)
        LoadGame("SavedData.ser");
      else
        LoadGame("../assets/TestingData.ser");
    }
    isEndlessMode = CCParams.NoCustomers == -1;

    if(!Test) {
      setupGameUI();
      setupPauseMenu();
    }
  }

  /**
   * Sets up the UI elements which will be displayed during the game.
   *
   * @author Jack Vickers
   */
  private void setupGameUI() {
    gameUIStage = new Stage();
    Gdx.input.setInputProcessor(gameUIStage);
    Table gameUITable = new Table();
    gameUIStage.addActor(gameUITable);
    gameUITable.setFillParent(true);
    gameUITable.align(Align.top);
    if (isEndlessMode) {
      modeLabel = new Label("ENDLESS MODE", new Label.LabelStyle(new BitmapFont(),
          Color.WHITE));
      modeLabel.setFontScale(1.1f * (scaleX + scaleY) / 2);
      gameUITable.add(modeLabel).align(Align.topLeft).expandX();
    } else {
      modeLabel = new Label("SCENARIO MODE",
          new Label.LabelStyle(new BitmapFont(), Color.WHITE));
      modeLabel.setFontScale(1.1f * (scaleX + scaleY) / 2);
      gameUITable.add(modeLabel).align(Align.topLeft).expandX();
    }
    updateCustomerLabel();
    TextureRegion pauseBtn = new TextureRegion(new Texture("PauseUp.png"));
    TextureRegion pauseBtnDown = new TextureRegion(new Texture("PauseDown.png"));
    Drawable pauseBtnDrawable = new TextureRegionDrawable(pauseBtn);
    Drawable pauseBtnDrawableDown = new TextureRegionDrawable(pauseBtnDown);
    Button.ButtonStyle pauseButtonStyle = new Button.ButtonStyle();
    Button pauseButton = new Button();
    pauseButton.setStyle(pauseButtonStyle);
    pauseButtonStyle.up = pauseBtnDrawable;
    pauseButtonStyle.down = pauseBtnDrawableDown;
    gameUITable.add(pauseButton).width(48 * scaleX).height(48 * scaleY).align(Align.topRight)
        .expandX();
    gameUITable.row();
    pauseButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        Paused = true;
        Gdx.input.setInputProcessor(pauseStage); // set the input processor to the pause stage
      }
    });
    //TODO: Possibly use this function for the powerup menu in the future

    //TODO: Add a level which displays the number of customers remaining for the scenario mode

  }

  private void updateCustomerLabel() {
    modeLabel.setText("Customers remaining: " + customerController.getRemainingNumberOfCustomers());
  }

  /**
   * Creates the pause menu.
   *
   * @author Jack Vickers
   * @date 07/04/2023
   */
  private void setupPauseMenu() {

    Image pauseImage = new Image(new Texture("SemiTransparentBG.png"));
    pauseImage.setSize(pauseStage.getWidth(), pauseStage.getHeight());
    pauseImage.setPosition(0, 0);
    pauseStage.addActor(pauseImage);
    Table pauseTable = new Table(); // create a table to hold the pause menu
    pauseStage.addActor(pauseTable); // add the table to the stage
    pauseTable.setFillParent(true);
    pauseTable.align(Align.center);

    // The following block of code creates the resume button and adds it to the table
    TextureRegion resumeBtn = new TextureRegion(new Texture("ResumeUp.png"));
    TextureRegion resumeBtnDown = new TextureRegion(new Texture("ResumeDown.png"));
    Drawable drawableResumeBtn = new TextureRegionDrawable(resumeBtn);
    Drawable drawableResumeBtnDown = new TextureRegionDrawable(resumeBtnDown);
    Button.ButtonStyle resumeButtonStyle = new Button.ButtonStyle();
    Button resumeButton = new Button();
    resumeButton.setStyle(resumeButtonStyle);
    resumeButtonStyle.up = drawableResumeBtn;
    resumeButtonStyle.down = drawableResumeBtnDown;
    pauseTable.add(resumeButton).width(250 * scaleX).height(50 * scaleY).padBottom(50 * scaleY)
        .padTop(80 * scaleY);
    pauseTable.row();

    // The following block of code adds a listener to the resume button
    resumeButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        Paused = false; // when clicked, the game is no longer paused
        Gdx.input.setInputProcessor(gameUIStage); // set the input processor to the game UI stage
      }
    });

    // The following block of code creates the instruction button and adds it to the table
    TextureRegion instructionBtn = new TextureRegion(new Texture("HowToPlayUp.png"));
    TextureRegion instructionBtnDown = new TextureRegion(new Texture("HowToPlayDown.png"));
    Drawable drawableInstructionBtn = new TextureRegionDrawable(instructionBtn);
    Drawable drawableInstructionBtnDown = new TextureRegionDrawable(instructionBtnDown);
    Button.ButtonStyle instructionButtonStyle = new Button.ButtonStyle();
    Button instructionButton = new Button();
    instructionButton.setStyle(instructionButtonStyle);
    instructionButtonStyle.up = drawableInstructionBtn;
    instructionButtonStyle.down = drawableInstructionBtnDown;
    pauseTable.add(instructionButton).width(250 * scaleX).height(50 * scaleY).padBottom(50 * scaleY);
    pauseTable.row();

    // The following block of code adds a listener to the instruction button
    instructionButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        System.out.println("For now this doesn't do anything");
      }
    });

    // The following block of code creates the save button and adds it to the table
    TextureRegion saveBtn = new TextureRegion(new Texture("SaveUp.png"));
    TextureRegion saveBtnDown = new TextureRegion(new Texture("SaveDown.png"));
    Drawable drawableSaveBtn = new TextureRegionDrawable(saveBtn);
    Drawable drawableSaveBtnDown = new TextureRegionDrawable(saveBtnDown);
    Button.ButtonStyle saveButtonStyle = new Button.ButtonStyle();
    Button saveButton = new Button();
    saveButton.setStyle(saveButtonStyle);
    saveButtonStyle.up = drawableSaveBtn;
    saveButtonStyle.down = drawableSaveBtnDown;
    pauseTable.add(saveButton).width(250 * scaleX).height(50 * scaleY).padBottom(20 * scaleY);
    pauseTable.row();

    // Creates a label for the save button which will be used to give the user feedback
    Label saveLabel = new Label("", new LabelStyle(new BitmapFont(), Color.WHITE));
    saveLabel.setFontScale((scaleX + scaleY) / 2);

    saveLabel.setAlignment(Align.right);
    pauseTable.add(saveLabel).padBottom(10 * scaleY);
    pauseTable.row();

    // The following block of code adds a listener to the save button
    saveButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        SaveGame();
        saveLabel.setText("Game has successfully been saved");
      }
    });

    // The following block of code creates the quit button and adds it to the table
    TextureRegion quitBtn = new TextureRegion(new Texture("ExitUp.png"));
    TextureRegion quitBtnDown = new TextureRegion(new Texture("ExitDown.png"));
    Drawable drawableQuitBtn = new TextureRegionDrawable(quitBtn);
    Drawable drawableQuitBtnDown = new TextureRegionDrawable(quitBtnDown);
    Button.ButtonStyle quitButtonStyle = new Button.ButtonStyle();
    Button quitButton = new Button();
    quitButton.setStyle(quitButtonStyle);
    quitButtonStyle.up = drawableQuitBtn;
    quitButtonStyle.down = drawableQuitBtnDown;
    pauseTable.add(quitButton).width(250 * scaleX).height(50 * scaleY).padBottom(50 * scaleY);
    pauseTable.row();

    // The following block of code adds a listener to the quit button
    quitButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        gameMusic.stop();
        game.setScreen(new MenuScreen(game));
      }
    });
  }


  /**
   * End game sequence
   * @param values
   */
  public void EndGame(EndOfGameValues values){

    VictoryScreen screen = new VictoryScreen(game, this, timer, values);
    game.setScreen(screen);

  }


  /**
   * Plays the game music when the screen is shown.
   * @author Amy Cross
   * @author Jack Vickers
   */
  @Override
  public void show() {
    if (!gameMusic.isPlaying()) {
      gameMusic.setVolume(0.6f);
      gameMusic.play(); // only play the music if it's not already playing
    }
  }

  /**
   * Displays the timer.
   *
   * @author Amy Cross
   * @author Felix Seanor
   */
  public void displayTimer() {
    seconds += Gdx.graphics.getDeltaTime();
    if (seconds >= 1f) {
      timer++;
      seconds = 0f;
    }
    CharSequence str = "TIME: " + timer;
    timerFont.draw(game.batch, str, 380, 35);
    timerFont.getData().setScale(1.5f, 1.5f);
    timerLabel.setText(str);

    CharSequence str2 = "Money: ¥" + customerController.Money;
    timerFont.draw(game.batch, str2, 500, 35);
    timerFont.getData().setScale(1.5f, 1.5f);

    if (customerController.Reputation != -1) {
      CharSequence str3 = "Reputation: " + customerController.Reputation;
      timerFont.draw(game.batch, str3, 650, 35);
      timerFont.getData().setScale(1.5f, 1.5f);
    }

    CharSequence str3 = "Patience: ";
    timerFont.draw(game.batch, str3, 200, 25);

  }

  /**
   * Calls all logic updates and sprite draws as well as checks if game has been completed
   *
   * @param delta The time in seconds since the last render.
   *
   * @author Felix Seanor
   * @author Amy Cross
   * @author Jack Hinton
   */
  @Override
  public void render(float delta) {
    //create world
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    //create camera
    camera.update();
    mapRenderer.setView(camera);
    mapRenderer.render();

//    if(Gdx.input.isKeyJustPressed(Keys.B))
//      SaveGame();

    if (Gdx.input.isKeyJustPressed(Keys.V)) {
      LoadGame("SavedData.ser");
    }

    //Removed and simplified logic

    world.step(1 / 60f, 6, 2);

    game.batch.setProjectionMatrix(camera.combined);

    //Begins drawing the game batch
    game.batch.begin();

    if (!Paused) {
      displayTimer();
      //Update Scripts
      GameObjectManager.objManager.doUpdate(Gdx.graphics.getDeltaTime());
      updateCustomerLabel();
      //New rendering system
      RenderManager.renderer.onRender(game.batch);

      // Mutes or plays the music
      if (Gdx.input.isKeyJustPressed((Input.Keys.M))) {
        if (gameMusic.isPlaying()) {
          gameMusic.pause();
        } else {
          gameMusic.play();
        }
      }
      if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
        powerup.doSpeedPowerup();
      }
      if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
        powerup.buyReputation();
      }
      if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
        powerup.superFood();
      }
      if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
        powerup.tetrisSuperFood();
      }
    } else {
      pauseStage.draw();
    }
    // THIS IS SAM'S CODE:
//    LeaderBoard x = new LeaderBoard();
//    x.createJSONFile();
//    try {
//      x.readJSONData();
//    } catch (IOException e) {
//      throw new RuntimeException(e);
//    }

    game.batch.end();

    // The following code must occur after the batch is ended.
    // Otherwise, it causes issues with customer positioning.
    if (!Paused) { // displays the game UI if the game is not paused
      gameUIStage.draw();
    }
  }


  /**
   * Finds all the collisions and assigns the names Also has a convenience function to disregard the
   * chef collision
   *
   * @author Amy Cross
   */
  public void createCollisionListener() {
    world.setContactListener(new ContactListener() {

      /**
       * gets the collision start and finds the names of the things colliding
       *
       * @param contact The object containing collision information
       */
      @Override
      public void beginContact(Contact contact) {

        Object objectA = contact.getFixtureA().getBody().getUserData();
        Object objectB = contact.getFixtureB().getBody().getUserData();
        Gdx.app.log("beginContact", "between " + objectA + " and " + objectB);
      }


      /**
       * outputs when two objects have stopped colliding
       *
       * @param contact The object containing decollision information
       */
      @Override
      public void endContact(Contact contact) {

        Object objectA = contact.getFixtureA().getBody().getUserData();
        Object objectB = contact.getFixtureB().getBody().getUserData();
        Gdx.app.log("endContact", "between " + objectA + " and " + objectB);
      }

      /**
       * Finds out when the two chefs have collided to ignore this collision
       *
       * @param contact The object containing collision information
       * @param oldManifold Needed by the override
       */
      @Override
      public void preSolve(Contact contact, Manifold oldManifold) {
        Object objectA = contact.getFixtureA().getBody().getUserData();
        Object objectB = contact.getFixtureB().getBody().getUserData();
        if ((objectA.toString().contentEquals("Chef0")) && (objectB.toString()
            .contentEquals("Chef1"))) {
          System.out.println("CONTACT");
          contact.setEnabled(false);
        }
      }

      @Override
      public void postSolve(Contact contact, ContactImpulse impulse) {
      }

    });
  }

  /**
   * Save the game.
   *
   * @author Felix Seanor
   */
  public void SaveGame(){
    SaveState Saving = new SaveState();
    Saving.SaveState("SavedData.ser", masterChef, customerController, difficulty, timer, seconds, constructMachines.Stations, constructMachines.customerCounters, constructMachines.assemblyStations);

  }

  /**
   * Load the game from save
   * @author Felix Seanor
   */
  public void LoadGame(String path){
    SaveState Saving = new SaveState();

    GameState state = Saving.LoadState(path);

    LoadState(state);
    masterChef.LoadState(state);
    customerController.LoadState(state);


  }

  /**
   * Loads the state of a previous state of the world, all LoadGame to a full sweep.
   * @param state
   *
   * @author Felix Seanor
   */
  public void LoadState(GameState state) {

    int i = 0;
    timer = state.Timer;
    seconds = state.seconds;
    difficulty = state.difficulty;
    for (GameObject station : constructMachines.Stations) {
      Scriptable scriptable = station.GetScript(0);
      if (scriptable instanceof Station) {
        ((Station) scriptable).LoadState(state.FoodOnCounters.get(i++));
      }


    }

    for (GameObject station : constructMachines.customerCounters) {
      ((Station) station.GetScript(0)).LoadState(state.FoodOnCounters.get(i++));
    }

    for (GameObject station : constructMachines.assemblyStations) {
      ((Station) station.GetScript(0)).LoadState(state.FoodOnCounters.get(i++));
    }
  }


  public Difficulty getDifficulty() {
    return difficulty;
  }

  public int getTimer() {
    return timer;
  }

  public float getSeconds() {
    return seconds;
  }

  public List<GameObject> getStations() {
    return constructMachines.Stations;
  }

  public List<GameObject> getCustomerCounters() {
    return constructMachines.customerCounters;
  }

  public List<GameObject> getAssemblyStations() {
    return constructMachines.assemblyStations;
  }



//  /**
//   * Resizes the stage when the window is resized so that the buttons are in the correct place.
//   * Parameters inherited from interface com.badlogic.gdx.Screen and not explicitly used.
//   */
  @Override
  public void resize(int width, int height) {
    pauseStage.getViewport().update(width, height, true);
    gameUIStage.getViewport().update(width, height, true);
//    viewport.update(width, height);
  }

  @Override
  public void pause() {
  }

  @Override
  public void resume() {
  }

  @Override
  public void hide() {
  }

  /**
   * Disposes of all sprites from memory to keep it optimised
   * @author Felix Seanor
   */
  @Override
  public void dispose() {
    game.batch.dispose();
  }

}
