package piazzapanictests.tests;

import com.mygdx.game.Core.GameObjectManager;
import com.mygdx.game.Core.GameState.Difficulty;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)

public class CustomerCounterTests extends MasterTestClass {

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
        Integer value = customerController.getCurrentWaitingCustomerGroup().getMembers().size(); // to check of value of group is one less than the original
        assertTrue(customerController.tryGiveFood(itemToGIve));
        assertEquals("Numbers of customers  seated or walking should be 1", 1, customerController.getMemberSeatedOrWalking().size());

    }

    @Test
    public void TestGiveCustomerWrongItem(){
        if (GameObjectManager.objManager == null) {
            // creates a game object manager making sure it is not null when it is needed
            new GameObjectManager();
        }

        instantiateCustomerScripts(Difficulty.Relaxing);
        customerController.CanAcceptNewCustomer();
        instantiateWorldAndCustomerCounter();
        customerCounter.GiveItem(new Item(ItemEnum.Buns));
        assertNotNull(customerCounter.item);

    }

    @Test
    public void TestCustomerCounterCanGiveCanRetrieve(){
        instantiateCustomerScripts();
        instantiateWorldAndCustomerCounter();
        customerController.CanAcceptNewCustomer();
        Item item = new Item(ItemEnum.Buns);
//        if (item == null){
//
//        } else {
//            assertTrue("Item can be given to customer counter", customerCounter.CanGive());
//        }
//
//        if (item == null){
//            assertTrue("Nothing to retrieve at customer counter", customerCounter.CanRetrieve());
//        } else{
//            assertFalse("Item cannot be retrieved by customer counter", customerCounter.CanRetrieve());
//        }
        assertTrue("Should be able to give something to the customer counter because it has nothing on it", customerCounter.CanGive());
        customerCounter.GiveItem(item);
        assertEquals("Item is in inventory", item, customerCounter.RetrieveItem());
    }

    @Test
    public void TestInteractCustomerCounter(){
        instantiateWorldAndCustomerCounter();
        instantiateCustomerScripts();
        customerController.CanAcceptNewCustomer();

        assertEquals("Can be interacted",0 , 0, customerCounter.Interact());
        assertFalse(customerCounter.CanInteract());
    }



}
