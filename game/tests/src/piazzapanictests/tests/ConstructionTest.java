package piazzapanictests.tests;

import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Core.ConstructMachines;
import com.mygdx.game.Core.GameState.Difficulty;
import com.mygdx.game.Core.GameState.DifficultyMaster;
import com.mygdx.game.Core.GameState.DifficultyState;
import com.mygdx.game.Items.ItemEnum;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests that the assets are present and that they can be constructed.
 * @Satisfies Requirements for UR_WORKSTATIONS, UR_CHEF_MOVEMENT
 * @author Felix Seanor
 * @date 01/05/2023
 */
@RunWith(GdxTestRunner.class)
public class ConstructionTest extends MasterTestClass
{
  ConstructMachines machines;

  /**
   * Constructs the framework for the other construction tests
   * @return obj to create
   * @author Felix Seanor
   * @date 26/04/23
   */
  public Rectangle construct(){


    DifficultyState difficultyState = DifficultyMaster.getDifficulty(Difficulty.Stressful);
    params = difficultyState.ccParams;

    instantiateCustomerScripts();

    DifficultyState state = DifficultyMaster.getDifficulty(Difficulty.Stressful);
    machines = new ConstructMachines(customerController,state,pathfinding);
    Rectangle rect = new Rectangle();
    rect.height=10;
    rect.width=10;
    rect.x=0;
    rect.y = 0;

    return  rect;
  }

  /**
   * Test if the hob can be constructed
   * @Satisfies UR_WORKSTATIONS
   * @author Felix Seanor
   * @date 26/04/23
   */
  @Test
  public void ConstructHob()
  {
    Rectangle rect = construct();
    machines.createHobs(rect);
    assertTrue("Must have constructed stations", machines.stations.size()>0);
  }

  /**
   * Test if the bin can be constructed
   * @Satisfies UR_WORKSTATIONS
   * @author Felix Seanor
   * @date 26/04/23
   */

  @Test
  public void ConstructBins()
  {
    Rectangle rect = construct();
    machines.createBin(rect);
    assertTrue("Must have constructed stations", machines.stations.size()>0);
  }
  /**
   * Test if the oven can be constructed
   * @Satisfies UR_WORKSTATIONS
   * @author Felix Seanor
   * @date 26/04/23
   */

  @Test
  public void ConstructOven()
  {
    Rectangle rect = construct();
    machines.createOven(rect, customerController);
    assertTrue("Must have constructed stations", machines.stations.size()>0);
  }
  /**
   * Test if the chopping can be constructed
   * @Satisfies UR_WORKSTATIONS
   * @author Felix Seanor
   * @date 26/04/23
   */
  @Test
  public void ConstructChopping()
  {
    Rectangle rect = construct();
    machines.createChopping(rect);
    assertTrue("Must have constructed stations", machines.stations.size()>0);
  }

  /**
   * Test if the toaster can be constructed
   * @Satisfies UR_WORKSTATIONS
   * @author Felix Seanor
   * @date 26/04/23
   */
  @Test
  public void ConstructToaster()
  {
    Rectangle rect = construct();
    machines.createToaster(rect);
    assertTrue("Must have constructed stations", machines.stations.size()>0);
  }
  /**
   * Test if the customerCounter can be constructed
   * @Satisfies UR_WORKSTATIONS
   * @author Felix Seanor
   * @date 26/04/23
   */

  @Test
  public void ConstructCustomerCounters()
  {
    Rectangle rect = construct();
    machines.createCustomerCounters(rect);
    assertTrue("Must have constructed stations", machines.customerCounters.size()>0);
  }

  /**
   * Test if a food crate can be constructed
   * @Satisfies UR_WORKSTATIONS
   * @author Felix Seanor
   * @date 26/04/23
   */
  @Test
  public void ConstructFoodCrate()
  {
    Rectangle rect = construct();
    machines.createFoodCrates(rect, ItemEnum.Lettuce);
    assertTrue("Must have constructed stations", machines.stations.size()>0);
  }
  /**
   * Test if the hob can be constructed
   * @Satisfies UR_WORKSTATIONS
   * @author Felix Seanor
   * @date 26/04/23
   */
  @Test
  public void ConstructAssembly()
  {
    Rectangle rect = construct();
    machines.createAssembly(rect);
    assertTrue("Must have constructed stations", machines.assemblyStations.size()>0);
  }

  /**
   * Test if an object can be constructed
   * @Satisfies UR_CHEF_MOVEMENT
   * @author Felix Seanor
   * @date 26/04/23
   */
  @Test
  public void ConstructObject()
  {
    Rectangle rect = construct();
    instantiateWorld();
    machines.buildObject(rect.x, rect.y, rect.width,rect.height,"Static","TestObj");
  }

}
