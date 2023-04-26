package piazzapanictests.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.mygdx.game.Core.DistanceTest;
import com.mygdx.game.Core.Pathfinding;
import com.mygdx.game.Core.Powerup;
import com.mygdx.game.GameScreen;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.badlogic.gdx.math.Vector2;



/**
 Testing pathfinding
 @author Felix Seanor
 **/
@RunWith(GdxTestRunner.class)
public class PowerupsTests extends MasterTestClass
{
  @Test
  public void SpeedUpTest(){
    instantiateCustomerScripts();
    instantiateMasterChef();

    Powerup powerup = new Powerup(masterChef,cust);

    float currentSpeed = masterChef.getChef(0).speed;
    powerup.doSpeedPowerup();
    float newSpeed=  masterChef.getChef(0).speed;

    assertTrue("New Speed must greater after the power up", newSpeed>currentSpeed);

  }

  @Test
  public void buyReputation(){
    instantiateCustomerScripts();
    instantiateMasterChef();

    Powerup powerup = new Powerup(masterChef,cust);

    int reputation = cust.Reputation;
    powerup.buyReputation();
    int nrep = cust.Reputation;

    assertTrue("New reputation must greater after the power up", nrep>reputation);

  }
}
