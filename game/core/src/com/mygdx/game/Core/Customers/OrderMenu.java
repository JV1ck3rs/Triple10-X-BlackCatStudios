package com.mygdx.game.Core.Customers;

import com.mygdx.game.Items.ItemEnum;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class OrderMenu
{

  List<OrderType> OrderCatagories = new LinkedList<>();
  Random rand;
  int minStock;

  /**
   * Create an order menu with certain specification
   * @param defaultStock Salad and Burger probability
   * @param rareStock    Potato and Pizza Probability
   * @param minStock     Lowest probability allowed
   * @param OrderTypePerishables  What types are allowed
   * @author Felix Seanor
   */
  public OrderMenu(int defaultStock, int rareStock, int minStock,List<ItemEnum> OrderTypePerishables){
    rand = new Random();
    this.minStock = minStock;

    OrderType burgers = new OrderType(defaultStock, minStock, ItemEnum.Burger,ItemEnum.CheeseBurger);
    OrderType salads =  new OrderType(defaultStock, minStock, ItemEnum.LettuceOnionSalad,ItemEnum.LettuceTomatoSalad,ItemEnum.TomatoOnionLettuceSalad);
    OrderType potato =  new OrderType(rareStock,    minStock, ItemEnum.BakedPotato, ItemEnum.MeatBake, ItemEnum.CheeseBake);
    OrderType pizza =   new OrderType(rareStock,    minStock, ItemEnum.CheesePizzaCooked, ItemEnum.MeatPizzaCooked, ItemEnum.VegPizzaCooked);

    if(OrderTypePerishables.contains(ItemEnum.Burger))
      OrderCatagories.add(burgers);
    if(OrderTypePerishables.contains(ItemEnum.TomatoOnionLettuceSalad))
      OrderCatagories.add(salads);
    if(OrderTypePerishables.contains(ItemEnum.BakedPotato))
      OrderCatagories.add(potato);
    if(OrderTypePerishables.contains(ItemEnum.CheesePizza))
    OrderCatagories.add(pizza);
  }

  /**
   * Creates a new order for new customers
   * @param count Number of orders (customer no generally)
   * @param random Randomisation spec, Uniform generally produces a better noise pattern
   * @return List of item orders
   * @author Felix Seanor
   */
  public List<ItemEnum> CreateNewOrder(int count, Randomisation random)
  {
    if(Randomisation.TrueRandom == random)
      return CreateTrueRandomOrder(count);
    else if(Randomisation.Normalised == random)
      return CreateNormalisedOrder(count);

    return new LinkedList<>();

  }

  /**
   * Creates a true random order, each order has no knowledge of previous ones
   * @param count no orders
   * @return
   * @author Felix Seanor
   */
  List<ItemEnum> CreateTrueRandomOrder(int count){
    List<ItemEnum> orders = new LinkedList<>();
    for (int i = 0; i < count; i++) {
      int catagory = rand.nextInt(OrderCatagories.size());
      orders.add(OrderCatagories.get(catagory).GetOrder(rand));
    }
    return orders;
  }

  /**
   * Create a normalised random order. This has knowledge about previous orders and randomly chooses
   * the next order with a higher likelyhood of lower picked foods.
   * @param count no orders
   * @return
   * @author Felix Seanor
   */
  List<ItemEnum> CreateNormalisedOrder(int count){

    List<ItemEnum> orders = new LinkedList<>();
    for (int j = 0; j < count; j++) {

    int totalStock = 0;

    for (OrderType catagory: OrderCatagories
    ) {
      totalStock += catagory.stock;
    }
    int catagoryID = rand.nextInt(totalStock-1);
    for (OrderType catagory:OrderCatagories
    ) {
      catagoryID -=  catagory.stock;

      if(catagoryID<=0)
      {
       orders.add(catagory.GetOrder(rand));
       break;
      }
    }
    }

    return  orders;
  }

  /**
   * Restocks the order and resets the probability
   * @author Felix Seanor
   */
  public void Restock(){
    for (OrderType type: OrderCatagories
    ) {
      type.Restock();
    }
  }

}
