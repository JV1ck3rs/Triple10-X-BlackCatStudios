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
 * Converts some items into their fried forms BlackCatStudio's Code
 *
 * @author Jack Hinton
 * @date 18/04/23
 */
public class HobStation extends Station {

  boolean interacted;
  boolean ready;
  public static ArrayList<ItemEnum> itemWhiteList;
  public float progress;
  public float maxProgress;
  public int imageSize = 14;

  private ContinousSound burnersSFX;
  private ContinousSound fryingSFX;


  /**
   * Creates a hobstation
   *
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
    burnersSFX = new ContinousSound(soundsEnum.GasCooker);
    fryingSFX = new ContinousSound(soundsEnum.Frying);
    if (itemWhiteList == null) {
      itemWhiteList = new ArrayList<>(Arrays.asList(ItemEnum.RawPatty, ItemEnum.CookedPatty));
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
   *
   * @param item The item you want to give to the station
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
    changeItem(item);
    checkItem();
    return true;
  }


  /**
   * Retrieve an item from the station
   *
   * @return Item
   * @author Jack Hinton
   * @Authpr Jack Vickers
   */
  @Override
  public Item retrieveItem() {
    returnItem = item;
    deleteItem();
    currentRecipe = null;
    timer.isVisible = false;
    warningIcon.isVisible = false;
    readyBubble.isVisible = false;
    return returnItem;
  }


  /**
   * Checks if the chef can retrieve an item from the station
   *
   * @return boolean
   * @author Jack Hinton
   */
  @Override
  public boolean canRetrieve() {
    return item != null;
  }


  /**
   * Checks if the chef can give the station an item
   *
   * @return boolean
   * @author Jack Hinton
   */
  @Override
  public boolean canGive() {
    return item == null;
  }


  /**
   * Checks if the item is in the whitelist, if yes it gets the item's recipe
   *
   * @author Jack Hinton
   * @Author Jack Vickers
   */
  public void checkItem() {
    if (itemWhiteList.contains(item.name)) {
      currentRecipe = RecipeDict.recipes.RecipeMap.get(item.name);
      timer.isVisible = true;
      if (item.step == 1 || currentRecipe.recipeSteps.size() == 1) {
        warningIcon.isVisible = true;
      }
      if (currentRecipe.recipeSteps.size() == 1 && item.name == ItemEnum.CookedPatty) {
        readyBubble.isVisible = true;
      }
    } else {
      currentRecipe = null;
      timer.isVisible = false;
      readyBubble.isVisible = false;
    }
  }


  /**
   * Checks if the chef can interact with the station
   *
   * @return boolean
   * @Author Jack Hinton
   */
  @Override
  public boolean canInteract() {
    return currentRecipe != null;
  }


  /**
   * Interact with the station
   *
   * @return float
   * @Author Jack Hinton
   */
  @Override
  public float interact() {
    interacted = true;
    return 0;
  }


  /**
   * Burns the item in the station
   *
   * @Author Jack Hinton
   */
  public void burnItem() {
    changeItem(new Item(ItemEnum.Cinder));
  }


  /**
   * Cooks the current item and checks if it is ready
   *
   * @param dt delta time
   * @Author Jack Hinton
   * @Author Felix Seanor
   * @Author Jack Vickers
   */
  public void cook(float dt) {
    ready = currentRecipe.recipeSteps.get(item.step)
        .timeStep(item, dt - stationTimeDecrease, interacted, maxProgress);
    burnersSFX.shouldPlay = true;
    fryingSFX.shouldPlay = !ready;
    if (ready && item.progress == 0) {
      item.step++;
      warningIcon.isVisible = item.step == 1;

      if (item.step == currentRecipe.recipeSteps.size()) {
        changeItem(new Item(currentRecipe.endItem));
        SoundFrame.SoundEngine.playSound(soundsEnum.FoodReadyBell);
        readyBubble.isVisible = true;
        checkItem();
      } else {
        SoundFrame.SoundEngine.playSound(soundsEnum.StepAchieved);
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
      heldItem.setPosition(gameObject.position.x + 4,
          gameObject.position.y + (gameObject.getHeight() / 2) + 8);
    } else {
      heldItem.image = new BlackTexture(Item.getItemPath(item.name));
      heldItem.image.setSize(imageSize, imageSize);
    }

  }


  /**
   * Move the animation
   *
   * @Author Jack Hinton
   */
  @Override
  public void moveAnimation() {
    return;
  }


  /**
   * Update the chopping station
   *
   * @param dt delta time
   * @Author Jack Hinton
   * @Author Felix Seanor
   */
  @Override
  public void update(float dt) {
    if (currentRecipe != null) {
      cook(dt);
    }
    fryingSFX.doSoundCheck();
    burnersSFX.doSoundCheck();
    interacted = false;
  }
}
