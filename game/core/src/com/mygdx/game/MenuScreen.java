package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Core.GameObjectManager;
import com.mygdx.game.Core.GameState.Difficulty;
import com.mygdx.game.Core.RenderManager;

import java.io.IOException;

/**
 * Implements the screen that's displayed for the start of the game
 *
 * @author Kelvin Chen
 * @author Amy Cross
 * @author Felix Seanor
 * @author Amy Cross
 * @author Jack Vickers
 */
public class MenuScreen implements Screen {

  GameScreen gameScreen;
  GameScreen VictoryScreen;
  TextureAtlas mainMenuAtlas;
  final MyGdxGame root;

  private final TextureRegion playbtnUp;
  private final TextureRegion playbtnDown;
  private final TextureRegion scenariobtn;
  private final TextureRegion scenariobtnDown;
  private final TextureRegion exitbtn;
  private final TextureRegion exitbtnDown;
  private final Stage stage;
  private final Table table;
  float scaleX;
  float scaleY;
  Button loadButton;
  Button playBtn;
  Button scenarioBtn;
  Button exitBtn;
  Button highScoresBtn;

  /**
   * constructs the screen including the position of the buttons and their hitboxes;
   *
   * @param root The base object
   *
   * @author Amy Cross
   * @author Felix Seanor
   * @author Jack Vickers
   */
  public MenuScreen(final MyGdxGame root) {

    if (GameObjectManager.objManager == null) {
      new GameObjectManager();
    } else {
      GameObjectManager.objManager.reset();
    }

    if (RenderManager.renderer == null) {
      new RenderManager();
    }

    this.root = root;

    mainMenuAtlas = new TextureAtlas(Gdx.files.internal("mainMenu.atlas"));

    playbtnUp = new TextureRegion(new TextureRegion(new Texture("EndlessGameUp.png")));
    playbtnDown = new TextureRegion(new TextureRegion(new Texture("EndlessGameDown.png")));
    scenariobtn = new TextureRegion(mainMenuAtlas.findRegion("scenarioButton"));
    scenariobtnDown = new TextureRegion(mainMenuAtlas.findRegion("scenarioButtonDown"));
    exitbtn = new TextureRegion(mainMenuAtlas.findRegion("exitButton"));
    exitbtnDown = new TextureRegion(mainMenuAtlas.findRegion("exitButtonDown"));
    TextureRegion highScoresBtnUp = new TextureRegion(new Texture("HighScoresUp.png"));
    TextureRegion highScoresBtnDown = new TextureRegion(new Texture("HighScoresDown.png"));

    stage = new Stage();

    // Calculates the scale of the screen to the original size of the game
    scaleX = Gdx.graphics.getWidth() / 640f;
    scaleY = Gdx.graphics.getHeight() / 480f;
    Gdx.input.setInputProcessor(stage);

    table = new Table();
    table.setFillParent(true);
    table.align(Align.center);
    stage.addActor(table);

    // Only creates and adds the load button if there is a save file
    if (Gdx.files.internal("SavedData.ser").exists()) {
      TextureRegion loadbtn = new TextureRegion(new Texture("LoadUp.png"));
      TextureRegion loadbtnDown = new TextureRegion(new Texture("LoadDown.png"));
      Drawable drawableLoadbtnUp = new TextureRegionDrawable(loadbtn);
      Drawable drawableLoadbtnDown = new TextureRegionDrawable(loadbtnDown);
      Button.ButtonStyle loadbtnStyle = new Button.ButtonStyle();
      loadButton = new Button();
      loadButton.setStyle(loadbtnStyle);
      loadbtnStyle.up = drawableLoadbtnUp;
      loadbtnStyle.down = drawableLoadbtnDown;
      table.add(loadButton).width(210 * scaleX).height(35 * scaleY).padTop(90 * scaleY).row();

      // Adds a click listener to the load button
      loadButton.addListener(
          new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { // if clicked, load the game
              gameScreen = new GameScreen(root, root.map, -1, Difficulty.Relaxing, true);
              root.setScreen(gameScreen);
              dispose();
            }
          });
    }

    Drawable drawablePlaybtnUp = new TextureRegionDrawable(new TextureRegion(playbtnUp));
    Drawable drawablePlaybtnDown = new TextureRegionDrawable(new TextureRegion(playbtnDown));
    Drawable drawableScenariobtnUp = new TextureRegionDrawable(new TextureRegion(scenariobtn));
    Drawable drawableScenariobtnDown = new TextureRegionDrawable(
        new TextureRegion(scenariobtnDown));
    Drawable drawableExitbtnUp = new TextureRegionDrawable(new TextureRegion(exitbtn));
    Drawable drawableExitbtnDown = new TextureRegionDrawable(new TextureRegion(exitbtnDown));
    Drawable drawableHighScoresBtnUp = new TextureRegionDrawable(new TextureRegion(highScoresBtnUp));
    Drawable drawableHighScoresBtnDown = new TextureRegionDrawable(new TextureRegion(highScoresBtnDown));

