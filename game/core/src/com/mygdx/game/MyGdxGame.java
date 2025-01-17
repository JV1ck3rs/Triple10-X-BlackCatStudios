package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.game.Core.Leaderboard.LeaderBoard;
import com.mygdx.game.Core.Rendering.TextureDictionary;
import com.mygdx.game.Core.SFX.SoundFrame;

/**
 * Creates the initial base layer and main objects such as sprite batches and screens Also declares
 * the render, create and dispose functions Mixture of BlackCatStudios Code and TeamTriple10s
 *
 * @author Labib Zabeneh
 * @author Robin Graham
 * @author Riko Puusepp
 * @author Amy Cross
 * @date 28 /04/23
 */
public class MyGdxGame extends Game {

  /**
   * The Batch.
   */
  public SpriteBatch batch;
  /**
   * The Menu.
   */
  public MenuScreen menu;
  /**
   * The Map.
   */
  public TiledMap map;
  /**
   * The Leader board.
   */
  public LeaderBoard leaderBoard;
  /**
   * The Sound frame.
   */
  public SoundFrame soundFrame;
  /**
   * The Texture dictionary.
   */
  public TextureDictionary textureDictionary;
  /**
   * The Camera functions.
   */
  public CameraFunctions cameraFunctions = new CameraFunctions();

  /**
   * creates maps and sprites
   */
  @Override
  public void create() {
    //Triple10s
    batch = new SpriteBatch();
    map = new TmxMapLoader().load("PiazzaPanicMap.tmx");
    //BlackCatStudios
    textureDictionary = new TextureDictionary();
    leaderBoard = new LeaderBoard();
    soundFrame = new SoundFrame();
    //Triple10s
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

