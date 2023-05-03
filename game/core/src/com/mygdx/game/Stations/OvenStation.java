package com.mygdx.game.Stations;

import com.mygdx.game.Core.GameState.CookingParams;
import com.mygdx.game.Core.Rendering.BlackTexture;
import com.mygdx.game.Core.Rendering.GameObject;
import com.mygdx.game.Core.SFX.soundFrame;
import com.mygdx.game.Core.SFX.soundFrame.soundsEnum;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;
import com.mygdx.game.RecipeAndComb.recipeDict;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Bakes potatoes and pizzas BlackCatStudio's Code
 *
 * @author Jack Hinton
 * @date 30/04/23
 */
public class OvenStation extends Station {

  boolean interacted;
  boolean ready;
  public float maxProgress;
  public float progress;
  public static ArrayList<ItemEnum> itemWhiteList;
  Consumer<Boolean> ovenMade;


  /**
   * Create an oven station
   *
   * @param params             Cooking parameters
   * @param customerController a way to access the customerController's updateMenu method
   * @author Jack Hinton
   * @author Felix Seanor
   * @author Jack Vickers
   */
  public OvenStation(CookingParams params, Consumer<Boolean> customerController) {
    super(params);
    ovenMade = customerController;
    ready = false;
    maxProgress = 10;
    animation = new GameObject(new BlackTexture("Items/OvenActive.png"));
    animation.isVisible = false;
    animation.image.layer = -1;
    if (itemWhiteList == null) {
      itemWhiteList = new ArrayList<>(
          Arrays.asList(ItemEnum.Potato, ItemEnum.CheesePotato, ItemEnum.MeatPotato,
              ItemEnum.CheesePizza, ItemEnum.MeatPizza, ItemEnum.VegPizza,
              ItemEnum.CheesePizzaCooked,
              ItemEnum.MeatPizzaCooked, ItemEnum.VegPizzaCooked, ItemEnum.BakedPotato,
              ItemEnum.CheeseBake,
              ItemEnum.MeatBake));
    }
  }


  /**
   * Give an item to the station
   *
   * @param item The item you want to give
   * @return boolean
   * @author Jack Hinton
   * @author Jack Vickers
   */
  @Override
  public boolean GiveItem(Item item) {
    if (getLocked()) {
      boolean repaired = checkRepairTool(item);
      if (repaired) {
        if (numOvens < 1) {
          ovenMade.accept(true);
          numOvens++;
        }
        deleteItem();
      }
      return repaired;
    }
    if (this.item != null || !itemWhiteList.contains(item.name)) {
      return false;
    }
    changeItem(item);
    checkItem();
    animation.isVisible = true;
    return true;
  }


  /**
   * Retrieve an item from the station
   *
   * @return Item
   * @author Jack Hinton
   * @author Jack Vickers
   */
  @Override
  public Item RetrieveItem() {
    timer.isVisible = false;
    readyBubble.isVisible = false;
    returnItem = item;
    deleteItem();
    currentRecipe = null;
    animation.isVisible = false;
    return returnItem;
  }


  /**
   * Checks if the chef can retrieve an item
   *
   * @return boolean
   * @author Jack Hinton
   */
  @Override
  public boolean CanRetrieve() {
    return item != null;
  }


  /**
   * Check if the chef can give the station an item
   *
   * @return boolean
   * @author Jack Hinton
   */
  @Override
  public boolean CanGive() {

    return item == null;
  }


  /**
   * Check if the chef can interact with a station
   *
   * @return boolean
   * @author Jack Hinton
   */
  @Override
  public boolean CanInteract() {
    return false;
  }


  /**
   * Interact with the station
   *
   * @return float
   * @author Jack Hinton
   */
  @Override
  public float Interact() {
    return 0;
  }


  /**
   * Checks if the item is in the whitelist, if yes it gets the item's recipe
   *
   * @author Jack Hinton
   * @author Jack Vickers
   */
  public void checkItem() {
    if (itemWhiteList.contains(item.name)) {
      currentRecipe = recipeDict.recipes.RecipeMap.get(item.name);
      timer.isVisible = true;
      if (item.name == ItemEnum.CheesePizzaCooked || item.name == ItemEnum.MeatPizzaCooked
          || item.name == ItemEnum.VegPizzaCooked) {
        readyBubble.isVisible = true;
      }
    } else {
      currentRecipe = null;
      timer.isVisible = false;
    }
  }


  /**
   * Cooks the current item and checks if it is ready
   *
   * @param dt delta time
   * @author Jack Hinton
   * @Author Felix Seanor
   * @Author Jack Vickers
   */
  public void Cook(float dt) {
    ready = currentRecipe.recipeSteps.get(item.step)
        .timeStep(item, dt - stationTimeDecrease, interacted, maxProgress);
    if (ready) {
      changeItem(new Item(currentRecipe.endItem));
      readyBubble.isVisible = true;
      soundFrame.SoundEngine.playSound(soundsEnum.FoodReadyBell);
      checkItem();
      return;
    }
    progressBar();
  }


  /**
   * Updates the progress bubble
   *
   * @Author Jack Hinton
   */
  public void progressBar() {
    timer.image = new BlackTexture("Timer/0" + getProgress() + ".png");
  }


  /**
   * Gets the progress of the item currently held
   *
   * @return int
   */
  public int getProgress() {
    progress = item.progress / maxProgress;
    return (int) (progress / 0.125) + 1;
  }


  /**
   * Updates the picture on the station.
   *
   * @author Jack Hinton
   */
  @Override
  public void updatePictures() {
    return;
  }


  /**
   * Move the animation
   *
   * @Author Jack Hinton
   */
  @Override
  public void moveAnim() {
    animation.setPosition(gameObject.position.x, gameObject.position.y);
  }


  /**
   * Update the chopping station
   *
   * @param dt delta time
   * @Author Jack Hinton
   */
  @Override
  public void Update(float dt) {
    if (currentRecipe != null) {
      Cook(dt);
    }
  }
}