package com.mygdx.game.Core.Customers;
import com.badlogic.gdx.math.Vector2;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Table
{

  //Position of table
  Vector2 position;

  //Tables unique ID
  public int ID;

  //All seats
   List<Vector2> seats = new LinkedList<>();

   //How many sitting
   int currentlySeated = 0;
   //seat offset so seating patttern isnt always the same
  int offset = 0;
  //Size of table
  int Radius;

  /**
   * Create a new table
   * @param pos position
   * @param _ID unique ID
   * @param Radius size
   * @author Felix Seanor
   */
  public Table(Vector2 pos, int _ID, int Radius){
    position = pos;
    this.Radius = Radius;
    ID = _ID;
  }

  /**
   * Set the seat positions
   * @param count members in group
   * @param rand randomisation class
   * @author Felix Seanor
   */
  public void DesignateSeating(int count, Random rand){
    currentlySeated = 0;

    offset = rand.nextInt(count);

    seats = new LinkedList<>();

    seats.add(new Vector2(1*Radius,0));
    seats.add(new Vector2(-1*Radius,0));
    seats.add(new Vector2(0,1*Radius));
    seats.add(new Vector2(0,-1*Radius));



  }

  /**
   * Get the next avalible seat
   * @return seat position
   * @author Felix Seanor
   */
  public Vector2 GetNextSeat(){
    Vector2 seat =seats.get((offset + currentlySeated)%seats.size());
    currentlySeated++;
    return seat.add(position);
  }

  /**
   * has no group been designated this table
   * @return
   * @author Felix Seanor
   */
  public boolean isFree(){
    return seats.size()==0;
//    return currentlySeated == 0;
  }


  /**
   * free table for new group
   * @author Felix Seanor
   */
  public void relinquish(){
    seats = new LinkedList<>();
  }
}