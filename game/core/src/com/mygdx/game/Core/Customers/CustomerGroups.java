package com.mygdx.game.Core.Customers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Core.GameState.CustomerGroupState;
import com.mygdx.game.Core.Rendering.BlackSprite;
import com.mygdx.game.Core.Rendering.GameObject;
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

  public List<Customer> members = new LinkedList<>();
  public List<Customer> membersInLine = new LinkedList<>();
  public List<Customer> membersSeatedOrWalking = new LinkedList<>();
  public List<ItemEnum> orders = new LinkedList<>();

  public Table table;
  public float frustration;

  private float recoveryValue;
  static float frustrationRecovery = .1f;

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
    orders = OrderMenu;
    this.frustration = frustration * MemberCount;
    recoveryValue = frustrationRecovery * this.frustration;
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

      members.add(custLogic);
      addMemberToLine(custLogic);
    }

  }


  void addMemberToLine(Customer customer) {
    membersInLine.add(customer);
    customer.waitingAtCounter = true;
  }

  void addMemberToSitting(Customer customer) {
    membersSeatedOrWalking.add(customer);
    updateFrustrationOnSucessfulService();
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
    orders = Arrays.asList(state.orders);
    frustration = state.frustration;
    recoveryValue = frustrationRecovery * frustration;
    for (int i = 0; i < state.customerPositions.length; i++) {

      Customer customerLogic = new Customer(state.CustomerStartID + i, state.orders[i],
          Customer.getCustomerAtlas(customerAtlas));
      GameObject customer = new GameObject(new BlackSprite());
      customer.position.set(state.customerPositions[i]);
      customer.attachScript(customerLogic);
      customer.isVisible = true;
      members.add(customerLogic);
      membersInLine.add(customerLogic);
    }
  }

  public List<ItemEnum> getOrders() {
    return orders;
  }

  public void showIcons() {
    for (int i = 0; i < membersInLine.size(); i++) {
      //System.out.println(MembersInLine.get(i));
      List<Vector2> path = membersInLine.get(i).getPath();
      membersInLine.get(i).foodIcon.getBlackTexture().height = 25;
      membersInLine.get(i).foodIcon.getBlackTexture().width = 25;
      membersInLine.get(i).foodIcon.setPosition(membersInLine.get(i).getX() + 15,
          membersInLine.get(i).getY() + 20);
      membersInLine.get(i).foodIcon.image.layer = 10;
      if (path.isEmpty()) {
        membersInLine.get(i).foodIcon.isVisible = true;
      }
    }
  }

  public void removeIcons(Customer customer) {
    customer.foodIcon.isVisible = false;
  }


  public void checkClicks() {
    for (int i = 0; i < members.size(); i++) {
      if (members.get(i).foodIcon.isClicked() && members.get(i).foodIcon.isVisible) {
        members.get(i).foodRecipe.isVisible = true;
        members.get(i).recipeCloseButton.isVisible = true;
        members.get(i).foodRecipeOpen = true;
      }
    }

    for (int i = 0; i < members.size(); i++) {
      if (members.get(i).foodRecipeOpen) {
        if (members.get(i).recipeCloseButton.isClicked()) {
          members.get(i).foodRecipe.isVisible = false;
          members.get(i).recipeCloseButton.isVisible = false;
        }
      }
    }

  }


  public Customer RemoveFirstCustomer() {
    Customer customer = membersInLine.remove(0);
    addMemberToSitting(customer);
    removeIcons(customer);
    return membersSeatedOrWalking.get(membersSeatedOrWalking.size() - 1);
  }

  public Customer removeAnyCustomer(Integer customerToRemove) {
    Customer customer = null;
    if (membersInLine.size() >= customerToRemove) {
      customer = membersInLine.get(customerToRemove);
      membersInLine.remove(customerToRemove);
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
    for (int i = 0; i < membersInLine.size(); i++) {
      if (membersInLine.get(i).dish == item) {
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
    frustration += recoveryValue;
  }

  public Customer FeedSpecificCustomer(int i) {
    Customer customer = membersInLine.remove(i);
    addMemberToSitting(customer);
    removeIcons(customer);

    return membersSeatedOrWalking.get(membersSeatedOrWalking.size() - 1);
  }

  /**
   * Updates animations of all customers
   *
   * @author Felix Seanor
   */
  public void updateSpriteFromInput() {
    for (Customer customer : members) {
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
  public void CheckFrustration(float dt, Consumer<CustomerGroups> CauseLeave,
      Boolean updateFrustration) {
    if (updateFrustration) {
      frustration -= dt;
      if (frustration <= 0) {
        for (Customer customer : members
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
    state.customerPositions = new Vector2[members.size()];
    state.customersInGroupOrdering = new int[membersInLine.size()];
    state.orders = new ItemEnum[members.size()];
    state.table = table.ID;
    state.frustration = frustration;
    state.CustomerStartID = members.get(0).customerNumber;
    state.numCustomersWalkingToTable = membersSeatedOrWalking.size();

    for (int i = 0; i < members.size(); i++) {
      state.customerPositions[i] = members.get(i).gameObject.position;
      state.orders[i] = members.get(i).dish;

    }

    for (int i = 0; i < membersInLine.size(); i++) {
      state.customersInGroupOrdering[i] = i;
    }

    state.leaving = leaving;

    return state;
  }

  // get Members
  public List<Customer> getMembers() {
    return members;
  }

  /**
   * Destroy this entire group
   *
   * @author Felix Seanor
   */
  public void destroy() {
    for (Customer cust : members
    ) {
      cust.Destroy();
    }
  }

}
