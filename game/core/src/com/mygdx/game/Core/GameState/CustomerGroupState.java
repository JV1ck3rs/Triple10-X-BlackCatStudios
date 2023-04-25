package com.mygdx.game.Core.GameState;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Customer;
import com.mygdx.game.Items.ItemEnum;
import java.io.Serializable;
import java.util.List;

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

}
