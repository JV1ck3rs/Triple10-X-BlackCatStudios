package com.mygdx.game.Core.Customers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.Core.BlackSprite;
import com.mygdx.game.Core.GameObject;
import com.mygdx.game.Core.GameState.CustomerGroupState;
import com.mygdx.game.Customer;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import com.badlogic.gdx.math.Vector2;
import java.util.function.Consumer;

public class CustomerGroups {

  public List<Customer> Members = new LinkedList<>();
  public List<Customer> MembersInLine = new LinkedList<>();
  public List<Customer> MembersSeatedOrWalking = new LinkedList<>();
  public List<ItemEnum> Orders = new LinkedList<>();

  public Table table;
  public float Frustration;

  private float RecoveryValue;
  static float FrustrationRecovery = .1f;

  /**
   * Creates a customer group with given parameters
   *
   * @param MemberCount  number of customers in group
   * @param CustomerStart
   * @param Spawn
   * @param frustration
   * @param OrderMenu     food each member should order
   * @author Felix Seanor
   */
  public CustomerGroups(int MemberCount, int CustomerStart, Vector2 Spawn, int frustration,
      List<ItemEnum> OrderMenu, ArrayList<TextureAtlas> customerAtlas) {
    Orders = OrderMenu;
    Frustration = frustration * MemberCount;
    RecoveryValue = FrustrationRecovery * Frustration;
    for (int i = 0; i < MemberCount; i++) {
      if (OrderMenu.size() < MemberCount) {
        i = i;
      }

      Customer custLogic = new Customer(CustomerStart + i, OrderMenu.get(i),
          Customer.getCustomerAtlas(customerAtlas));
      GameObject customer = new GameObject(new BlackSprite());
    //  customer.position.set( new Vector2(0,30*i).sub(Spawn));
      customer.position.set(Spawn);
      customer.attachScript(custLogic);
      customer.isVisible = true;

      Members.add(custLogic);
      MembersInLine.add(custLogic);
    }

  }

  public CustomerGroups(CustomerGroupState state, ArrayList<TextureAtlas> customerAtlas){
    Orders = Arrays.asList(state.orders);
    Frustration = state.frustration;
    RecoveryValue =  FrustrationRecovery * Frustration;
    for (int i = 0; i < state.customerPositions.length; i++) {


      Customer custLogic = new Customer(state.CustomerStartID + i,state.orders[i], Customer.getCustomerAtlas(customerAtlas));
      GameObject customer = new GameObject(new BlackSprite());
      customer.position.set(state.customerPositions[i]);
      customer.attachScript(custLogic);
      customer.isVisible = true;

      Members.add(custLogic);
      MembersInLine.add(custLogic);
    }

  }

  /**
   * Removes the first customer from a member in line. CAN RETURN NULL
   *
   * @return Customer removed
   * @author Felix Seanor
   */
  public Customer RemoveFirstCustomer() {
    MembersSeatedOrWalking.add(MembersInLine.remove(0));
    return MembersSeatedOrWalking.get(MembersSeatedOrWalking.size() - 1);
  }

  /**
   * Try and remove a customer given an item
   *
   * @param item
   * @return if removal successful
   * @author Felix Seanor
   */

  public int SeeIfDishIsCorrect(ItemEnum item) {
    for (int i = 0; i < MembersInLine.size(); i++) {
      if (MembersInLine.get(i).dish == item) {
        return i;
      }
    }

    return -1;

  }

  public int SeeIfDishIsCorrect(Item item) {
    return SeeIfDishIsCorrect(item.name);
  }

  public void updateFrustrationOnSucessfulService() {
    Frustration += RecoveryValue;
  }

  /**
   * Updates animations of all customers
   *
   * @author Felix Seanor
   */
  public void updateSpriteFromInput() {
    for (Customer customer : Members) {
      customer.updateSpriteFromInput(customer.getMove());
    }


  }

  /**
   * Changes frustration If too frustrated then leave
   *
   * @param dt
   * @param CauseLeave Function causing this customer to leave
   * @author Felix Seanor
   */
  public void CheckFrustration(float dt, Consumer<CustomerGroups> CauseLeave) {
    Frustration -= dt;
    if (Frustration <= 0) {
      CauseLeave.accept(this);
    }
  }

public CustomerGroupState SaveState(boolean leaving){
    CustomerGroupState state = new CustomerGroupState();
    state.customerPositions = new Vector2[Members.size()];
    state.customersInGroupOrdering = new int[MembersInLine.size()];
    state.orders = new ItemEnum[Members.size()];
    state.Table = table.ID;
    state.frustration = Frustration;
    state.CustomerStartID = Members.get(0).customerNumber;

  for (int i = 0; i < Members.size(); i++) {
    state.customerPositions[i] = Members.get(i).gameObject.position;
    state.orders[i] = Members.get(i).dish;

  }

  for (int i = 0; i < MembersInLine.size(); i++) {
    state.customersInGroupOrdering[i] = i;
  }

  state.leaving = leaving;

  return state;


}

public void destroy()
{
  for (Customer cust: Members
  ) {
    cust.Destroy();
  }
}

}
