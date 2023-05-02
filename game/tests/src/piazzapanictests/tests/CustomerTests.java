package piazzapanictests.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import com.mygdx.game.Core.BlackSprite;
import com.mygdx.game.Core.CustomerController;
import com.mygdx.game.Core.Customers.CustomerGroups;
import com.mygdx.game.Core.Customers.OrderMenu;
import com.mygdx.game.Core.Customers.Randomisation;
import com.mygdx.game.Core.GameObject;
import com.mygdx.game.Core.GameObjectManager;
import com.mygdx.game.Core.GameState.Difficulty;
import com.mygdx.game.Customer;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the behaviour of the customers when they order food
 *
 * @satfies UR_CUSTOMER_ORDER UR_REPUTATION_POINTS UR_CUSTOMER_TIME_LIMIT UR_ENDLESS_END UR_INTERACTIONS FR_DISPLAY_ORDER
 * @author Felix Seanor
 * @author Jack Vickers
 * @date 01/05/23
 */
@RunWith(GdxTestRunner.class)

public class CustomerTests extends MasterTestClass {

  /**
   * Test if the game should end either buy reputation or no waves left
   * @author Felix Seanor
   * @satisifies UR_REPUTATION_POINTS UR_CUSTOMER_TIME_LIMIT UR_ENDLESS_END
   * @date 01/05/23
   */
  @Test
  public void TestEndGame() {
    instantiateCustomerScripts();

    customerController.SetWaveAmount(0);
    customerController.ModifyReputation(-10);

    assertNotNull("The game must do an end state call", vals);

    vals = null;

    customerController.ModifyReputation(20);

    customerController.CanAcceptNewCustomer();

    assertNotNull("The game must do an end state call", vals);


  }

  /**
   * Test if frustration changes with time
   * @author Felix Seanor
   * @satisfies UR_CUSTOMER_TIME_LIMIT
   * @date 01/05/23
   */
  @Test
  public void TestFrustration() {
    instantiateCustomerScripts();
    customerController.SetWaveAmount(1);
    customerController.CanAcceptNewCustomer();
    float frustration = customerController.getCurrentWaitingCustomerGroup().Frustration;

    customerController.getCurrentWaitingCustomerGroup().CheckFrustration(1, null, true);
    assertNotEquals(frustration, customerController.getCurrentWaitingCustomerGroup().Frustration);

  }

  /**
   * Test if customers move around their groups correctly
   * @author Felix Seanor
   * @date 01/05/23
   * @satisfies UR_CUSTOMER_ORDER
   */
  @Test
  public void TestCustomerTransference() {
    instantiateCustomerScripts();

    customerController.SetWaveAmount(-1);

    customerController.CanAcceptNewCustomer();

    CustomerGroups group = customerController.getCurrentWaitingCustomerGroup();
    assertTrue(group.Members.size() == group.MembersInLine.size());

    for (int i = 0; i < group.Members.size(); i++) {
      assertFalse(customerController.DoSatisfactionCheck());
      group.RemoveFirstCustomer();


    }

    assertTrue(group.Members.size() == group.MembersSeatedOrWalking.size());
    assertTrue(customerController.DoSatisfactionCheck());

    customerController.CanAcceptNewCustomer();

    assertNotEquals(customerController.SittingCustomerCount(), 0);

    customerController.SeeIfCustomersShouldLeave(20);

    assertEquals(customerController.SittingCustomerCount(), 0);

    assertNotEquals(customerController.LeavingCustomerCount(), 0);


  }

  /**
   * Test that customer held items are displayed
   * Felix Seanor
   * @date 26/04/23
   * FR_DISPLAY_ORDER
   */
  @Test
  public void TestHeldItems() {
    instantiateCustomerScripts();
    customerController.CanAcceptNewCustomer();

    customerController.SetWaveAmount(-1);

    customerController.CanAcceptNewCustomer();

    CustomerGroups group = customerController.getCurrentWaitingCustomerGroup();
    Customer customer = group.Members.get(0);
    assertNotNull(customer.returnHeldItem());

    assertTrue(customer.returnHeldItem().position.x == 0);
    assertTrue(customer.returnHeldItem().position.y == 0);

    customer.displayItem();

    assertTrue(customer.returnHeldItem().position.x > 0);
    assertTrue(customer.returnHeldItem().position.y > 0);

    customer.hideItem();
    assertFalse("Item must be hidden", customer.returnHeldItem().isVisible);
    customer.displayItem();
    assertTrue("Item must be hidden", customer.returnHeldItem().isVisible);


  }

