package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.Core.Leaderboard.LeaderboardScreen;
import com.mygdx.game.Core.Rendering.BlackTexture;
import com.mygdx.game.Core.ValueStructures.EndOfGameValues;
import java.io.IOException;

/**
 * This class creates and displays the victory screen Black cat studios and a bit of Team Triple 10s
 * Most of the Triple10s code has been heavily modified and refactors + additional code
 *
 * @author Robin Graham
 * @author Felix Seanor
 * @author Jack Vickers
 * @date 30 /04/23
 */
public class EndScreen implements Screen {

  /**
   * The Root.
   */
  final MyGdxGame root;
  /**
   * The Game screen.
   */
  GameScreen gameScreen;

  /**
   * The Victory screen.
   */
  Texture victoryScreen;
  /**
   * The Stage.
   */
  Stage stage;
  /**
   * The Timer.
   */
  int timer;
  private final BitmapFont timerFont;
  private final Label timerLabel;
  //  private final Label VictoryOrLoss;
  private final Table table;
  /**
   * The Scale x.
   */
  float scaleX;
  /**
   * The Scale y.
   */
  float scaleY;


  /**
   * Assigns all the necessary variables needed for the victory screen and other objects such as the
   * image used BlackCat studios and Team Triple 10s
   *
   * @param root                    the base object to interact with
   * @param gameScreen              the game screen
   * @param time                    the integer time value set for timer
   * @param values                  the values
   * @param numberOfCustomersServed the number of customers served
   */
  public EndScreen(final MyGdxGame root, GameScreen gameScreen, int time, EndOfGameValues values,
      int numberOfCustomersServed) {

    //Triple 10s
    this.root = root;

    //this might cause issues if so, change back to new GameScreen
    this.gameScreen = gameScreen;

    //Black cat studios
    if (values.won) {
      victoryScreen = new Texture(Gdx.files.internal("SuccessBG.png"));
    } else {
      victoryScreen = new Texture(Gdx.files.internal("FailBG.png"));
    }
    Image image = new Image(victoryScreen);
    // Calculates the scale of the screen to the original size of the game
    scaleX = Gdx.graphics.getWidth() / 640f;
    scaleY = Gdx.graphics.getHeight() / 480f;
    stage = new Stage();
    image.setSize(stage.getWidth(), stage.getHeight());
    image.setPosition(0, 0);
    stage.addActor(image);
    this.timer = time;
    Gdx.input.setInputProcessor(stage);
    timerFont = new BitmapFont();
    timerLabel = new Label("TIME: " + timer,
        new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    String VL = (values.won) ? "won!" : "lost.";

    BlackTexture exitButtonUpTexture = new BlackTexture("ExitUp.png");
    BlackTexture exitButtonDownTexture = new BlackTexture("ExitDown.png");

    table = new Table();
    table.setFillParent(true);
    stage.addActor(table);

    if (numberOfCustomersServed >= 0) {
      Label customersServedLabel = new Label("Customers Served: " + numberOfCustomersServed,
          new Label.LabelStyle(new BitmapFont(), Color.WHITE));
      customersServedLabel.setFontScale(1.5f * scaleX, 1.5f * scaleY);
      table.add(customersServedLabel).pad(50 * scaleY, 25, 25, 25).row();
    }

    Drawable drawableScenariobtnUp = new TextureRegionDrawable(exitButtonUpTexture.textureRegion);
    Drawable drawableScenariobtnDown = new TextureRegionDrawable(
        exitButtonDownTexture.textureRegion);

    Button scenariobtn = new Button();
    Button.ButtonStyle scenariobtnStyle = new Button.ButtonStyle();
    scenariobtn.setStyle(scenariobtnStyle);
    scenariobtnStyle.up = drawableScenariobtnUp;
    scenariobtnStyle.down = drawableScenariobtnDown;

    table.add(scenariobtn).width(250 * scaleX).height(50 * scaleY).pad(200 * scaleY, 25, 25, 25);
    table.row();

    ChangeListener playbtnMouseListener = new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
//        gameScreen.gameMusic.stop();
        try {
          if (numberOfCustomersServed < 0) {
            root.setScreen(new MenuScreen(root));

          } else {
            root.setScreen(new LeaderboardScreen(root, values, numberOfCustomersServed));
          }
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        dispose();
      }
    };
    scenariobtn.addListener(playbtnMouseListener);


  }

  /**
   * Team Triple 10s
   */
  @Override
  public void show() {

  }

  /**
   * Displays the timer onto the screen with the set time defined Team Triple 10s
   */
  public void displayTimer() {
    CharSequence str = "Final Time: " + timer + "s";
    timerFont.draw(root.batch, str, 400, 300);
    timerFont.getData().setScale(3f, 3f);
    timerLabel.setText(str);
  }


  /**
   * Renders the stage and assets Team Triple 10s
   *
   * @param delta used for working with time
   */
  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    stage.act(Gdx.graphics.getDeltaTime());
    stage.draw();
    root.batch.begin();
    displayTimer();
    root.batch.end();
  }

  /**
   * Resize the window Team Triple 10s
   *
   * @param width  of the window
   * @param height of the window
   */
  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
  }

  /**
   * Team Triple 10s
   */
  @Override
  public void pause() {

  }

  /**
   * Team Triple 10s
   */
  @Override
  public void resume() {

  }

  /**
   * Team Triple 10s
   */
  @Override
  public void hide() {

  }

  /**
   * Team Triple 10s
   */
  @Override
  public void dispose() {
    stage.dispose();
  }
}
