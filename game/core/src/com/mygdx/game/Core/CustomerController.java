package com.mygdx.game.Core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.Core.Customers.CustomerGroups;
import com.mygdx.game.Core.Customers.OrderMenu;
import com.mygdx.game.Core.Customers.Randomisation;
import com.mygdx.game.Core.Customers.Table;
import com.mygdx.game.Core.GameState.CustomerGroupState;
import com.mygdx.game.Core.GameState.GameState;
import com.mygdx.game.Core.ValueStructures.CustomerControllerParams;
import com.mygdx.game.Core.ValueStructures.EndOfGameValues;
import com.mygdx.game.Customer;
import com.mygdx.game.Items.Item;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import com.badlogic.gdx.math.Vector2;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;

/**
  This script controls the customers and handles their logic through a variety of secondary scripts.
  Also handles when the current game should end.

  BlackCatStudio's Code
  @author Felix Seanor
  @author Jack Vickers

  Last modified: 23/04/2023
 */

public class CustomerController extends Scriptable
{

  /**The current customer group waiting in line*/
  CustomerGroups currentWaiting = null;

  /**List of customers sitting down and eating. Groups only enter this when all members are eating*/
  List<CustomerGroups> SittingCustomers = new LinkedList<>();
  /**List of all customer groups trying to leave*/
  List<CustomerGroups> WalkingBackCustomers = new LinkedList<>();
  /**Pathfinding module used*/
  Pathfinding pathfinding;

  /**
   * List of tables
   */
  List<Table> tables;
  /** Call this to start endge game sequence */
  Consumer<EndOfGameValues> CallEndGame;

  public int Reputation;
  public int Money;
  int MaxMoney;
  int MaxReputation;
  int MoneyPerCustomer;

  int Waves = -1;
  OrderMenu menu;
  int currentCustomer = 0;
  int currentWave = 0;
  private float EatingTime = 7;
  private int TimerWidth = 50;
  private int TimerHeight = 10;

  /** Frustration Time*/
  private GameObject FrustrationTimer;
  private GameObject FrustrationTimerBackground;

  /** Creates a new randomisation class based on the current time */
  Random rand = new Random(System.currentTimeMillis());

  /** The minimum and maximum group size for customers groups */
  private Vector2 groupSize = new Vector2(1,4);

  /** timer defining when the next eating customer will leave */
  float NextToLeave = EatingTime;
  int MaxCustomers;
  int CustomersRemaining;
  ArrayList<Integer> customersPerWave;
/** this is call back for customer groups who get too frustrated so they need to leave */
  Consumer<CustomerGroups> FrustrationCallBack;

  /** Door position */
  Vector2 DoorTarget;
  /** where ordering queue should start */
  Vector2 OrderAreaTarget;

  /**
   * All customer texture atlases
   */
  private ArrayList<TextureAtlas> CustomerAtlas = new ArrayList<>();

  /** how long it takes for a group to be frustrated and leave without being served*/
  private int CustomerFrustrationStart = 80;

  boolean updateFrustration = true;
  int numCustomersServed = 0;

