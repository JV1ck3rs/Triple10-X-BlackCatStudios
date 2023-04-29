package piazzapanictests.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Core.ConstructMachines;
import com.mygdx.game.Core.CustomerController;
import com.mygdx.game.Core.GameState.Difficulty;
import com.mygdx.game.Core.GameState.DifficultyMaster;
import com.mygdx.game.Core.GameState.DifficultyState;
import com.mygdx.game.Core.ValueStructures.EndOfGameValues;
import com.mygdx.game.Items.ItemEnum;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.function.Consumer;

/**
 * Tests that the assets are present.
 */
@RunWith(GdxTestRunner.class)
public class ConstructionTest extends MasterTestClass
{
  ConstructMachines machines;
  CustomerController cust = new CustomerController(new Vector2(0, 0), new Vector2(32, 0), pathfinding,
        (
  EndOfGameValues a) -> EndGame(a), params, new Vector2(190, 390), new Vector2(190, 290),
        new Vector2(290, 290));

  public Rectangle construct(){

    instantiateCustomerScripts();

    DifficultyState state = DifficultyMaster.getDifficulty(Difficulty.Stressful);
    machines = new ConstructMachines(cust,state,pathfinding);
    Rectangle rect = new Rectangle();
    rect.height=10;
    rect.width=10;
    rect.x=0;
    rect.y = 0;

    return  rect;
  }

  @Test
  public void ConstructHob()
  {
    Rectangle rect = construct();
    machines.CreateHobs(rect);
    assertTrue("Must have constructed stations", machines.Stations.size()>0);
  }


  @Test
  public void ConstructBins()
  {
    Rectangle rect = construct();
    machines.CreateBin(rect);
    assertTrue("Must have constructed stations", machines.Stations.size()>0);
  }

  @Test
  public void ConstructOven()
  {
    Rectangle rect = construct();
    machines.CreateOven(rect, cust);
    assertTrue("Must have constructed stations", machines.Stations.size()>0);
  }

  @Test
  public void ConstructChopping()
  {
    Rectangle rect = construct();
    machines.CreateChopping(rect);
    assertTrue("Must have constructed stations", machines.Stations.size()>0);
  }


  @Test
  public void ConstructToaster()
  {
    Rectangle rect = construct();
    machines.CreateToaster(rect);
    assertTrue("Must have constructed stations", machines.Stations.size()>0);
  }


  @Test
  public void ConstructCustomerCounters()
  {
    Rectangle rect = construct();
    machines.CreateCustomerCounters(rect);
    assertTrue("Must have constructed stations", machines.customerCounters.size()>0);
  }
  @Test
  public void ConstructFoodCrate()
  {
    Rectangle rect = construct();
    machines.CreateFoodCrates(rect, ItemEnum.Lettuce);
    assertTrue("Must have constructed stations", machines.Stations.size()>0);
  }

  @Test
  public void ConstructAssembly()
  {
    Rectangle rect = construct();
    machines.CreateAssembly(rect);
    assertTrue("Must have constructed stations", machines.assemblyStations.size()>0);
  }


  @Test
  public void ConstructObject()
  {
    Rectangle rect = construct();
    instantiateWorld();
    machines.buildObject(world,rect.x, rect.y, rect.width,rect.height,"Static","TestObj");
  }

}
