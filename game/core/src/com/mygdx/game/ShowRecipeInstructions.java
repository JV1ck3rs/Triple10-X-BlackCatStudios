package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Core.Rendering.GameObject;
import com.mygdx.game.Core.Rendering.TextureDictionary;
import java.util.HashMap;

/**
 * This class shows how to create food from their recipes when clicked BlackCatStudio's code
 *
 * @author Sam Toner
 * @date 21 /03/23
 */
public class ShowRecipeInstructions {

  /**
   * The Recipe map.
   */
  HashMap<String, String> recipeMap = new HashMap<>();
  /**
   * The Image object.
   */
  public GameObject imageObject;

  /**
   * Instantiates a new Show recipe instructions.
   */
  public ShowRecipeInstructions() {
    recipeMap.put("Empty", "Recipes/EmptyRecipe.png");
    recipeMap.put("salad", "speech_dish1.png");
    recipeMap.put("burger", "Recipes/BurgerRecipe.png");
  }


  /**
   * Change instruction page.
   *
   * @param dish the dish
   */
  public void changeInstructionPage(String dish) {
    Texture tex = TextureDictionary.textures.get(recipeMap.get(dish));
    imageObject.getBlackTexture().changeTexture(tex);
  }

  /**
   * Make visible.
   */
  public void makeVisible() {
    imageObject.isVisible = true;
  }

  /**
   * Make invisible.
   */
  public void makeInvisible() {
    imageObject.isVisible = false;
  }

  /**
   * Remove instruction page.
   */
  public void removeInstructionPage() {
    imageObject = null;
  }

  /**
   * Create instruction page.
   *
   * @param empty the empty
   */
  public void createInstructionPage(String empty) {
  }
}
