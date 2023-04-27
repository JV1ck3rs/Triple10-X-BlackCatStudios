package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Interface for person which is used to create chefs and customers
 * Team Triple 10s
 * @author Robin Graham
 */
@Deprecated
public interface Person {

  void updateSpriteFromInput(String newOrientation);

  void setTexture(String spriteState);

  String getMove();
}
