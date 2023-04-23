package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.game.Core.TextureDictionary;

/**
 * Creates the initial base layer and main objects such as sprite batches and screens Also declares
 * the render, create and dispose functions
 *
 * @author Labib Zabeneh
 * @author Robin Graham
 * @author Riko Puusepp
 * @author Amy Cross
 */
public class MyGdxGame extends Game {

  public SpriteBatch batch;
  public MenuScreen menu;
  public TiledMap map;
  public soundFrame soundFrame;
  public TextureDictionary textureDictionary;
  public CameraFunctions cameraFunctions = new CameraFunctions();

  /**
   * creates maps and sprites
   */
  @Override
  public void create() {
    batch = new SpriteBatch();
    map = new TmxMapLoader().load("PiazzaPanicMap.tmx");
    textureDictionary = new TextureDictionary();
    soundFrame = new soundFrame();
    menu = new MenuScreen(this);
    setScreen(menu);
  }

  /**
   * disposes maps and sprites
   */
  @Override
  public void dispose() {
    super.dispose();
    batch.dispose();
  }

  /**
   * Renders objects
   */
  @Override
  public void render() {
    super.render();
  }

}

