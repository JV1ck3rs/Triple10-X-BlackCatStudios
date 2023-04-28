package com.mygdx.game.RecipeAndComb;

import com.mygdx.game.Items.Item;

import static java.lang.Math.min;

/**
 * This is a time step. A certain amount of time must pass before this step completes
 * BlackCatStudio's Code
 * @author Jack Hinton
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
