package com.mygdx.game.Core.PathFinder;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.Core.Chef.RayPoint;
import com.mygdx.game.Core.Scriptable;
import java.util.LinkedList;
import java.util.List;


/**
 * A pathfinding agent abstraction
 *   BlackCatStudio's Code
 * @author Felix Seanor
 * Last modified 10/04/23
 */
public class PathfindingAgent extends Scriptable {

  List<Vector2> path;
  public float speed = 100;

  Vector2 prev;
  public Body b2body;

  /**
   * Give this agent a new path
   * @param newPath
   */
  public void GivePath(List<Vector2> newPath) {
    prev = new Vector2(gameObject.position);
    path = newPath;
  }

  public List<Vector2> getPath() {
    return path;
  }

  protected PathfindingAgent() {
    path = new LinkedList<>();
  }

  /**
   * Gets the nearest position on line from a 3D position
   *
   * @param linePnt - point the line passes through
   * @param lineDir - unit vector in direction of line, either direction works
   * @param pnt     - the point to find nearest on line for
   * @return
   * @author Felix Seanor
   */
  public static RayPoint NearestPointOnLine(Vector2 linePnt, Vector2 lineDir, Vector2 pnt) {

    lineDir = lineDir.nor();//this needs to be a unit vector

    Vector2 v = new Vector2(0, 0);
    v.add(pnt).sub(linePnt);
    float d = v.dot(lineDir);
    RayPoint rayPoint = new RayPoint();
    rayPoint.pos = new Vector2(0, 0);
    rayPoint.pos.set(linePnt).mulAdd(lineDir, d);
    rayPoint.t = d;
    return rayPoint;
  }

  /**
   * Get the current movement direction
   *
   * @return Movement direction
   * @author Felix Seanor
   * @author Jack Vickers
   */
  protected Vector2 GetMoveDir() {
    if (path == null || path.size() == 0) {
      return new Vector2(0, 0);
    }

    Vector2 rayDir = new Vector2(path.get(0));
    rayDir.sub(prev);

    return rayDir;
  }

  /**
   * Moves the chef to the next location
   *
   * @param dt
   * @author Felix Seanor
   * @author Jack Vickers
   */
  void move(float dt) {

    if (path == null || path.size() == 0) {
      return;
    }

    Vector2 rayDir = GetMoveDir();

    Vector2 SimulatedPosition = new Vector2(gameObject.position);
    Vector2 NextPos = new Vector2(path.get(0));
    RayPoint point = NearestPointOnLine(NextPos, rayDir, SimulatedPosition);
    float distT = (float) Math.sqrt(rayDir.dot(rayDir));

    //System.out.println(distT + " : " + point.t + point.pos);
    if (Math.floor(point.t * 100) / 100.0 >= Math.floor(distT * 100) / 100.0
        || NextPos.epsilonEquals(gameObject.position)) {
      prev = new Vector2(path.get(0));
      path.remove(0);
    }

    if (path == null || path.size() == 0) {
      return;
    }

    Vector2 Move = new Vector2(path.get(0));

    Move.sub(gameObject.position);

    if (Move.dot(Move) > speed * dt * speed * dt) {
      Move = Move.nor().scl(dt * speed);
    }

    gameObject.position.add(Move);
    // b2body.setTransform(gameObject.position.x,gameObject.position.y,b2body.getAngle());

  }

  @Override
  public void Update(float dt) {
    move(dt);
  }

}
