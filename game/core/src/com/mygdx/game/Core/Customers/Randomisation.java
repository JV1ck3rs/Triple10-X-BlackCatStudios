package com.mygdx.game.Core.Customers;

/**
 * Randomisation styles BlackCatStudio's Code
 *
 * @author Felix Seanor
 * @date 20 /03/23
 */
public enum Randomisation {
  /**
   * True random randomisation.
   */
  TrueRandom, //White noise, every order is chosen with equal probability
  /**
   * The Normalised.
   */
  Normalised //Every order is chosen based on which order was chose less often
}
