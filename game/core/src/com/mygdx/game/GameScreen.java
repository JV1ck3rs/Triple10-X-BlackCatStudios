package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Core.Chef.MasterChef;
import com.mygdx.game.Core.ConstructMachines;
import com.mygdx.game.Core.Customers.CustomerController;
import com.mygdx.game.Core.GameState.Difficulty;
import com.mygdx.game.Core.GameState.DifficultyMaster;
import com.mygdx.game.Core.GameState.DifficultyState;
import com.mygdx.game.Core.GameState.GameState;
import com.mygdx.game.Core.GameState.SaveState;
import com.mygdx.game.Core.PathFinder.Pathfinding;
import com.mygdx.game.Core.Powerups.Powerup;
import com.mygdx.game.Core.Powerups.PowerupPurchaseMenu;
import com.mygdx.game.Core.Rendering.BlackTexture;
import com.mygdx.game.Core.Rendering.GameObject;
import com.mygdx.game.Core.Rendering.GameObjectManager;
import com.mygdx.game.Core.Rendering.RenderManager;
import com.mygdx.game.Core.SFX.SoundFrame;
import com.mygdx.game.Core.Scriptable;
import com.mygdx.game.Core.ValueStructures.CustomerControllerParams;
import com.mygdx.game.Core.ValueStructures.EndOfGameValues;
import com.mygdx.game.Items.ItemEnum;
import com.mygdx.game.RecipeAndComb.CombinationDict;
import com.mygdx.game.RecipeAndComb.RecipeDict;
import com.mygdx.game.Stations.Station;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * This is the main class of the game which runs all the logic and rendering Here all the outside
 * objects are created and drawn as well as interactions registered
 * <p>
 * Black Cat Studios and Team Triple 10s
 *
 * @author Robin Graham
 * @author Riko Puusepp
 * @author Kelvin Chen
 * @author Amy Cross
 * @author Labib Zabeneh
 * @author Felix Seanor
 * @author Jack Vickers
 * @author Jack Hinton
 * @author Sam Toner
 * @date 01 /05/23
 */
public class GameScreen implements Screen {

  // game attributes
  private final MyGdxGame game;

  // camera
  private final OrthographicCamera camera;
  private Pathfinding pathfinding;
  /**
   * The constant TILE_WIDTH.
   */
  public static final int TILE_WIDTH = 32;
  /**
   * The constant TILE_HEIGHT.
   */
  public static final int TILE_HEIGHT = 32;

  /**
   * The Customer controller.
   */
  public CustomerController customerController;

  /**
   * The Powerup.
   */
  public Powerup powerup;
  /**
   * The Powerup purchase menu.
   */
  public PowerupPurchaseMenu powerupPurchaseMenu;


  // map
  private final TiledMapRenderer mapRenderer;

  /**
   * The Master chef.
   */
  public MasterChef masterChef;

  /**
   * The Num ovens.
   */
  public int numOvens = 0;
  /**
   * The Exit logo.
   */
  public GameObject exitLogo = new GameObject(new BlackTexture("Exit.png"));


  // game timer and displayTimer
  private float seconds = 0f;
  private int timer = 0;
  private final Label timerLabel;
  private final Label moneyLabel;
  private final BitmapFont timerFont;

  /**
   * The Paused.
   */
  boolean paused = false;
  /**
   * The Difficulty state.
   */
  DifficultyState difficultyState;

  /**
   * The Construct machines.
   */
  ConstructMachines constructMachines;
  /**
   * The Difficulty.
   */
  Difficulty difficulty;
  /**
   * The constant viewportWidth.
   */
  public static final int viewportWidth = 32 * TILE_WIDTH;
  /**
   * The constant viewportHeight.
   */
  public static final int viewportHeight = 18 * TILE_HEIGHT;
  /**
   * The Game music.
   */
  Music gameMusic;

  /**
   * The Recipe screen.
   */
  public ShowRecipeInstructions recipeScreen;
  /**
   * The Mode label.
   */
  Label modeLabel;

