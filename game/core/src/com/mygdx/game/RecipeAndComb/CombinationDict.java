package com.mygdx.game.RecipeAndComb;

import com.mygdx.game.Items.ItemEnum;
import java.util.HashMap;
import java.util.Map;

/**
 * Takes an input of items and returns a new item if applicable BlackCatStudio's Code
 *
 * @author Jack Hinton
 * @author Jack Vickers
 * @date 01/05/23
 */
public class CombinationDict {

  public static CombinationDict combinations;
  public Map<String, ItemEnum> CombinationMap = new HashMap<>();

  /**
   * Method to implement all the combinations.
   *
   * @author Jack Hinton
   */
  public void implementItems() {
    //Salads
    CombinationMap.put(ItemEnum.CutLettuce.name() + " " + ItemEnum.CutTomato.name(),
        ItemEnum.LettuceTomatoSalad);
    CombinationMap.put(ItemEnum.CutLettuce.name() + " " + ItemEnum.CutOnion.name(),
        ItemEnum.LettuceOnionSalad);
    CombinationMap.put(ItemEnum.CutTomato.name() + " " + ItemEnum.CutOnion.name(),
        ItemEnum.TomatoOnionSalad);
    CombinationMap.put(ItemEnum.CutLettuce.name() + " " + ItemEnum.TomatoOnionSalad.name(),
        ItemEnum.TomatoOnionLettuceSalad);
    CombinationMap.put(ItemEnum.CutTomato.name() + " " + ItemEnum.LettuceOnionSalad.name(),
        ItemEnum.TomatoOnionLettuceSalad);
    CombinationMap.put(ItemEnum.LettuceTomatoSalad.name() + " " + ItemEnum.CutOnion.name(),
        ItemEnum.TomatoOnionLettuceSalad);

    //Burgers
    CombinationMap.put(ItemEnum.CookedPatty.name() + " " + ItemEnum.ToastedBuns.name(),
        ItemEnum.Burger);
    CombinationMap.put(ItemEnum.Burger.name() + " " + ItemEnum.Cheese.name(),
        ItemEnum.CheeseBurger);

    //Pizzas
    CombinationMap.put(ItemEnum.PizzaBase.name() + " " + ItemEnum.TomSauce.name(),
        ItemEnum.TomPizza);
    CombinationMap.put(ItemEnum.TomPizza.name() + " " + ItemEnum.Cheese.name(),
        ItemEnum.CheesePizza);
    CombinationMap.put(ItemEnum.CheesePizza.name() + " " + ItemEnum.Mince.name(),
        ItemEnum.MeatPizza);
    CombinationMap.put(ItemEnum.CheesePizza.name() + " " + ItemEnum.CutOnion.name(),
        ItemEnum.VegPizza);

    //Potatoes
    CombinationMap.put(ItemEnum.Cheese.name() + " " + ItemEnum.Potato.name(),
        ItemEnum.CheesePotato);
    CombinationMap.put(ItemEnum.Mince.name() + " " + ItemEnum.Potato.name(), ItemEnum.MeatPotato);
  }

  /**
   * Constructor for CombinationDict.
   *
   * @author Jack Hinton
   */
  public CombinationDict() {
    if (combinations != null) {
      return;
    }
    combinations = this;
  }
}
