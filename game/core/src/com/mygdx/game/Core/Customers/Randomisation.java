package com.mygdx.game.Core.Customers;

public enum Randomisation {
  TrueRandom, //White noise, every order is chosen with equal probability
  Normalised //Every order is chosen based on which order was chose less often
}
