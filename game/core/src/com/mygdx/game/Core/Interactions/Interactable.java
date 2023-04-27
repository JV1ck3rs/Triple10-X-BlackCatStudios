package com.mygdx.game.Core.Interactions;

import com.mygdx.game.Items.Item;
import java.util.Optional;

/**
 * Interface for interactions. An interaction looks to find this on GameObjects Scripts
 * BlackCatStudio's Code
 * @author Felix Seanor
 * @author Jack Hinton
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
   * @return
   * @author Jack Hinton
   */
  public boolean Interact();
}
