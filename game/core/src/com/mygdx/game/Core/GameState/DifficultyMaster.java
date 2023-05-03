package com.mygdx.game.Core.GameState;


import com.mygdx.game.Core.ValueStructures.CustomerControllerParams;
import com.mygdx.game.Items.ItemEnum;
import java.util.LinkedList;

/**
 * Returns Difficulty settings BlackCatStudio's Code
 *
 * @author Felix Seanor
 * @author Jack Vickers
 */
public class DifficultyMaster {


  /**
   * return a difficulty settings
   *
   * @param difficaulty
   * @return
   * @author Felix Seanor
   * @date 29/04/23
   */
  public static DifficultyState getDifficulty(Difficulty difficaulty) {
    switch (difficaulty) {
      case Relaxing:
        return getRelaxing();
      case Stressful:
        return getStressful();
      case Mindbreaking:
        return getMindBreaking();
    }
    //return default otherwise (prevent crashing at all cost)
    return getStressful();
  }


  public static DifficultyState getMindBreaking() {//Hard
    DifficultyState diff = new DifficultyState();
    CustomerControllerParams CCParams = new CustomerControllerParams();
    ChefParams chefParams = new ChefParams();
    CookingParams cookingParams = new CookingParams();

    CCParams.maxMoney = 1000;
    CCParams.reputation = 2;
    CCParams.moneyStart = 0;
    CCParams.maxCustomersPerWave = 4;
    CCParams.minCustomersPerWave = 3;
    CCParams.moneyPerCustomer = 10;
    CCParams.eatingTime = 5;
    CCParams.orderTypePermissable = new LinkedList<>();
    CCParams.frustrationStart = 60;

    CCParams.orderTypePermissable.add(ItemEnum.BakedPotato);
    CCParams.orderTypePermissable.add(ItemEnum.TomatoOnionLettuceSalad);
    CCParams.orderTypePermissable.add(ItemEnum.Burger);
    CCParams.orderTypePermissable.add(ItemEnum.CheesePizza);

    chefParams.moveSpeed = 200;

    cookingParams.cookSpeed = 2;
    cookingParams.chopspeed = 2f;
    cookingParams.burnSpeed = 1.5f;

    diff.cookingParams = cookingParams;
    diff.ccParams = CCParams;
    diff.chefParams = chefParams;

    return diff;


  }

  public static DifficultyState getStressful() {//Medium
    DifficultyState diff = new DifficultyState();
    CustomerControllerParams CCParams = new CustomerControllerParams();
    ChefParams chefParams = new ChefParams();
    CookingParams cookingParams = new CookingParams();

    CCParams.maxMoney = 2000;
    CCParams.reputation = 3;
    CCParams.moneyStart = 20;
    CCParams.maxCustomersPerWave = 4;
    CCParams.minCustomersPerWave = 2;
    CCParams.moneyPerCustomer = 20;
    CCParams.eatingTime = 15;
    CCParams.orderTypePermissable = new LinkedList<>();
    CCParams.frustrationStart = 90;

    CCParams.orderTypePermissable.add(ItemEnum.BakedPotato);
    CCParams.orderTypePermissable.add(ItemEnum.TomatoOnionLettuceSalad);
    CCParams.orderTypePermissable.add(ItemEnum.Burger);

    chefParams.moveSpeed = 250;

    cookingParams.cookSpeed = 1;
    cookingParams.chopspeed = 1f;
    cookingParams.burnSpeed = 1f;

    diff.cookingParams = cookingParams;
    diff.ccParams = CCParams;
    diff.chefParams = chefParams;

    return diff;

  }

  public static DifficultyState getRelaxing() {//Easy
    DifficultyState diff = new DifficultyState();
    CustomerControllerParams CCParams = new CustomerControllerParams();
    ChefParams chefParams = new ChefParams();
    CookingParams cookingParams = new CookingParams();

    CCParams.maxMoney = 2000;
    CCParams.reputation = 5;
    CCParams.moneyStart = 50;
    CCParams.maxCustomersPerWave = 2;
    CCParams.minCustomersPerWave = 1;
    CCParams.moneyPerCustomer = 25;
    CCParams.eatingTime = 15;
    CCParams.orderTypePermissable = new LinkedList<>();
    CCParams.frustrationStart = 120;

    CCParams.orderTypePermissable.add(ItemEnum.BakedPotato);
    CCParams.orderTypePermissable.add(ItemEnum.Burger);

    chefParams.moveSpeed = 280;

    cookingParams.cookSpeed = .75f;
    cookingParams.chopspeed = .75f;
    cookingParams.burnSpeed = .75f;

    diff.cookingParams = cookingParams;
    diff.ccParams = CCParams;
    diff.chefParams = chefParams;

    return diff;
  }


}
