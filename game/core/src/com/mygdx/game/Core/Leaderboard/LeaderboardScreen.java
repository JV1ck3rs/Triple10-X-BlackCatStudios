package com.mygdx.game.Core.Leaderboard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Core.ValueStructures.EndOfGameValues;
import com.mygdx.game.InputChecker;
import com.mygdx.game.MenuScreen;
import com.mygdx.game.MyGdxGame;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * This class creates and displays the leaderboard screen. BlackCatStudio's Code
 *
 * @author Azzam Amirul Bahri
 * @author Jack Vickers
 * @date 28 /04/23
 */
public class LeaderboardScreen implements Screen {

  /**
   * The Game.
   */
  final MyGdxGame game;
  /**
   * The Scale x.
   */
  float scaleX;
  /**
   * The Scale y.
   */
  float scaleY;
  /**
   * The Stage.
   */
  Stage stage;
  /**
   * The Text field.
   */
  TextField textField;

  /**
   * The Error message.
   */
  Label errorMessage;

  /**
   * Instantiates a new Leaderboard screen.
   *
   * @param game                    the game
   * @param values                  the values
   * @param numberOfCustomersServed the number of customers served
   * @throws IOException the io exception
   */
  public LeaderboardScreen(MyGdxGame game, EndOfGameValues values, int numberOfCustomersServed)
      throws IOException {
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
    List<LeaderboardData> scores = game.leaderBoard.readFSONData();
    Collections.sort(scores);
    Collections.reverse(scores);
    for (int i = scores.size(); i < LeaderBoard.maxHighscorers; i++) {
      scores.add(new LeaderboardData(0, "N/A"));
    }
    // Creates the font for the labels
    FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
    FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("ka1.ttf"));
    params.size = 20;
    Color colour = new Color(0x5584AC);
    params.color = colour;
    params.shadowColor = Color.WHITE;
    params.shadowOffsetX = 3;
    params.shadowOffsetY = 3;
    BitmapFont font = gen.generateFont(params);
    Label.LabelStyle fontStyle = new Label.LabelStyle();
    fontStyle.font = font;

    // Creates labels which will be used to display the scores
    Label score1 = new Label("1.     " + scores.get(0).toString(),
        fontStyle);
    score1.setFontScale((scaleX + scaleY) / 2);
    score1.setColor(Color.WHITE);
    table.add(score1).center().padTop(150 * scaleY).row();
    Label score2 = new Label("2.     " + scores.get(1).toString(),
        fontStyle);
    score2.setFontScale((scaleX + scaleY) / 2);
    table.add(score2).center().row();
    Label score3 = new Label("3.     " + scores.get(2).toString(),
        fontStyle);
    score3.setFontScale((scaleX + scaleY) / 2);
    table.add(score3).center().row();
    Label score4 = new Label("4.     " + scores.get(3).toString(),
        fontStyle);
    score4.setFontScale((scaleX + scaleY) / 2);
    table.add(score4).center().row();
    Label score5 = new Label("5.     " + scores.get(4).toString(),
        fontStyle);
    score5.setFontScale((scaleX + scaleY) / 2);
    table.add(score5).center().padBottom(15 * scaleY).row();

    // Creates the error message label which will be used to tell
    // the user if they have entered an invalid text for highscores.
    errorMessage = new Label("",
        new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    errorMessage.setFontScale(1.10f * (scaleX + scaleY) / 2);
    errorMessage.setAlignment(Align.left);
    //errorMessage.setPosition(0,0);
    table.add(errorMessage);
    table.row();

    if (numberOfCustomersServed >= 0) {
      // Creates a skin for the text field using the clean-crispy-ui.json file
      Skin skin = new Skin(Gdx.files.internal("clean-crispy-ui.json"));

      // Creates a text field which will be used to enter the player's name
      textField = new TextField("", skin);
      textField.getStyle().font.getData().setScale(1.50f * (scaleX + scaleY) / 2);
      textField.setAlignment(Align.center);
      stage.addActor(textField); // Adds the text field to the stage
      table.add(textField).width(250 * scaleX)
          .height(50 * scaleY); // Adds the text field to the table
      table.row();
    }
    // Creates a button which will be used to save the player's score
    TextureRegion saveScoreTexture;
    TextureRegion saveScoreDownTexture;
    if (numberOfCustomersServed >= 0) {
      saveScoreTexture = new TextureRegion(new Texture("SaveExitUp.png"));
      saveScoreDownTexture = new TextureRegion(new Texture("SaveExitDown.png"));
    } else {
      saveScoreTexture = new TextureRegion(new Texture("ExitUp.png"));
      saveScoreDownTexture = new TextureRegion(new Texture("ExitDown.png"));
    }
    Drawable drawableScoreBtnUp = new TextureRegionDrawable(saveScoreTexture);
    Drawable drawableScoreBtnDown = new TextureRegionDrawable(saveScoreDownTexture);
    Button.ButtonStyle scoreBtnStyle = new Button.ButtonStyle();
    Button scoreBtn = new Button();
    scoreBtn.setStyle(scoreBtnStyle);
    scoreBtnStyle.up = drawableScoreBtnUp;
    scoreBtnStyle.down = drawableScoreBtnDown;
    scoreBtn.align(Align.left);
    table.add(scoreBtn).width(250 * scaleX).height(50 * scaleY).padTop(10 * scaleY);
    table.row();

    // Adds a click listener to the button
    scoreBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        if (numberOfCustomersServed >= 0) {
          if (InputChecker.checkLeaderboardName(textField.getText())) {
            LeaderboardData data = new LeaderboardData();
            data.score = numberOfCustomersServed;
            data.name = textField.getText();
            game.leaderBoard.writeHighscores(data);
            game.setScreen(new MenuScreen(game));
            dispose();
          } else {
//            System.out.println("WRONG");
            errorMessage.setText("Please input only letters with no numbers up to 5 characters!");

          }
        } else {
          game.setScreen(new MenuScreen(game));
          dispose();
        }
      }
    });
  }

  /**
   * Called when this screen becomes the current screen
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
   */
  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);
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
   * Called when this screen is no longer the current screen
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