  /**
   * Creates the customer controller
   * @param DoorPosition Customer spawn and exit.
   * @param OrderArea First position in order line
   * @param path    Pathfinding Module.
   * @param CallUpGameFinish Game Finish Function.
   * @param params Parameter class
   * @param TablePositions Where the tables are, TEMPORARY
   * @author Felix Seanor
   */
  public CustomerController(Vector2 DoorPosition, Vector2 OrderArea, Pathfinding path,
      Consumer<EndOfGameValues> CallUpGameFinish, CustomerControllerParams params, Vector2... TablePositions){
    tables = new LinkedList<>();
    FrustrationCallBack = (CustomerGroups a)   -> FrustrationLeave(a);
    MoneyPerCustomer = params.MoneyPerCustomer;
    CallEndGame = CallUpGameFinish;
    Money = params.MoneyStart;
    MaxMoney = params.MaxMoney;
    Reputation = params.Reputation;
    MaxReputation = 5; // set the max reputation to 5
//    CustomerFrustrationStart = params.FrustrationStart;
    CustomerFrustrationStart = 1;
    groupSize.y = Math.min(params.MaxCustomersPerWave, groupSize.y);
    groupSize.x = Math.max(params.MinCustomersPerWave, groupSize.x);

    CalculateWavesFromNoCustomers(params.NoCustomers);

    BlackTexture Black = new BlackTexture("Black.png");
    BlackTexture FrustBack = new BlackTexture("FrustrationBackground.png");

    FrustrationTimerBackground = new GameObject(FrustBack);
    FrustrationTimerBackground.position.set(298, 8);
    FrustBack.layer = -1;
    FrustBack.setSize(TimerWidth + 4, TimerHeight + 4);

    FrustrationTimer = new GameObject(Black);

    FrustrationTimer.position.set(300, 10);

    if (Waves != -1) {
      Reputation = Math.min(Reputation, Waves);
    }
    generateCustomerArray();

    int ID = 0;
    for (Vector2 pos : TablePositions
    ) {
      tables.add(new Table(pos, ID++, 30));
    }

    pathfinding = path;
    OrderAreaTarget = OrderArea;
    DoorTarget = DoorPosition;

    menu = new OrderMenu(10, 7, 3, params.OrderTypePermissable);
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


  public void CalculateWavesFromNoCustomers(int NoCustomers) {
    MaxCustomers = NoCustomers;
    CustomersRemaining = NoCustomers;

    if (NoCustomers == -1) {
      SetWaveAmount(-1);
      return;
    }

    customersPerWave = new ArrayList<>();
    int tempVal = MaxCustomers;

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

    Waves = customersPerWave.size();
    SetWaveAmount(Waves);
  }

  public int SittingCustomerCount() {
    return SittingCustomers.size();
  }

  public OrderMenu getMenu() {
    return menu;
  }

  public ArrayList<Integer> getCustomersPerScenarioWave() {
    return customersPerWave;
  }

  /***
   * Set the maximum number of waves to do, exclusively. Resets currentWave to 0.
   * @param amount
   * @author Felix Seanor
   */
  public void SetWaveAmount(int amount) {
    Waves = amount;
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
      CustomerAtlas.add(customerAtlas);
    }
  }

  /**
   * Updates all customers in groups to update their animation.
   *
   * @param customers
   * @author Felix Seanor
   */
  public void UpdateCustomerMovements(List<CustomerGroups> customers) {
    for (int i = 0; i < customers.size(); i++) {
      customers.get(i).updateSpriteFromInput();
    }
  }

  public CustomerGroups getCurrentWaitingCustomerGroup() {
    return currentWaiting;
  }

  public int getNumberOfCustomersServed() {
    return numCustomersServed;
  }

  public int getRemainingNumberOfCustomers() {
    if (currentWaiting != null) {
      return CustomersRemaining + currentWaiting.MembersInLine.size();
    }
    return CustomersRemaining;
  }

  /**
   *  Modifies the reputation, if reputation + DR <= 0 END GAME.
   * @param DR delta reputation
   * @author Felix Seanor
   */
  public void ModifyReputation(int DR) {
    Reputation += DR;
    Reputation = Math.min(Reputation, MaxReputation);
    if (Reputation <= 0) {
      EndGame();
    }
  }

  /**
   * Do check and modify money.
   *
   * @param DM Delta Money
   * @return decrease in money is allowed.
   * @author Felix Seanor
   */
  public boolean ChangeMoney(float DM) {
    if (DM >= 0) {
      Money += DM;
      Money = Math.min(MaxMoney, Money);
      return true;
    }

    if (Money - DM >= 0) {
      Money += DM;
      return true;
    }

    return false;


  }


  @Override
  public void Update(float dt) {
    super.Update(dt);
    if (currentWaiting != null) {
      currentWaiting.showIcons();
      currentWaiting.checkClicks();
      currentWaiting.updateSpriteFromInput();
    }

    UpdateCustomerMovements(SittingCustomers);
    UpdateCustomerMovements(WalkingBackCustomers);

    FrustrationCheck(dt);

    RemoveCustomerTest();
    SeeIfCustomersShouldLeave(dt);
    CanAcceptNewCustomer();

    ChangeFrustrationTimer();


  }

  /**
   * Gets the next avalible Table.
   *
   * @return next table. NULL if no free
   * @author Felix Seanor
   */
  public Table GetTable() {
    for (Table option : tables) {
      if (option.isFree()) {
        return option;
      }
    }

    return null;
  }