  /**
   * Test that customer groups are constructed correctly
   * @author Felix Seanor
   * @date 26/04/23
   */
  @Test
  public void TestCustomerGroups() {
    instantiateCustomerScripts();

    customerController.CanAcceptNewCustomer();

    CustomerGroups group = customerController.getCurrentWaitingCustomerGroup();
    Customer cust1 = group.Members.get(0);

    assertNotEquals("Group must have members", 0, group.Members.size());

    ItemEnum dish = group.Members.get(0).dish;
    assertNotNull("Members must have a dish", dish);

    boolean attempt = group.SeeIfDishIsCorrect(dish) != -1;

    assertTrue("Must be able to remove a customer by their food", attempt);

    customerController.tryGiveFood(new Item(dish));

    assertEquals("A member must be trying to sit down now", group.MembersSeatedOrWalking.size(), 1);
    assertEquals("That member must have the correct dish to be able to sit down", dish,
        group.MembersSeatedOrWalking.get(0).dish);

    assertTrue("Money must have increased", CustomerController.Money>0);


  }


  /**
   * If this test fails rerun it, there's a 1% chance it fails due to probability.
   * @author Felix Seanor
   * @date 29/04/23
   */
  @Test
  public void TestDishCreation() {
    instantiateCustomerScripts(Difficulty.Mindbreaking);
    customerController.updateMenu(true);

    List<ItemEnum> order = customerController.getMenu().CreateNewOrder(1000, Randomisation.TrueRandom);

    OrderMenu menu = customerController.getMenu();

    boolean containsBurger =
        order.contains(ItemEnum.Burger) && order.contains(ItemEnum.CheeseBurger);
    boolean containsSalad = order.contains(ItemEnum.TomatoOnionLettuceSalad) && order.contains(
        ItemEnum.LettuceTomatoSalad) && order.contains(ItemEnum.LettuceOnionSalad);
    boolean containsPotato =
        order.contains(ItemEnum.BakedPotato) && order.contains(ItemEnum.MeatBake) && order.contains(
            ItemEnum.CheeseBake);
    boolean containsPizza =
        order.contains(ItemEnum.CheesePizzaCooked) && order.contains(ItemEnum.MeatPizzaCooked)
            && order.contains(ItemEnum.VegPizzaCooked);

    assertTrue(containsPizza);
    assertTrue(containsBurger);
    assertTrue(containsPotato);
    assertTrue(containsSalad);

    order.clear();

    order = customerController.getMenu().CreateNewOrder(1000, Randomisation.Normalised);

    containsBurger = order.contains(ItemEnum.Burger) && order.contains(ItemEnum.CheeseBurger);
    containsSalad = order.contains(ItemEnum.TomatoOnionLettuceSalad) && order.contains(
        ItemEnum.LettuceTomatoSalad) && order.contains(ItemEnum.LettuceOnionSalad);
    containsPotato =
        order.contains(ItemEnum.BakedPotato) && order.contains(ItemEnum.MeatBake) && order.contains(
            ItemEnum.CheeseBake);
    containsPizza =
        order.contains(ItemEnum.CheesePizzaCooked) && order.contains(ItemEnum.MeatPizzaCooked)
            && order.contains(ItemEnum.VegPizzaCooked);

    assertTrue(containsPizza);
    assertTrue(containsBurger);
    assertTrue(containsPotato);
    assertTrue(containsSalad);


  }

  /**
   * Tries to construct an atlas
   * @author Felix Seanor
   * @date 19/04/23
   */
  @Test
  public void TryAtlas() {
    instantiateCustomerScripts(Difficulty.Mindbreaking);
    customerController.generateCustomerArray();

  }

