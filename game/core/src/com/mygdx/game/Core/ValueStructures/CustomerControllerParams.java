package com.mygdx.game.Core.ValueStructures;

import com.mygdx.game.Items.ItemEnum;
import java.util.List;

/**
 * This is a structure containing parameters to construct the Customer Controller BlackCatStudio's
 * Code
 *
 * @author Felix Seanor
 * @date 18 /04/23
 */
public class CustomerControllerParams {

  /**
   * The Money start.
   */
  public int moneyStart;
  /**
   * The Max money.
   */
  public int maxMoney;
  /**
   * The Reputation.
   */
  public int reputation;
  /**
   * The Money per customer.
   */
  public int moneyPerCustomer = 10;

  /**
   * The No customers.
   */
//Number of customers
  public int noCustomers;

  /**
   * The Max customers per wave.
   */
//The maximum number customers per wave (MAX 4)
  public int maxCustomersPerWave;


  /**
   * The Min customers per wave.
   */
//Min 1
  public int minCustomersPerWave;

  /**
   * The Order type permissable.
   */
  public List<ItemEnum> orderTypePermissable;
  /**
   * The Eating time.
   */
  public int eatingTime = 10;
  /**
   * The Tables.
   */
  public int tables = 4;
  /**
   * The Frustration start.
   */
  public int frustrationStart = 90;

}
