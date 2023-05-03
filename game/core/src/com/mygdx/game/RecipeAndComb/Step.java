package com.mygdx.game.RecipeAndComb;

import com.mygdx.game.Items.Item;

/**
 * Abstraction for steps in a recipe BlackCatStudio's Code
 *
 * @author Jack Hinton
 * @date 01 /03/23
 */
public abstract class Step {

  /**
   * Time step boolean.
   *
   * @param item        the item
   * @param dt          the dt
   * @param Interacted  the interacted
   * @param MaxProgress the max progress
   * @return the boolean
   */
  public abstract boolean timeStep(Item item, float dt, boolean Interacted, float MaxProgress);

}