  /**
   * Test if the sprites update given a new animation state
   * @author Felix Seanor
   * @date 26/04/23
   */
  @Test
  public void UpdateSpriteTest() {
    instantiateCustomerScripts(Difficulty.Mindbreaking);

    customerController.CanAcceptNewCustomer();

    CustomerGroups group = customerController.getCurrentWaitingCustomerGroup();

    Customer customer = group.Members.get(0);

    customer.updateSpriteFromInput("idlewest");
    String currentAnimation = customer.getCurrentOrientation();

    assertTrue("Must have set the current animation state to idlewest",
        currentAnimation == "idlewest");
  }

  /**
   * Test whether the orientation of the customer changes depending on the direction theyre moving
   * Uses same code as chef orientation
   * @satisfies FR_CHEF_ORIENTATION
   * @author Felix Seanor
   * @date 26/04/23
   */
  @Test
  public void OrientationTest() {
    instantiateCustomerScripts(Difficulty.Mindbreaking);

    customerController.CanAcceptNewCustomer();

    CustomerGroups group = customerController.getCurrentWaitingCustomerGroup();

    Customer customer = group.Members.get(0);

    assertNotNull("Must have a move state", customer.getMove());

    List<Vector2> path = new LinkedList<>();

    path.add(new Vector2(0, 0));
    customer.GivePath(path);
    assertTrue("Must have a move state", customer.getMove().contains("idle"));

    path = new LinkedList<>();

    path.add(new Vector2(0, 1));
    customer.GivePath(path);
    assertTrue("Must have a move state", customer.getMove().contains("north"));

    path = new LinkedList<>();

    path.add(new Vector2(0, -1));
    customer.GivePath(path);
    assertTrue("Must have a move state", customer.getMove().contains("south"));

    path = new LinkedList<>();

    path.add(new Vector2(1, 0));
    customer.GivePath(path);
    assertTrue("Must have a move state", customer.getMove().contains("east"));

    path = new LinkedList<>();

    path.add(new Vector2(-1, 0));
    customer.GivePath(path);
    assertTrue("Must have a move state", customer.getMove().contains("west"));
  }

  /**
   * Tests that the customer orientation updates correctly when given a new input.
   * uses same code as chef
   * @satisfies  FR_CHEF_ORIENTATION
   * @author Jack Vickers
   * @author Felix Seanor
   * @date 01/05/2023
   */
  @Test
  public void testUpdateSpriteFromInput() {
    instantiateCustomerScripts(Difficulty.Mindbreaking);
    customerController.CanAcceptNewCustomer();
    CustomerGroups group = customerController.getCurrentWaitingCustomerGroup();
    Customer customer = group.Members.get(0);
    customer.updateSpriteFromInput("west");
    assertEquals("west", customer.getCurrentOrientation());
    customer.updateSpriteFromInput("east");
    assertEquals("east", customer.getCurrentOrientation());
    customer.updateSpriteFromInput("north");
    assertEquals("north", customer.getCurrentOrientation());
    customer.updateSpriteFromInput("south");
    assertEquals("south", customer.getCurrentOrientation());
  }

  /**
   * Tests the behaviour of the customer update function by spying on the Customer class.
   * @satisfies UR_CUSTOMER_ORDER
   * @author Jack Vickers
   * @date 30/04/2023
   */
  @Test
  public void testCustomerUpdate() {
    if (GameObjectManager.objManager == null) {
      // creates game object manager which makes sure that the game object manager is not null when it is needed
      new GameObjectManager();
    }
    // Creates a customer and spies on the class
    Customer spyCustomer = spy(
        new Customer(1, ItemEnum.Burger, new TextureAtlas("Customers/Customer1/customer1.txt")));
    GameObject customer = new GameObject(new BlackSprite());
    customer.position.set(0, 0);
    customer.attachScript(spyCustomer); // ensures that the customer is attached to the game object
    customer.isVisible = true;
    spyCustomer.Update(1 / 60.f); // calls the update function
    // verifies that the update function was called
    verify(spyCustomer, times(1)).Update(1 / 60.f);
    // verifies that the display item function was called
    verify(spyCustomer, times(1)).displayItem();
    spyCustomer.eaten = true;
    spyCustomer.Update(1 / 60.f);
    // verifies that the hide item function was called (because the item has been eaten)
    verify(spyCustomer, times(1)).hideItem();
  }

  //TODO: Write test for loading a scenario game

}
