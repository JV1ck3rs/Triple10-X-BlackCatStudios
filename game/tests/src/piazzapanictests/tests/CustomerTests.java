package piazzapanictests.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.mygdx.game.Core.Customers.CustomerGroups;
import com.mygdx.game.Core.Customers.OrderMenu;
import com.mygdx.game.Core.Customers.Randomisation;
import com.mygdx.game.Core.GameState.Difficulty;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)

public class CustomerTests extends MasterTestClass {

@Test
  public void TestEndGame(){
      instantiateCustomerScripts();

      cust.SetWaveAmount(0);
      cust.ModifyReputation(-10);

  assertNotNull("The game must do an end state call",vals);

  vals = null;

  cust.ModifyReputation(20);

  cust.CanAcceptNewCustomer();


  assertNotNull("The game must do an end state call",vals);


  }
  @Test
  public void TestFrustration(){
    instantiateCustomerScripts();
    cust.SetWaveAmount(1);
    cust.CanAcceptNewCustomer();
    float frustration = cust.getCurrentWaitingCustomerGroup().Frustration;

    cust.getCurrentWaitingCustomerGroup().CheckFrustration(1,null);
    assertNotEquals(frustration,cust.getCurrentWaitingCustomerGroup().Frustration);

  }

  @Test
  public void TestCustomerTransference(){
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

    assertNotEquals(cust.LeavingCustomerCount(),0);



  }

  @Test
  public void TestHeldItems() {
    instantiateCustomerScripts();
    cust.CanAcceptNewCustomer();

    cust.SetWaveAmount(-1);

    cust.CanAcceptNewCustomer();

    CustomerGroups group = cust.getCurrentWaitingCustomerGroup();

    assertNotNull(group.Members.get(0).returnHeldItem());

    assertTrue(group.Members.get(0).returnHeldItem().position.x  == 0);
    assertTrue(group.Members.get(0).returnHeldItem().position.y  == 0);

    group.Members.get(0).displayItem();

    assertTrue(group.Members.get(0).returnHeldItem().position.x >0);
    assertTrue(group.Members.get(0).returnHeldItem().position.y >0);

  }
  @Test
  public void TestCustomerGroups(){
    instantiateCustomerScripts();


    cust.CanAcceptNewCustomer();

    CustomerGroups group = cust.getCurrentWaitingCustomerGroup();

    assertNotEquals("Group must have members", 0,group.Members.size());

    ItemEnum dish = group.Members.get(0).dish;
    assertNotNull("Members must have a dish", dish);

    boolean attempt  = group.SeeIfDishIsCorrect(dish) != -1;

    assertTrue("Must be able to remove a customer by their food",attempt);

    cust.tryGiveFood(new Item(dish));


    assertEquals("A member must be trying to sit down now", group.MembersSeatedOrWalking.size(),1);
    assertEquals("That member must have the correct dish to be able to sit down", dish,group.MembersSeatedOrWalking.get(0).dish);


  }


  /**
   * If this test fails rerun it, theres a 1% chance it fails due to probability
   */
  @Test
  public void TestDishCreation(){
    instantiateCustomerScripts(Difficulty.Mindbreaking);

  List<ItemEnum> order =  cust.getMenu().CreateNewOrder(1000, Randomisation.TrueRandom);

    OrderMenu menu = cust.getMenu();

    boolean containsBurger = order.contains(ItemEnum.Burger) && order.contains(ItemEnum.CheeseBurger);
    boolean containsSalad = order.contains(ItemEnum.TomatoOnionLettuceSalad) && order.contains(ItemEnum.LettuceTomatoSalad) && order.contains(ItemEnum.LettuceOnionSalad);
    boolean containsPotato = order.contains(ItemEnum.BakedPotato) && order.contains(ItemEnum.MeatBake) && order.contains(ItemEnum.CheeseBake);
    boolean containsPizza = order.contains(ItemEnum.CheesePizzaCooked) && order.contains(ItemEnum.MeatPizzaCooked) && order.contains(ItemEnum.VegPizzaCooked);

    assertTrue(containsPizza);
    assertTrue(containsBurger);
    assertTrue(containsPotato);
    assertTrue(containsSalad);


    order.clear();

    order = cust.getMenu().CreateNewOrder(1000,Randomisation.Normalised);

    containsBurger = order.contains(ItemEnum.Burger) && order.contains(ItemEnum.CheeseBurger);
    containsSalad = order.contains(ItemEnum.TomatoOnionLettuceSalad) && order.contains(ItemEnum.LettuceTomatoSalad) && order.contains(ItemEnum.LettuceOnionSalad);
    containsPotato = order.contains(ItemEnum.BakedPotato) && order.contains(ItemEnum.MeatBake) && order.contains(ItemEnum.CheeseBake);
    containsPizza = order.contains(ItemEnum.CheesePizzaCooked) && order.contains(ItemEnum.MeatPizzaCooked) && order.contains(ItemEnum.VegPizzaCooked);


    assertTrue(containsPizza);
    assertTrue(containsBurger);
    assertTrue(containsPotato);
    assertTrue(containsSalad);


  }

  @Test
  public void TryAtlas(){
    instantiateCustomerScripts(Difficulty.Mindbreaking);
    cust.generateCustomerArray();

  }

}
