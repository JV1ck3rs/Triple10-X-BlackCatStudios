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
  public Map<String, ItemEnum> combinationMap = new HashMap<>();

  /**
   * Method to implement all the combinations.
   *
   * @author Jack Hinton
   */
  public void implementItems() {
    //Salads
    combinationMap.put(ItemEnum.CutLettuce.name() + " " + ItemEnum.CutTomato.name(),
        ItemEnum.LettuceTomatoSalad);
    combinationMap.put(ItemEnum.CutLettuce.name() + " " + ItemEnum.CutOnion.name(),
        ItemEnum.LettuceOnionSalad);
    combinationMap.put(ItemEnum.CutTomato.name() + " " + ItemEnum.CutOnion.name(),
        ItemEnum.TomatoOnionSalad);
    combinationMap.put(ItemEnum.CutLettuce.name() + " " + ItemEnum.TomatoOnionSalad.name(),
        ItemEnum.TomatoOnionLettuceSalad);
    combinationMap.put(ItemEnum.CutTomato.name() + " " + ItemEnum.LettuceOnionSalad.name(),
        ItemEnum.TomatoOnionLettuceSalad);
    combinationMap.put(ItemEnum.LettuceTomatoSalad.name() + " " + ItemEnum.CutOnion.name(),
        ItemEnum.TomatoOnionLettuceSalad);

    //Burgers
    combinationMap.put(ItemEnum.CookedPatty.name() + " " + ItemEnum.ToastedBuns.name(),
        ItemEnum.Burger);
    combinationMap.put(ItemEnum.Burger.name() + " " + ItemEnum.Cheese.name(),
        ItemEnum.CheeseBurger);

    //Pizzas
    combinationMap.put(ItemEnum.PizzaBase.name() + " " + ItemEnum.TomSauce.name(),
        ItemEnum.TomPizza);
    combinationMap.put(ItemEnum.TomPizza.name() + " " + ItemEnum.Cheese.name(),
        ItemEnum.CheesePizza);
    combinationMap.put(ItemEnum.CheesePizza.name() + " " + ItemEnum.Mince.name(),
        ItemEnum.MeatPizza);
    combinationMap.put(ItemEnum.CheesePizza.name() + " " + ItemEnum.CutOnion.name(),
        ItemEnum.VegPizza);

    //Potatoes
    combinationMap.put(ItemEnum.Cheese.name() + " " + ItemEnum.Potato.name(),
        ItemEnum.CheesePotato);
    combinationMap.put(ItemEnum.Mince.name() + " " + ItemEnum.Potato.name(), ItemEnum.MeatPotato);
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
