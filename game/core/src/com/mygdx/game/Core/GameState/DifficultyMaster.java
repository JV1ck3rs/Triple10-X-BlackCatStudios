package com.mygdx.game.Core.GameState;


import com.mygdx.game.Core.ValueStructures.CustomerControllerParams;
import com.mygdx.game.Items.ItemEnum;
import java.util.LinkedList;

/**
 * Returns Difficulty settings
 * BlackCatStudio's Code
 * @author Felix Seanor
 * @author Jack Vickers
 */
public class DifficultyMaster {


  /**
   * return a difficulty settings
   * @param difficaulty
   * @return
   * @author Felix Seanor
   */
  public static DifficultyState getDifficulty(Difficulty difficaulty){
      switch (difficaulty)
      {
        case Relaxing:
          return  getRelaxing();
        case Stressful:
          return  getStressful();
        case Mindbreaking:
          return getMindBreaking();
      }
      //return default otherwise (prevent crashing at all cost)
      return getStressful();
  }


  public static DifficultyState getMindBreaking(){//Hard
    DifficultyState diff = new DifficultyState();
    CustomerControllerParams CCParams = new CustomerControllerParams();
    ChefParams chefParams = new ChefParams();
    CookingParams cookingParams = new CookingParams();

    CCParams.MaxMoney = 1000;
    CCParams.Reputation = 2;
    CCParams.MoneyStart = 0;
    CCParams.MaxCustomersPerWave = 4;
    CCParams.MinCustomersPerWave = 3;
    CCParams.MoneyPerCustomer = 10;
    CCParams.EatingTime = 5;
    CCParams.OrderTypePermissable = new LinkedList<>();
    CCParams.FrustrationStart = 60;

    CCParams.OrderTypePermissable.add(ItemEnum.BakedPotato);
    CCParams.OrderTypePermissable.add(ItemEnum.TomatoOnionLettuceSalad);
    CCParams.OrderTypePermissable.add(ItemEnum.Burger);
    CCParams.OrderTypePermissable.add(ItemEnum.CheesePizza);



    chefParams.MoveSpeed = 200;

    cookingParams.CookSpeed = 2;
    cookingParams.ChopSpeed = 1.5f;
    cookingParams.BurnSpeed = 1.5f;

    diff.cookingParams = cookingParams;
    diff.ccParams = CCParams;
    diff.chefParams = chefParams;

    return diff;


  }

  public static DifficultyState getStressful(){//Medium
    DifficultyState diff = new DifficultyState();
    CustomerControllerParams CCParams = new CustomerControllerParams();
    ChefParams chefParams = new ChefParams();
    CookingParams cookingParams = new CookingParams();

    CCParams.MaxMoney = 2000;
    CCParams.Reputation = 3;
    CCParams.MoneyStart = 20;
    CCParams.MaxCustomersPerWave = 4;
    CCParams.MinCustomersPerWave = 2;
    CCParams.MoneyPerCustomer = 20;
    CCParams.EatingTime = 15;
    CCParams.OrderTypePermissable = new LinkedList<>();
    CCParams.FrustrationStart = 90;

    CCParams.OrderTypePermissable.add(ItemEnum.BakedPotato);
    CCParams.OrderTypePermissable.add(ItemEnum.TomatoOnionLettuceSalad);
    CCParams.OrderTypePermissable.add(ItemEnum.Burger);



    chefParams.MoveSpeed = 250;

    cookingParams.CookSpeed = 1;
    cookingParams.ChopSpeed = 1f;
    cookingParams.BurnSpeed = 1f;

    diff.cookingParams = cookingParams;
    diff.ccParams = CCParams;
    diff.chefParams = chefParams;


    return diff;

  }

  public  static DifficultyState getRelaxing(){//Easy
    DifficultyState diff = new DifficultyState();
    CustomerControllerParams CCParams = new CustomerControllerParams();
    ChefParams chefParams = new ChefParams();
    CookingParams cookingParams = new CookingParams();

    CCParams.MaxMoney = 2000;
    CCParams.Reputation = 5;
    CCParams.MoneyStart = 50;
    CCParams.MaxCustomersPerWave = 2;
    CCParams.MinCustomersPerWave = 1;
    CCParams.MoneyPerCustomer = 25;
    CCParams.EatingTime = 15;
    CCParams.OrderTypePermissable = new LinkedList<>();
    CCParams.FrustrationStart = 120;

    CCParams.OrderTypePermissable.add(ItemEnum.BakedPotato);
    CCParams.OrderTypePermissable.add(ItemEnum.Burger);



    chefParams.MoveSpeed = 280;

    cookingParams.CookSpeed = .75f;
    cookingParams.ChopSpeed = .75f;
    cookingParams.BurnSpeed = .75f;


    diff.cookingParams = cookingParams;
    diff.ccParams = CCParams;
    diff.chefParams = chefParams;


    return diff;
  }


}
