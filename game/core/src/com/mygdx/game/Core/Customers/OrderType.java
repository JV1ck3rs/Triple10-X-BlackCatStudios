package com.mygdx.game.Core.Customers;

import com.mygdx.game.Items.ItemEnum;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * This is a type of order and contains how many times in relation to others this has been ordered
 * BlackCatStudio's Code
 *
 * @author Felix Seanor
 * @date 23 /04/23
 */
public class OrderType {

  /**
   * The Order class.
   */
//Order class burger salad ect
  public ItemEnum orderClass;

  /**
   * The Orderables.
   */
// List of all orderable types in the class
  public List<ItemEnum> orderables;

  /**
   * The Stock.
   */
//Probability counter for this class to be picked next
  public int stock;

  /**
   * The Def stock.
   */
//Default stock for restocking
  int defStock;

  /**
   * The Min stock.
   */
//Lowest "probability allowed"
  int minStock;


  /**
   * Creates a new order type
   *
   * @param stock    Probability start
   * @param minStock lowest probability
   * @param orders   what orders are allowed
   * @author Felix Seanor
   */
  public OrderType(int stock, int minStock, ItemEnum... orders) {
    orderables = new LinkedList<>(Arrays.asList(orders));
    this.stock = stock;
    this.minStock = minStock;
    defStock = stock;
  }

  /**
   * Reduce the probability after an order
   *
   * @param count the count
   * @author Felix Seanor
   */
  public void takeStock(int count) {
    stock = Math.max(stock - count, minStock);

  }


  /**
   * Get a new random single order
   *
   * @param rand randomisation class
   * @return new ItemEnum for order
   */
  public ItemEnum getOrder(Random rand) {
    int orderID = rand.nextInt(orderables.size());
    takeStock(1);
    return orderables.get(orderID);

  }

  /**
   * Reset stock "probability"
   *
   * @author Felix Seanor
   */
  public void restock() {
    stock = defStock;
  }

}
