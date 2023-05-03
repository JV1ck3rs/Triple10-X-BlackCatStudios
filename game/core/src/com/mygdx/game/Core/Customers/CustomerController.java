package com.mygdx.game.Core.Customers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Core.GameState.CustomerGroupState;
import com.mygdx.game.Core.GameState.GameState;
import com.mygdx.game.Core.Inputs;
import com.mygdx.game.Core.PathFinder.DistanceTest;
import com.mygdx.game.Core.PathFinder.Pathfinding;
import com.mygdx.game.Core.Rendering.BlackTexture;
import com.mygdx.game.Core.Rendering.GameObject;
import com.mygdx.game.Core.SFX.SoundFrame;
import com.mygdx.game.Core.SFX.SoundFrame.soundsEnum;
import com.mygdx.game.Core.Scriptable;
import com.mygdx.game.Core.ValueStructures.CustomerControllerParams;
import com.mygdx.game.Core.ValueStructures.EndOfGameValues;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;

/**
 * This script controls the customers and handles their logic through a variety of secondary
 * scripts. Also handles when the current game should end.
 * <p>
 * BlackCatStudio's Code
 *
 * @author Felix Seanor
 * @author Jack Vickers <p> Last modified: 23/04/2023
 */
public class CustomerController extends Scriptable {

  /**
   * The current customer group waiting in line
   */
  CustomerGroups currentWaitingCustomer = null;

  /**
   * List of customers sitting down and eating. Groups only enter this when all members are eating
   */
  List<CustomerGroups> sittingCustomers = new LinkedList<>();
  /**
   * List of all customer groups trying to leave
   */
  List<CustomerGroups> walkingBackCustomers = new LinkedList<>();
  /**
   * Pathfinding module used
   */
  Pathfinding pathfinding;

  /**
   * List of tables
   */
  List<Table> tables;
  /**
   * Call this to start endge game sequence
   */
  Consumer<EndOfGameValues> callEndGame;

  /**
   * The Reputation.
   */
  public int reputation;
  /**
   * The constant money.
   */
  public static int money;
  /**
   * The Max money.
   */
  int maxMoney;
  /**
   * The Max reputation.
   */
  int maxReputation;
  /**
   * The Money per customer.
   */
  int moneyPerCustomer;

  /**
   * The Waves.
   */
  int waves = -1;
  /**
   * The Menu.
   */
  public OrderMenu menu;
  /**
   * The Current customer.
   */
  int currentCustomer = 0;
  /**
   * The Current wave.
   */
  int currentWave = 0;
  private float eatingTime = 7;
  private int timerWidth = 50;
  private int timerHeight = 10;

  /**
   * The Ovens added.
   */
  Boolean OvensAdded = false;

  /**
   * Frustration Time
   */
  private GameObject frustrationTimer;
  private GameObject frustrationTimerBackground;

  /**
   * Creates a new randomisation class based on the current time
   */
  Random rand = new Random(System.currentTimeMillis());

  /**
   * The minimum and maximum group size for customers groups
   */
  private Vector2 groupSize = new Vector2(1, 4);

  /**
   * timer defining when the next eating customer will leave
   */
  float timeToNextCustomerLeaving = eatingTime;
  /**
   * The Max customers.
   */
  int maxCustomers;
  /**
   * The Customers remaining.
   */
  int customersRemaining;
  /**
   * The Customers per wave.
   */
  ArrayList<Integer> customersPerWave;
  /**
   * this is call back for customer groups who get too frustrated so they need to leave
   */
  Consumer<CustomerGroups> frustrationCallBack;

  /**
   * Door position
   */
  Vector2 doorTarget;
  /**
   * where ordering queue should start
   */
  Vector2 orderAreaTarget;

  /**
   * All customer texture atlases
   */
  public ArrayList<TextureAtlas> customerAtlas = new ArrayList<>();

  /**
   * how long it takes for a group to be frustrated and leave without being served
   */
  private int customerFrustrationStart;

  /**
   * The Update frustration.
   */
  public boolean updateFrustration = true;
  /**
   * The Num customers served.
   */
  int numCustomersServed = 0;

