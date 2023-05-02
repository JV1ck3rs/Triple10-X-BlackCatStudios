package com.mygdx.game.Stations;

import com.mygdx.game.Core.Interactions.Interactable;
import com.mygdx.game.Core.Scriptable;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;

/**
 * This lets chef pick up raw ingredients
 * BlackCatStudio's Code
 * @author Jack Hinton
 * @date 29/04/23
 */
public class FoodCrate extends Scriptable implements Interactable {
  // TODO: Write more tests for this class

  private ItemEnum ingredient;

  /**
   * Creates a food crate based off of the given Enum
   * @param ingredient item you want the crate to dispense
   * @Author Jack Hinton
   */
  public FoodCrate(ItemEnum ingredient) {
    this.ingredient = ingredient;
  }


  /**
   * Give the crate an item
   * @param item - Item you want to give
   * @return boolean
   * @Author Jack Hinton
   */
  @Override
  public boolean GiveItem(Item item) {
    return false;
  }


  /**
   * Retrieve an item from the crate
   * @return Item
   * @Author Jack Hinton
   */
  @Override
  public Item RetrieveItem() {
    return new Item(ingredient);
  }


  /**
   * Checks if the chef can retrieve an item from the crate
   * @return boolean
   * @Author Jack Hinton
   */
  @Override
  public boolean CanRetrieve() {
    return true;
  }


  /**
   * Checks if the chef can give the crate an item
   * @return boolean
   * @Author Jack Hinton
   */
  @Override
  public boolean CanGive() {
    return false;
  }


  /**
   * Checks if the chef can interact with the crate
   * @return boolean
   * @Author Jack Hinton
   */
  @Override
  public boolean CanInteract() {
    return false;
  }


  /**
   * Interact with the crate
   * @return float
   * @Author Jack Hinton
   */
  @Override
  public float Interact() {
    return 0;
  }
}
