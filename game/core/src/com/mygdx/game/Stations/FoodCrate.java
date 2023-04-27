package com.mygdx.game.Stations;

import com.mygdx.game.Core.Interactions.Interactable;
import com.mygdx.game.Core.Scriptable;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;

/**
 * This lets chef pick up raw ingredients
 * BlackCatStudio's Code
 * @author Jack Hinton
 */
public class FoodCrate extends Scriptable implements Interactable {
  // TODO: Write more tests for this class

  private ItemEnum ingredient;


  public FoodCrate(ItemEnum ingredient) {
    this.ingredient = ingredient;
  }


  @Override
  public boolean GiveItem(Item item) {
    return false;
  }


  @Override
  public Item RetrieveItem() {
    return new Item(ingredient);
  }


  @Override
  public boolean CanRetrieve() {
    return true;
  }


  @Override
  public boolean CanGive() {
    return false;
  }


  @Override
  public boolean CanInteract() {
    return false;
  }


  @Override
  public boolean Interact() {
    return false;
  }
}
