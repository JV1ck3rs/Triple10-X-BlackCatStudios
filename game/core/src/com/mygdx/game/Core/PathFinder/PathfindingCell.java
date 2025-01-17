package com.mygdx.game.Core.PathFinder;


/**
 * Stored A* data for each discorvered cell BlackCatStudio's Code
 *
 * @author Felix Seanor
 * @date 03 /03/23
 */
public class PathfindingCell implements Comparable<PathfindingCell> {

  /**
   * cost to goal
   */
  public float heuristic;

  /**
   * cost along path
   */
  public float pathCost;

  /**
   * x position
   */
  public int x;
  /**
   * y position
   */
  public int y;

  /**
   * index
   */
  public int index;


  /**
   * parent cell, null if root node
   */
  public PathfindingCell parent;

  /**
   * Creates a new pathfinding cell. This was written by Vickers so may be wrong.
   *
   * @param x         The x position of the cell
   * @param y         The y position of the cell
   * @param index     The index of the cell
   * @param heuristic The heuristic value of the cell
   * @param path      The path cost of the cell
   * @author Felix Seanor
   */
  public PathfindingCell(int x, int y, int index, float heuristic, float path) {
    this.x = x;
    this.y = y;

    this.heuristic = heuristic;
    pathCost = path;
    this.index = index;


  }

  /**
   * heuristics + pathcost
   *
   * @return float
   * @author Felix Seanor
   */
  public float score() {
    return heuristic + pathCost;
  }

  /**
   * Allowest for sorting
   *
   * @param o the object to be compared.
   * @return
   * @author Felix Seanor
   */
  @Override
  public int compareTo(PathfindingCell o) {
    if (score() > o.score()) {
      return 1;
    }
    if (score() < o.score()) {
      return -1;
    }
    return 0;
  }


}
