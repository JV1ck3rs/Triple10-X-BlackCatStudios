package com.mygdx.game.Stations;

import com.mygdx.game.Core.BlackTexture;
import com.mygdx.game.Core.GameObject;
import com.mygdx.game.Core.GameState.CookingParams;
import com.mygdx.game.Items.Item;

import java.util.function.Function;

/**
 * This handles serving food to customers BlackCatStudio's Code
 *
 * @author Jack Hinton
 */
public class CustomerCounters extends Station {


  /**
   * Customer Controller, can the item give be accepted
   */
  Function<Item, Boolean> GiveItemToCustomer;

  public CustomerCounters(Function<Item, Boolean> script, CookingParams params) {

    super(params);
    this.GiveItemToCustomer = script;

  }


  @Override
  public boolean GiveItem(Item item) {
    changeItem(item);
    GiveFood();
    return true;
  }


  @Override
  public Item RetrieveItem() {
    returnItem = item;
    deleteItem();
    currentRecipe = null;
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


  public void GiveFood() {
    boolean answer = GiveItemToCustomer.apply(item);
    if (answer) {
      deleteItem();
    }
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
      heldItem.setPosition(gameObject.position.x,
          gameObject.position.y + ((gameObject.getHeight() / 2) - 10));
    } else {
      heldItem.image = new BlackTexture(Item.GetItemPath(item.name));
      heldItem.image.setSize(imageSize, imageSize);
    }
  }


  @Override
  public void moveAnim() {
    return;
  }


  @Override
  public void Update(float dt) {
  }
}