  public void ChangeFrustrationTimer() {

    float TT  = 0;
    float Max = CustomerFrustrationStart;

    if (currentWaiting != null) {
      TT = currentWaiting.Frustration;
      Max *= currentWaiting.Members.size();
    }
    ((BlackTexture) FrustrationTimer.image).setSize(
        (int) (( TT / Max) * TimerWidth), TimerHeight);


  }
  /**
   * Change frustration of the currently waiting customer group
   *
   * @param dt delta time
   * @author Felix Seanor
   */
  private void FrustrationCheck(float dt) {
    if (currentWaiting == null) {
      return;
    }
    currentWaiting.CheckFrustration(dt, FrustrationCallBack);
  }

  /**
   * See if a currently seated customer should leave to make space for a new customer to enter.
   *
   * @param dt
   * @author Felix Seanor
   */
  public void SeeIfCustomersShouldLeave(float dt) {
    if (SittingCustomers.size() > 0) {
      NextToLeave -= dt;
    }

    if (NextToLeave <= 0) {
      RemoveCurrentlySeatedCustomers();
    }

    TryDeleteCustomers();

  }

  /**
   * Removes the first currently seated customer and makes them walk outside and despawn.
   * @author Felix Seanor
   */
  void RemoveCurrentlySeatedCustomers() {
    CustomerGroups groups = SittingCustomers.get(0);
    groups.table.relinquish();
    WalkingBackCustomers.add(groups);
    SittingCustomers.remove(0);

    for (Customer customer: groups.Members
    ) {
      customer.eaten = true;
    }



    SetCustomerGroupTarget(groups, DoorTarget);

    NextToLeave = EatingTime;

  }

  /**
   * Trys to remove customers when they reach the exit.
   * @author Felix Seanor
   */
  void TryDeleteCustomers() {
    List<Integer> removals = new LinkedList<>();
    int r = 0;
    for (CustomerGroups group : WalkingBackCustomers
    ) {

      for (int i = group.Members.size() - 1; i >= 0; i--) {

        if (group.Members.get(i).gameObject.position.dst(DoorTarget.x, DoorTarget.y) < 1) {
          group.Members.get(i).Destroy();
          group.Members.remove(i);
        }

      }

      if (group.Members.size() == 0) {
        removals.add(r);
      }

      r++;

    }
    for (Integer i : removals) {
      WalkingBackCustomers.remove(i);
    }
  }

  int WavesLeft() {
    return Waves - currentWave;
  }

  /**
   * Creates a new customer group of a random size, and gives them a list of foods to order.
   *
   * @author Felix Seanor
   * @author Jack Vickers
   */


  public int calculateCustomerAmount() {

    if (Waves == -1) {
      return rand.nextInt((int) groupSize.y - (int) groupSize.x) + (int) groupSize.x;
    }
    if (WavesLeft() == 0) {
      return CustomersRemaining;
    }

//    int rnd = rand.nextInt((int) groupSize.y - (int) groupSize.x) + (int) groupSize.x;
//
//    int minimumCustomerDraw = WavesLeft() * (int) groupSize.x;
//    int MaxDraw = (CustomersRemaining - rnd) - WavesLeft() * (int) groupSize.y;
//
//    minimumCustomerDraw = CustomersRemaining - minimumCustomerDraw;
//
//    return Math.min(minimumCustomerDraw, rnd) + Math.max(0, MaxDraw);

    // gets the number of customer for the current wave from the list of customers per wave
    return customersPerWave.get(currentWave - 1);

  }

  public int getCustomersRemaining() {
    return CustomersRemaining;
  }


  /**
   * Lets a new customer group wapk through the door
   * @author Felix Seanor
   * @author Jack Vickers
   */
  void CreateNewCustomer() {
    Table table = GetTable();

    int customerAmount = calculateCustomerAmount();
    CustomersRemaining -= customerAmount;

    currentWaiting = new CustomerGroups(customerAmount, currentCustomer, DoorTarget,
        CustomerFrustrationStart, menu.CreateNewOrder(customerAmount, Randomisation.Normalised),
        CustomerAtlas);
    currentCustomer += customerAmount;

    currentWaiting.table = table;
    table.DesignateSeating(customerAmount, rand);

    SetWaitingForOrderTarget();



  }

