package piazzapanictests.tests;

import static org.junit.Assert.assertEquals;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Core.CustomerController;
import com.mygdx.game.Core.GameObjectManager;
import com.mygdx.game.Core.Pathfinding;
import com.mygdx.game.Core.TextureDictionary;
import com.mygdx.game.Core.ValueStructures.CustomerControllerParams;
import com.mygdx.game.Core.ValueStructures.EndOfGameValues;
import com.mygdx.game.GameScreen;
import java.util.ArrayList;
import java.util.Collections;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class ScenarioModeTests {

  GameObjectManager manager;
  CustomerController cust;
  Pathfinding pathfinding;
  EndOfGameValues vals;
  CustomerControllerParams params = new CustomerControllerParams();

  void InstantiateCustomerScripts(CustomerControllerParams params) {
    GameObjectManager.objManager = null;
    TextureDictionary dico = new TextureDictionary();
    pathfinding = new Pathfinding(GameScreen.TILE_WIDTH / 4, GameScreen.viewportWidth,
        GameScreen.viewportWidth);
    manager = new GameObjectManager();

    cust = new CustomerController(new Vector2(0, 0), new Vector2(32, 0), pathfinding,
        (EndOfGameValues a) -> EndGame(a), params, new Vector2(190, 390), new Vector2(190, 290),
        new Vector2(290, 290));
  }

  /**
   * This test checks whether the customer controller is correctly setting up the waves of customers
   * for the scenario mode when the number of customers is more than 10.
   *
   * @author Jack Vickers
   */
  @Test
  public void testWaveSetupWithMoreThan10Customers() {
    params = new CustomerControllerParams();
    params.MoneyStart = 10;
    params.Reputation = 3;
    params.MaxMoney = 100;
    params.NoCustomers = 37; // This is the number of customers in the scenario
    params.MaxCustomersPerWave = 4;
    params.MinCustomersPerWave = 2;
    InstantiateCustomerScripts(params);
    ArrayList<Integer> customersPerScenarioWave = cust.getCustomersPerScenarioWave();
    int totalCustomers = 0;
    for (int i : customersPerScenarioWave) {
      totalCustomers += i;
    }
    assertEquals("There should be 37 customers across all the waves", 37, totalCustomers);
    assertEquals("There should be 11 waves of 1 customer (this has been precalculated)", 11,
        Collections.frequency(customersPerScenarioWave, 1));
    assertEquals("There should be 7 waves of 2 customers (this has been precalculated)", 7,
        Collections.frequency(customersPerScenarioWave, 2));
    assertEquals("There should be 4 waves of 3 customers (this has been precalculated)", 4,
        Collections.frequency(customersPerScenarioWave, 3));
  }

  /**
   * This test checks whether the customer controller is correctly setting up the waves of customers
   * for the scenario mode when the number of customers is 5.
   *
   * @author Jack Vickers
   */
  @Test
  public void testWaveSetupWithLessThan10Customers() {
    params = new CustomerControllerParams();
    params.MoneyStart = 10;
    params.Reputation = 3;
    params.MaxMoney = 100;
    params.NoCustomers = 5; // This is the number of customers in the scenario
    params.MaxCustomersPerWave = 4;
    params.MinCustomersPerWave = 2;
    InstantiateCustomerScripts(params);
    ArrayList<Integer> customersPerScenarioWave = cust.getCustomersPerScenarioWave();
    int totalCustomers = 0;
    for (int i : customersPerScenarioWave) {
      totalCustomers += i;
    }
    assertEquals("There should be 5 customers across all the waves", 5, totalCustomers);
    assertEquals("There should be 3 waves of 1 customer (this has been precalculated)", 3,
        Collections.frequency(customersPerScenarioWave, 1));
    assertEquals("There should be 1 waves of 2 customers (this has been precalculated)", 1,
        Collections.frequency(customersPerScenarioWave, 2));
  }

  /**
   * This test checks whether the customer controller is correctly setting up the waves of customers
   * for the scenario mode when the number of customers is 10.
   *
   * @author Jack Vickers
   */
  @Test
  public void testWaveSetupWith10Customers() {
    params = new CustomerControllerParams();
    params.MoneyStart = 10;
    params.Reputation = 3;
    params.MaxMoney = 100;
    params.NoCustomers = 10; // This is the number of customers in the scenario
    params.MaxCustomersPerWave = 4;
    params.MinCustomersPerWave = 2;
    InstantiateCustomerScripts(params);
    ArrayList<Integer> customersPerScenarioWave = cust.getCustomersPerScenarioWave();
    int totalCustomers = 0;
    for (int i : customersPerScenarioWave) {
      totalCustomers += i;
    }
    assertEquals("There should be 10 customers across all the waves", 10, totalCustomers);
    assertEquals("There should be 3 waves of 1 customer (this has been precalculated)", 3,
        Collections.frequency(customersPerScenarioWave, 1));
    assertEquals("There should be 2 waves of 2 customers (this has been precalculated)", 2,
        Collections.frequency(customersPerScenarioWave, 2));
    assertEquals("There should be 1 waves of 3 customers (this has been precalculated)", 1,
        Collections.frequency(customersPerScenarioWave, 3));
  }

  /**
   * This test checks whether the customer controller is correctly setting up the waves of customers
   * for the scenario mode when the number of customers is 6. This is a special case as it is the
   * smallest number of customers that will cause a wave of each size to be created.
   *
   * @author Jack Vickers
   */
  @Test
  public void testWaveSetupWith6Customers() {
    params = new CustomerControllerParams();
    params.MoneyStart = 10;
    params.Reputation = 3;
    params.MaxMoney = 100;
    params.NoCustomers = 6; // This is the number of customers in the scenario
    params.MaxCustomersPerWave = 4;
    params.MinCustomersPerWave = 2;
    InstantiateCustomerScripts(params);
    ArrayList<Integer> customersPerScenarioWave = cust.getCustomersPerScenarioWave();
    int totalCustomers = 0;
    for (int i : customersPerScenarioWave) {
      totalCustomers += i;
    }
    assertEquals("There should be 6 customers across all the waves", 6, totalCustomers);
    assertEquals("There should be 1 waves of 1 customer (this has been precalculated)", 1,
        Collections.frequency(customersPerScenarioWave, 1));
    assertEquals("There should be 1 waves of 2 customers (this has been precalculated)", 1,
        Collections.frequency(customersPerScenarioWave, 2));
    assertEquals("There should be 1 waves of 3 customers (this has been precalculated)", 1,
        Collections.frequency(customersPerScenarioWave, 3));
  }

  /**
   * This test checks whether the customer controller is correctly setting up the waves of customers
   * for the scenario mode when the number of customers is 100. This is the maximum
   * number of customers allowed for the scenario mode.
   *
   * @author Jack Vickers
   */
  @Test
  public void testWaveSetupWith100Customers() {
    params = new CustomerControllerParams();
    params.MoneyStart = 10;
    params.Reputation = 3;
    params.MaxMoney = 100;
    params.NoCustomers = 100; // This is the number of customers in the scenario
    params.MaxCustomersPerWave = 4;
    params.MinCustomersPerWave = 2;
    InstantiateCustomerScripts(params);
    ArrayList<Integer> customersPerScenarioWave = cust.getCustomersPerScenarioWave();
    int totalCustomers = 0;
    for (int i : customersPerScenarioWave) {
      totalCustomers += i;
    }
    assertEquals("There should be 100 customers across all the waves", 100, totalCustomers);
    assertEquals("There should be 30 waves of 1 customer (this has been precalculated)", 30,
        Collections.frequency(customersPerScenarioWave, 1));
    assertEquals("There should be 20 waves of 2 customers (this has been precalculated)", 20,
        Collections.frequency(customersPerScenarioWave, 2));
    assertEquals("There should be 10 waves of 3 customers (this has been precalculated)", 10,
        Collections.frequency(customersPerScenarioWave, 3));
  }

  void EndGame(EndOfGameValues val) {
    vals = val;
  }
}
