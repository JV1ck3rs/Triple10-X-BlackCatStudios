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
 * Chopping station. Turns items into chopped form
 * BlackCatStudio's Code
 * @author Jack Hinton
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
    return currentRecipe != null;
  }

  public void checkItem(){
    if(ItemWhiteList.contains(item.name)) {
      currentRecipe = RecipeDict.recipes.RecipeMap.get(item.name);
      bubble.isVisible = true;
    }
    else {
      currentRecipe = null;
      bubble.isVisible = false;
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


  @Override
  public boolean Interact() {
    return interacted = true;
  }

  public void Cut(float dt) {
    ready = currentRecipe.RecipeSteps.get(item.step).timeStep(item, dt * CookingSpeed, interacted, maxProgress);
    choppingSFX.ShouldPlay = true;
    if (ready) {
      changeItem(new Item(currentRecipe.endItem));
      checkItem();
      soundFrame.SoundEngine.playSound(soundsEnum.FoodReadyBell);
      interacted = false;
    }
    progressBar();
  }


  public void progressBar(){
    bubble.image = new BlackTexture("Timer/0"+getProgress()+".png");
  }


  public int getProgress() {
    progress = item.progress / maxProgress;
    return (int) (progress/0.125) + 1;
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
      heldItem.setPosition(gameObject.position.x + (gameObject.PhysicalWidth / 2) - 12,
          gameObject.position.y + (gameObject.getHeight() / 2) + 5);
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
    if (currentRecipe != null && interacted) {
      Cut(dt);
    }
    choppingSFX.DoSoundCheck();


  }
}
