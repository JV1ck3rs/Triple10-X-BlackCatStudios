package com.mygdx.game.RecipeAndComb;

import com.mygdx.game.Items.ItemEnum;
import java.util.ArrayList;

/**
 * The recipe class, List of steps -> Item BlackCatStudio's Code
 *
 * @author Jack Hinton
 * @date 01/03/23
 */
public class Recipe {

  public ArrayList<Step> recipeSteps = new ArrayList<>();
  public ItemEnum endItem;

  public Recipe(ItemEnum endItem, ArrayList<Step> stepList) {
    this.endItem = endItem;
    recipeSteps.addAll(stepList);
  }


}
