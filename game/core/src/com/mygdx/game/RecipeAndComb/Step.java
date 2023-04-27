package com.mygdx.game.RecipeAndComb;

import com.mygdx.game.Items.Item;

/**
 * Abstraction for steps in a recipe
 * BlackCatStudio's Code
 * @author Jack Hinton
 */
public abstract class Step {

    public abstract boolean timeStep(Item item, float dt, boolean Interacted, float MaxProgress);

}
