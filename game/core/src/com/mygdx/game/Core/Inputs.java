package com.mygdx.game.Core;

import com.badlogic.gdx.Input.Keys;

/**
 * Tracks all the different inputs into the game and gives names to them for ease of use
 * Also prevents key reuse
 *   BlackCatStudio's Code
 * @author Felix Seanor
 * @author Jack Vickers
 */
public class Inputs {

  public static final int SPAWN_NEW_CHEF = Keys.MINUS;
  public static int MOVE_CHEF_UP = Keys.W;
public static int MOVE_CHEF_DOWN = Keys.S;
public static int MOVE_CHEF_LEFT = Keys.A;
public static int MOVE_CHEF_RIGHT = Keys.D;
public static int CYCLE_STACK = Keys.W;
public static int GIVE_ITEM = Keys.Q;
public static int FETCH_ITEM = Keys.E;
public static int DROP_ITEM = Keys.F;
public static int INTERACT = Keys.SPACE;
}
