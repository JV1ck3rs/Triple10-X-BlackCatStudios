package com.mygdx.game.Core.ValueStructures;

import com.mygdx.game.Items.ItemEnum;
import java.util.List;

/**
 * This is a structure containing parameters to construct the Customer Controller BlackCatStudio's
 * Code
 *
 * @author Felix Seanor
 * @date 18/04/23
 */
public class CustomerControllerParams {

  public int moneyStart;
  public int maxMoney;
  public int reputation;
  public int moneyPerCustomer = 10;

  //Number of customers
  public int noCustomers;

  //The maximum number customers per wave (MAX 4)
  public int maxCustomersPerWave;


  //Min 1
  public int minCustomersPerWave;

  public List<ItemEnum> orderTypePermissable;
  public int eatingTime = 10;
  public int tables = 4;
  public int frustrationStart = 90;

}
