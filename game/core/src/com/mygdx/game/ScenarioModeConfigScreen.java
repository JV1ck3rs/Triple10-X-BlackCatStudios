package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Core.GameState.Difficulty;

/**
 * Implements the screen that the allows the user to configure the number of customers for the
 * scenario mode. BlackCatStudios
 *
 * @author Jack Vickers
 * @date 04 /04/2023
 */
public class ScenarioModeConfigScreen implements Screen {

  /**
   * The Game.
   */
  final MyGdxGame game;
  /**
   * The Game screen.
   */
  GameScreen gameScreen;
  /**
   * The Scenario config texture.
   */
  Texture scenarioConfigTexture;
  /**
   * The Stage.
   */
  Stage stage;
  /**
   * The Table.
   */
  Table table;
  /**
   * The Text field.
   */
  TextField textField;
  /**
   * The Scenario config atlas.
   */
  TextureAtlas scenarioConfigAtlas;
  /**
   * The Error message.
   */
  Label errorMessage;
  /**
   * The Hard btn.
   */
  Button hardBtn;

  /**
   * Constructor for the scenario mode config screen.
   *
   * @param game The MyGdxGame object
   * @author Jack Vickers
   * @date 04 /04/2023
   */
  public ScenarioModeConfigScreen(MyGdxGame game) {
    float scaleX;
    float scaleY;

    // Calculates the scale of the screen to the original size of the game
    scaleX = Gdx.graphics.getWidth() / 640f;
    scaleY = Gdx.graphics.getHeight() / 480f;
    this.game = game;

    // Load the scenario mode config screen assets
    scenarioConfigAtlas = new TextureAtlas(Gdx.files.internal("mainMenu.atlas"));
    scenarioConfigTexture = new Texture(Gdx.files.internal("mainMenu.png"));
    Image image = new Image(scenarioConfigTexture);

    // Create the stage and add the background image
    stage = new Stage();
    image.setSize(stage.getWidth(), stage.getHeight());
    image.setPosition(0, 0);
    stage.addActor(image);
    Gdx.input.setInputProcessor(stage); // Make the stage consume events
    table = new Table(); // Create a table that fills the screen. Everything will go inside this table.
    stage.addActor(table);
    table.setFillParent(true);
    table.align(Align.center);

    // Creates the title and instructions for the scenario mode config screen
    Label title = new Label("Scenario Mode Options", new LabelStyle(new BitmapFont(), Color.WHITE));
    title.setFontScale(1.50f * (scaleX + scaleY) / 2);
    table.add(title).padBottom(30 * scaleY).padTop(70 * scaleY)
        .colspan(3); // Adds the title to the table
    table.row(); // Adds a new row to the table
    Label instructions = new Label(
        "Enter the number of customers for the scenario mode: \n The maximum number of customers is 100",
        new LabelStyle(new BitmapFont(), Color.WHITE));
    instructions.setFontScale(1.10f * (scaleX + scaleY) / 2);
    instructions.setAlignment(Align.center);
    table.add(instructions).padBottom(20 * scaleY).colspan(3);
    table.row();

    // Sets the background using a section of the background image used on the main menu screen
    table.setBackground(new TextureRegionDrawable(scenarioConfigAtlas.findRegion("menuPP")));

    // Creates a skin for the text field using the clean-crispy-ui.json file
    Skin skin = new Skin(Gdx.files.internal("clean-crispy-ui.json"));
    textField = new TextField("5", skin);
    textField.getStyle().font.getData().setScale(1.50f * (scaleX + scaleY) / 2);
    textField.setAlignment(Align.center);
    stage.addActor(textField); // Adds the text field to the stage
    table.add(textField).width(250 * scaleX).height(50 * scaleY)
        .colspan(3); // Adds the text field to the table
    table.row();

    // Creates the error message label which will be used to tell
    // the user if they have entered an invalid number of customers.
    errorMessage = new Label("",
        new LabelStyle(new BitmapFont(), Color.WHITE));
    errorMessage.setFontScale(1.10f * (scaleX + scaleY) / 2);
    errorMessage.setAlignment(Align.left);
    table.add(errorMessage).padBottom(20 * scaleY).colspan(3);
    table.row();

    // Create the easy (relaxing mode) button and add it to the table
    TextureRegion easyBtnTexture = new TextureRegion(new Texture("RelaxingUp.png"));
    TextureRegion easyBtnDownTexture = new TextureRegion(new Texture("RelaxingDown.png"));
    Drawable drawableEasyBtnUp = new TextureRegionDrawable(easyBtnTexture);
    Drawable drawableEasyBtnDown = new TextureRegionDrawable(easyBtnDownTexture);
    Button.ButtonStyle easyBtnStyle = new Button.ButtonStyle();
    Button easyBtn = new Button();
    easyBtn.setStyle(easyBtnStyle);
    easyBtnStyle.up = drawableEasyBtnUp;
    easyBtnStyle.down = drawableEasyBtnDown;
    easyBtn.align(Align.left);
    table.add(easyBtn).width(125 * scaleX).height(50 * scaleY).padBottom(25 * scaleY)
        .padRight(10 * scaleY);

    // Adds a click listener to the easy button
    easyBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        checkInput(Difficulty.Relaxing);
      }
    });

    // Create the medium (stressful mode) button and add it to the table
    TextureRegion mediumBtnTexture = new TextureRegion(new Texture("StressfulUp.png"));
    TextureRegion mediumBtnDownTexture = new TextureRegion(new Texture("StressfulDown.png"));
    Drawable drawableMediumBtnUp = new TextureRegionDrawable(mediumBtnTexture);
    Drawable drawableMediumBtnDown = new TextureRegionDrawable(mediumBtnDownTexture);
    Button.ButtonStyle mediumBtnStyle = new Button.ButtonStyle();
    Button mediumBtn = new Button();
    mediumBtn.setStyle(mediumBtnStyle);
    mediumBtnStyle.up = drawableMediumBtnUp;
    mediumBtnStyle.down = drawableMediumBtnDown;
    mediumBtn.align(Align.center);
    // The button is added to the same row of the table as the easy button so that they
    // are side by side
    table.add(mediumBtn).width(125 * scaleX).height(50 * scaleY).padBottom(25 * scaleY)
        .padRight(10 * scaleY);

    // Adds a click listener to the medium button
    mediumBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        checkInput(Difficulty.Stressful);
      }
    });

    // Create the hard (extreme mode) button and add it to the table
    TextureRegion hardBtnTexture = new TextureRegion(new Texture("ExtremeUp.png"));
    TextureRegion hardBtnDownTexture = new TextureRegion(new Texture("ExtremeDown.png"));
    Drawable drawableHardBtnUp = new TextureRegionDrawable(hardBtnTexture);
    Drawable drawableHardBtnDown = new TextureRegionDrawable(hardBtnDownTexture);
    Button.ButtonStyle hardBtnStyle = new Button.ButtonStyle();
    hardBtn = new Button();
    hardBtn.setStyle(hardBtnStyle);
    hardBtnStyle.up = drawableHardBtnUp;
    hardBtnStyle.down = drawableHardBtnDown;
    hardBtn.align(Align.right);
    // The button is added to the same row of the table as the easy & medium buttons so that they
    // are side by side
    table.add(hardBtn).width(125 * scaleX).height(50 * scaleY).padBottom(25 * scaleY).row();

    // Adds a click listener to the hard button
    hardBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        checkInput(Difficulty.Mindbreaking);
      }
    });
  }

  /**
   * Gets hard btn.
   *
   * @return the hard btn
   */
  public Button getHardBtn() {
    return hardBtn;
  }

  /**
   * Checks the user text input and sets the number of customers in the game.
   *
   * @param difficultyLevel The difficulty level of the game.
   * @author Jack Vickers
   * @date 19 /04/2023
   */
  public void checkInput(Difficulty difficultyLevel) {
    int numCustomers = 5;
    if (InputChecker.checkCustomerNumberInput(textField.getText())) {
      numCustomers = Integer.parseInt(textField.getText());
      gameScreen = new GameScreen(game, game.map, numCustomers, difficultyLevel, false);
      game.setScreen(gameScreen);
    } else {
      errorMessage.setText("Please enter a positive number which is less than 100");
    }
  }

  /**
   * Called when this screen becomes the current screen.
   */
  @Override
  public void show() {

  }

  /**
   * Called when the screen should render itself.
   *
   * @param delta The time in seconds since the last render.
   */
  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    stage.act(Gdx.graphics.getDeltaTime());
    stage.draw();
    game.batch.begin();
    game.batch.end();
  }

  /**
   * Resize the screen.
   *
   * @param width  the new width
   * @param height the new height
   */
  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
  }

  /**
   *
   */
  @Override
  public void pause() {

  }

  /**
   *
   */
  @Override
  public void resume() {

  }

  /**
   * Called when this screen is no longer the current screen.
   */
  @Override
  public void hide() {

  }

  /**
   * Called when this screen should release all resources.
   */
  @Override
  public void dispose() {

  }
}
