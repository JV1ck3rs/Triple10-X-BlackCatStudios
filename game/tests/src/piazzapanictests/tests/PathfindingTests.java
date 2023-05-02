package piazzapanictests.tests;

import static org.junit.Assert.assertEquals;

import com.mygdx.game.Core.PathFinder.DistanceTest;
import com.mygdx.game.GameScreen;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.badlogic.gdx.math.Vector2;


/**
 * Testing pathfinding
 * @satisfies UR_CHEF_MOVEMEMENT
 * @author Felix Seanor
 * @date 01/04/23
 **/
@RunWith(GdxTestRunner.class)

public class PathfindingTests extends MasterTestClass {

  /**
   * Test that the pathfinding algorithm will return a empty path given a start == goal
   * @satisfies UR_CHEF_MOVEMEMENT
   * @author Felix Seanor
   * @date 01/04/23
   */
  @Test
  public void TestPathfindingNullMove() {
    SetUpPathfinding();
    List<Vector2> path = pathfinding.FindPath(0, 0, 0, 0, DistanceTest.Euclidean);

    assertEquals("The path must be empty for a move of zero", new LinkedList<>(), path);
  }

  /**
   * Test if the pathfinding algorithm will produce a path with a the goal included
   * @satisfies UR_CHEF_MOVEMEMENT
   * @author Felix Seanor
   * @date 01/04/23
   */
  @Test
  public void TestPathfinding() {
    SetUpPathfinding();
    int stepSize = GameScreen.TILE_WIDTH / 4;
    List<Vector2> pathA = pathfinding.FindPath(0, 0, stepSize, 0, DistanceTest.Euclidean);

    List<Vector2> PathAActual = new LinkedList<>();
    PathAActual.add(new Vector2(stepSize, 0));
    assertEquals("The path must be have only one move upwards", PathAActual, pathA);

    List<Vector2> pathB = pathfinding.FindPath(0, 0, stepSize * 5, stepSize * 5,
        DistanceTest.Euclidean);
    Vector2 End = pathB.get(pathB.size() - 1);
    assertEquals("The path must reach the end goal", new Vector2(stepSize * 5, stepSize * 5), End);


  }

  /**
   * Test whether a path will avoid filled squares
   * @satisfies UR_CHEF_MOVEMEMENT
   * @author Felix Seanor
   * @date 01/04/23
   */
  @Test
  public void TestObsticalAvoidance() {
    SetUpPathfinding();
    int stepSize = GameScreen.TILE_WIDTH / 4;

    pathfinding.addStaticObject(0, 5 * stepSize, stepSize * 2, stepSize * 2);

    List<Vector2> path = pathfinding.FindPath(0, 0, 0, 10 * stepSize, DistanceTest.Manhatten);

    Boolean Fails = path.contains(new Vector2(0, 5 * stepSize));

    assertEquals("The path must avoid the obstacle", false, Fails);
  }

}




