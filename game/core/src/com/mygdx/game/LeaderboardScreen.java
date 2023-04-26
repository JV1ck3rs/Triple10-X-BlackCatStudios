package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

public class LeaderboardScreen implements Screen {

    final MyGdxGame game;
    float scaleX;
    float scaleY;
    public LeaderboardScreen(MyGdxGame game){
        // Calculates the scale of the screen to the original size of the game
        scaleX = Gdx.graphics.getWidth() / 640f;
        scaleY = Gdx.graphics.getHeight() / 480f;
        this.game = game;

        Texture leaderboardScreenTexture = new Texture(Gdx.files.internal("SemiTransparentBG.png"));
        Image image = new Image(leaderboardScreenTexture);

        // Create the stage and add the background image
        Stage stage = new Stage();
        image.setSize(stage.getWidth(), stage.getHeight());
        image.setPosition(0, 0);
        stage.addActor(image);
        Gdx.input.setInputProcessor(stage); // Make the stage consume events
        Table table = new Table(); // Create a table that fills the screen. Everything will go inside this table.
        stage.addActor(table);
        table.setFillParent(true);
        table.align(Align.center);

        // Creates a skin for the text field using the clean-crispy-ui.json file
        Skin skin = new Skin(Gdx.files.internal("clean-crispy-ui.json"));

        TextField textField = new TextField(" ", skin);
        textField.getStyle().font.getData().setScale(1.50f * (scaleX + scaleY) / 2);
        textField.setAlignment(Align.center);
        stage.addActor(textField); // Adds the text field to the stage
        table.add(textField).width(250 * scaleX).height(50 * scaleY)
                .colspan(3); // Adds the text field to the table
        table.row();
    }
}
