package com.mygdx.game.Core.GameState;

import com.badlogic.gdx.math.Vector2;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class containing saved data of the game, to be saved to disk or loaded back in
 * BlackCatStudio's Code
 * @author Felix Seanor
 * @author Jack Vickers
 */
public class GameState implements Serializable
{
  public int Money;
  public int MaxMoney;

  public CustomerGroupState[] CustomerGroupsData;
  public Vector2[] ChefPositions;
  public ItemState[] ChefHoldingStacks;//Must be in format of chefs 1 items (if not holding an item at slot N then null)
  public List<List<ItemState>> FoodOnCounters;
  public Vector2 GroupSize;
  public int Wave;
  public int MaxWave;
  public int MaxFrustration;

  public int Timer;
  public float seconds;
  public int Reputation;
  public int MaxReputation;
  public ArrayList<Integer> CustomersPerWave;
  public Difficulty difficulty;


  public boolean IsChefPartEquals(GameState state){
    boolean  eq = true;

    for (int i = 0; i < ChefPositions.length; i++) {
      eq &= ChefPositions[i].epsilonEquals( state.ChefPositions[i]);

    }

    for (int i = 0; i < ChefHoldingStacks.length; i++) {
      eq &= ChefHoldingStacks[i] == ( state.ChefHoldingStacks[i]);

    }

    return eq;

  }

  public boolean IsCustomerPartEquals(GameState state){
    boolean eq = true;

   // eq &= difficulty == state.difficulty; gamescreen
    eq &= CustomersPerWave == state.CustomersPerWave;
    eq &= MaxReputation == state.MaxReputation;
    eq &= Reputation == state.Reputation;
    eq &= MaxFrustration == state.MaxFrustration;
    eq &= MaxWave == state.MaxWave;
    eq &= Wave == state.Wave;
    eq &= GroupSize.epsilonEquals(state.GroupSize);

//    for (int i = 0; i < FoodOnCounters.size(); i++) {
//      eq &=  FoodOnCounters.get(i).equals(state.FoodOnCounters.get(i));
//    } a game screen one
    for (int i = 0; i < CustomerGroupsData.length; i++) {
      eq &= state.CustomerGroupsData[i].equals(CustomerGroupsData[i]);
    }



    eq &= MaxMoney == state.MaxMoney;
    eq &= Money == state.Money;


    return eq;




  }

}

