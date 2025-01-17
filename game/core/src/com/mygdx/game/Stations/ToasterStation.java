package com.mygdx.game.Stations;

import com.mygdx.game.Core.GameState.CookingParams;
import com.mygdx.game.Core.Rendering.BlackTexture;
import com.mygdx.game.Core.Rendering.GameObject;
import com.mygdx.game.Core.SFX.SoundFrame;
import com.mygdx.game.Core.SFX.SoundFrame.soundsEnum;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;
import com.mygdx.game.RecipeAndComb.RecipeDict;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Toasts items such as bun
 *
 * @author Jack Hinton
 * @date 01 /05/23
 */
public class ToasterStation extends Station {

  /**
   * The Interacted.
   */
  boolean interacted;
  /**
   * The Ready.
   */
  boolean ready;
  /**
   * The Max progress.
   */
  public float maxProgress;
  /**
   * The Progress.
   */
  public float progress;
  /**
   * The Item white list.
   */
  public static ArrayList<ItemEnum> itemWhiteList;


  /**
   * Creates a toaster
   *
   * @param params cooking parameters i.e. cooking speed, burning speed
   * @author Jack Hinton
   * @author Felix Seanor
   */
  public ToasterStation(CookingParams params) {
    super(params);
    ready = false;
    maxProgress = 8;
    if (itemWhiteList == null) {
      itemWhiteList = new ArrayList<>(Arrays.asList(ItemEnum.Buns));
    }
    animation = new GameObject(new BlackTexture("Items/ToasterActive.png"));
    animation.isVisible = false;
    animation.image.layer = -1;

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
  public boolean giveItem(Item item) {
    if (getLocked()) {
      return checkRepairTool(item);
    }
    if (this.item != null) {
      return false;
    }
    if (itemWhiteList.contains(item.name)) {
      animation.isVisible = true;
      changeItem(item);
      checkItem();
      return true;
    }
    return false;
  }


  /**
   * Retrieve an item from the station
   *
   * @return Item
   * @author Jack Hinton
   * @author Jack Vickers
   */
  @Override
  public Item retrieveItem() {
    returnItem = item;
    deleteItem();
    currentRecipe = null;
    timer.isVisible = false;
    readyBubble.isVisible = false;
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
  public boolean canRetrieve() {
    return item != null;
  }


  /**
   * Check if the chef can give the station an item
   *
   * @return boolean
   * @author Jack Hinton
   */
  @Override
  public boolean canGive() {
    return item == null;
  }


  /**
   * Check if the chef can interact with a station
   *
   * @return boolean
   * @author Jack Hinton
   */
  @Override
  public boolean canInteract() {
    return false;
  }


  /**
   * Interact with the station
   *
   * @return float
   * @author Jack Hinton
   */
  @Override
  public float interact() {
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
      currentRecipe = RecipeDict.recipes.RecipeMap.get(item.name);
      timer.isVisible = true;
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
   * @author Felix Seanor
   * @author Jack Vickers
   */
  public void Cook(float dt) {
    ready = currentRecipe.recipeSteps.get(item.step)
        .timeStep(item, dt - stationTimeDecrease, interacted, maxProgress);

    if (ready) {
      changeItem(new Item(currentRecipe.endItem));
      readyBubble.isVisible = true;
      SoundFrame.SoundEngine.playSound(soundsEnum.FoodReadyBell);
      checkItem();
      return;
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
   * @return int progress
   */
  public int getProgress() {
    progress = item.progress / maxProgress;
    return (int) (progress / 0.125) + 1;
  }


  /**
   * Gets the items progress
   *
   * @return float cooking time
   * @author Hubert Solecki
   */
  public float getCookingTime() {
    return item.progress;
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
   * @author Jack Hinton
   */
  @Override
  public void moveAnimation() {
    animation.setPosition(gameObject.position.x + 3,
        gameObject.position.y + gameObject.getHeight() - animation.getHeight());
  }


  /**
   * Update the chopping station
   *
   * @param dt delta time
   * @author Jack Hinton
   */
  @Override
  public void update(float dt) {
    if (currentRecipe != null) {
      Cook(dt);
    }

  }
}
