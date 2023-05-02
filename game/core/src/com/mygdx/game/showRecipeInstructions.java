package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Core.GameObject;

import com.mygdx.game.Core.TextureDictionary;
import java.util.HashMap;

/**
 * This class shows how to create food from their recipes when clicked BlackCatStudio's code
 *
 * @author Sam Toner
 * @date 21/03/23
 */
public class showRecipeInstructions {

  HashMap<String, String> recipeMap = new HashMap<>();
  public GameObject imageObject;

  public showRecipeInstructions() {
    recipeMap.put("Empty", "Recipes/EmptyRecipe.png");
    recipeMap.put("salad", "speech_dish1.png");
    recipeMap.put("burger", "Recipes/BurgerRecipe.png");
  }


  public void changeInstructionPage(String dish) {
    Texture tex = TextureDictionary.textures.Get(recipeMap.get(dish));
    imageObject.getBlackTexture().changeTexture(tex);
  }

  public void makeVisible() {
    imageObject.isVisible = true;
  }

  public void makeInvisible() {
    imageObject.isVisible = false;
  }

  public void removeInstructionPage() {
    imageObject = null;
  }

  public void createInstructionPage(String empty) {
  }
}