  /**
   * Makes the currently waiting customers to queue up dynamically.
   *
   * @author Felix Seanor
   */
  void SetWaitingForOrderTarget() {
    for (int i = 0; i < currentWaiting.MembersInLine.size(); i++) {
      SetCustomerTarget(currentWaiting.MembersInLine.get(i),
          new Vector2(0, 40 * i).add(OrderAreaTarget));
    }
  }

  /**
   * Checks if a new customer can be accepted if so, add a new one in. End the game if the set number of waves has elapsed.
   * Set Waves to -1 for "endless"
   * @author Felix Seanor
   */

  public void CanAcceptNewCustomer() {
    if (DoSatisfactionCheck()) {
      SittingCustomers.add(currentWaiting);
      currentWaiting = null;
    }

    if (currentWaiting == null && SittingCustomers.size() < tables.size()) {
      if (Waves != currentWave++) //if not the max number of waves increment
      {
        CreateNewCustomer();
      } else {
        EndGame();
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
  public void FrustrationLeave(CustomerGroups group) {
    SetCustomerGroupTarget(group, DoorTarget);
    group.table.relinquish();
    currentWaiting = null;
    WalkingBackCustomers.add(group);

    ModifyReputation(-1);
  }

  /**
   * Sets the pathfinding target of an entire group, making them walk to the location.
   *
   * @param group
   * @param target
   * @author Felix Seanor
   */
  public void SetCustomerGroupTarget(CustomerGroups group, Vector2 target) {
    for (Customer customer : group.Members
    ) {
      SetCustomerTarget(customer, target);
    }
  }

  /**
   * Sets an individual customers pathfinding target. Begins pathfinding
   *
   * @param customer
   * @param target
   * @author Felix Seanor
   */

  public void SetCustomerTarget(Customer customer, Vector2 target) {
    customer.GivePath(pathfinding.FindPath((int) customer.gameObject.position.x,
        (int) customer.gameObject.position.y, (int) target.x, (int) target.y,
        DistanceTest.Manhatten));

  }

  /**
   * Test remove customers.
   */
  void RemoveCustomerTest() {
    if (Gdx.input.isKeyJustPressed(
        Keys.S) && currentWaiting != null) {
      Customer customer = currentWaiting.RemoveFirstCustomer();
      numCustomersServed += 1;
      SetCustomerTarget(customer, currentWaiting.table.GetNextSeat());
      ChangeMoney(MoneyPerCustomer);
      SetWaitingForOrderTarget();
    }
  }

  void superFoodUpgrade() {
    RemoveCustomerTest();
  }


  /**
   * End the game sequence. Call upper end game sequence.
   *
   * @author Felix Seanor
   */
  private void EndGame() {
    //calculate win or loss

    //Calculate points
    EndOfGameValues values = new EndOfGameValues();
    values.score = Money;
    values.Won = Reputation > 0;
    CallEndGame.accept(values);
  }

  /**
   * Interface with the customer from the chefs via customer counters. Checks to see if the given
   * food is an ordered food Makes customer sit down if so
   *
   * @param item
   * @return True if accepted, otherwise false
   * @author Felix Seanor
   */
  public boolean tryGiveFood(Item item) {
    int success = currentWaiting.SeeIfDishIsCorrect(item);

    if (success != -1) {
      currentWaiting.MembersSeatedOrWalking.add(currentWaiting.MembersInLine.remove(success));
      currentWaiting.updateFrustrationOnSucessfulService();
      SetCustomerTarget(currentWaiting.MembersSeatedOrWalking.get(
          currentWaiting.MembersSeatedOrWalking.size() - 1), currentWaiting.table.GetNextSeat());
      SetWaitingForOrderTarget();
      ChangeMoney(MoneyPerCustomer);
    }
    return success != -1;
  }

  /**
   * Checks to see if the current customer group can be expelled from the currenly waiting slot
   *
   * @return True if so, otherwise false
   * @author Felix Seanor
   */
  public boolean DoSatisfactionCheck() {
    return currentWaiting != null && currentWaiting.MembersInLine.size() == 0;
  }

  public void deleteAllCustomers() {
    if (currentWaiting != null) {
      currentWaiting.destroy();

    }
    for (CustomerGroups group : SittingCustomers) {
      group.destroy();
    }

    for (CustomerGroups group : WalkingBackCustomers) {
      group.destroy();
    }

    SittingCustomers.clear();
    WalkingBackCustomers.clear();
  }

  /**
   * Loads the state of the game from a GameState object.
   *
   * @param state The state to load
   * @author Felix Seanor
   * @author Jack Vickers
   */
  public void LoadState(GameState state) {
    //Wave State
    currentWave = state.Wave;
    Waves = state.MaxWave;
    groupSize = state.GroupSize;
    //Reputation
    Reputation = state.Reputation;
    MaxReputation = state.MaxReputation;
    CustomerFrustrationStart = state.MaxFrustration;
    //Money
    MaxMoney = state.MaxMoney;
    Money = state.Money;

    customersPerWave = state.CustomersPerWave;
    if (!Objects.isNull(customersPerWave)) { // if this is not null, a scenario game is being loaded
      if (customersPerWave.size() > 0) { // this is true for the scenario mode
        CustomersRemaining = 0;
      }
      // correctly calculates the number of customers remaining (scenario mode)
      for (int i = currentWave; i < customersPerWave.size(); i++) {
        CustomersRemaining += customersPerWave.get(i);
      }
    }
    deleteAllCustomers();

    if (state.CustomerGroupsData[0] != null) {
      currentWaiting = new CustomerGroups(state.CustomerGroupsData[0], CustomerAtlas);
      Table table = tables.get(state.CustomerGroupsData[0].Table);
      table.DesignateSeating(state.CustomerGroupsData[0].customerPositions.length, rand);
      currentWaiting.table = table;

      // if there are customers walking to the table
      if (state.CustomerGroupsData[0].NumCustomersWalkingToTable > 0) {
        for (Customer cust : currentWaiting.Members) {
          // If the customer is not at the order point and is not at the entrance (therefore is moving towards a table)
          if ((cust.getX() > 360.0f || cust.getX() < 360.0f) && (cust.getX() != 200
              && cust.getY() != 100)) {
            SetCustomerTarget(cust,
                currentWaiting.table.GetNextSeat()); // set the customer to walk to the table
            currentWaiting.MembersInLine.remove(cust); // remove the customer from the line
          }
        }
      }

      // set the target of the customers who should be in line to the order point
      SetWaitingForOrderTarget();

    }

    int i = 0;
    for (CustomerGroupState groupState : state.CustomerGroupsData
    ) {
      if (i == 0) {
        i++;
        continue;
      }

      CustomerGroups customerGroups = new CustomerGroups(groupState, CustomerAtlas);
      Table table = tables.get(state.CustomerGroupsData[i].Table);
      if (groupState.leaving) { // customers are leaving
        SetCustomerGroupTarget(customerGroups, DoorTarget);
        WalkingBackCustomers.add(customerGroups);
      } else { // customers are/ should be sitting
        customerGroups.table = table;
        table.DesignateSeating(state.CustomerGroupsData[i].customerPositions.length, rand);

        // ensures that a table target is set for these customers
        for (Customer cust : customerGroups.Members) {
          SetCustomerTarget(cust, table.GetNextSeat());
        }
        SittingCustomers.add(customerGroups);
      }

      i++;
    }


  }

  public void SaveState(GameState state) {
    state.Wave = currentWave;

    // List of number of customers per wave
    state.CustomersPerWave = customersPerWave;

    state.MaxWave = Waves;
    state.GroupSize = groupSize;
    //Reputation
    state.Reputation = Reputation;
    state.MaxReputation = MaxReputation;
    state.MaxFrustration = CustomerFrustrationStart;
    //Money
    state.MaxMoney = MaxMoney;
    state.Money = Money;

    //Customers

    List<CustomerGroupState> savedGroups = new LinkedList<>();
    if (currentWaiting == null) {
      savedGroups.add(null);
    } else {
      savedGroups.add(currentWaiting.SaveState(false)); // customer groups that are waiting

    }
    for (CustomerGroups group : SittingCustomers) { // customer groups that are sitting
      savedGroups.add(group.SaveState(false));
    }

    for (CustomerGroups group : WalkingBackCustomers) { // customer groups that are leaving
      if (group.Members.size() > 0) {
        savedGroups.add(group.SaveState(true));
      }
    }
    state.CustomerGroupsData = savedGroups.toArray(new CustomerGroupState[0]);
  }

  public int LeavingCustomerCount() {
    return WalkingBackCustomers.size();
  }
}
