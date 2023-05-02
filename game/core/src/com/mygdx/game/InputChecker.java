package com.mygdx.game;


/**
 * Stores input checks for functions
 * @author Jack Vickers
 * @date 28/04/23
 */
public class InputChecker {

  /**
   * Checks if an input number for customer is correct
   * @param input
   * @return
   * @author Jack Vickers
   */
  public static boolean checkCustomerNumberInput(String input) {
    return input.matches("[0-9]+") && input.length() <= 3 && Integer.parseInt(input) <= 100
        && Integer.parseInt(input) > 0;
  }

  /**
   * Leaderboard name must be alphanumberic
   * @param input
   * @return
   * @author Jack Vickers
   */
  public static boolean checkLeaderboardName(String input) {
    return (input.matches("[a-zA-Z]+") && input.length() <= 5);
  }

}