  /**
   * Creates the customer controller
   *
   * @param DoorPosition     Customer spawn and exit.
   * @param OrderArea        First position in order line
   * @param path             Pathfinding Module.
   * @param CallUpGameFinish Game Finish Function.
   * @param params           Parameter class
   * @param tables           Where the tables are, TEMPORARY
   * @author Felix Seanor
   */
  public CustomerController(Vector2 DoorPosition, Vector2 OrderArea, Pathfinding path,
      Consumer<EndOfGameValues> CallUpGameFinish, CustomerControllerParams params,
      Vector2... tables) {
    frustrationCallBack = (CustomerGroups a) -> frustrationLeave(a);
    moneyPerCustomer = params.moneyPerCustomer;
    callEndGame = CallUpGameFinish;
    money = params.moneyStart;
    maxMoney = params.maxMoney;
    reputation = params.reputation;
    maxReputation = 5; // set the max reputation to 5
    customerFrustrationStart = params.frustrationStart;
    groupSize.y = Math.min(params.maxCustomersPerWave, groupSize.y);
    groupSize.x = Math.max(params.minCustomersPerWave, groupSize.x);

    calculateWavesFromNoCustomers(params.noCustomers);

    BlackTexture Black = new BlackTexture("Black.png");
    BlackTexture FrustBack = new BlackTexture("FrustrationBackground.png");

    frustrationTimerBackground = new GameObject(FrustBack);
    frustrationTimerBackground.position.set(298, 8);
    FrustBack.layer = -1;
    FrustBack.setSize(timerWidth + 4, timerHeight + 4);

    frustrationTimer = new GameObject(Black);

    frustrationTimer.position.set(300, 10);

    setTables(tables);

    if (waves != -1) {
      reputation = Math.min(reputation, waves);
    }
    generateCustomerArray();

    pathfinding = path;
    orderAreaTarget = OrderArea;
    doorTarget = DoorPosition;

    menu = new OrderMenu(10, 7, 3, params.orderTypePermissable);
  }

  /**
   * Convenience function which helps to calculate the number of customers in each wave of the
   * scenario mode.
   *
   * @param numCustomersPerWave The arraylist which will store the number of customers per wave
   * @param loopValue           Integer used to limit the number of times the while loop executes
   * @return
   */
  private ArrayList<Integer> calculateNumCustomersPerScenarioWave(
      ArrayList<Integer> numCustomersPerWave, int loopValue) {
    while (loopValue > 0) {
      if (loopValue < 6) {
        if (loopValue <= 3) {
          for (int i = 0; i < loopValue; i++) {
            customersPerWave.add(1);
          }

          loopValue = 0;
        } else {
          loopValue -= 2;
          customersPerWave.add(2);
        }
      } else {
        loopValue -= 5;
        customersPerWave.add(3);
        customersPerWave.add(2);
      }
    }
    return numCustomersPerWave;
  }

  /**
   * Gets money.
   *
   * @return the money
   */
  public int getMoney() {
    return money;
  }


  /**
   * Sets tables.
   *
   * @param tab the tab
   */
  public void setTables(Vector2... tab) {
    int ID = 0;
    tables = new LinkedList<>();
    for (Vector2 pos : tab
    ) {
      tables.add(new Table(pos.add(5, 10), ID++, 35));
    }

  }

  /**
   * Calculate waves from no customers.
   *
   * @param NoCustomers the no customers
   */
  public void calculateWavesFromNoCustomers(int NoCustomers) {
    maxCustomers = NoCustomers;
    customersRemaining = NoCustomers;

    if (NoCustomers == -1) {
      setWaveAmount(-1);
      return;
    }

    customersPerWave = new ArrayList<>();
    int tempVal = maxCustomers;

    /* The following block of code calculates the number of customers that should be
     * in each wave of the scenario mode.
     * The number of customers per wave is stored in the customersPerWave arraylist.
     * Waves can have 1, 2, or 3 customers. Waves with fewer customers are more likely to occur.
     */
    if (tempVal > 10) {
      double numLoops = Math.floor(tempVal / 10);
      int finalLoopVal = tempVal % 10; // The remainder of tempVal / 10
      tempVal = 10; // The waves are calculated with a limit of 10 each time the algorithm
      // is called. This way, it is ensured that waves with fewer customers are more likely to
      // occur.
      for (int c = 0; c < numLoops; c++) {
        calculateNumCustomersPerScenarioWave(customersPerWave, tempVal);
      }
      // The following line ensures that the correct number of customers will appear
      calculateNumCustomersPerScenarioWave(customersPerWave, finalLoopVal);
    } else {
      calculateNumCustomersPerScenarioWave(customersPerWave, tempVal);
    }
    // Sorts the arraylist in ascending order so that waves with fewer customers occur first
    Collections.sort(customersPerWave);

    waves = customersPerWave.size();
    setWaveAmount(waves);
  }

