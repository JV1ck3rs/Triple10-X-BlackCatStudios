package com.mygdx.game.Stations;


import com.badlogic.gdx.Gdx;
import com.mygdx.game.Core.BlackTexture;
import com.mygdx.game.Core.ContinousSound;
import com.mygdx.game.Core.GameObject;
import com.mygdx.game.Core.GameState.CookingParams;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;
import com.mygdx.game.RecipeAndComb.RecipeDict;

import com.mygdx.game.RecipeAndComb.RecipeDict;
import com.mygdx.game.soundFrame;
import com.mygdx.game.soundFrame.soundsEnum;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Converts some items into their fried forms
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
   */
  public boolean GetInteracted() {
    return interacted;
  }

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


    @Override
    public Item RetrieveItem() {
        returnItem = item;
        deleteItem();
        currentRecipe = null;
        bubble.isVisible = false;
        bubble2.isVisible = false;
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


    public void checkItem(){
        if(ItemWhiteList.contains(item.name)) {
            currentRecipe = RecipeDict.recipes.RecipeMap.get(item.name);
            bubble.isVisible = true;
            if(item.step == 1||currentRecipe.RecipeSteps.size() == 1)
                bubble2.isVisible = true;
        }
        else {
            currentRecipe = null;
            bubble.isVisible = false;
            bubble2.isVisible = false;
        }
    }


  @Override
  public boolean CanInteract() {
    return currentRecipe != null;
  }

  @Override
  public boolean Interact() {
    return interacted = true;
  }


  public void burnItem() {
    changeItem(new Item(ItemEnum.Cinder));
  }


  public void Cook(float dt) {
    ready = currentRecipe.RecipeSteps.get(item.step)
        .timeStep(item, dt - stationTimeDecrease, interacted, maxProgress);
    BurnersSFX.ShouldPlay = true;
    FryingSFX.ShouldPlay = !ready;
    if (ready && item.progress == 0) {
      item.step++;
      System.out.println("PRESS SPACE TO FLIP BURGER");
      bubble2.isVisible = item.step == 1;

      if(item.step == currentRecipe.RecipeSteps.size()){
        changeItem(new Item(currentRecipe.endItem));
        soundFrame.SoundEngine.playSound(soundsEnum.FoodReadyBell);
        bubble2.isVisible = true;
        checkItem();
      }
      else {
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

  public void progressBar() {
    bubble.image = new BlackTexture("Timer/0" + getProgress() + ".png");
  }


  public int getProgress() {
    progress = item.progress / maxProgress;
    return (int) (progress / 0.125) + 1;
  }


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
          gameObject.position.y + (gameObject.getHeight() / 2) + 2);
    } else {
      heldItem.image = new BlackTexture(Item.GetItemPath(item.name));
      heldItem.image.setSize(imageSize, imageSize);
    }

  }


  @Override
  public void moveAnim(){
    return;
  }


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
