package com.mygdx.game.Core.Customers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
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
import java.util.function.Consumer;

/**
 * This manages a group (wave) of customers. It stores their current status in lists such eating
 * lining up BlackCatStudio's Code
 *
 * @author Felix Seanor
 * @date 29/04/23
 */
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
   * @param MemberCount   number of customers in group
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
      customer.position.set(Spawn);
      customer.attachScript(custLogic);
      customer.isVisible = true;

      Members.add(custLogic);
      addMemberToLine(custLogic);
    }

  }


  void addMemberToLine(Customer customer) {
    MembersInLine.add(customer);
    customer.waitingAtCounter = true;
  }

  void addMemberToSitting(Customer customer) {
    MembersSeatedOrWalking.add(customer);
    customer.waitingAtCounter = false;
  }

  /**
   * Create customer group from saved state
   *
   * @param state
   * @param customerAtlas
   * @author Felix Seanor
   */
  public CustomerGroups(CustomerGroupState state, ArrayList<TextureAtlas> customerAtlas) {
    Orders = Arrays.asList(state.orders);
    Frustration = state.frustration;
    RecoveryValue = FrustrationRecovery * Frustration;
    for (int i = 0; i < state.customerPositions.length; i++) {

      Customer custLogic = new Customer(state.CustomerStartID + i, state.orders[i],
          Customer.getCustomerAtlas(customerAtlas));
      GameObject customer = new GameObject(new BlackSprite());
      customer.position.set(state.customerPositions[i]);
      customer.attachScript(custLogic);
      customer.isVisible = true;
      Members.add(custLogic);
      MembersInLine.add(custLogic);
    }
  }

  public List<ItemEnum> getOrders() {
    return Orders;
  }

  public void showIcons() {
    for (int i = 0; i < MembersInLine.size(); i++) {
      //System.out.println(MembersInLine.get(i));
      List<Vector2> path = MembersInLine.get(i).getPath();
      MembersInLine.get(i).foodIcon.getBlackTexture().height = 25;
      MembersInLine.get(i).foodIcon.getBlackTexture().width = 25;
      MembersInLine.get(i).foodIcon.setPosition(MembersInLine.get(i).getX() + 15,
          MembersInLine.get(i).getY() + 20);
      MembersInLine.get(i).foodIcon.image.layer = 10;
      if (path.isEmpty()) {
        MembersInLine.get(i).foodIcon.isVisible = true;
      }
    }
  }

  public void removeIcons(Customer customer) {
    customer.foodIcon.isVisible = false;
  }


  public void checkClicks() {
    for (int i = 0; i < Members.size(); i++) {
      if (Members.get(i).foodIcon.isClicked() && Members.get(i).foodIcon.isVisible) {
        Members.get(i).foodRecipe.isVisible = true;
        Members.get(i).recipeCloseButton.isVisible = true;
        Members.get(i).foodRecipeOpen = true;
      }
    }

    for (int i = 0; i < Members.size(); i++) {
      if (Members.get(i).foodRecipeOpen) {
        if (Members.get(i).recipeCloseButton.isClicked()) {
          Members.get(i).foodRecipe.isVisible = false;
          Members.get(i).recipeCloseButton.isVisible = false;
        }
      }
    }

  }


  public Customer RemoveFirstCustomer() {
    Customer customer = MembersInLine.remove(0);
    addMemberToSitting(customer);
    removeIcons(customer);
    return MembersSeatedOrWalking.get(MembersSeatedOrWalking.size() - 1);
  }

  public Customer removeAnyCustomer(Integer customerToRemove){
    Customer customer = null;
    if(MembersInLine.size() >= customerToRemove){
      customer = MembersInLine.get(customerToRemove);
      MembersInLine.remove(customerToRemove);
      addMemberToSitting(customer);
    }
    return customer;
  }

  /**
   * See if the given dish is correct
   *
   * @param item
   * @return if is able to remove
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

  /**
   * Is supplied dish in this group
   *
   * @param item
   * @return
   * @author Felix Seanor
   */
  public int SeeIfDishIsCorrect(Item item) {
    return SeeIfDishIsCorrect(item.name);
  }

  /**
   * Increases Frustration after a successful service (adds more time on)
   *
   * @author Felix Seanor
   */
  public void updateFrustrationOnSucessfulService() {
    Frustration += RecoveryValue;
  }

  public Customer FeedSpecificCustomer(int i) {
    Customer customer = MembersInLine.remove(i);
    addMemberToSitting(customer);
    removeIcons(customer);


    return MembersSeatedOrWalking.get(MembersSeatedOrWalking.size() - 1);
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
  public void CheckFrustration(float dt, Consumer<CustomerGroups> CauseLeave, Boolean updateFrustration) {
    if (updateFrustration){
      Frustration -= dt;
      if (Frustration <= 0) {
        for (Customer customer : Members
        ) {
          customer.foodIcon.isVisible = false;
        }
        CauseLeave.accept(this);
      }
    }

  }

  /**
   * Save the current state of this group into CustomerGroupState
   *
   * @param leaving if this group is leaving
   * @return the current state of this group
   * @author Felix Seanor
   */
  public CustomerGroupState SaveState(boolean leaving) {
    CustomerGroupState state = new CustomerGroupState();
    state.customerPositions = new Vector2[Members.size()];
    state.customersInGroupOrdering = new int[MembersInLine.size()];
    state.orders = new ItemEnum[Members.size()];
    state.Table = table.ID;
    state.frustration = Frustration;
    state.CustomerStartID = Members.get(0).customerNumber;
    state.NumCustomersWalkingToTable = MembersSeatedOrWalking.size();

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

  // get Members
  public List<Customer> getMembers() {
    return Members;
  }

  /**
   * Destroy this entire group
   *
   * @author Felix Seanor
   */
  public void destroy() {
    for (Customer cust : Members
    ) {
      cust.Destroy();
    }
  }

}