  /**
   * The Pause stage.
   */
  Stage pauseStage; // stage for the pause menu
  /**
   * The Game ui stage.
   */
  Stage gameUIStage; // stage for the game UI
  /**
   * The Instructions stage.
   */
  Stage instructionsStage; // stage for the instructions
  /**
   * The Scale x.
   */
  float scaleX;
  /**
   * The Scale y.
   */
  float scaleY;
  /**
   * The Is endless mode.
   */
  boolean isEndlessMode;
  /**
   * The Viewport.
   */
  FitViewport viewport;
  /**
   * The End screen.
   */
  EndScreen endScreen;
  /**
   * The Is instructions visible.
   */
  boolean isInstructionsVisible;
  /**
   * The Resume button.
   */
  Button resumeButton;
  /**
   * The Instructions resume button.
   */
  Button instructionsResumeButton;

  /**
   * Constructor class which initialises all the variables needed to draw the sprites and also
   * manage the logic of the render as well as setting the camera and map Mixture of BlackCatStudios
   * and TeamTriple10
   *
   * @param game            base Object which is used to draw on
   * @param map             the map
   * @param numCustomers    the num customers
   * @param difficultyLevel the difficulty level
   * @param loadSave        the load save
   * @author Amy Cross
   * @author Felix Seanor
   * @author Sam Toner
   * @author Jack Vickers
   * @author Jack Hinton
   */
  public GameScreen(MyGdxGame game, TiledMap map, int numCustomers,
      Difficulty difficultyLevel, boolean loadSave) {
    //Triple10s
    this.game = game;
    camera = new OrthographicCamera();
    recipeScreen = new ShowRecipeInstructions();
    GameState savedData = new GameState();
    if (loadSave) { // if the game is being loaded from a save
      savedData = loadGameState("SavedData.ser");
      difficulty = savedData.difficulty;
    } else {
      difficulty = difficultyLevel;

    }

    //recipeScreen.showRecipeInstruction();
    //BlackCatStudios
    CameraFunctions camera1 = CameraFunctions.camera;
    camera1.updateCamera(camera);
    viewport = new FitViewport(720, 1280, camera);
//Triple10s
    camera.setToOrtho(false, viewportWidth, viewportHeight);
    camera.update();

    gameMusic = Gdx.audio.newMusic(Gdx.files.internal("gameMusic.mp3"));
    gameMusic.setLooping(true);

    //BlackCatStudios
    recipeScreen.createInstructionPage("Empty");

    exitLogo.isVisible = false;
    exitLogo.getBlackTexture().height = 30;
    exitLogo.getBlackTexture().width = 30;
    exitLogo.position = new Vector2(713, 454);

    // add map
    mapRenderer = new OrthogonalTiledMapRenderer(map);
    mapRenderer.setView(camera);

    difficultyState = DifficultyMaster.getDifficulty(difficulty);

    pathfinding = new Pathfinding(TILE_WIDTH / 4, viewportWidth, viewportWidth);

    masterChef = new MasterChef(3, camera, pathfinding, difficultyState.chefParams,
        difficultyState.cookingParams);
    GameObjectManager.objManager.appendLooseScript(masterChef);

    CustomerControllerParams CCParams = difficultyState.ccParams;

    CCParams.noCustomers = numCustomers;
    List<Vector2> tables = getTablesDirty(map);

    Vector2[] tablePositions = new Vector2[tables.size()];

    int z = 0;
    for (Vector2 t : tables
    ) {
      tablePositions[z++] = t;
    }

    customerController = new CustomerController(new Vector2(224, 0), new Vector2(360, 180),
        pathfinding, (EndOfGameValues vals) -> endGame(vals), CCParams, tablePositions);
    // customerController.SetWaveAmount(1);//Demonstration on how to do waves, -1 for endless

    powerup = new Powerup(masterChef, customerController); // powerup object
    powerupPurchaseMenu = new PowerupPurchaseMenu(customerController, powerup, masterChef);
    powerupPurchaseMenu.initialiseState();
    GameObjectManager.objManager.appendLooseScript(powerupPurchaseMenu);
    GameObjectManager.objManager.appendLooseScript(customerController);

    constructMachines = new ConstructMachines(customerController, difficultyState, pathfinding);

    new CombinationDict();
    CombinationDict.combinations.implementItems();
    new RecipeDict();
    RecipeDict.recipes.implementRecipes();

    int[] objectLayers = {3, 4, 6, 9, 11, 13, 16, 18, 20, 22, 24, 25, 26, 27, 28, 29, 30, 31, 32,
        33, 34, 35, 36, 37, 38, 39};

    //Fixed the hideous mechanism for creating collidable objects
    for (int n = 0; n < 18; n++) {
      MapLayer layer = map.getLayers().get(n);
      String name = layer.getName();

      for (MapObject object : layer.getObjects()
          .getByType(RectangleMapObject.class)) {

        Rectangle rect = ((RectangleMapObject) object).getRectangle();
        constructMachines.buildObject(rect.getX(), rect.getY(), rect.getWidth(),
            rect.getHeight(), "Static",
            name);

        switch (name) {
          case "tables":
            break;
          case "bin":
            constructMachines.createBin(rect);
            break;
          case "counter":
            constructMachines.createAssembly(rect);
            break;
          case "frying":
            constructMachines.createHobs(rect);
            break;
          case "chopping board":
            constructMachines.createChopping(rect);
            break;
          case "toaster":
            constructMachines.createToaster(rect);
            break;
          case "oven":
            constructMachines.createOven(rect, customerController);
            break;
          case "customer counter":
            constructMachines.createCustomerCounters(rect);
            break;
          case "tomato":
            constructMachines.createFoodCrates(rect, ItemEnum.Tomato);
            break;
          case "lettuce":
            constructMachines.createFoodCrates(rect, ItemEnum.Lettuce);
            break;
          case "onion":
            constructMachines.createFoodCrates(rect, ItemEnum.Onion);
            break;
          case "mince":
            constructMachines.createFoodCrates(rect, ItemEnum.Mince);
            break;
          case "buns":
            constructMachines.createFoodCrates(rect, ItemEnum.Buns);
            break;
          case "dough":
            constructMachines.createFoodCrates(rect, ItemEnum.Dough);
            break;
          case "cheese":
            constructMachines.createFoodCrates(rect, ItemEnum.Cheese);
            break;
          case "potato":
            constructMachines.createFoodCrates(rect, ItemEnum.Potato);
            break;
          case "toolbox":
            constructMachines.createFoodCrates(rect, ItemEnum.RepairTool);
        }
      }

    }

    //Team  Triple10
    timerLabel = new Label("TIME: " + timer,
        new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    //BlackCatStudios
    moneyLabel = new Label("Money: ¥" + timer,
        new Label.LabelStyle(new BitmapFont(), Color.WHITE));

    timerFont = new BitmapFont();
    pauseStage = new Stage();

    // Calculates the scale of the screen to the original size of the game
    scaleX = Gdx.graphics.getWidth() / 640f;
    scaleY = Gdx.graphics.getHeight() / 480f;
    if (loadSave) { // if the game is being loaded from a save
      loadGame(savedData);

    }

    if (!Objects.isNull(customerController.getCustomersPerScenarioWave())) {
      isEndlessMode = !(customerController.getCustomersPerScenarioWave().size() > 0);
    } else {
      isEndlessMode = CCParams.noCustomers == -1;
    }
    setupPauseMenu();
    setupGameUI();
  }


  private List<Vector2> getTablesDirty(TiledMap map) {
    List<Vector2> tables = new LinkedList<>();
    for (int n = 0; n < 18; n++) {
      MapLayer layer = map.getLayers().get(n);
      String name = layer.getName();

      if (!name.contains("tables")) {
        continue;
      }

      for (MapObject object : layer.getObjects()
          .getByType(RectangleMapObject.class)) {

        Rectangle rect = ((RectangleMapObject) object).getRectangle();

        tables.add(rect.getPosition(new Vector2()));

      }
    }

    return tables;
  }

  /**
   * Sets up the UI elements which will be displayed during the game. BlackCatStudio's code
   *
   * @author Jack Vickers
   */
  private void setupGameUI() {
    // sets up a stage for the UI
    gameUIStage = new Stage();
    Gdx.input.setInputProcessor(gameUIStage);
    Table gameUITable = new Table();
    gameUIStage.addActor(gameUITable);
    gameUITable.setFillParent(true);
    gameUITable.align(Align.top);
    // creates a label which displays whether the game is the endless mode or scenario mode
    // This label gets updates elsewhere to show either number of customers left or number
    // of customers served depending on the mode.
    if (isEndlessMode) {
      modeLabel = new Label("ENDLESS MODE", new Label.LabelStyle(new BitmapFont(),
          Color.WHITE));
      modeLabel.setFontScale(1.1f * (scaleX + scaleY) / 2);
      gameUITable.add(modeLabel).expandX().align(Align.topLeft);
    } else {
      modeLabel = new Label("SCENARIO MODE",
          new Label.LabelStyle(new BitmapFont(), Color.WHITE));
      modeLabel.setFontScale(1.1f * (scaleX + scaleY) / 2);
      gameUITable.add(modeLabel).expandX().align(Align.topLeft);
    }
    updateCustomerLabel();

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
    gameUITable.add(instructionButton).width(100 * scaleX).height(30 * scaleY).align(Align.center)
        .uniform();

    // The following block of code adds a listener to the instruction button
    instructionButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        Gdx.input.setInputProcessor(instructionsStage);
        isInstructionsVisible = true;
      }
    });

    // Creates an instruction stage an table which will be used to display the game instructions
    instructionsStage = new Stage();
    Image instructionImage = new Image(new Texture("Controls.png"));
    instructionImage.setSize(instructionsStage.getWidth(), instructionsStage.getHeight());
    instructionImage.setPosition(0, 0);
    Image iconsImage = new Image(new Texture("Icons.png"));
    iconsImage.setPosition(0, 0);
    iconsImage.setSize(instructionsStage.getWidth(), instructionsStage.getHeight());
    instructionsStage.addActor(instructionImage);
    Table instructionsTable = new Table();
    instructionsStage.addActor(instructionsTable);
    instructionsTable.setFillParent(true);
    instructionsTable.align(Align.top);

    // The following block of code creates the resume button and adds it to the instructions table
    TextureRegion resumeBtn = new TextureRegion(new Texture("ResumeUp.png"));
    TextureRegion resumeBtnDown = new TextureRegion(new Texture("ResumeDown.png"));
    Drawable drawableResumeBtn = new TextureRegionDrawable(resumeBtn);
    Drawable drawableResumeBtnDown = new TextureRegionDrawable(resumeBtnDown);
    Button.ButtonStyle instructionsResumeButtonStyle = new Button.ButtonStyle();
    instructionsResumeButton = new Button();
    instructionsResumeButton.setStyle(instructionsResumeButtonStyle);
    instructionsResumeButtonStyle.up = drawableResumeBtn;
    instructionsResumeButtonStyle.down = drawableResumeBtnDown;
    instructionsTable.add(instructionsResumeButton).width(150 * scaleX).height(30 * scaleY)
        .expandX().align(Align.topLeft).pad(10);
    instructionsTable.row();

    // The following block of code adds a click listener to the resume button
    instructionsResumeButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        Gdx.input.setInputProcessor(gameUIStage); // set the input processor to the game UI stage
        isInstructionsVisible = false;
        instructionsStage.clear();
        instructionsStage.addActor(instructionImage);
        instructionsStage.addActor(instructionsTable);
      }
    });

    TextureRegion iconsBtnUpTexture = new TextureRegion(new Texture("IconsUp.png"));
    TextureRegion iconsBtnDownTexture = new TextureRegion(new Texture("IconsDown.png"));
    Drawable iconsBtnUpDrawable = new TextureRegionDrawable(iconsBtnUpTexture);
    Drawable iconsBtnDownDrawable = new TextureRegionDrawable(iconsBtnDownTexture);
    Button.ButtonStyle iconsButtonStyle = new Button.ButtonStyle();
    Button iconsButton = new Button();
    iconsButton.setStyle(iconsButtonStyle);
    iconsButtonStyle.up = iconsBtnUpDrawable;
    iconsButtonStyle.down = iconsBtnDownDrawable;
    gameUITable.add(iconsButton).width(100 * scaleX).height(30 * scaleY).expandX()
        .align(Align.center).uniform();
    iconsButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        Gdx.input.setInputProcessor(instructionsStage);
        isInstructionsVisible = true;
        instructionsStage.clear();
        instructionsStage.addActor(iconsImage);
        instructionsStage.addActor(instructionsTable);
      }
    });

    // Creates the pause button
    TextureRegion pauseBtn = new TextureRegion(new Texture("PauseUp.png"));
    TextureRegion pauseBtnDown = new TextureRegion(new Texture("PauseDown.png"));
    TextureRegion powerUpMenuBtn = new TextureRegion(
        new Texture("PowerupAssets/PowerUpMenuButton.png"));
    Drawable pauseBtnDrawable = new TextureRegionDrawable(pauseBtn);
    Drawable pauseBtnDrawableDown = new TextureRegionDrawable(pauseBtnDown);
    Drawable powerUpButtonUp = new TextureRegionDrawable(powerUpMenuBtn);
    Button.ButtonStyle pauseButtonStyle = new Button.ButtonStyle();
    Button.ButtonStyle powerUpButtonStyle = new Button.ButtonStyle();
    Button pauseButton = new Button();
    Button powerUpButton = new Button();
    pauseButton.setStyle(pauseButtonStyle);
    powerUpButton.setStyle(powerUpButtonStyle);
    pauseButtonStyle.up = pauseBtnDrawable;
    pauseButtonStyle.down = pauseBtnDrawableDown;
    powerUpButtonStyle.up = powerUpButtonUp;
    powerUpButtonStyle.down = powerUpButtonUp;
    gameUITable.add(powerUpButton).width(80 * scaleX).height(40 * scaleY).expandX();
    gameUITable.add(pauseButton).width(48 * scaleX).height(52 * scaleY).expandX()
        .align(Align.topRight).row();
    pauseButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        paused = true;
        Gdx.input.setInputProcessor(pauseStage); // set the input processor to the pause stage
      }
    });
    powerUpButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        powerupPurchaseMenu.showPowerUpMenu();
      }
    });
    // Creates the button which is use to increase the number of chefs
    TextureRegion addChefBtn = new TextureRegion(new Texture("AddChef.png"));
    TextureRegion addChefBtnDown = new TextureRegion(new Texture("AddChefDown.png"));
    Drawable addChefBtnDrawable = new TextureRegionDrawable(addChefBtn);
    Drawable addChefBtnDrawableDown = new TextureRegionDrawable(addChefBtnDown);
    Button.ButtonStyle addChefButtonStyle = new Button.ButtonStyle();
    Button addChefButton = new Button();
    addChefButton.setStyle(addChefButtonStyle);
    addChefButtonStyle.up = addChefBtnDrawable;
    addChefButtonStyle.down = addChefBtnDrawableDown;
    gameUITable.add(addChefButton).width(48 * scaleX).padTop(10 * scaleY).height(52 * scaleY)
        .align(Align.right)
        .colspan(6).row();

    // Creates the label which displays an error message if the player tries to add a
    // chef when they do not have enough money
    // or if they already have the maximum number of chefs
    Label chefError = new Label("", new LabelStyle(new BitmapFont(), Color.WHITE));
    chefError.setWrap(true);
    chefError.setWidth(5);
    chefError.setAlignment(Align.center);
    chefError.setFontScale(0.75f * scaleX, 0.75f * scaleY);
    gameUITable.add(chefError).width(50 * scaleX).colspan(6).align(Align.right).row();

    addChefButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        if (customerController.getMoney() >= 50 && masterChef.getChefList().size() < 5) {
          masterChef.AddNewChefIn();
          customerController.changeMoney(-50);
          chefError.setText("");
        } else if (masterChef.getChefList().size() >= 5) {
          chefError.setText("You already have the maximum number of chefs");
        } else {
          chefError.setText("You do not have enough money to call in more chefs");
        }
      }
    });

    Image muteImage = new Image(new Texture("m_key.png"));
    muteImage.setSize(70, 48);
    gameUITable.add(muteImage).width(70 * scaleX).height(48 * scaleY).expandX().expandY().colspan(6)
        .align(Align.bottomRight);
  }

  /**
   * BlackCatStudio's code
   */
  private void updateCustomerLabel() {
    if (isEndlessMode) {
      modeLabel.setText("Customers served: " + customerController.getNumberOfCustomersServed());
    } else {
      modeLabel.setText(
          "Customers remaining: " + customerController.getRemainingNumberOfCustomers());
    }
  }

  /**
   * Creates the pause menu. BlackCatStudio's code
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
        paused = false; // when clicked, the game is no longer paused
        Gdx.input.setInputProcessor(gameUIStage); // set the input processor to the game UI stage
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
        saveGame();
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
   * End game sequence BlackCatStudio's code
   *
   * @param values the values
   */
  public void endGame(EndOfGameValues values) {

    SoundFrame.SoundEngine.stopAllSounds();
    if (!isEndlessMode) {
      endScreen = new EndScreen(game, this, timer, values, -1);
    } else {
      endScreen = new EndScreen(game, this, timer, values,
          customerController.getNumberOfCustomersServed());
    }
    gameMusic.stop();
    game.setScreen(endScreen);

  }


  /**
   * Plays the game music when the screen is shown. BlackCatStudio's code
   *
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
   * Displays the timer. Mixture of BlackCatStudios and Team Triple10s
   *
   * @author Amy Cross
   * @author Felix Seanor
   */
  public void displayTimer() {
    //Team Triple10s
    seconds += Gdx.graphics.getDeltaTime();
    if (seconds >= 1f) {
      timer++;
      seconds = 0f;
    }
    CharSequence str = "TIME: " + timer;
    timerFont.draw(game.batch, str, 380, 35);
    timerFont.getData().setScale(1.5f, 1.5f);
    timerLabel.setText(str);
    //BlackCatStudios
    CharSequence str2 = "Money: ¥" + customerController.money;
    timerFont.draw(game.batch, str2, 500, 35);
    timerFont.getData().setScale(1.5f, 1.5f);

    if (customerController.reputation != -1) {
      CharSequence str3 = "Reputation: " + customerController.reputation;
      timerFont.draw(game.batch, str3, 650, 35);
      timerFont.getData().setScale(1.5f, 1.5f);
    }

    CharSequence str3 = "Patience: ";
    timerFont.draw(game.batch, str3, 200, 25);

  }

  /**
   * Calls all logic updates and sprite draws as well as checks if game has been completed Mainly
   * BlackCatStudios code based on Team Triple10s design
   *
   * @param delta The time in seconds since the last render.
   * @author Felix Seanor
   * @author Amy Cross
   * @author Jack Hinton
   */
  @Override
  public void render(float delta) {
    //Team Triple10s
    //create world
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    //create camera
    camera.update();
    mapRenderer.setView(camera);
    mapRenderer.render();

    //Black Cat Studios

    /*if (Gdx.input.isKeyJustPressed(Keys.V)) {
      loadGame("SavedData.ser");
    }*/

    //Removed and simplified logic

    game.batch.setProjectionMatrix(camera.combined);
    game.batch.enableBlending();

    //Begins drawing the game batch
    game.batch.begin();
    // Mutes or plays the music
    if (Gdx.input.isKeyJustPressed((Input.Keys.M))) {
      if (gameMusic.isPlaying()) {
        SoundFrame.SoundEngine.muteSound();
        gameMusic.stop();
      } else {
        SoundFrame.SoundEngine.unmuteSound();
        gameMusic.play();
      }
    }

    if (!paused && !isInstructionsVisible) {

      displayTimer();
      //Update Scripts
      GameObjectManager.objManager.doUpdate(Gdx.graphics.getDeltaTime());
      updateCustomerLabel();
      //New rendering system
      RenderManager.renderer.onRender(game.batch);
    /*  if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
        powerup.doSpeedPowerup();
      }
      if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
        powerup.buyReputation();
      }
      if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
        powerup.superFood();
      }
      if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
        powerup.tetrisSuperFoodGive();
      }*/
    } else if (paused && !isInstructionsVisible) {
      pauseStage.draw();
    } else {
      instructionsStage.draw();
    }
    game.batch.end();

    // The following code must occur after the batch is ended.
    // Otherwise, it causes issues with customer positioning.
    if (!paused && !isInstructionsVisible) { // displays the game UI if the game is not paused
      gameUIStage.draw();
    }
  }

  /**
   * Save the game. BlackCatStudios Code
   *
   * @author Felix Seanor
   */
  public void saveGame() {
    SaveState saving = new SaveState();
    saving.saveState("SavedData.ser", masterChef, customerController, difficulty, timer, seconds,
        constructMachines.stations, constructMachines.customerCounters,
        constructMachines.assemblyStations);

  }

  /**
   * Load the game from save BlackCatStudios Code
   *
   * @param path the path
   * @author Felix Seanor
   */
  public void loadGame(String path) {
    SaveState saving = new SaveState();

    GameState state = saving.loadState(path);

    loadGame(state);

  }

  public GameState loadGameState(String path){
    SaveState saving = new SaveState();

    return saving.loadState(path);
  }

  public void loadGame(GameState state) {

    loadState(state);
    masterChef.LoadState(state);
    customerController.loadState(state);

  }


  /**
   * Loads the state of a previous state of the world, all LoadGame to a full sweep. BlackCatStudios
   * Code
   *
   * @param state the state
   * @author Felix Seanor
   */
  public void loadState(GameState state) {

    int i = 0;
    timer = state.timer;
    seconds = state.seconds;
    difficulty = state.difficulty;
    for (GameObject station : constructMachines.stations) {
      Scriptable scriptable = station.getScript(0);
      if (scriptable instanceof Station) {
        ((Station) scriptable).loadState(state.foodOnCounters.get(i), state.repairState.get(i++));
      }


    }

    for (GameObject station : constructMachines.customerCounters) {
      ((Station) station.getScript(0)).loadState(state.foodOnCounters.get(i++), true);
    }

    for (GameObject station : constructMachines.assemblyStations) {
      ((Station) station.getScript(0)).loadState(state.foodOnCounters.get(i++), true);
    }
  }


  /**
   * Gets difficulty.
   *
   * @return the difficulty
   */
  public Difficulty getDifficulty() {
    return difficulty;
  }

  /**
   * Gets timer.
   *
   * @return the timer
   */
  public int getTimer() {
    return timer;
  }

  /**
   * Gets seconds.
   *
   * @return the seconds
   */
  public float getSeconds() {
    return seconds;
  }

  /**
   * Gets stations.
   *
   * @return the stations
   */
  public List<GameObject> getStations() {
    return constructMachines.stations;
  }

  /**
   * Gets customer counters.
   *
   * @return the customer counters
   */
  public List<GameObject> getCustomerCounters() {
    return constructMachines.customerCounters;
  }

  /**
   * Gets assembly stations.
   *
   * @return the assembly stations
   */
  public List<GameObject> getAssemblyStations() {
    return constructMachines.assemblyStations;
  }


  //  /**
//   * Resizes the stage when the window is resized so that the buttons are in the correct place.
//   * Parameters inherited from interface com.badlogic.gdx.Screen and not explicitly used.
  // Team Triple 10s
//   */
  @Override
  public void resize(int width, int height) {
    pauseStage.getViewport().update(width, height, true);
    gameUIStage.getViewport().update(width, height, true);
    instructionsStage.getViewport().update(width, height, true);
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
   *
   * @author Felix Seanor
   */
  @Override
  public void dispose() {
    game.batch.dispose();
  }

}
