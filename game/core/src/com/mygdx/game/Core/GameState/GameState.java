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
 * @date 25 /04/23
 */
public class GameState implements Serializable {

  /**
   * The Money.
   */
  public int money;
  /**
   * The Max money.
   */
  public int maxMoney;

  public int customerNo;

  /**
   * The Customer group states.
   */
  public CustomerGroupState[] customerGroupStates;
  /**
   * The Chef positions.
   */
  public Vector2[] chefPositions;
  /**
   * The Chef holding stacks.
   */
  public ItemState[] chefHoldingStacks;//Must be in format of chefs 1 items (if not holding an item at slot N then null)
  /**
   * The Food on counters.
   */
  public List<List<ItemState>> foodOnCounters;
  /**
   * The Repair state.
   */
  public List<Boolean> repairState;
  /**
   * The Group size.
   */
  public Vector2 groupSize;
  /**
   * The Wave.
   */
  public int wave;
  /**
   * The Max wave.
   */
  public int maxWave;
  /**
   * The Max frustration.
   */
  public int maxFrustration;

  /**
   * The Timer.
   */
  public int timer;
  /**
   * The Seconds.
   */
  public float seconds;
  /**
   * The Reputation.
   */
  public int reputation;
  /**
   * The Max reputation.
   */
  public int maxReputation;
  /**
   * The Customers per wave.
   */
  public ArrayList<Integer> customersPerWave;
  /**
   * The Difficulty.
   */
  public Difficulty difficulty;


  /**
   * Is chef part equals boolean.
   *
   * @param state the state
   * @return the boolean
   */
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

  /**
   * Is customer part equals boolean.
   *
   * @param state the state
   * @return the boolean
   */
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

