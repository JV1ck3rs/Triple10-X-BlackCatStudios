package com.mygdx.game.Core.GameState;

import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;

/**
 * Saved data for items BlackCatStudio's Code
 *
 * @author Felix Seanor
 * @author Jack Vickers
 * @date 25 /04/23
 */
public class ItemState implements java.io.Serializable {

  /**
   * The Item.
   */
  public ItemEnum item;
  /**
   * The Step.
   */
  public int step;
  /**
   * The Progress.
   */
  public float progress;

  /**
   * Instantiates a new Item state.
   *
   * @param obj the obj
   */
  public ItemState(Item obj) {
    if (obj == null) {
      item = null;
      step = 0;
      progress = 0;

      return;
    }
    item = obj.name;
    step = obj.step;
    progress = obj.progress;

  }

  @Override
  public boolean equals(Object obj) {
    return this.item == ((ItemState) obj).item &&
        this.step == ((ItemState) obj).step &&
        this.progress == ((ItemState) obj).progress;
  }

}
