package com.mygdx.game.Core.Customers;

import com.badlogic.gdx.math.Vector2;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * The table class stores where customers should go to sit after getting their food BlackCatStudio's
 * Code
 *
 * @author Felix Seanor
 * @date 29 /04/23
 */
public class Table {

  /**
   * The Position.
   */
//Position of table
  Vector2 position;

  /**
   * The Id.
   */
//Tables unique ID
  public int ID;

  /**
   * The Seats.
   */
//All seats
  List<Vector2> seats = new LinkedList<>();

  /**
   * The Currently seated.
   */
//How many sitting
  int currentlySeated = 0;
  /**
   * The Offset.
   */
//seat offset so seating patttern isnt always the same
  int offset = 0;
  /**
   * The Radius.
   */
//Size of table
  int radius;

  /**
   * Create a new table
   *
   * @param pos    position
   * @param _ID    unique ID
   * @param Radius size
   * @author Felix Seanor
   */
  public Table(Vector2 pos, int _ID, int Radius) {
    position = pos;
    this.radius = Radius;
    ID = _ID;
  }

  /**
   * Set the seat positions
   *
   * @param count members in group
   * @param rand  randomisation class
   * @author Felix Seanor
   */
  public void designateSeating(int count, Random rand) {
    currentlySeated = 0;

    offset = rand.nextInt(count);

    seats = new LinkedList<>();

    seats.add(new Vector2(1 * radius, 0));
    seats.add(new Vector2(-1 * radius, 0));
    seats.add(new Vector2(0, 1 * radius));
    seats.add(new Vector2(0, -1 * radius));


  }

  /**
   * Get the next avalible seat
   *
   * @return seat position
   * @author Felix Seanor
   */
  public Vector2 GetNextSeat() {
    Vector2 seat = seats.get((offset + currentlySeated) % seats.size());
    currentlySeated++;
    return seat.add(position);
  }

  /**
   * has no group been designated this table
   *
   * @return boolean
   * @author Felix Seanor
   */
  public boolean isFree() {
    return seats.size() == 0;
//    return currentlySeated == 0;
  }


  /**
   * free table for new group
   *
   * @author Felix Seanor
   */
  public void relinquish() {
    seats = new LinkedList<>();
  }
}