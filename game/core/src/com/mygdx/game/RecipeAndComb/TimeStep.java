package com.mygdx.game.RecipeAndComb;

import static java.lang.Math.min;

import com.mygdx.game.Items.Item;

/**
 * This is a time step. A certain amount of time must pass before this step completes
 * BlackCatStudio's Code
 *
 * @author Jack Hinton
 * @date 25/03/23
 */
public class TimeStep extends Step {

  public boolean timeStep(Item item, float dt, boolean Interacted, float MaxProgress) {

    item.progress = min(item.progress + dt, MaxProgress);
    if (item.progress == MaxProgress) {
      item.progress = 0;
      return true;
    }
    return false;
  }

}
