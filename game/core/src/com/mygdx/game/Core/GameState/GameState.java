package com.mygdx.game.Core.GameState;

import com.badlogic.gdx.math.Vector2;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class containing saved data of the game, to be saved to disk or loaded back in BlackCatStudio's
 * Code
 *
 * @author Felix Seanor
 * @author Jack Vickers
 * @date 25/04/23
 */
public class GameState implements Serializable {

  public int money;
  public int maxMoney;

  public CustomerGroupState[] customerGroupStates;
  public Vector2[] chefPositions;
  public ItemState[] chefHoldingStacks;//Must be in format of chefs 1 items (if not holding an item at slot N then null)
  public List<List<ItemState>> foodOnCounters;
  public List<Boolean> repairState;
  public Vector2 groupSize;
  public int wave;
  public int maxWave;
  public int maxFrustration;

  public int timer;
  public float seconds;
  public int reputation;
  public int maxReputation;
  public ArrayList<Integer> customersPerWave;
  public Difficulty difficulty;


  public boolean isChefPartEquals(GameState state) {
    boolean eq = true;

    for (int i = 0; i < chefPositions.length; i++) {
      eq &= chefPositions[i].epsilonEquals(state.chefPositions[i]);

    }

    for (int i = 0; i < chefHoldingStacks.length; i++) {
      eq &= chefHoldingStacks[i] == (state.chefHoldingStacks[i]);

    }

    return eq;

  }

  public boolean isCustomerPartEquals(GameState state) {
    boolean eq = true;

    // eq &= difficulty == state.difficulty; gamescreen
    eq &= customersPerWave == state.customersPerWave;
    eq &= maxReputation == state.maxReputation;
    eq &= reputation == state.reputation;
    eq &= maxFrustration == state.maxFrustration;
    eq &= maxWave == state.maxWave;
    eq &= wave == state.wave;
    eq &= groupSize.epsilonEquals(state.groupSize);

//    for (int i = 0; i < FoodOnCounters.size(); i++) {
//      eq &=  FoodOnCounters.get(i).equals(state.FoodOnCounters.get(i));
//    } a game screen one
    for (int i = 0; i < customerGroupStates.length; i++) {
      eq &= state.customerGroupStates[i].equals(customerGroupStates[i]);
    }

    eq &= maxMoney == state.maxMoney;
    eq &= money == state.money;

    return eq;


  }

}

