package com.mygdx.game.Stations;

import com.mygdx.game.Core.Rendering.BlackTexture;
import com.mygdx.game.Core.SFX.ContinousSound;
import com.mygdx.game.Core.Rendering.GameObject;
import com.mygdx.game.Core.GameState.CookingParams;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;
import com.mygdx.game.RecipeAndComb.RecipeDict;
import com.mygdx.game.Core.SFX.soundFrame;
import com.mygdx.game.Core.SFX.soundFrame.soundsEnum;
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
  public static ArrayList<ItemEnum> ItemWhiteList;
  public float progress;
  public float maxProgress;
  public int imageSize = 18;

  private ContinousSound choppingSFX;

  public boolean GetInteracted() {
    return interacted;
  }

  /**
   * Creates a chopping station
   * @param params The parameters for cooking speed, burning speed etc.
   * @author Jack Hinton
   * @author Felix Seanor
   * @author Jack Vickers
   */
  public ChopStation(CookingParams params) {

    super(params);

    CookingSpeed = params.ChopSpeed;

    interacted = false;
    ready = false;
    maxProgress = 5;
    choppingSFX = new ContinousSound(soundsEnum.KnifeChop);

    if (ItemWhiteList == null) {
      ItemWhiteList = new ArrayList<>(Arrays.asList(ItemEnum.Lettuce, ItemEnum.Tomato,
          ItemEnum.Onion, ItemEnum.Mince, ItemEnum.CutTomato, ItemEnum.Dough));
    }
  }


  /**
   * Gives the chopping station an item
   * @param item The item you want to give to the chopping station
   * @return boolean - If the method was successful giving an item
   * @author Jack Hinton
   */
  @Override
  public boolean GiveItem(Item item) {
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
   * @return Item
   * @author Jack Hinton
   */
  @Override
  public Item RetrieveItem() {
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
   * @return boolean
   * @author Jack Hinton
   */
  @Override
  public boolean CanRetrieve() {
    return item != null;
  }


  /**
   * Checks if you can give an item to the chopping station
   * @return boolean
   * @author Jack Hinton
   */
  @Override
  public boolean CanGive() {
    return item == null;
  }


  /**
   * Checks if the user can interact with the chopping station
   * @return boolean
   * @author Jack Hinton
   */
  @Override
  public boolean CanInteract() {
    return currentRecipe != null;
  }


  /**
   * Checks if the item is in the whitelist, if yes it gets the items recipe
   * @author Jack Hinton
   */
  public void checkItem() {
    if (ItemWhiteList.contains(item.name)) {
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
   * Interact with the chopping station to cut, returns a float to tell the chef how long to lock for
   * @return float
   * @author Jack Hinton
   */
  @Override
  public float Interact() {
    bubble.isVisible = true;
    interacted = true;
    return maxProgress;
  }


  /**
   * Cuts the item and checks if it is ready
   * @param dt delta time
   * @author Jack Hinton
   * @author Felix Seanor
   */
  public void Cut(float dt) {
    ready = currentRecipe.RecipeSteps.get(item.step)
        .timeStep(item, dt * CookingSpeed, interacted, maxProgress);
    choppingSFX.ShouldPlay = true;
    if (ready) {
      changeItem(new Item(currentRecipe.endItem));
      checkItem();
      soundFrame.SoundEngine.playSound(soundsEnum.FoodReadyBell);
      interacted = false;
      bubble.isVisible = false;
    }
    progressBar();
  }


  /**
   * Updates the progress bubble
   * @author Jack Hinton
   */
  public void progressBar() {
    bubble.image = new BlackTexture("Timer/0" + getProgress() + ".png");
  }


  /**
   * Gets the progress of the item currently held
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
      heldItem.Destroy();
      heldItem = null;
      return;
    }
    if (heldItem == null) {
      heldItem = new GameObject(new BlackTexture(Item.GetItemPath(item.name)));
      heldItem.image.setSize(imageSize, imageSize);
      heldItem.setPosition(gameObject.position.x + (gameObject.PhysicalWidth / 2) - 12,
          gameObject.position.y + (gameObject.getHeight()) - imageSize - 7);
    } else {
      heldItem.image = new BlackTexture(Item.GetItemPath(item.name));
      heldItem.image.setSize(imageSize, imageSize);
    }
  }


  /**
   * Move the animation
   * @author Jack Hinton
   */
  @Override
  public void moveAnim() {
    return;
  }

  /**
   * Update the chopping station
   * @param dt delta time
   * @author Jack Hinton
   * @author Felix Seanor
   */
  @Override
  public void Update(float dt) {
    if (currentRecipe != null && interacted) {
      Cut(dt);
    }
    choppingSFX.DoSoundCheck();
  }
}
