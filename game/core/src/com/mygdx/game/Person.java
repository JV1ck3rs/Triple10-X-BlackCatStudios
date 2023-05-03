package com.mygdx.game;


/**
 * Interface for person which is used to create chefs and customers Team Triple 10s
 *
 * @author Robin Graham
 */
@Deprecated
public interface Person {

  /**
   * Update sprite from input.
   *
   * @param newOrientation the new orientation
   */
  void updateSpriteFromInput(String newOrientation);

  /**
   * Sets texture.
   *
   * @param spriteState the sprite state
   */
  void setTexture(String spriteState);

  /**
   * Gets move.
   *
   * @return the move
   */
  String getMove();
}
