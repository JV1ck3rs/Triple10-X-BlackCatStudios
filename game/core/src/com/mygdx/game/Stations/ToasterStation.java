package com.mygdx.game.Stations;

import com.mygdx.game.Core.GameState.CookingParams;
import com.mygdx.game.Core.Rendering.BlackTexture;
import com.mygdx.game.Core.Rendering.GameObject;
import com.mygdx.game.Core.SFX.soundFrame;
import com.mygdx.game.Core.SFX.soundFrame.soundsEnum;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;
import com.mygdx.game.RecipeAndComb.RecipeDict;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Toasts items such as bun
 *
 * @author Jack Hinton
 * @date 01/05/23
 */
public class ToasterStation extends Station {

  boolean interacted;
  boolean ready;
  public float maxProgress;
  public float progress;
  public static ArrayList<ItemEnum> ItemWhiteList;


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
    if (ItemWhiteList == null) {
      ItemWhiteList = new ArrayList<>(Arrays.asList(ItemEnum.Buns));
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
  public boolean GiveItem(Item item) {
    if (getLocked()) {
      return checkRepairTool(item);
    }
    if (this.item != null) {
      return false;
    }
    if (ItemWhiteList.contains(item.name)) {
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
   * @Author Jack Vickers
   */
  @Override
  public Item RetrieveItem() {
    returnItem = item;
    deleteItem();
    currentRecipe = null;
    bubble.isVisible = false;
    bubble4.isVisible = false;
    animation.isVisible = false;

    return returnItem;
  }


  /**
   * Checks if the chef can retrieve an item
   *
   * @return boolean
   * @Author Jack Hinton
   */
  @Override
  public boolean CanRetrieve() {
    return item != null;
  }


  /**
   * Check if the chef can give the station an item
   *
   * @return boolean
   * @Author Jack Hinton
   */
  @Override
  public boolean CanGive() {
    return item == null;
  }


  /**
   * Check if the chef can interact with a station
   *
   * @return boolean
   * @Author Jack Hinton
   */
  @Override
  public boolean CanInteract() {
    return false;
  }


  /**
   * Interact with the station
   *
   * @return float
   * @Author Jack Hinton
   */
  @Override
  public float Interact() {
    return 0;
  }


  /**
   * Checks if the item is in the whitelist, if yes it gets the item's recipe
   *
   * @Author Jack Hinton
   * @Author Jack Vickers
   */
  public void checkItem() {
    if (ItemWhiteList.contains(item.name)) {
      currentRecipe = RecipeDict.recipes.RecipeMap.get(item.name);
      bubble.isVisible = true;
    } else {
      currentRecipe = null;
      bubble.isVisible = false;
    }
  }


  /**
   * Cooks the current item and checks if it is ready
   *
   * @param dt delta time
   * @Author Jack Hinton
   * @Author Felix Seanor
   * @Author Jack Vickers
   */
  public void Cook(float dt) {
    ready = currentRecipe.RecipeSteps.get(item.step)
        .timeStep(item, dt - stationTimeDecrease, interacted, maxProgress);

    if (ready) {
      changeItem(new Item(currentRecipe.endItem));
      bubble4.isVisible = true;
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
    bubble.image = new BlackTexture("Timer/0" + getProgress() + ".png");
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
   * Gets the items progress
   *
   * @return float
   * @Author Hubert Solecki
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
   * @Author Jack Hinton
   */
  @Override
  public void moveAnim() {
    animation.setPosition(gameObject.position.x + 3,
        gameObject.position.y + gameObject.getHeight() - animation.getHeight());
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
