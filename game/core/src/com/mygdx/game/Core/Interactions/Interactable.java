package com.mygdx.game.Core.Interactions;

import com.mygdx.game.Items.Item;

/**
 * Interface for interactions. An interaction looks to find this on GameObjects Scripts
 * BlackCatStudio's Code
 *
 * @author Felix Seanor
 * @author Jack Hinton
 * @date 23 /04/23
 */
public interface Interactable {

  /**
   * Can this object be retrieved from
   *
   * @return boolean
   */
  public boolean canRetrieve();

  /**
   * Can this object be given to
   *
   * @return boolean
   */
  public boolean canGive();

  /**
   * Can this object be given to
   *
   * @return boolean
   */
  public boolean canInteract();

  /**
   * Retrieve the item
   *
   * @return item
   * @author Jack Hinton
   */
  public Item retrieveItem();

  /**
   * Give item to object
   *
   * @param item the item
   * @return boolean
   * @author Felix Seanor
   */
  public boolean giveItem(Item item);

  /**
   * Interact with object
   *
   * @return float - So the chef knows how long to be locked for if they need to be there for an
   * interaction e.g. cutting
   * @author Jack Hinton
   */
  public float interact();
}
