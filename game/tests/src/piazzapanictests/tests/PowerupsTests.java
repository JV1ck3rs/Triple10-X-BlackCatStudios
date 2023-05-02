package piazzapanictests.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.mygdx.game.Core.DistanceTest;
import com.mygdx.game.Core.Pathfinding;
import com.mygdx.game.Core.Powerup;
import com.mygdx.game.Customer;
import com.mygdx.game.GameScreen;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.badlogic.gdx.math.Vector2;


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

    Powerup powerup = new Powerup(masterChef, cust);

    float currentSpeed = masterChef.getChef(0).speed;
    powerup.doSpeedPowerup();
    float newSpeed = masterChef.getChef(0).speed;

    assertTrue("New Speed must greater after the power up", newSpeed > currentSpeed);

  }

  @Test
  public void buyReputation() {
    instantiateCustomerScripts();
    instantiateMasterChef();

    Powerup powerup = new Powerup(masterChef, cust);

    int reputation = cust.Reputation;
    powerup.buyReputation();
    int nrep = cust.Reputation;

    assertTrue("New reputation must greater after the power up", nrep > reputation);

  }

  @Test
  public void stopFrustration(){
    instantiateCustomerScripts();
    instantiateMasterChef();

    Powerup powerup = new Powerup(masterChef, cust);

    float frustration =  cust.getCurrentWaitingCustomerGroup().Frustration;
    powerup.stopFrustration(2000);
    final float[] newFrust = {0f};
    final float[] afterFrust = {0f};
    new java.util.Timer().schedule(
            new java.util.TimerTask() {
              @Override
              public void run() {
                newFrust[0] = cust.getCurrentWaitingCustomerGroup().Frustration;
              }
            },
            1900
    );
    new java.util.Timer().schedule(
            new java.util.TimerTask() {
              @Override
              public void run() {
                afterFrust[0] = cust.getCurrentWaitingCustomerGroup().Frustration;
              }
            },
            500
    );

    assertTrue("Frustration pauses for 60 seconds then begins to update", (afterFrust[0] -frustration >0) && (newFrust[0] - frustration ==0));

  }

  public void superFood(){
    instantiateCustomerScripts();
    instantiateMasterChef();

    Powerup powerup = new Powerup(masterChef, cust);
    cust.CanAcceptNewCustomer();

    Customer firstCustomer = cust.getCurrentWaitingCustomerGroup().getMembers().get(0);
    Integer customerWaitingLength = cust.getCurrentWaitingCustomerGroup().getMembers().size();

    powerup.superFood();

    Customer newFirstCustomer = cust.getCurrentWaitingCustomerGroup().getMembers().get(0);
    Integer newCustomerWaitingLength = cust.getCurrentWaitingCustomerGroup().getMembers().size();

    assertTrue("The first customer has been removed", (firstCustomer != newFirstCustomer) && (customerWaitingLength != newCustomerWaitingLength));


  }



}
