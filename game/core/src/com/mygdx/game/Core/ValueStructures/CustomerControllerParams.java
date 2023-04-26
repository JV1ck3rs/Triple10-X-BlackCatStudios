package com.mygdx.game.Core.ValueStructures;

import com.mygdx.game.Items.ItemEnum;
import java.util.List;

public class CustomerControllerParams
{
  public int MoneyStart;
  public int MaxMoney;
  public int Reputation;
  public int MoneyPerCustomer = 10;

  //Number of customers
  public int NoCustomers;

  //The maximum number customers per wave (MAX 4)
  public int MaxCustomersPerWave;


  //Min 1
  public int MinCustomersPerWave;

  public List<ItemEnum> OrderTypePermissable;
  public int EatingTime = 10;
  public int Tables = 4;
  public int FrustrationStart= 90;

}
