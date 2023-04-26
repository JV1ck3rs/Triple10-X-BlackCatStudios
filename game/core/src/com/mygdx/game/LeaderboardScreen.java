package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Core.ValueStructures.EndOfGameValues;

/**
 * This class creates and displays the leaderboard screen.
 *
 * @author Azzam Amirul Bahri
 * @author Jack Vickers
 *
 */
public class LeaderboardScreen implements Screen {

  final MyGdxGame game;
  float scaleX;
  float scaleY;
  Stage stage;

  public LeaderboardScreen(MyGdxGame game, EndOfGameValues values, int numberOfCustomersServed) {
    // Calculates the scale of the screen to the original size of the game
    scaleX = Gdx.graphics.getWidth() / 640f;
    scaleY = Gdx.graphics.getHeight() / 480f;
    this.game = game;

    Texture leaderboardScreenTexture = new Texture(Gdx.files.internal("LeaderBoardBG.png"));
    Image image = new Image(leaderboardScreenTexture);

    // Create the stage and add the background image
    stage = new Stage();
    image.setSize(stage.getWidth(), stage.getHeight());
    image.setPosition(0, 0);
    stage.addActor(image);
    Gdx.input.setInputProcessor(stage); // Make the stage consume events
    Table table = new Table(); // Create a table that fills the screen. Everything will go inside this table.
    stage.addActor(table);
    table.setFillParent(true);
    table.align(Align.center);

    Label score1 = new Label("1. " + "", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    score1.setFontScale((scaleX + scaleY) / 2);
    table.add(score1).align(Align.left).row();
    Label score2 = new Label("2. " + "", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    score2.setFontScale((scaleX + scaleY) / 2);
    table.add(score2).align(Align.left).row();
    Label score3 = new Label("3. " + "", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    score3.setFontScale((scaleX + scaleY) / 2);
    table.add(score3).align(Align.left).row();
    Label score4 = new Label("4. " + "", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    score4.setFontScale((scaleX + scaleY) / 2);
    table.add(score4).align(Align.left).row();
    Label score5 = new Label("5. " + "", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    score5.setFontScale((scaleX + scaleY) / 2);
    table.add(score5).align(Align.left).row();
    if (numberOfCustomersServed >= 0) {
      // Creates a skin for the text field using the clean-crispy-ui.json file
      Skin skin = new Skin(Gdx.files.internal("clean-crispy-ui.json"));

      TextField textField = new TextField(" ", skin);
      textField.getStyle().font.getData().setScale(1.50f * (scaleX + scaleY) / 2);
      textField.setAlignment(Align.center);
      stage.addActor(textField); // Adds the text field to the stage
      table.add(textField).width(250 * scaleX)
          .height(50 * scaleY); // Adds the text field to the table
      table.row();

      TextureRegion saveScoreTexture = new TextureRegion(new Texture("SaveExitUp.png"));
      TextureRegion saveScoreDownTexture = new TextureRegion(new Texture("SaveExitDown.png"));
      Drawable drawableScoreBtnUp = new TextureRegionDrawable(saveScoreTexture);
      Drawable drawableScoreBtnDown = new TextureRegionDrawable(saveScoreDownTexture);
      Button.ButtonStyle scoreBtnStyle = new Button.ButtonStyle();
      Button scoreBtn = new Button();
      scoreBtn.setStyle(scoreBtnStyle);
      scoreBtnStyle.up = drawableScoreBtnUp;
      scoreBtnStyle.down = drawableScoreBtnDown;
      scoreBtn.align(Align.left);
      table.add(scoreBtn).width(250 * scaleX).height(50 * scaleY).padTop(25 * scaleY)
          .padRight(10 * scaleY);
      table.row();

      // Adds a click listener to the easy button
      scoreBtn.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
          if (values.Won) {
            LeaderboardData data = new LeaderboardData();
            data.score = numberOfCustomersServed;
            data.name = textField.getText();
            game.leaderBoard.WriteHighscores(data);
            game.setScreen(new MenuScreen(game));
            dispose();
          }
        }
      });
    }
  }

  /**
   * Called when this screen becomes the current screen for a {@link Game}.
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
   * @param width
   * @param height
   * @see ApplicationListener#resize(int, int)
   */
  @Override
  public void resize(int width, int height) {

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
