package com.mygdx.game.Core.GameState;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Items.ItemEnum;
import java.io.Serializable;

/**
 * Saved data for customer groups BlackCatStudio's Code
 *
 * @author Felix Seanor
 * @author Jack Vickers
 * @date 25/04/23
 */
public class CustomerGroupState implements Serializable {

  public ItemEnum[] orders;
  public int Table;
  public int CustomerStartID;
  public float frustration;
  public Vector2[] customerPositions;
  public int[] customersInGroupOrdering;

  public boolean leaving;

  // Saves the following instead of the actual customer objects because they are not serializable
  public int NumCustomersWalkingToTable;


  @Override
  public boolean equals(Object obj) {

    if (!(obj instanceof CustomerGroupState)) {
      return false;
    }

    boolean eq = true;

    for (int i = 0; i < orders.length; i++) {
      eq &= orders[i] == ((CustomerGroupState) obj).orders[i];
    }

    eq &= Table == ((CustomerGroupState) obj).Table;
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
