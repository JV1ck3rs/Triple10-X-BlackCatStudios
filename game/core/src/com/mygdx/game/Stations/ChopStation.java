package com.mygdx.game.Stations;

import com.mygdx.game.Core.GameState.CookingParams;
import com.mygdx.game.Core.Rendering.BlackTexture;
import com.mygdx.game.Core.Rendering.GameObject;
import com.mygdx.game.Core.SFX.ContinousSound;
import com.mygdx.game.Core.SFX.SoundFrame;
import com.mygdx.game.Core.SFX.SoundFrame.soundsEnum;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;
import com.mygdx.game.RecipeAndComb.RecipeDict;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Chopping station. Turns items into chopped form BlackCatStudio's Code
 *
 * @author Jack Hinton
 * @date 02/05/23
 */
public class ChopStation extends Station {


  boolean interacted;
  boolean ready;
  public static ArrayList<ItemEnum> itemWhiteList;
  public float progress;
  public float maxProgress;
  public int imageSize = 18;

  private ContinousSound choppingSFX;

  public boolean getInteracted() {
    return interacted;
  }

  /**
   * Creates a chopping station
   *
   * @param params The parameters for cooking speed, burning speed etc.
   * @author Jack Hinton
   * @author Felix Seanor
   * @author Jack Vickers
   */
  public ChopStation(CookingParams params) {

    super(params);

    cookingSpeed = params.chopSpeed;

    interacted = false;
    ready = false;
    maxProgress = 5;
    choppingSFX = new ContinousSound(soundsEnum.KnifeChop);

    if (itemWhiteList == null) {
      itemWhiteList = new ArrayList<>(Arrays.asList(ItemEnum.Lettuce, ItemEnum.Tomato,
          ItemEnum.Onion, ItemEnum.Mince, ItemEnum.CutTomato, ItemEnum.Dough));
    }
  }


  /**
   * Gives the chopping station an item
   *
   * @param item The item you want to give to the chopping station
   * @return boolean - If the method was successful giving an item
   * @author Jack Hinton
   */
  @Override
  public boolean giveItem(Item item) {
    if (getLocked()) {
      return checkRepairTool(item);
    }
    if (this.item == null) {
      changeItem(item);
      checkItem();
      return true;
    }
    return false;
  }


  /**
   * Returns item
   *
   * @return Item
   * @author Jack Hinton
   */
  @Override
  public Item retrieveItem() {
    if (item != null) {
      returnItem = item;
      deleteItem();
      currentRecipe = null;
      return returnItem;
    }
    return null;
  }

  /**
   * Checks if you can retrieve an item from the chopping station
   *
   * @return boolean
   * @author Jack Hinton
   */
  @Override
  public boolean canRetrieve() {
    return item != null;
  }


  /**
   * Checks if you can give an item to the chopping station
   *
   * @return boolean
   * @author Jack Hinton
   */
  @Override
  public boolean canGive() {
    return item == null;
  }


  /**
   * Checks if the user can interact with the chopping station
   *
   * @return boolean
   * @author Jack Hinton
   */
  @Override
  public boolean canInteract() {
    return currentRecipe != null;
  }


  /**
   * Checks if the item is in the whitelist, if yes it gets the items recipe
   *
   * @author Jack Hinton
   */
  public void checkItem() {
    if (itemWhiteList.contains(item.name)) {
      currentRecipe = RecipeDict.recipes.RecipeMap.get(item.name);
    } else {
      currentRecipe = null;
    }
  }

  /**
   * Returns the item in the station.
   *
   * @return The item in the station.
   * @author Jack Vickers
   */
  public Item returnItem() {
    return item;
  }


  /**
   * Interact with the chopping station to cut, returns a float to tell the chef how long to lock
   * for
   *
   * @return float
   * @author Jack Hinton
   */
  @Override
  public float interact() {
    timer.isVisible = true;
    interacted = true;
    return maxProgress;
  }


  /**
   * Cuts the item and checks if it is ready
   *
   * @param dt delta time
   * @author Jack Hinton
   * @author Felix Seanor
   */
  public void cut(float dt) {
    ready = currentRecipe.recipeSteps.get(item.step)
        .timeStep(item, dt * cookingSpeed, interacted, maxProgress);
    choppingSFX.shouldPlay = true;
    if (ready) {
      changeItem(new Item(currentRecipe.endItem));
      checkItem();
      SoundFrame.SoundEngine.playSound(soundsEnum.FoodReadyBell);
      interacted = false;
      timer.isVisible = false;
    }
    progressBar();
  }


  /**
   * Updates the progress bubble
   *
   * @author Jack Hinton
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
    if (item == null) {
      if (heldItem == null) {
        return;
      }
      heldItem.destroy();
      heldItem = null;
      return;
    }
    if (heldItem == null) {
      heldItem = new GameObject(new BlackTexture(Item.getItemPath(item.name)));
      heldItem.image.setSize(imageSize, imageSize);
      heldItem.setPosition(gameObject.position.x + (gameObject.physicalWidth / 2) - 12,
          gameObject.position.y + (gameObject.getHeight()) - imageSize - 7);
    } else {
      heldItem.image = new BlackTexture(Item.getItemPath(item.name));
      heldItem.image.setSize(imageSize, imageSize);
    }
  }


  /**
   * Move the animation
   *
   * @author Jack Hinton
   */
  @Override
  public void moveAnimation() {
    return;
  }

  /**
   * Update the chopping station
   *
   * @param dt delta time
   * @author Jack Hinton
   * @author Felix Seanor
   */
  @Override
  public void update(float dt) {
    if (currentRecipe != null && interacted) {
      cut(dt);
    }
    choppingSFX.doSoundCheck();
  }
}
