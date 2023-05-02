package com.mygdx.game.RecipeAndComb;

import static java.lang.Math.min;

import com.mygdx.game.Items.Item;

/**
 * An interactable step in a recipe. Requires a chef to interact with it before the time runs out
 * BlackCatStudio's Code
 *
 * @author Jack Hinton
 * @date 01/03/23
 */
public class InteractionStep extends Step {

  public boolean timeStep(Item item, float dt, boolean Interacted, float MaxProgress) {
    if (Interacted) {
      item.progress = 0;
      return true;
    }
    item.progress = min(item.progress + dt, MaxProgress);
    return item.progress == MaxProgress;
  }

}