  /**
   * Sitting customer count int.
   *
   * @return the int
   */
  public int sittingCustomerCount() {
    return sittingCustomers.size();
  }

  /**
   * Gets menu.
   *
   * @return the menu
   */
  public OrderMenu getMenu() {
    return menu;
  }

  /**
   * Used to update the ordermenu when an oven has been bought
   *
   * @param a the a
   * @author Jack Hinton
   */
  public void updateMenu(boolean a) {

    if (!OvensAdded) {
      menu.ovenAdded();
    }

    menu.restock();

    OvensAdded = a;
  }

  /**
   * Gets customers per scenario wave.
   *
   * @return the customers per scenario wave
   */
  public ArrayList<Integer> getCustomersPerScenarioWave() {
    return customersPerWave;
  }

  /***
   * Set the maximum number of waves to do, exclusively. Resets currentWave to 0.
   * @param amount the amount
   * @author Felix Seanor
   */
  public void setWaveAmount(int amount) {
    waves = amount;
    currentWave = 0;
  }

  /**
   * Generates a customer array which can be used to get random customer sprites from the customer
   * class
   */
  public void generateCustomerArray() {
    String filename;
    TextureAtlas customerAtlas;

    //The file path takes it to data for each animation
    //The TextureAtlas creates a texture atlas where the you pass through the string of the number and it returns the image.
    //Taking all pictures in the diretory of the file
    for (int i = 1; i < 9; i++) {
      filename = "Customers/Customer" + i + "/customer" + i + ".txt";
      customerAtlas = new TextureAtlas(filename);
      this.customerAtlas.add(customerAtlas);
    }
  }

  /**
   * Updates all customers in groups to update their animation.
   *
   * @param customers the customers
   * @author Felix Seanor
   */
  public void updateCustomerMovements(List<CustomerGroups> customers) {
    for (int i = 0; i < customers.size(); i++) {
      customers.get(i).updateSpriteFromInput();
    }
  }

  /**
   * Gets current waiting customer group.
   *
   * @return the current waiting customer group
   */
  public CustomerGroups getCurrentWaitingCustomerGroup() {
    return currentWaitingCustomer;
  }

  /**
   * Sets current waiting customer group.
   *
   * @param group the group
   */
  public void setCurrentWaitingCustomerGroup(CustomerGroups group) {
    currentWaitingCustomer = group;
  }

  /**
   * Gets number of customers served.
   *
   * @return the number of customers served
   */
  public int getNumberOfCustomersServed() {
    return numCustomersServed;
  }

  /**
   * Gets remaining number of customers.
   *
   * @return the remaining number of customers
   */
  public int getRemainingNumberOfCustomers() {
    if (currentWaitingCustomer != null) {
      return customersRemaining + currentWaitingCustomer.membersInLine.size();
    }
    return customersRemaining;
  }

  /**
   * Modifies the reputation, if reputation + DR <= 0 END GAME.
   *
   * @param DR delta reputation
   * @author Felix Seanor
   */
  public void modifyReputation(int DR) {
    reputation += DR;
    reputation = Math.min(reputation, maxReputation);
    if (reputation <= 0) {
      endGame();
    }
  }

  /**
   * Do check and modify money.
   *
   * @param DM Delta Money
   * @return decrease in money is allowed.
   * @author Felix Seanor
   */
  public boolean changeMoney(float DM) {
    if (DM >= 0) {
      money += DM;
      money = Math.min(maxMoney, money);
      return true;
    }

    if (money - DM >= 0) {
      money += DM;
      SoundFrame.SoundEngine.playSound(soundsEnum.BuyItem);
      return true;
    }

    return false;


  }


