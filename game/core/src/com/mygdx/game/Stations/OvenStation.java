package com.mygdx.game.Stations;

import com.mygdx.game.Core.BlackTexture;
import com.mygdx.game.Core.CustomerController;
import com.mygdx.game.Core.GameObject;
import com.mygdx.game.Core.GameState.CookingParams;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;

import com.mygdx.game.soundFrame;
import com.mygdx.game.soundFrame.soundsEnum;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Bakes potatoes and pizzas BlackCatStudio's Code
 *
 * @author Jack Hinton
 */
public class OvenStation extends Station {

  boolean interacted;
  boolean ready;
  public float maxProgress;
  public float progress;
  public static ArrayList<ItemEnum> ItemWhiteList;
  Consumer<Boolean> OvenMade;


  public OvenStation(CookingParams params, Consumer<Boolean> customerController) {
    super(params);
    OvenMade = customerController;
    ready = false;
    maxProgress = 10;
    animation = new GameObject(new BlackTexture("Items/OvenActive.png"));
    animation.isVisible = false;
    animation.image.layer= -1;
    if (ItemWhiteList == null) {
      ItemWhiteList = new ArrayList<>(
          Arrays.asList(ItemEnum.Potato, ItemEnum.CheesePotato, ItemEnum.MeatPotato,
              ItemEnum.CheesePizza, ItemEnum.MeatPizza, ItemEnum.VegPizza,
              ItemEnum.CheesePizzaCooked,
              ItemEnum.MeatPizzaCooked, ItemEnum.VegPizzaCooked, ItemEnum.BakedPotato,
              ItemEnum.CheeseBake,
              ItemEnum.MeatBake));
    }
  }


  @Override
  public boolean GiveItem(Item item) {
    if (getLocked()) {
      boolean repaired = checkRepairTool(item);
      if (repaired) {
        if (numOvens < 1) {
          OvenMade.accept(true);
          numOvens++;
        }
        deleteItem();
      }
      return repaired;
    }
    if (this.item != null || !ItemWhiteList.contains(item.name)) {
      return false;
    }
    changeItem(item);
    checkItem();
    animation.isVisible = true;
    return true;
  }


  @Override
  public Item RetrieveItem() {
    bubble.isVisible = false;
    bubble4.isVisible = false;
    returnItem = item;
    deleteItem();
    currentRecipe = null;
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
      currentRecipe = recipes.RecipeMap.get(item.name);
      bubble.isVisible = true;
      if (item.name == ItemEnum.CheesePizzaCooked || item.name == ItemEnum.MeatPizzaCooked || item.name == ItemEnum.VegPizzaCooked) {
        bubble4.isVisible = true;
      }
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


  @Override
  public void updatePictures() {
    return;
  }


  @Override
  public void moveAnim() {
    animation.setPosition(gameObject.position.x, gameObject.position.y);
  }


  @Override
  public void Update(float dt) {
    if (currentRecipe != null) {
      Cook(dt);
    }
  }
}