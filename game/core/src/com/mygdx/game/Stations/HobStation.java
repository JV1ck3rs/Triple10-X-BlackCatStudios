package com.mygdx.game.Stations;


import com.badlogic.gdx.Gdx;
import com.mygdx.game.Core.BlackTexture;
import com.mygdx.game.Core.GameObject;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;

import java.util.ArrayList;
import java.util.Arrays;

public class HobStation extends Station {

  boolean interacted;
  boolean ready;
  public static ArrayList<ItemEnum> ItemWhiteList;
  public float progress;
  public float maxProgress;
  public int imageSize = 14;

  public HobStation() {
    interacted = false;
    ready = false;
    maxProgress = 10;
    if (ItemWhiteList == null) {
      ItemWhiteList = new ArrayList<>(Arrays.asList(ItemEnum.RawPatty, ItemEnum.CookedPatty));
    }
  }


  @Override
  public boolean GiveItem(Item item) {
    if (this.item != null) {
      return false;
    }
    changeItem(item);
    checkItem();
    bubble.isVisible = true;
    return true;
  }


  @Override
  public Item RetrieveItem() {
    returnItem = item;
    deleteItem();
    currentRecipe = null;
    bubble.isVisible = false;
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


  public void checkItem() {
    if (ItemWhiteList.contains(item.name)) {
      currentRecipe = recipes.RecipeMap.get(item.name);
    } else {
      currentRecipe = null;
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
    ready = currentRecipe.RecipeSteps.get(item.step).timeStep(item, dt, interacted, maxProgress);

    if (ready && item.progress == 0) {
      item.step++;
      System.out.println("PRESS SPACE TO FLIP BURGER");
      if (item.step == currentRecipe.RecipeSteps.size()) {
        changeItem(new Item(currentRecipe.endItem));
        checkItem();
      }
      return;
    }
    if (ready) {
      burnItem();
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
  public void Update(float dt) {
    if (currentRecipe != null) {
      Cook(dt);
    }

    interacted = false;
  }
}
