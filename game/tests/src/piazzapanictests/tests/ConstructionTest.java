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
 *
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
   * @author Felix Seanor
   * @date 26/04/23
   */
  @Test
  public void ConstructHob()
  {
    Rectangle rect = construct();
    machines.CreateHobs(rect);
    assertTrue("Must have constructed stations", machines.Stations.size()>0);
  }

  /**
   * Test if the bin can be constructed
   * @author Felix Seanor
   * @date 26/04/23
   */

  @Test
  public void ConstructBins()
  {
    Rectangle rect = construct();
    machines.CreateBin(rect);
    assertTrue("Must have constructed stations", machines.Stations.size()>0);
  }
  /**
   * Test if the oven can be constructed
   * @author Felix Seanor
   * @date 26/04/23
   */

  @Test
  public void ConstructOven()
  {
    Rectangle rect = construct();
    machines.CreateOven(rect, customerController);
    assertTrue("Must have constructed stations", machines.Stations.size()>0);
  }
  /**
   * Test if the chopping can be constructed
   * @author Felix Seanor
   * @date 26/04/23
   */
  @Test
  public void ConstructChopping()
  {
    Rectangle rect = construct();
    machines.CreateChopping(rect);
    assertTrue("Must have constructed stations", machines.Stations.size()>0);
  }

  /**
   * Test if the toaster can be constructed
   * @author Felix Seanor
   * @date 26/04/23
   */
  @Test
  public void ConstructToaster()
  {
    Rectangle rect = construct();
    machines.CreateToaster(rect);
    assertTrue("Must have constructed stations", machines.Stations.size()>0);
  }
  /**
   * Test if the customerCounter can be constructed
   * @author Felix Seanor
   * @date 26/04/23
   */

  @Test
  public void ConstructCustomerCounters()
  {
    Rectangle rect = construct();
    machines.CreateCustomerCounters(rect);
    assertTrue("Must have constructed stations", machines.customerCounters.size()>0);
  }

  /**
   * Test if a food crate can be constructed
   * @author Felix Seanor
   * @date 26/04/23
   */
  @Test
  public void ConstructFoodCrate()
  {
    Rectangle rect = construct();
    machines.CreateFoodCrates(rect, ItemEnum.Lettuce);
    assertTrue("Must have constructed stations", machines.Stations.size()>0);
  }
  /**
   * Test if the hob can be constructed
   * @author Felix Seanor
   * @date 26/04/23
   */
  @Test
  public void ConstructAssembly()
  {
    Rectangle rect = construct();
    machines.CreateAssembly(rect);
    assertTrue("Must have constructed stations", machines.assemblyStations.size()>0);
  }

  /**
   * Test if an object can be constructed
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