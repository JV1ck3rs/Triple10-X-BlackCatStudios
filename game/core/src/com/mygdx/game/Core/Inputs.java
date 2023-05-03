package com.mygdx.game.Core;

import com.badlogic.gdx.Input.Keys;

/**
 * Tracks all the different inputs into the game and gives names to them for ease of use Also
 * prevents key reuse BlackCatStudio's Code
 *
 * @author Felix Seanor
 * @author Jack Vickers
 * @date 01 /05/23
 */
public class Inputs {

  /**
   * The constant SPAWN_NEW_CHEF.
   */
  public static final int SPAWN_NEW_CHEF = Keys.MINUS;
  /**
   * The constant MOVE_CHEF_UP.
   */
  public static int MOVE_CHEF_UP = Keys.W;
  /**
   * The constant MOVE_CHEF_DOWN.
   */
  public static int MOVE_CHEF_DOWN = Keys.S;
  /**
   * The constant MOVE_CHEF_LEFT.
   */
  public static int MOVE_CHEF_LEFT = Keys.A;
  /**
   * The constant MOVE_CHEF_RIGHT.
   */
  public static int MOVE_CHEF_RIGHT = Keys.D;
  /**
   * The constant CYCLE_STACK.
   */
  public static int CYCLE_STACK = Keys.W;
  /**
   * The constant GIVE_ITEM.
   */
  public static int GIVE_ITEM = Keys.Q;
  /**
   * The constant FETCH_ITEM.
   */
  public static int FETCH_ITEM = Keys.E;
  /**
   * The constant DROP_ITEM.
   */
  public static int DROP_ITEM = Keys.F;
  /**
   * The constant INTERACT.
   */
  public static int INTERACT = Keys.SPACE;

  /**
   * The constant SELL_RESTURANT.
   */
  public static int SELL_RESTURANT = Keys.L;
}
