package com.mygdx.game;

public class InputChecker {

  public static boolean checkCustomerNumberInput(String input) {
    return input.matches("[0-9]+") && input.length() <= 3 && Integer.parseInt(input) <= 100
        && Integer.parseInt(input) > 0;
  }

  public static boolean checkLeaderboardName(String input) {
    return (input.matches("[a-zA-Z]+") && input.length() <= 5);
  }

}
