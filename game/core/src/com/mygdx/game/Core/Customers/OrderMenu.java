package com.mygdx.game.Core.Customers;

import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * This manages the foods that can be given and offers different randomisation patterns
 * BlackCatStudio's Code
 *
 * @author Felix Seanor
 * @date 23 /04/23
 */
public class OrderMenu {

  /**
   * The Order catagories.
   */
  List<OrderType> orderCatagories = new LinkedList<>();
  /**
   * The Rand.
   */
  Random rand;
  /**
   * The Min stock.
   */
  int minStock;
  /**
   * The Burgers.
   */
  OrderType burgers;
  /**
   * The Salads.
   */
  OrderType salads;
  /**
   * The Potato.
   */
  OrderType potato;
  /**
   * The Pizza.
   */
  OrderType pizza;
  /**
   * The Completed recipes.
   */
  LinkedList<ItemEnum> completedRecipes;

  /**
   * Create an order menu with certain specification
   *
   * @param defaultStock         Salad and Burger probability
   * @param rareStock            Potato and Pizza Probability
   * @param minStock             Lowest probability allowed
   * @param OrderTypePerishables What types are allowed
   * @author Felix Seanor
   */
  public OrderMenu(int defaultStock, int rareStock, int minStock,
      List<ItemEnum> OrderTypePerishables) {
    rand = new Random();
    this.minStock = minStock;

    burgers = new OrderType(defaultStock, minStock, ItemEnum.Burger, ItemEnum.CheeseBurger);
    salads = new OrderType(defaultStock, minStock, ItemEnum.LettuceOnionSalad,
        ItemEnum.LettuceTomatoSalad, ItemEnum.TomatoOnionLettuceSalad);
    potato = new OrderType(rareStock, minStock, ItemEnum.BakedPotato, ItemEnum.MeatBake,
        ItemEnum.CheeseBake);
    pizza = new OrderType(rareStock, minStock, ItemEnum.CheesePizzaCooked, ItemEnum.MeatPizzaCooked,
        ItemEnum.VegPizzaCooked);

    if (OrderTypePerishables.contains(ItemEnum.Burger)) {
      orderCatagories.add(burgers);
    }
    if (OrderTypePerishables.contains(ItemEnum.TomatoOnionLettuceSalad)) {
      orderCatagories.add(salads);
    }
    if (OrderTypePerishables.contains(ItemEnum.BakedPotato)) {
      orderCatagories.add(potato);
    }
    if (OrderTypePerishables.contains(ItemEnum.CheesePizza)) {
      orderCatagories.add(pizza);
    }
  }

  /**
   * Used to add potatoes and pizzas to the order menu when an oven has been added
   *
   * @author Jack Hinton
   */
  public void ovenAdded() {
    orderCatagories.add(potato);
    orderCatagories.add(pizza);
  }

  /**
   * Creates a new order for new customers
   *
   * @param count  Number of orders (customer no generally)
   * @param random Randomisation spec, Uniform generally produces a better noise pattern
   * @return List of item orders
   * @author Felix Seanor
   */
  public List<ItemEnum> createNewOrder(int count, Randomisation random) {
    if (Randomisation.TrueRandom == random) {
      return createTrueRandomOrder(count);
    } else if (Randomisation.Normalised == random) {
      return createNormalisedOrder(count);
    }

    return new LinkedList<>();

  }

  /**
   * Creates a true random order, each order has no knowledge of previous ones
   *
   * @param count no orders
   * @return list
   * @author Felix Seanor
   */
  List<ItemEnum> createTrueRandomOrder(int count) {
    List<ItemEnum> orders = new LinkedList<>();
    for (int i = 0; i < count; i++) {
      int catagory = rand.nextInt(orderCatagories.size());
      orders.add(orderCatagories.get(catagory).getOrder(rand));
    }
    return orders;
  }

  /**
   * Create a normalised random order. This has knowledge about previous orders and randomly chooses
   * the next order with a higher likelyhood of lower picked foods.
   *
   * @param count no orders
   * @return list
   * @author Felix Seanor
   */
  List<ItemEnum> createNormalisedOrder(int count) {

    List<ItemEnum> orders = new LinkedList<>();
    for (int j = 0; j < count; j++) {

      int totalStock = 0;

      for (OrderType catagory : orderCatagories
      ) {
        totalStock += catagory.stock;
      }
      int catagoryID = rand.nextInt(totalStock - 1);
      for (OrderType catagory : orderCatagories
      ) {
        catagoryID -= catagory.stock;

        if (catagoryID <= 0) {
          orders.add(catagory.getOrder(rand));
          break;
        }
      }
    }

    return orders;
  }

  /**
   * Restocks the order and resets the probability
   *
   * @author Felix Seanor
   */
  public void restock() {
    for (OrderType type : orderCatagories
    ) {
      type.restock();
    }
  }

  /**
   * Gets all order types.
   *
   * @return the all order types
   */
  public LinkedList<OrderType> getAllOrderTypes() {
    LinkedList<OrderType> orderTypes = new LinkedList<>();
    orderTypes.add(burgers);
    orderTypes.add(salads);
    orderTypes.add(potato);
    orderTypes.add(pizza);
    return orderTypes;
  }


  /**
   * Gets super from dish.
   *
   * @param dish the dish
   * @return the super from dish
   */
  public Item getSuperFromDish(ItemEnum dish) {
    if (burgers.orderables.contains(dish)) {
      return new Item(ItemEnum.SuperBurger);
    } else if (salads.orderables.contains(dish)) {
      return new Item(ItemEnum.SuperSalad);
    } else if (pizza.orderables.contains(dish)) {
      return new Item(ItemEnum.SuperPizza);
    } else if (potato.orderables.contains(dish)) {
      return new Item(ItemEnum.SuperPotato);
    } else {
      return new Item(ItemEnum.valueOf(dish.name()));
    }
  }

  /**
   * Gets order type from super.
   *
   * @param item the item
   * @return the order type from super
   */
  public OrderType getOrderTypeFromSuper(Item item) {
    if (item.name == ItemEnum.SuperBurger) {
      return burgers;
    } else if (item.name == ItemEnum.SuperSalad) {
      return salads;
    } else if (item.name == ItemEnum.SuperPotato) {
      return potato;
    } else {
      return pizza;
    }
  }

}
