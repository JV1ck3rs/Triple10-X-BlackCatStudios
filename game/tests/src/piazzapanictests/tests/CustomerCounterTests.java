package piazzapanictests.tests;

import com.mygdx.game.Core.GameObjectManager;
import com.mygdx.game.Core.GameState.Difficulty;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Tests for the CustomerCounter Class
 * @satisfies UR_CUSTOMER_ORDER FR_DISH_VALIDATION
 * @author Azzam Amirul
 * @author Jack Vickers
 * @author Felix Seanor
 * @date 02/05/2023
 */
@RunWith(GdxTestRunner.class)

public class CustomerCounterTests extends MasterTestClass {

  /**
   * Tests that a customer takes the food if it matches their order.
   * @satisfies UR_CUSTOMER_ORDER FR_DISH_VALIDATION
   * @author Azzam Amirul Bahri
   * @author Jack Vickers
   * @date 01/05/2023
   */
  @Test
  public void TestGiveValidFoodToCustomer() {
    if (GameObjectManager.objManager == null) {
      // creates a game object manager making sure it is not null when it is needed
      new GameObjectManager();
    }

    instantiateCustomerScripts(Difficulty.Relaxing);
    instantiateWorldAndCustomerCounter();
    customerController.CanAcceptNewCustomer();
    Item itemToGIve = new Item(customerController.getCurrentWaitingCustomerGroup().Orders.get(0));
    Integer numCustomersInLine = customerController.getCurrentWaitingCustomerGroup().getMembers()
        .size();
    customerCounter.GiveItem(itemToGIve);
    assertEquals("Numbers of customers  seated or walking should be 1", 1,
        customerController.getMemberSeatedOrWalking().size());
    assertEquals("Number of customers in line should be 1 less than before", numCustomersInLine - 1,
        customerController.getCurrentWaitingCustomerGroup().MembersInLine.size());
  }

  /**
   * Tests that a customer does not take the food if it does not match their order.
   * @satisfies UR_CUSTOMER_ORDER FR_DISH_VALIDATION
   * @author Jack Vickers
   * @author Azzam Amirul
   * @date 01/05/2023
   */
  @Test
  public void TestGiveCustomerWrongItem() {
    if (GameObjectManager.objManager == null) {
      // creates a game object manager making sure it is not null when it is needed
      new GameObjectManager();
    }
    instantiateCustomerScripts(Difficulty.Relaxing);
    customerController.CanAcceptNewCustomer();
    instantiateWorldAndCustomerCounter();
    int numCustomersInLine = customerController.getCurrentWaitingCustomerGroup().MembersInLine
        .size();
    customerCounter.GiveItem(new Item(ItemEnum.Buns));
    assertNotNull("There should be an item on the counter (because no customers take it)", customerCounter.item);
    assertEquals("Numbers of customers  seated or walking should be 0", 0,
        customerController.getMemberSeatedOrWalking().size());
    assertEquals("Number of customers in line should be the same as before", numCustomersInLine,
        customerController.getCurrentWaitingCustomerGroup().MembersInLine.size());
  }

  /**
   * Tests that items can be placed and taken off the customer counter by checking if items are already on the counter.
   * @satisfies UR_INTERACTIONS UR_REMOVE_ITEM UR_COLLECT_ITEM
   * @author Azzam Amirul
   * @author Jack Vickers
   * @date 02/05/2023
   */
  @Test
  public void TestCustomerCounterCanGiveCanRetrieve() {
    instantiateCustomerScripts();
    instantiateWorldAndCustomerCounter();
    customerController.CanAcceptNewCustomer();
    Item item = new Item(ItemEnum.Buns);
    assertTrue(
        "Should be able to give something to the customer counter because it has nothing on it",
        customerCounter.CanGive());
    customerCounter.GiveItem(item);
    assertEquals("Item is in inventory", item, customerCounter.RetrieveItem());
  }

  /**
   * Tests that a chef can interact with the counter depending on whether there is a customer present.
   * @satisfies UR_INTERACTIONS
   * @author Azzam Amirul
   * @author  Felix Seanor
   * @date 01/05/2023
   */
  @Test
  public void TestInteractCustomerCounter() {
    instantiateWorldAndCustomerCounter();
    instantiateCustomerScripts();
    customerController.CanAcceptNewCustomer();

    assertEquals("Can be interacted", 0, 0, customerCounter.Interact());
    assertFalse(customerCounter.CanInteract());
  }


}
