package piazzapanictests.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Core.Customers.CustomerGroups;
import com.mygdx.game.Core.Powerups.Powerup;
import com.mygdx.game.Core.Customers.Customer;

import java.util.*;

import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Testing pathfinding
 * @satisfies FR_POWERUP UR_ENJOYABILITY NFR_SPEED_POWERUP NFR_FUSTRATION_STALL_POWERUP NFR_SUPERFOOD_POWERUP NFR_MEGAFOOD_POWERUP NFR_BUYREPUTATIONPOINT_POWERUP
 * @author Felix Seanor
 **/
@RunWith(GdxTestRunner.class)
public class PowerupsTests extends MasterTestClass {

  /**
   * Tests if the chefs increase in speed after the speed powerup
   * @satisfies FR_POWERUP NFR_SPEED_POWERUP
   * @author Felix Seanor
   * @date 26/04/23
   */
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
  /**
   * Tests if the chefs increase in reputation after the reputation powerup
   * @satisfies FR_POWERUP NFR_BUYREPUTATIONPOINT_POWERUP
   * @author Felix Seanor
   * @date 26/04/23
   */
  @Test
  public void buyReputation() {
    instantiateCustomerScripts();
    instantiateMasterChef();

    Powerup powerup = new Powerup(masterChef, customerController);

    int reputation = customerController.reputation;
    powerup.buyReputation();
    int nrep = customerController.reputation;

    assertTrue("New reputation must greater after the power up", nrep > reputation);

  }

  /**
   * Tests if stop frustration will run theoretically. Manual Testing will
   * have to be done to assure they freeze frustation for the correct amount of time.
   * @satisfies FR_POWERUP  NFR_FUSTRATION_STALL_POWERUP
   * @author Sam Toner
   * @date 02/05/23
   */
  @Test
  public void stopFrustration(){
    instantiateCustomerScripts();
    instantiateMasterChef();
    customerController.canAcceptNewCustomer();

    Powerup powerup = new Powerup(masterChef, customerController);

    float frustration =  customerController.getCurrentWaitingCustomerGroup().frustration;
    powerup.stopFrustration(2000);
    float[] newFrust = {0f};
    float[] afterFrust = {0f};
    new Timer().schedule(
            new TimerTask() {
              @Override
              public void run() {
                newFrust[0] = customerController.getCurrentWaitingCustomerGroup().frustration;
              }
            },
            1900
    );
    new Timer().schedule(
            new TimerTask() {
              @Override
              public void run() {
                afterFrust[0] = customerController.getCurrentWaitingCustomerGroup().frustration;
              }
            },
            500
    );

    assertTrue("Frustration is >0", frustration >0);





  }

  /**
   * Tests if a customer gets removed via super food powerup
   * @satisfies FR_POWERUP NFR_SUPERFOOD_POWERUP
   * @author Sam Toner
   * @date 02/05/23
   */

  @Test
  public void superFood(){
    instantiateCustomerScripts();
    instantiateMasterChef();

    Powerup powerup = new Powerup(masterChef, customerController);
    customerController.canAcceptNewCustomer();

    Customer firstCustomer = customerController.getCurrentWaitingCustomerGroup().membersInLine.get(0);
    Integer customerWaitingLength = customerController.getCurrentWaitingCustomerGroup().membersInLine.size();
    Integer served = customerController.getNumberOfCustomersServed();

    powerup.superFood();


    Customer newFirstCustomer = customerController.getCurrentWaitingCustomerGroup().membersSeatedOrWalking.get(0);
    Integer newCustomerWaitingLength = customerController.getCurrentWaitingCustomerGroup().membersSeatedOrWalking.size();
    Integer newServed = customerController.getNumberOfCustomersServed();


    assertTrue("The first customer is different", (firstCustomer == newFirstCustomer));
    assertTrue("The length of the list is different", (customerWaitingLength == newCustomerWaitingLength));
    assertTrue("The number of customers served has increased", newServed > served);
  }

  /**
   * Tests is the Mega super food removes multiple customers of the same class
   * @satisfies FR_POWERUP NFR_MEGAFOOD_POWERUP
   * @date 02/05/23
   * @author Sam Toner
   */
  @Test
  public void MegaSuperFood(){
    instantiateCustomerScripts();
    instantiateMasterChef();

    Powerup powerup = new Powerup(masterChef, customerController);
    LinkedList<ItemEnum> orders = new LinkedList<>();
    orders.add(ItemEnum.Burger);
    orders.add(ItemEnum.Burger);
    orders.add(ItemEnum.Burger);
    orders.add(ItemEnum.TomatoOnionSalad);
    CustomerGroups newCustGroup = new CustomerGroups(4, 0, new Vector2(0, 0), 90, orders, customerController.customerAtlas);
    newCustGroup.table = customerController.getTable();
    newCustGroup.table.designateSeating(4, new Random());
    customerController.setCurrentWaitingCustomerGroup(newCustGroup);
    Integer customerWaitingLength = customerController.getCurrentWaitingCustomerGroup().membersInLine.size();
    Integer served = customerController.getNumberOfCustomersServed();
    Integer NumberWithSameTypeDish = 3;
    LinkedList<Customer> customersMeantToLeave = new LinkedList<>();

    for(int j = 0; j <= customerController.getCurrentWaitingCustomerGroup().membersInLine.size()-2; j++){
      customerController.getCurrentWaitingCustomerGroup().membersInLine.get(j).dish = ItemEnum.Burger;
      customersMeantToLeave.add(customerController.getCurrentWaitingCustomerGroup().membersInLine.get(j));
    }
    customerController.getCurrentWaitingCustomerGroup().membersInLine.get(3).dish = ItemEnum.LettuceOnionSalad;

    Item burgerItem = new Item(ItemEnum.SuperBurger);
    masterChef.getCurrentChef().GiveChefItem(burgerItem);

    powerup.tetrisSuperFoodGive();
    customerController.tetrisSuperFoodAction(burgerItem);

    Integer newCustomerWaitingLength = customerController.getCurrentWaitingCustomerGroup().membersInLine.size();
    Integer newServed = customerController.getNumberOfCustomersServed();
    List<Customer> customerServed = customerController.getCurrentWaitingCustomerGroup().membersSeatedOrWalking;
    Boolean allRight = true;
    for(int k = 0; k < customerServed.size(); k++){
      if (!customersMeantToLeave.contains(customerServed.get(k))){
        allRight = false;
      }
    }
    assertTrue("Correct number of customers remain in the line", newCustomerWaitingLength == 1);
    assertTrue("Correct customers left the line", allRight);
    assertTrue("Correct number of customers were served", newServed-served == 3);
    assertTrue("Correct number of customers left to seating", customerController.getCurrentWaitingCustomerGroup().membersSeatedOrWalking.size() == NumberWithSameTypeDish && customerController.getCurrentWaitingCustomerGroup().membersSeatedOrWalking.size() == customersMeantToLeave.size());
  }



}
