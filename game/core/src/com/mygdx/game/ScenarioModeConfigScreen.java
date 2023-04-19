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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Core.GameState.Difficulty;

/**
 * Implements the screen that the allows the user to configure the number of customers for the
 * scenario mode.
 *
 * @author Jack Vickers
 * @date 04/04/2023
 */
public class ScenarioModeConfigScreen implements Screen {

  final MyGdxGame game;
  GameScreen gameScreen;
  Texture scenarioConfigTexture;
  Stage stage;
  Table table;
  TextField textField;
  TextureAtlas scenarioConfigAtlas;
  Label errorMessage;

  /**
   * Constructor for the scenario mode config screen
   *
   * @param game The MyGdxGame object
   * @author Jack Vickers
   * @date 04/04/2023
   */

  public ScenarioModeConfigScreen(MyGdxGame game) {
    float scale = 1.0f;
    this.game = game;

    // Load the scenario mode config screen assets
    scenarioConfigAtlas = new TextureAtlas(Gdx.files.internal("mainMenu.atlas"));
    scenarioConfigTexture = new Texture(Gdx.files.internal("mainMenu.png"));
    Image image = new Image(scenarioConfigTexture);

    // Create the stage and add the background image
    stage = new Stage();
    if (stage.getViewport().getScreenWidth() > 720) {
      scale = 0.5f * ((stage.getViewport().getScreenWidth() / 720f) + (
          stage.getViewport().getScreenHeight() / 1280f));
    }
    image.setSize(stage.getWidth(), stage.getHeight());
    image.setPosition(0, 0);
    stage.addActor(image);
    Gdx.input.setInputProcessor(stage); // Make the stage consume events
    table = new Table(); // Create a table that fills the screen. Everything will go inside this table.
    stage.addActor(table);
    table.setFillParent(true);
    table.align(Align.center);

    // Creates a skin for the text field using the clean-crispy-ui.json file
    Skin skin = new Skin(Gdx.files.internal("clean-crispy-ui.json"));

    // Creates the title and instructions for the scenario mode config screen
    Label title = new Label("Scenario Mode Options", new LabelStyle(new BitmapFont(), Color.WHITE));
    title.setFontScale(1.50f * scale);
    table.add(title).padBottom(30 * scale).padTop(70 * scale)
        .colspan(3); // Adds the title to the table
    table.row(); // Adds a new row to the table
    Label instructions = new Label(
        "Enter the number of customers for the scenario mode: \n The maximum number of customers is 100",
        new LabelStyle(new BitmapFont(), Color.WHITE));
    instructions.setFontScale(1.10f * scale);
    instructions.setAlignment(Align.center);
    table.add(instructions).padBottom(20 * scale).colspan(3);
    table.row();

    // Sets the background using a section of the background image used on the main menu screen
    table.setBackground(new TextureRegionDrawable(scenarioConfigAtlas.findRegion("menuPP")));
    textField = new TextField("5", skin);
    textField.getStyle().font.getData().setScale(1.50f * scale);
    textField.setAlignment(Align.center);
    stage.addActor(textField); // Adds the text field to the stage
    table.add(textField).width(250 * scale).height(50 * scale)
        .colspan(3); // Adds the text field to the table
    table.row();

    // Creates the error message label which will be used to tell
    // the user if they have entered an invalid number of customers.
    errorMessage = new Label("",
        new LabelStyle(new BitmapFont(), Color.WHITE));
    errorMessage.setFontScale(1.10f * scale);
    errorMessage.setAlignment(Align.left);
    table.add(errorMessage).padBottom(20 * scale).colspan(3);
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
    table.add(easyBtn).width(125 * scale).height(50 * scale).padBottom(25 * scale)
        .padRight(10 * scale);

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
    table.add(mediumBtn).width(125 * scale).height(50 * scale).padBottom(25 * scale)
        .padRight(10 * scale);

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
    Button hardBtn = new Button();
    hardBtn.setStyle(hardBtnStyle);
    hardBtnStyle.up = drawableHardBtnUp;
    hardBtnStyle.down = drawableHardBtnDown;
    hardBtn.align(Align.right);
    // The button is added to the same row of the table as the easy & medium buttons so that they
    // are side by side
    table.add(hardBtn).width(125 * scale).height(50 * scale).padBottom(25 * scale).row();

    // Adds a click listener to the hard button
    hardBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        checkInput(Difficulty.Mindbreaking);
      }
    });
  }

  /**
   * Checks the user text input and sets the number of customers in the game.
   *
   * @param difficultyLevel The difficulty level of the game.
   * @author Jack Vickers
   * @date 19/04/2023
   */
  private void checkInput(Difficulty difficultyLevel) {
    int numCustomers = 5;
    if (textField.getText().length() > 3) { // Checks if the user has entered a number out of the
      // integer range
      errorMessage.setText("Please enter a number which is less than 100");
    } else if (textField.getText().matches("[0-9]+")) {
      if (Integer.parseInt(textField.getText()) > 100) {
        errorMessage.setText("The maximum number of customers is 100. Please enter a lower number");
      } else if (Integer.parseInt(textField.getText()) <= 0) {
        errorMessage.setText("Please enter a number which is greater than 0");
      } else {
        numCustomers = Integer.parseInt(textField.getText());
        gameScreen = new GameScreen(game, numCustomers, false, difficultyLevel);
        game.setScreen(gameScreen);
      }
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
   * @see ApplicationListener#pause()
   */
  @Override
  public void pause() {

  }

  /**
   * @see ApplicationListener#resume()
   */
  @Override
  public void resume() {

  }

  /**
   * Called when this screen is no longer the current screen for a {@link Game}.
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
