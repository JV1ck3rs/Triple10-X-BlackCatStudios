package com.mygdx.game.Stations;

import com.mygdx.game.Core.Interactions.Interactable;
import com.mygdx.game.Core.Scriptable;
import com.mygdx.game.Items.Item;


/**
 * This is a trash can to remove unwanted items BlackCatStudio's Code
 *
 * @author Jack Hinton
 * @date 29/04/23
 */
public class TrashCan extends Scriptable implements Interactable {


  /**
   * Give an item to the trashcan
   *
   * @param item Item you want to give
   * @return boolean
   * @author Jack Hinton
   */
  @Override
  public boolean giveItem(Item item) {
    return true;
  }


  /**
   * Retrieve an item from the trashcan
   *
   * @return Item
   * @author Jack Hinton
   */
  @Override
  public Item retrieveItem() {
    return null;
  }


  /**
   * Check if you can retrieve from the trash can
   *
   * @return boolean
   * @author Jack Hinton
   */
  @Override
  public boolean canRetrieve() {
    return false;
  }


  /**
   * Check if you can give an item to the trash can
   *
   * @return boolean
   * @author Jack Hinton
   */
  @Override
  public boolean canGive() {
    return true;
  }


  /**
   * Check if you can interact with the trash can
   *
   * @return boolean
   * @author Jack Hinton
   */
  @Override
  public boolean canInteract() {
    return false;
  }


  /**
   * Interact with the trash can
   *
   * @return float
   * @author Jack Hinton
   */
  @Override
  public float interact() {
    return 0;
  }


}