    Button.ButtonStyle playbtnStyle = new Button.ButtonStyle();
    playBtn = new Button();
    playBtn.setStyle(playbtnStyle);
    playbtnStyle.up = drawablePlaybtnUp;
    playbtnStyle.down = drawablePlaybtnDown;
    if (Gdx.files.internal("SavedData.ser").exists()) {
      table.add(playBtn).width(210 * scaleX).height(35 * scaleY).padTop(25 * scaleY).padBottom(25 * scaleY).row();
    } else {
      table.add(playBtn).width(210 * scaleX).height(35 * scaleY).padTop(75 * scaleY)
          .padBottom(25 * scaleY);
      table.row();
    }

    scenarioBtn = new Button();
    Button.ButtonStyle scenariobtnStyle = new Button.ButtonStyle();
    scenarioBtn.setStyle(scenariobtnStyle);
    scenariobtnStyle.up = drawableScenariobtnUp;
    scenariobtnStyle.down = drawableScenariobtnDown;
    table.add(scenarioBtn).width(210 * scaleX).height(35 * scaleY).padBottom(25 * scaleY);
    table.row();

    Button.ButtonStyle highScoresBtnStyle = new Button.ButtonStyle();
    highScoresBtn = new Button();
    highScoresBtn.setStyle(highScoresBtnStyle);
    highScoresBtnStyle.up = drawableHighScoresBtnUp;
    highScoresBtnStyle.down = drawableHighScoresBtnDown;
    table.add(highScoresBtn).width(210 * scaleX).height(35 * scaleY).padBottom(25 * scaleY);
    table.row();

    Button.ButtonStyle exitbtnStyle = new Button.ButtonStyle();
    exitBtn = new Button();
    exitBtn.setStyle(exitbtnStyle);
    exitbtnStyle.up = drawableExitbtnUp;
    exitbtnStyle.down = drawableExitbtnDown;
    table.add(exitBtn).width(210 * scaleX).height(35 * scaleY);




    table.setBackground(new TextureRegionDrawable(mainMenuAtlas.findRegion("menuPP")));

    ChangeListener playbtnMouseListener = new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        createDifficultyButtons();
      }
    };
    playBtn.addListener(playbtnMouseListener);

    scenarioBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        ScenarioModeConfigScreen scenarioConfigScreen = new ScenarioModeConfigScreen(root);
        root.setScreen(scenarioConfigScreen);
        dispose();
      }
    });

    highScoresBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y){
        LeaderboardScreen leaderboardScreen = null;
        try {
          leaderboardScreen = new LeaderboardScreen(root, null, -1);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        root.setScreen(leaderboardScreen);
        dispose();
      }
    });

    ChangeListener exitbtnMouseListener = new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        Gdx.app.exit();
        dispose();
      }
    };
    exitBtn.addListener(exitbtnMouseListener);
  }

  private void createDifficultyButtons() {
    float initialTopPadding = 75;
    // Need to clear the table so that the difficulty buttons can be added
    // after the load button
    table.clearChildren();
    if (Gdx.files.internal("SavedData.ser").exists()) {
      table.add(loadButton).width(210 * scaleX).height(35 * scaleY).padTop(90 * scaleY)
          .colspan(3).row();
      initialTopPadding = 25;
    }

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
    table.add(easyBtn).width(100 * scaleX).height(40 * scaleY).padTop(initialTopPadding * scaleY).padBottom(25 * scaleY)
        .padRight(10 * scaleX);

    // Adds a click listener to the easy button
    easyBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        gameScreen = new GameScreen(root, root.map, -1, Difficulty.Relaxing, false);
        root.setScreen(gameScreen);
        dispose();
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
    table.add(mediumBtn).width(100 * scaleX).height(40 * scaleY).padTop(initialTopPadding * scaleY).padBottom(25 * scaleY)
        .padRight(10 * scaleX);

    // Adds a click listener to the medium button
    mediumBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        gameScreen = new GameScreen(root, root.map, -1, Difficulty.Stressful, false);
        root.setScreen(gameScreen);
        dispose();
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
    table.add(hardBtn).width(100 * scaleX).height(40 * scaleY).padTop(initialTopPadding * scaleY).padBottom(25 * scaleY).row();

    // Adds a click listener to the hard button
    hardBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        gameScreen = new GameScreen(root, root.map, -1, Difficulty.Mindbreaking, false);
        root.setScreen(gameScreen);
        dispose();
      }
    });

    table.add(scenarioBtn).width(210 * scaleX).height(35 * scaleY).padBottom(25 * scaleY).colspan(3)
        .row();
    table.add(highScoresBtn).width(210 * scaleX).height(35 * scaleY).padBottom(25 * scaleY)
        .colspan(3).row();
    table.add(exitBtn).width(210 * scaleX).height(35 * scaleY).colspan(3).row();
  }


  @Override
  public void show() {

  }

  /**
   * Renders the screen and checks for interactions
   *
   * @param delta The time in seconds since the last render.
   * @author Amy Cross
   */
  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    stage.act(Gdx.graphics.getDeltaTime());
    stage.draw();
  }

  /**
   * Sets the camera for the screen
   *
   * @param width  the width of the camera
   * @param height the height of the camera
   * @author Amy Cross
   */
  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);

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
   * Gets rid of excess sprites
   */
  @Override
  public void dispose() {
    stage.dispose();
  }


}
