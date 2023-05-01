package piazzapanictests.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.mygdx.game.Core.Powerup;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Testing pathfinding
 *
 * @author Felix Seanor
 **/
@RunWith(GdxTestRunner.class)
public class PowerupsTests extends MasterTestClass {

  @Test
  public void SpeedUpTest() {
    instantiateCustomerScripts();
    instantiateMasterChef();

    Powerup powerup = new Powerup(masterChef, customerController);

    float currentSpeed = masterChef.getChef(0).speed;
    powerup.doSpeedPowerup();
    float newSpeed = masterChef.getChef(0).speed;

    assertTrue("New Speed must greater after the power up", newSpeed > currentSpeed);

  }

  @Test
  public void buyReputation() {
    instantiateCustomerScripts();
    instantiateMasterChef();

    Powerup powerup = new Powerup(masterChef, customerController);

    int reputation = customerController.Reputation;
    powerup.buyReputation();
    int nrep = customerController.Reputation;

    assertTrue("New reputation must greater after the power up", nrep > reputation);

  }
}
