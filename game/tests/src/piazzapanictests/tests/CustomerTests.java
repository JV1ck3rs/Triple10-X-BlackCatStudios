package piazzapanictests.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Core.CustomerController;
import com.mygdx.game.Core.Customers.CustomerGroups;
import com.mygdx.game.Core.Customers.OrderMenu;
import com.mygdx.game.Core.Customers.Randomisation;
import com.mygdx.game.Core.GameObjectManager;
import com.mygdx.game.Core.GameState.Difficaulty;
import com.mygdx.game.Core.GameState.DifficultyMaster;
import com.mygdx.game.Core.GameState.DifficultyState;
import com.mygdx.game.Core.Pathfinding;
import com.mygdx.game.Core.TextureDictionary;
import com.mygdx.game.Core.ValueStructures.CustomerControllerParams;
import com.mygdx.game.Core.ValueStructures.EndOfGameValues;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)

public class CustomerTests {

  GameObjectManager manager;
  CustomerController cust;
  Pathfinding pathfinding;
  EndOfGameValues vals;
  CustomerControllerParams params = new CustomerControllerParams();
  void InstantiateCustomerScripts(Difficaulty difficaulty){

    GameObjectManager.objManager = null;
    TextureDictionary dico = new TextureDictionary();

    DifficultyState difficultyState = DifficultyMaster.getDifficulty(difficaulty);
    pathfinding = new Pathfinding(GameScreen.TILE_WIDTH/4,GameScreen.viewportWidth,GameScreen.viewportWidth);

    manager = new GameObjectManager();
  params = difficultyState.ccParams;
  params.NoCustomers= 5;
  cust = new CustomerController(new Vector2(0,0), new Vector2(32,0),pathfinding, (EndOfGameValues a)->EndGame(a), params,new Vector2(190,390),new Vector2(190,290),new Vector2(290,290));


  }

  void InstantiateCustomerScripts(){

   InstantiateCustomerScripts(Difficaulty.Stressful);
  }
@Test
  public void TestEndGame(){
      InstantiateCustomerScripts();

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
    InstantiateCustomerScripts();
    cust.SetWaveAmount(1);
    cust.CanAcceptNewCustomer();
    float frustration = cust.getCurrentWaitingCustomerGroup().Frustration;

    cust.getCurrentWaitingCustomerGroup().CheckFrustration(1,null);
    assertNotEquals(frustration,cust.getCurrentWaitingCustomerGroup().Frustration);

  }

  @Test
  public void TestCustomerTransference(){
    InstantiateCustomerScripts();

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
    InstantiateCustomerScripts();
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
    InstantiateCustomerScripts();


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
    InstantiateCustomerScripts(Difficaulty.Mindbreaking);

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
    InstantiateCustomerScripts(Difficaulty.Mindbreaking);
    cust.generateCustomerArray();

  }


  void EndGame(EndOfGameValues val){
  vals =val;
  }

}
