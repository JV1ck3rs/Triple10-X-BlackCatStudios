package com.mygdx.game.Stations;

import com.mygdx.game.Core.Interactions.Interactable;
import com.mygdx.game.Core.Scriptable;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;

/**
 * This lets chef pick up raw ingredients BlackCatStudio's Code
 *
 * @author Jack Hinton
 * @date 29 /04/23
 */
public class FoodCrate extends Scriptable implements Interactable {

  private ItemEnum ingredient;

  /**
   * Creates a food crate based off of the given Enum
   *
   * @param ingredient item you want the crate to dispense
   * @author Jack Hinton
   */
  public FoodCrate(ItemEnum ingredient) {
    this.ingredient = ingredient;
  }


  /**
   * Give the crate an item
   *
   * @param item - Item you want to give
   * @return boolean
   * @author Jack Hinton
   */
  @Override
  public boolean giveItem(Item item) {
    return false;
  }


  /**
   * Retrieve an item from the crate
   *
   * @return Item
   * @author Jack Hinton
   */
  @Override
  public Item retrieveItem() {
    return new Item(ingredient);
  }


  /**
   * Checks if the chef can retrieve an item from the crate
   *
   * @return boolean
   * @author Jack Hinton
   */
  @Override
  public boolean canRetrieve() {
    return true;
  }


  /**
   * Checks if the chef can give the crate an item
   *
   * @return boolean
   * @author Jack Hinton
   */
  @Override
  public boolean canGive() {
    return false;
  }


  /**
   * Checks if the chef can interact with the crate
   *
   * @return boolean
   * @author Jack Hinton
   */
  @Override
  public boolean canInteract() {
    return false;
  }


  /**
   * Interact with the crate
   *
   * @return float
   * @author Jack Hinton
   */
  @Override
  public float interact() {
    return 0;
  }
}
