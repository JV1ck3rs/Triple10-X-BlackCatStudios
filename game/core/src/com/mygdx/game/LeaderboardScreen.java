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
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Core.ValueStructures.EndOfGameValues;
import java.util.Collections;
import java.util.List;
import java.io.IOException;

/**
 * This class creates and displays the leaderboard screen.
 *
 * @author Azzam Amirul Bahri
 * @author Jack Vickers
 */
public class LeaderboardScreen implements Screen {

  final MyGdxGame game;
  float scaleX;
  float scaleY;
  Stage stage;
  TextField textField;

  Label errorMessage;

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
    for (int i = scores.size(); i < LeaderBoard.MaxHighscorers; i++) {
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

    // Creates the error message label which will be used to tell
    // the user if they have entered an invalid text for highscores.
    errorMessage = new Label("",
            new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    errorMessage.setFontScale(1.10f * (scaleX + scaleY) / 2);
    errorMessage.setAlignment(Align.left);
    //errorMessage.setPosition(0,0);
    table.add(errorMessage).padTop(80 * scaleY).colspan(3);
    table.row();

    // Creates labels which will be used to display the scores
    Label score1 = new Label("1.     " + scores.get(0).toString(),
        fontStyle);
    score1.setFontScale((scaleX + scaleY) / 2);
    score1.setColor(Color.WHITE);
    table.add(score1).center().padTop(5 * scaleY).row();
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
    table.add(score5).center().padBottom(25 * scaleY).row();



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
    table.add(scoreBtn).width(250 * scaleX).height(50 * scaleY).padTop(25 * scaleY)
        .padRight(10 * scaleY);
    table.row();

    // Adds a click listener to the button
    scoreBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        if (numberOfCustomersServed >= 0) {
          if (checkInput()) {
            LeaderboardData data = new LeaderboardData();
            data.score = numberOfCustomersServed;
            data.name = textField.getText();
            game.leaderBoard.WriteHighscores(data);
            game.setScreen(new MenuScreen(game));
            dispose();
          } else {
            System.out.println("WRONG");
            errorMessage.setText("Please input only letters with no numbers");

          }
        } else {
          game.setScreen(new MenuScreen(game));
          dispose();
        }
      }
    });
  }

  private boolean checkInput(){
    boolean validInput = false;

    // String namePattern = "[^\\p{P}|^\\d+]+";
    boolean result = textField.getText().matches("[a-zA-Z]+");
    return result;
    /**return validInput = true;
    char[] chars = textField.getText().toCharArray();

    for (char c : chars) {
      if(!Character.isLetter(c)) {
        return false;
      }
    }

    return true;
     */
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
    stage.getViewport().update(width, height, true);
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