  @Override
  public void update(float dt) {
    super.update(dt);

    if (Gdx.input.isKeyJustPressed(Inputs.SELL_RESTURANT)) {
      reputation = 0;
      endGame();
      ;
    }

    if (currentWaitingCustomer != null) {
      currentWaitingCustomer.showIcons();
      currentWaitingCustomer.checkClicks();
      currentWaitingCustomer.updateSpriteFromInput();
    }

    updateCustomerMovements(sittingCustomers);
    updateCustomerMovements(walkingBackCustomers);

    frustrationCheck(dt);

    //removeCustomerTest();
    seeIfCustomersShouldLeave(dt);
    canAcceptNewCustomer();

    changeFrustrationTimer();


  }

  /**
   * Gets the next avalible Table.
   *
   * @return next table. NULL if no free
   * @author Felix Seanor
   */
  public Table getTable() {
    for (Table option : tables) {
      if (option.isFree()) {
        return option;
      }
    }

    return null;
  }


  /**
   * Change frustration timer.
   */
  public void changeFrustrationTimer() {

    float TT = 0;
    float Max = customerFrustrationStart;

    if (currentWaitingCustomer != null) {
      TT = currentWaitingCustomer.frustration;
      Max *= currentWaitingCustomer.members.size();
    }
    ((BlackTexture) frustrationTimer.image).setSize(
        (int) ((TT / Max) * timerWidth), timerHeight);


  }

  /**
   * Change frustration of the currently waiting customer group
   *
   * @param dt delta time
   * @author Felix Seanor
   */
  private void frustrationCheck(float dt) {
    if (currentWaitingCustomer == null) {
      return;
    }
    currentWaitingCustomer.checkFrustration(dt, frustrationCallBack, updateFrustration);
  }

  /**
   * See if a currently seated customer should leave to make space for a new customer to enter.
   *
   * @param dt the dt
   * @author Felix Seanor
   */
  public void seeIfCustomersShouldLeave(float dt) {
    if (sittingCustomers.size() > 0) {
      timeToNextCustomerLeaving -= dt;
    }

    if (timeToNextCustomerLeaving <= 0) {
      removeCurrentlySeatedCustomers();
    }

    tryDeleteCustomers();

  }

  /**
   * Removes the first currently seated customer and makes them walk outside and despawn.
   *
   * @author Felix Seanor
   */
  void removeCurrentlySeatedCustomers() {
    CustomerGroups groups = sittingCustomers.get(0);
    groups.table.relinquish();
    walkingBackCustomers.add(groups);
    sittingCustomers.remove(0);

    for (Customer customer : groups.members
    ) {
      customer.eaten = true;
    }

    setCustomerGroupTarget(groups, doorTarget);

    timeToNextCustomerLeaving = eatingTime;

  }

  /**
   * Trys to remove customers when they reach the exit.
   *
   * @author Felix Seanor
   */
  void tryDeleteCustomers() {
    List<Integer> removals = new LinkedList<>();
    int r = 0;
    for (CustomerGroups group : walkingBackCustomers
    ) {

      for (int i = group.members.size() - 1; i >= 0; i--) {

        if (group.members.get(i).gameObject.position.dst(doorTarget.x, doorTarget.y) < 1) {
          group.members.get(i).destroy();
          group.members.remove(i);
        }

      }

      if (group.members.size() == 0) {
        removals.add(r);
      }

      r++;

    }
    for (Integer i : removals) {
      walkingBackCustomers.remove(i);
    }
  }

  /**
   * Waves left int.
   *
   * @return the int
   */
  int wavesLeft() {
    return waves - currentWave;
  }

  /**
   * Creates a new customer group of a random size, and gives them a list of foods to order.
   *
   * @return the int
   * @author Felix Seanor
   * @author Jack Vickers
   */


  public int calculateCustomerAmount() {

    if (waves == -1) {
      return rand.nextInt((int) groupSize.y - (int) groupSize.x) + (int) groupSize.x;
    }
    if (wavesLeft() == 0) {
      return customersRemaining;
    }

    // gets the number of customer for the current wave from the list of customers per wave
    return customersPerWave.get(currentWave - 1);

  }

