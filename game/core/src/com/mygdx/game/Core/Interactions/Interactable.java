package com.mygdx.game.Core.Interactions;

import com.mygdx.game.Items.Item;

/**
 * Interface for interactions. An interaction looks to find this on GameObjects Scripts
 * BlackCatStudio's Code
 * @author Felix Seanor
 * @author Jack Hinton
 * @date 23/04/23
 */
public interface Interactable {

  /**
   * Can this object be retrieved from
   * @return
   */
  public boolean CanRetrieve();

  /**
   * Can this object be given to
   * @return
   */
  public boolean CanGive();

  /**
   * Can this object be given to
   * @return
   */
  public boolean CanInteract();

  /**
   * Retrieve the item
   * @return
   * @author Jack Hinton
   */

  public Item RetrieveItem();

  /**
   * Give item to object
   * @param item
   * @return
   * @author Felix Seanor
   */
  public boolean GiveItem(Item item);

  /**
   * Interact with object
   * @return float - So the chef knows how long to be locked for if they need to be there for an interaction e.g. cutting
   * @author Jack Hinton
   */
  public float Interact();
}
