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
import org.mockito.Mock;

@RunWith(GdxTestRunner.class)

public class CustomerTests extends MasterTestClass {

  @Test
  public void TestEndGame() {
    instantiateCustomerScripts();

    cust.SetWaveAmount(0);
    cust.ModifyReputation(-10);

    assertNotNull("The game must do an end state call", vals);

    vals = null;

    cust.ModifyReputation(20);

    cust.CanAcceptNewCustomer();

    assertNotNull("The game must do an end state call", vals);


  }

  @Test
  public void TestFrustration() {
    instantiateCustomerScripts();
    cust.SetWaveAmount(1);
    cust.CanAcceptNewCustomer();
    float frustration = cust.getCurrentWaitingCustomerGroup().Frustration;

    cust.getCurrentWaitingCustomerGroup().CheckFrustration(1, null);
    assertNotEquals(frustration, cust.getCurrentWaitingCustomerGroup().Frustration);

  }

  @Test
  public void TestCustomerTransference() {
    instantiateCustomerScripts();

    cust.SetWaveAmount(-1);

    cust.CanAcceptNewCustomer();

    CustomerGroups group = cust.getCurrentWaitingCustomerGroup();
    assertTrue(group.Members.size() == group.MembersInLine.size());

    for (int i = 0; i < group.Members.size(); i++) {
      assertFalse(cust.DoSatisfactionCheck());
      group.RemoveFirstCustomer();


    }

    assertTrue(group.Members.size() == group.MembersSeatedOrWalking.size());
    assertTrue(cust.DoSatisfactionCheck());

    cust.CanAcceptNewCustomer();

    assertNotEquals(cust.SittingCustomerCount(), 0);

    cust.SeeIfCustomersShouldLeave(20);

    assertEquals(cust.SittingCustomerCount(), 0);

    assertNotEquals(cust.LeavingCustomerCount(), 0);


  }

  @Test
  public void TestHeldItems() {
    instantiateCustomerScripts();
    cust.CanAcceptNewCustomer();

    cust.SetWaveAmount(-1);

    cust.CanAcceptNewCustomer();

    CustomerGroups group = cust.getCurrentWaitingCustomerGroup();
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

  @Test
  public void TestCustomerGroups() {
    instantiateCustomerScripts();

    cust.CanAcceptNewCustomer();

    CustomerGroups group = cust.getCurrentWaitingCustomerGroup();
    Customer cust1 = group.Members.get(0);

    assertNotEquals("Group must have members", 0, group.Members.size());

    ItemEnum dish = group.Members.get(0).dish;
    assertNotNull("Members must have a dish", dish);

    boolean attempt = group.SeeIfDishIsCorrect(dish) != -1;

    assertTrue("Must be able to remove a customer by their food", attempt);

    cust.tryGiveFood(new Item(dish));

    assertEquals("A member must be trying to sit down now", group.MembersSeatedOrWalking.size(), 1);
    assertEquals("That member must have the correct dish to be able to sit down", dish,
        group.MembersSeatedOrWalking.get(0).dish);


  }


  /**
   * If this test fails rerun it, theres a 1% chance it fails due to probability.
   */
  @Test
  public void TestDishCreation() {
    instantiateCustomerScripts(Difficulty.Mindbreaking);

    List<ItemEnum> order = cust.getMenu().CreateNewOrder(1000, Randomisation.TrueRandom);

    OrderMenu menu = cust.getMenu();

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

    order = cust.getMenu().CreateNewOrder(1000, Randomisation.Normalised);

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

  @Test
  public void TryAtlas() {
    instantiateCustomerScripts(Difficulty.Mindbreaking);
    cust.generateCustomerArray();

  }


  @Test
  public void UpdateSpriteTest() {
    instantiateCustomerScripts(Difficulty.Mindbreaking);

    cust.CanAcceptNewCustomer();

    CustomerGroups group = cust.getCurrentWaitingCustomerGroup();

    Customer customer = group.Members.get(0);

    customer.updateSpriteFromInput("idlewest");
    String currentAnimation = customer.getCurrentOrientation();

    assertTrue("Must have set the current animation state to idlewest",
        currentAnimation == "idlewest");
  }

  @Test
  public void OrientationTest() {
    instantiateCustomerScripts(Difficulty.Mindbreaking);

    cust.CanAcceptNewCustomer();

    CustomerGroups group = cust.getCurrentWaitingCustomerGroup();

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
   *
   * @author Jack Vickers
   * @date 29/04/2023
   */
  @Test
  public void testUpdateSpriteFromInput() {
    instantiateCustomerScripts(Difficulty.Mindbreaking);
    cust.CanAcceptNewCustomer();
    CustomerGroups group = cust.getCurrentWaitingCustomerGroup();
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

}
