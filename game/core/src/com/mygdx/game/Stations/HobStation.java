package com.mygdx.game.Stations;


import com.mygdx.game.Core.BlackTexture;
import com.mygdx.game.Core.ContinousSound;
import com.mygdx.game.Core.GameObject;
import com.mygdx.game.Core.GameState.CookingParams;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;
import com.mygdx.game.RecipeAndComb.RecipeDict;

import com.mygdx.game.soundFrame;
import com.mygdx.game.soundFrame.soundsEnum;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Converts some items into their fried forms BlackCatStudio's Code
 *
 * @author Jack Hinton
 * @date 18/04/23
 */
public class HobStation extends Station {

  boolean interacted;
  boolean ready;
  public static ArrayList<ItemEnum> ItemWhiteList;
  public float progress;
  public float maxProgress;
  public int imageSize = 14;

  private ContinousSound BurnersSFX;
  private ContinousSound FryingSFX;


  /**
   * Creates a hobstation
   * @param params cooking parameters i.e. cooking speed, burning speed
   * @author Jack Hinton
   * @author Felix Seanor
   * @author Jack Vickers
   */
  public HobStation(CookingParams params) {

    super(params);

    interacted = false;
    ready = false;
    maxProgress = 10;
    BurnersSFX = new ContinousSound(soundsEnum.GasCooker);
    FryingSFX = new ContinousSound(soundsEnum.Frying);
    if (ItemWhiteList == null) {
      ItemWhiteList = new ArrayList<>(Arrays.asList(ItemEnum.RawPatty, ItemEnum.CookedPatty));
    }
  }


  /**
   * Retrieves the interacted attribute which is private for testing.
   *
   * @return boolean
   * @author Hubert Solecki
   */
  public boolean GetInteracted() {
    return interacted;
  }


  /**
   * Gives the station an item
   * @param item The item you want to give to the station
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
    changeItem(item);
    checkItem();
    return true;
  }


  /**
   * Retrieve an item from the station
   * @return Item
   * @author Jack Hinton
   * @Authpr Jack Vickers
   */
  @Override
  public Item RetrieveItem() {
    returnItem = item;
    deleteItem();
    currentRecipe = null;
    bubble.isVisible = false;
    bubble2.isVisible = false;
    bubble4.isVisible = false;
    return returnItem;
  }


  /**
   * Checks if the chef can retrieve an item from the station
   * @return boolean
   * @author Jack Hinton
   */
  @Override
  public boolean CanRetrieve() {
    return item != null;
  }


  /**
   * Checks if the chef can give the station an item
   * @return boolean
   * @author Jack Hinton
   */
  @Override
  public boolean CanGive() {
    return item == null;
  }


  /**
   * Checks if the item is in the whitelist, if yes it gets the item's recipe
   * @author Jack Hinton
   * @Author Jack Vickers
   */
  public void checkItem() {
    if (ItemWhiteList.contains(item.name)) {
      currentRecipe = RecipeDict.recipes.RecipeMap.get(item.name);
      bubble.isVisible = true;
      if (item.step == 1 || currentRecipe.RecipeSteps.size() == 1) {
        bubble2.isVisible = true;
      }
      if (currentRecipe.RecipeSteps.size() == 1 && item.name == ItemEnum.CookedPatty) {
        bubble4.isVisible = true;
      }
    } else {
      currentRecipe = null;
      bubble.isVisible = false;
      bubble4.isVisible = false;
    }
  }


  /**
   * Checks if the chef can interact with the station
   * @return boolean
   * @Author Jack Hinton
   */
  @Override
  public boolean CanInteract() {
    return currentRecipe != null;
  }


  /**
   * Interact with the station
   * @return float
   * @Author Jack Hinton
   */
  @Override
  public float Interact() {
    interacted = true;
    return 0;
  }


  /**
   * Burns the item in the station
   * @Author Jack Hinton
   */
  public void burnItem() {
    changeItem(new Item(ItemEnum.Cinder));
  }


  /**
   * Cooks the current item and checks if it is ready
   * @param dt delta time
   * @Author Jack Hinton
   * @Author Felix Seanor
   * @Author Jack Vickers
   */
  public void Cook(float dt) {
    ready = currentRecipe.RecipeSteps.get(item.step)
        .timeStep(item, dt - stationTimeDecrease, interacted, maxProgress);
    BurnersSFX.ShouldPlay = true;
    FryingSFX.ShouldPlay = !ready;
    if (ready && item.progress == 0) {
      item.step++;
      bubble2.isVisible = item.step == 1;

      if (item.step == currentRecipe.RecipeSteps.size()) {
        changeItem(new Item(currentRecipe.endItem));
        soundFrame.SoundEngine.playSound(soundsEnum.FoodReadyBell);
        bubble4.isVisible = true;
        checkItem();
      } else {
        soundFrame.SoundEngine.playSound(soundsEnum.StepAchieved);
      }
      return;
    }
    if (ready) {
      burnItem();
      checkItem();
      return;
    }
    progressBar();
  }


  /**
   * Updates the progress bubble
   * @Author Jack Hinton
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
      heldItem.setPosition(gameObject.position.x + 4,
          gameObject.position.y + (gameObject.getHeight() / 2) + 8);
    } else {
      heldItem.image = new BlackTexture(Item.GetItemPath(item.name));
      heldItem.image.setSize(imageSize, imageSize);
    }

  }


  /**
   * Move the animation
   * @Author Jack Hinton
   */
  @Override
  public void moveAnim() {
    return;
  }


  /**
   * Update the chopping station
   * @param dt delta time
   * @Author Jack Hinton
   * @Author Felix Seanor
   */
  @Override
  public void Update(float dt) {
    if (currentRecipe != null) {
      Cook(dt);
    }
    FryingSFX.DoSoundCheck();
    BurnersSFX.DoSoundCheck();
    interacted = false;
  }
}
