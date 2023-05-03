package com.mygdx.game.Core.GameState;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Items.ItemEnum;
import java.io.Serializable;

/**
 * Saved data for customer groups BlackCatStudio's Code
 *
 * @author Felix Seanor
 * @author Jack Vickers
 * @date 25 /04/23
 */
public class CustomerGroupState implements Serializable {

  /**
   * The Orders.
   */
  public ItemEnum[] orders;
  /**
   * The Table.
   */
  public int table;
  /**
   * The Customer start id.
   */
  public int CustomerStartID;
  /**
   * The Frustration.
   */
  public float frustration;
  /**
   * The Customer positions.
   */
  public Vector2[] customerPositions;
  /**
   * The Customers in group ordering.
   */
  public int[] customersInGroupOrdering;

  /**
   * The Leaving.
   */
  public boolean leaving;

  /**
   * The Num customers walking to table.
   */
// Saves the following instead of the actual customer objects because they are not serializable
  public int numCustomersWalkingToTable;


  @Override
  public boolean equals(Object obj) {

    if (!(obj instanceof CustomerGroupState)) {
      return false;
    }

    boolean eq = true;

    for (int i = 0; i < orders.length; i++) {
      eq &= orders[i] == ((CustomerGroupState) obj).orders[i];
    }

    eq &= table == ((CustomerGroupState) obj).table;
    eq &= CustomerStartID == ((CustomerGroupState) obj).CustomerStartID;
    eq &= frustration == ((CustomerGroupState) obj).frustration;

    for (int i = 0; i < customerPositions.length; i++) {
      eq &= customerPositions[i].epsilonEquals(((CustomerGroupState) obj).customerPositions[i]);
    }

    for (int i = 0; i < customersInGroupOrdering.length; i++) {
      eq &= customersInGroupOrdering[i] == ((CustomerGroupState) obj).customersInGroupOrdering[i];
    }

    return eq;
  }
}