  /**
   * Gets customers remaining.
   *
   * @return the customers remaining
   */
  public int getCustomersRemaining() {
    return customersRemaining;
  }


  /**
   * Lets a new customer group walk through the door
   *
   * @author Felix Seanor
   * @author Jack Vickers
   */
  void createNewCustomer() {
    Table table = getTable();
    int customerAmount = calculateCustomerAmount();

    if(numCustomersServed >12 && waves == -1){
      customerAmount = 4;
    }

    if(numCustomersServed % 10 == 0)
    {

      menu.restock();
    }

    customersRemaining -= customerAmount;

    currentWaitingCustomer = new CustomerGroups(customerAmount, currentCustomer, doorTarget,
        customerFrustrationStart, menu.createNewOrder(customerAmount, Randomisation.Normalised),
        customerAtlas);
    currentCustomer += customerAmount;

    currentWaitingCustomer.table = table;
    table.designateSeating(customerAmount, rand);

    setWaitingForOrderTarget();


  }

  /**
   * Makes the currently waiting customers to queue up dynamically.
   *
   * @author Felix Seanor
   */
  void setWaitingForOrderTarget() {
    for (int i = 0; i < currentWaitingCustomer.membersInLine.size(); i++) {
      setCustomerTarget(currentWaitingCustomer.membersInLine.get(i),
          new Vector2(0, 40 * i).add(orderAreaTarget));

      currentWaitingCustomer.membersInLine.get(i).eaten = false;
      currentWaitingCustomer.membersInLine.get(i).waitingAtCounter = true;
      currentWaitingCustomer.membersInLine.get(i).hideItem();
    }
  }

  /**
   * Checks if a new customer can be accepted if so, add a new one in. End the game if the set
   * number of waves has elapsed. Set Waves to -1 for "endless"
   *
   * @author Felix Seanor
   */
  public void canAcceptNewCustomer() {
    if (doSatisfactionCheck()) {
      sittingCustomers.add(currentWaitingCustomer);
      currentWaitingCustomer = null;
    }

    if (currentWaitingCustomer == null && sittingCustomers.size() < tables.size()) {
      if (waves != currentWave++) //if not the max number of waves increment
      {
        createNewCustomer();
      } else {
        endGame();
      }
    }
  }

  /**
   * If the current customer is too frustrated then make all customers in that group leave decrement
   * Frustration
   *
   * @param group group to leave
   * @author Felix Seanor
   */
  public void frustrationLeave(CustomerGroups group) {
    setCustomerGroupTarget(group, doorTarget);
    group.table.relinquish();
    currentWaitingCustomer = null;
    walkingBackCustomers.add(group);

    modifyReputation(-1);
  }

  /**
   * Sets the pathfinding target of an entire group, making them walk to the location.
   *
   * @param group  the group
   * @param target the target
   * @author Felix Seanor
   */
  public void setCustomerGroupTarget(CustomerGroups group, Vector2 target) {
    for (Customer customer : group.members
    ) {
      setCustomerTarget(customer, target);
    }
  }

  /**
   * Sets an individual customers pathfinding target. Begins pathfinding
   *
   * @param customer the customer
   * @param target   the target
   * @author Felix Seanor
   */
  public void setCustomerTarget(Customer customer, Vector2 target) {
    customer.givePath(pathfinding.findPath((int) customer.gameObject.position.x,
        (int) customer.gameObject.position.y, (int) target.x, (int) target.y,
        DistanceTest.Manhatten));
    customer.foodRecipe.isVisible = false;
    customer.recipeCloseButton.isVisible = false;

  }

  /**
   * Test remove customers.
   *
   * @author Felix Seanor
   */
  void removeCustomerTest() {
    if (Gdx.input.isKeyJustPressed(
            Keys.S) &&currentWaitingCustomer != null) {
      Customer customer = currentWaitingCustomer.removeFirstCustomer();
      numCustomersServed += 1;
      setCustomerTarget(customer, currentWaitingCustomer.table.GetNextSeat());
      changeMoney(moneyPerCustomer);
      setWaitingForOrderTarget();
    }
  }

  /**
   * Super food upgrade.
   */
  public void superFoodUpgrade() {
    Customer customer = currentWaitingCustomer.removeFirstCustomer();
    numCustomersServed += 1;
    setCustomerTarget(customer, currentWaitingCustomer.table.GetNextSeat());
    changeMoney(moneyPerCustomer);
    setWaitingForOrderTarget();
  }

