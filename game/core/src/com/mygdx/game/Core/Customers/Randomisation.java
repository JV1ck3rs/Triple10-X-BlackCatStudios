package com.mygdx.game.Core.Customers;

/**
 * Randomisation styles
 * BlackCatStudio's Code
 * @author Felix Seanor
 */
public enum Randomisation {
  TrueRandom, //White noise, every order is chosen with equal probability
  Normalised //Every order is chosen based on which order was chose less often
}
