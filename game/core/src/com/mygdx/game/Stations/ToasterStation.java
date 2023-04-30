package com.mygdx.game.Stations;

import com.mygdx.game.Core.BlackTexture;
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
 * Toasts items such as bun
 *
 * @author Jack Hinton
 */
public class ToasterStation extends Station {

  boolean interacted;
  boolean ready;
  public float maxProgress;
  public float progress;
  public static ArrayList<ItemEnum> ItemWhiteList;


  public ToasterStation(CookingParams params) {
    super(params);
    ready = false;
    maxProgress = 8;
    if (ItemWhiteList == null) {
      ItemWhiteList = new ArrayList<>(Arrays.asList(ItemEnum.Buns));
    }
    animation = new GameObject(new BlackTexture("Items/ToasterActive.png"));
    animation.isVisible = false;
  }


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


  @Override
  public boolean CanRetrieve() {
    return item != null;
  }


  @Override
  public boolean CanGive() {
    return item == null;
  }


  @Override
  public boolean CanInteract() {
    return false;
  }


  @Override
  public float Interact() {
    return 0;
  }


  public void checkItem() {
    if (ItemWhiteList.contains(item.name)) {
      currentRecipe = RecipeDict.recipes.RecipeMap.get(item.name);
      bubble.isVisible = true;
    } else {
      currentRecipe = null;
      bubble.isVisible = false;
    }
  }


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


  public void progressBar() {
    bubble.image = new BlackTexture("Timer/0" + getProgress() + ".png");
  }


  public int getProgress() {
    progress = item.progress / maxProgress;
    return (int) (progress / 0.125) + 1;
  }


  public float getCookingTime() {
    return item.progress;
  }

  @Override
  public void updatePictures() {
    return;
  }


  @Override
  public void moveAnim() {
    animation.setPosition(gameObject.position.x + 3,
        gameObject.position.y + gameObject.getHeight() - animation.getHeight());
  }


  @Override
  public void Update(float dt) {
    if (currentRecipe != null) {
      Cook(dt);
    }

  }
}