  /**
   * Remove any customer.
   *
   * @param customerToRemove the customer to remove
   */
  void removeAnyCustomer(Integer customerToRemove) {
    Customer customer = currentWaitingCustomer.removeAnyCustomer(customerToRemove);
    if (customer != null) {
      numCustomersServed += 1;
      setCustomerTarget(customer, currentWaitingCustomer.table.GetNextSeat());
      changeMoney(moneyPerCustomer);
      setWaitingForOrderTarget();
    }
  }


  /**
   * End the game sequence. Call upper end game sequence.
   *
   * @author Felix Seanor
   */
  private void endGame() {
    //calculate win or loss

    //Calculate points
    EndOfGameValues values = new EndOfGameValues();
    values.score = money;
    values.won = reputation > 0;
    callEndGame.accept(values);
  }

  /**
   * Tetris super food action boolean.
   *
   * @param dish the dish
   * @return the boolean
   */
  public boolean tetrisSuperFoodAction(Item dish) {
    Boolean anyCustomer = false;
    LinkedList<OrderType> orderTypes = menu.getAllOrderTypes();
    List<ItemEnum> toClear = null;
    OrderType orderType = menu.getOrderTypeFromSuper(dish);
    toClear = orderType.orderables;
    for (int i = currentWaitingCustomer.membersInLine.size() - 1; i >= 0; i--) {
      Customer current = currentWaitingCustomer.membersInLine.get(i);
      if (toClear.contains(current.getDish())) {
        setCustomerTarget(currentWaitingCustomer.membersInLine.get(i),
            currentWaitingCustomer.table.GetNextSeat());
        //currentWaiting.MembersSeatedOrWalking.add(currentWaiting.MembersInLine.get(i));
        currentWaitingCustomer.feedSpecificCustomer(i);
        numCustomersServed += 1;
        anyCustomer = true;
      }
    }
    return anyCustomer;

  }

  /**
   * Interface with the customer from the chefs via customer counters. Checks to see if the given
   * food is an ordered food Makes customer sit down if so
   *
   * @param item the item
   * @return True if accepted, otherwise false
   * @author Felix Seanor
   */
  public boolean tryGiveFood(Item item) {
    if (item.name().contains("Super")) {
      return tetrisSuperFoodAction(item);
    }
    int success = currentWaitingCustomer.seeIfDishIsCorrect(item);

    if (success != -1) {
      currentWaitingCustomer.feedSpecificCustomer(success);
      setCustomerTarget(currentWaitingCustomer.membersSeatedOrWalking.get(
              currentWaitingCustomer.membersSeatedOrWalking.size() - 1),
          currentWaitingCustomer.table.GetNextSeat());
      numCustomersServed += 1;
      setWaitingForOrderTarget();
      changeMoney(moneyPerCustomer);
    }
    return success != -1;
  }

  /**
   * Gets member seated or walking.
   *
   * @return the member seated or walking
   */
  public List<Customer> getMemberSeatedOrWalking() {
    return currentWaitingCustomer.membersSeatedOrWalking;
  }

  /**
   * Checks to see if the current customer group can be expelled from the currenly waiting slot
   *
   * @return True if so, otherwise false
   * @author Felix Seanor
   */
  public boolean doSatisfactionCheck() {
    return currentWaitingCustomer != null && currentWaitingCustomer.membersInLine.size() == 0;
  }

  /**
   * Delete all customers.
   */
  public void deleteAllCustomers() {
    if (currentWaitingCustomer != null) {
      currentWaitingCustomer.destroy();

    }
    for (CustomerGroups group : sittingCustomers) {
      group.destroy();
    }

    for (CustomerGroups group : walkingBackCustomers) {
      group.destroy();
    }

    sittingCustomers.clear();
    walkingBackCustomers.clear();
  }

