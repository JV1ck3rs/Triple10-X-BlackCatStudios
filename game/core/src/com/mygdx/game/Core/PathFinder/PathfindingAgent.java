package com.mygdx.game.Core.PathFinder;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Core.Chef.RayPoint;
import com.mygdx.game.Core.Scriptable;
import java.util.LinkedList;
import java.util.List;


/**
 * A pathfinding agent abstraction BlackCatStudio's Code
 *
 * @author Felix Seanor Last modified 10/04/23
 */
public class PathfindingAgent extends Scriptable {

  /**
   * The Path.
   */
  List<Vector2> path;
  /**
   * The Speed.
   */
  public float speed = 100;

  /**
   * The Prev.
   */
  Vector2 prev;

  /**
   * Give this agent a new path
   *
   * @param newPath the new path
   */
  public void givePath(List<Vector2> newPath) {
    prev = new Vector2(gameObject.position);
    path = newPath;
  }

  /**
   * Gets path.
   *
   * @return the path
   */
  public List<Vector2> getPath() {
    return path;
  }

  /**
   * Instantiates a new Pathfinding agent.
   */
  protected PathfindingAgent() {
    path = new LinkedList<>();
  }

  /**
   * Gets the nearest position on line from a 3D position
   *
   * @param linePnt - point the line passes through
   * @param lineDir - unit vector in direction of line, either direction works
   * @param pnt     - the point to find nearest on line for
   * @return ray point
   * @author Felix Seanor
   */
  public static RayPoint nearestPointOnLine(Vector2 linePnt, Vector2 lineDir, Vector2 pnt) {

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
  protected Vector2 getMoveDir() {
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
   * @param dt the dt
   * @author Felix Seanor
   * @author Jack Vickers
   */
  void move(float dt) {

    if (path == null || path.size() == 0) {
      return;
    }

    Vector2 rayDir = getMoveDir();

    Vector2 simulatedPosition = new Vector2(gameObject.position);
    Vector2 nextPos = new Vector2(path.get(0));
    RayPoint point = nearestPointOnLine(nextPos, rayDir, simulatedPosition);
    float distT = (float) Math.sqrt(rayDir.dot(rayDir));

    //System.out.println(distT + " : " + point.t + point.pos);
    if (Math.floor(point.t * 100) / 100.0 >= Math.floor(distT * 100) / 100.0
        || nextPos.epsilonEquals(gameObject.position)) {
      prev = new Vector2(path.get(0));
      path.remove(0);
    }

    if (path == null || path.size() == 0) {
      return;
    }

    Vector2 currentMove = new Vector2(path.get(0));

    currentMove.sub(gameObject.position);

    if (currentMove.dot(currentMove) > speed * dt * speed * dt) {
      currentMove = currentMove.nor().scl(dt * speed);
    }

    gameObject.position.add(currentMove);
    // b2body.setTransform(gameObject.position.x,gameObject.position.y,b2body.getAngle());

  }

  @Override
  public void update(float dt) {
    move(dt);
  }

}
