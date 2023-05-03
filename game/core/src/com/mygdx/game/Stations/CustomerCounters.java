package com.mygdx.game.Stations;

import com.mygdx.game.Core.GameState.CookingParams;
import com.mygdx.game.Core.Rendering.BlackTexture;
import com.mygdx.game.Core.Rendering.GameObject;
import com.mygdx.game.Items.Item;
import java.util.function.Function;

/**
 * This handles serving food to customers BlackCatStudio's Code
 *
 * @author Jack Hinton
 * @date 01 /05/23
 */
public class CustomerCounters extends Station {


  /**
   * Customer Controller, can the item give be accepted
   */
  Function<Item, Boolean> giveItemToCustomer;

  /**
   * Creates a customer counter
   *
   * @param script Script to give the item to a customer
   * @param params Cooking parameters i.e cooking speed, burn speed etc.
   * @author Jack Hinton
   * @author Felix Seanor
   */
  public CustomerCounters(Function<Item, Boolean> script, CookingParams params) {

    super(params);
    this.giveItemToCustomer = script;

  }

  /**
   * Give an item to the customer counter
   *
   * @param item The item you want to give to the counter
   * @return boolean
   * @author Jack Hinton
   */
  @Override
  public boolean giveItem(Item item) {
    changeItem(item);
    giveFood();
    return true;
  }


  /**
   * Retrieve the item from the counter
   *
   * @return Item
   * @author Jack Hinton
   */
  @Override
  public Item retrieveItem() {
    returnItem = item;
    deleteItem();
    currentRecipe = null;
    return returnItem;
  }


  /**
   * Checks if the chef can retrieve from the counter
   *
   * @return boolean
   * @author Jack Hinton
   */
  @Override
  public boolean canRetrieve() {
    return item != null;
  }


  /**
   * Checks if the chef can give the counter an item
   *
   * @return boolean
   * @author Jack Hinton
   */
  @Override
  public boolean canGive() {
    return item == null;
  }


  /**
   * Checks if the user can interact with the counter
   *
   * @return boolean
   * @author Jack Hinton
   */
  @Override
  public boolean canInteract() {
    return false;
  }


  /**
   * Interact with the counter
   *
   * @return float
   * @author Jack Hinton
   */
  @Override
  public float interact() {
    return 0;
  }


  /**
   * Give food to a customer
   *
   * @author Jack Hinton
   */
  public void giveFood() {
    boolean answer = giveItemToCustomer.apply(item);
    if (answer) {
      deleteItem();
    }
  }


  /**
   * Updates the pictures on the counter
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
      heldItem.setPosition(gameObject.position.x,
          gameObject.position.y + ((gameObject.getHeight() / 2) - 10));
    } else {
      heldItem.image = new BlackTexture(Item.getItemPath(item.name));
      heldItem.image.setSize(imageSize, imageSize);
    }
  }


  /**
   * Moves the animation
   *
   * @author Jack Hinton
   */
  @Override
  public void moveAnimation() {
    return;
  }


  /**
   * Updates the customer counter
   *
   * @param dt delta time
   * @author Jack Hinton
   */
  @Override
  public void update(float dt) {
  }
}