  /**
   * Loads the state of the game from a GameState object.
   *
   * @param state The state to load
   * @author Felix Seanor
   * @author Jack Vickers
   */
  public void loadState(GameState state) {
    //Wave State
    currentWave = state.wave;
    waves = state.maxWave;
    groupSize = state.groupSize;
    //Reputation
    reputation = state.reputation;
    maxReputation = state.maxReputation;
    customerFrustrationStart = state.maxFrustration;
    //Money
    maxMoney = state.maxMoney;
    money = state.money;

    customersPerWave = state.customersPerWave;
    if (!Objects.isNull(customersPerWave)) { // if this is not null, a scenario game is being loaded
      if (customersPerWave.size() > 0) { // this is true for the scenario mode
        customersRemaining = 0;
      }
      // correctly calculates the number of customers remaining (scenario mode)
      for (int i = currentWave; i < customersPerWave.size(); i++) {
        customersRemaining += customersPerWave.get(i);
      }
    }
    deleteAllCustomers();

    if (state.customerGroupStates[0] != null) {
      currentWaitingCustomer = new CustomerGroups(state.customerGroupStates[0], customerAtlas);
      Table table = tables.get(state.customerGroupStates[0].table);
      table.designateSeating(state.customerGroupStates[0].customerPositions.length, rand);
      currentWaitingCustomer.table = table;

      // if there are customers walking to the table
      if (state.customerGroupStates[0].numCustomersWalkingToTable > 0) {
        for (Customer cust : currentWaitingCustomer.members) {
          // If the customer is not at the order point and is not at the entrance (therefore is moving towards a table)
          if ((cust.getX() > 360.0f || cust.getX() < 360.0f) && (cust.getX() != 200
              && cust.getY() != 100)) {
            setCustomerTarget(cust,
                currentWaitingCustomer.table.GetNextSeat()); // set the customer to walk to the table
            currentWaitingCustomer.membersInLine.remove(cust); // remove the customer from the line
          } else {
            cust.eaten = false;
            cust.waitingAtCounter = true;
            cust.hideItem();
          }
        }
      }

      // set the target of the customers who should be in line to the order point
      setWaitingForOrderTarget();

    }

    int i = 0;
    for (CustomerGroupState groupState : state.customerGroupStates
    ) {
      if (i == 0) {
        i++;
        continue;
      }

      CustomerGroups customerGroups = new CustomerGroups(groupState, customerAtlas);

      Table table = tables.get(state.customerGroupStates[i].table);
      if (groupState.leaving) { // customers are leaving
        setCustomerGroupTarget(customerGroups, doorTarget);
        walkingBackCustomers.add(customerGroups);
      } else { // customers are/ should be sitting
        customerGroups.table = table;
        table.designateSeating(state.customerGroupStates[i].customerPositions.length, rand);

        // ensures that a table target is set for these customers
        for (Customer cust : customerGroups.members) {
          setCustomerTarget(cust, table.GetNextSeat());
        }
        sittingCustomers.add(customerGroups);
      }

      i++;
    }


  }

  /**
   * Save state.
   *
   * @param state the state
   */
  public void saveState(GameState state) {
    state.wave = currentWave;

    // List of number of customers per wave
    state.customersPerWave = customersPerWave;

    state.maxWave = waves;
    state.groupSize = groupSize;
    //Reputation
    state.reputation = reputation;
    state.maxReputation = maxReputation;
    state.maxFrustration = customerFrustrationStart;
    //Money
    state.maxMoney = maxMoney;
    state.money = money;

    //Customers

    List<CustomerGroupState> savedGroups = new LinkedList<>();
    if (currentWaitingCustomer == null) {
      savedGroups.add(null);
    } else {
      savedGroups.add(currentWaitingCustomer.saveState(false)); // customer groups that are waiting

    }
    for (CustomerGroups group : sittingCustomers) { // customer groups that are sitting
      savedGroups.add(group.saveState(false));
    }

    for (CustomerGroups group : walkingBackCustomers) { // customer groups that are leaving
      if (group.members.size() > 0) {
        savedGroups.add(group.saveState(true));
      }
    }
    state.customerGroupStates = savedGroups.toArray(new CustomerGroupState[0]);
  }

  /**
   * Leaving customer count int.
   *
   * @return the int
   */
  public int leavingCustomerCount() {
    return walkingBackCustomers.size();
  }

  /**
   * Sets money.
   *
   * @param i the
   */
  public void setMoney(int i) {
    money = i;
  }
}
