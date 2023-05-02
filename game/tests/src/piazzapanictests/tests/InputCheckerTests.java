package piazzapanictests.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.mygdx.game.InputChecker;
import org.junit.Test;

/**
 * Tests for the InputChecker class. This checks the user input on the scenario mode config screen
 * and the leaderboard screen.
 * @satisfies FR_SCENARIO_SET_CUSTOMERS UR_SCENARIO_SET_CUSTOMER UR_LEADERBOARD NFR_LEADERBOARD
 * @author Jack Vickers
 * @date 28/04/2023
 */
public class InputCheckerTests {

  /**
   * Tests that an empty string doesnt work
   * @author Jack Vickers
   * @satisfies FR_SCENARIO_SET_CUSTOMERS UR_SCENARIO_SET_CUSTOMER
   */
  @Test
  public void testCustomerNumberEmpty() {
    assertFalse(InputChecker.checkCustomerNumberInput(""));
  }

  /**
   * cant use a negative number
   * @author Jack Vickers
   * @satisfies FR_SCENARIO_SET_CUSTOMERS UR_SCENARIO_SET_CUSTOMER
   */
  @Test
  public void testCustomerNumberNegative() {
    assertFalse(InputChecker.checkCustomerNumberInput("-1"));
  }

  /**
   * Tests that it cant be too high
   * @author Jack Vickers
   * @satisfies FR_SCENARIO_SET_CUSTOMERS UR_SCENARIO_SET_CUSTOMER
   */
  @Test
  public void testCustomerNumberTooLarge() {
    assertFalse(InputChecker.checkCustomerNumberInput("101"));
  }
  /**
   * Tests that it cant be too long
   * @author Jack Vickers
   * @satisfies FR_SCENARIO_SET_CUSTOMERS UR_SCENARIO_SET_CUSTOMER
   */
  @Test
  public void testCustomerNumberTooLong() {
    assertFalse(InputChecker.checkCustomerNumberInput("1000"));
  }
  /**
   * Tests that it must be number
   * @author Jack Vickers
   * @satisfies FR_SCENARIO_SET_CUSTOMERS UR_SCENARIO_SET_CUSTOMER
   */
  @Test
  public void testCustomerNumberNotNumber() {
    assertFalse(InputChecker.checkCustomerNumberInput("abc"));
  }
  /**
   * Tests that a valid number works
   * @author Jack Vickers
   * @satisfies FR_SCENARIO_SET_CUSTOMERS UR_SCENARIO_SET_CUSTOMER
   */
  @Test
  public void testCustomerNumberValid() {
    assertTrue(InputChecker.checkCustomerNumberInput("50"));
  }
  /**
   * Tests that low number works
   * @author Jack Vickers
   * @satisfies FR_SCENARIO_SET_CUSTOMERS UR_SCENARIO_SET_CUSTOMER
   */
  @Test
  public void testCustomerNumberMinimum() {
    assertTrue(InputChecker.checkCustomerNumberInput("1"));
  }

  /**
   * Tests that the max number works
   * @author Jack Vickers
   * @satisfies FR_SCENARIO_SET_CUSTOMERS UR_SCENARIO_SET_CUSTOMER
   */
  @Test
  public void testCustomerNumberMaximum() {
    assertTrue(InputChecker.checkCustomerNumberInput("100"));
  }

  /**
   * Tests that 0 doesnt work
   * @author Jack Vickers
   * @satisfies FR_SCENARIO_SET_CUSTOMERS UR_SCENARIO_SET_CUSTOMER
   */
  @Test
  public void testCustomerNumberZero() {
    assertFalse(InputChecker.checkCustomerNumberInput("0"));
  }

  /**
   * Tests that a leaderboard name is correct
   * @author Jack Vickers
   * @satisfies UR_LEADERBOARD NFR_LEADERBOARD
   */
  @Test
  public void testLeaderboardNameEmpty() {
    assertFalse(InputChecker.checkLeaderboardName(""));
  }
  /**
   * Tests that a leaderboard name cant be too long
   * @author Jack Vickers
   * @satisfies UR_LEADERBOARD NFR_LEADERBOARD
   */
  @Test
  public void testLeaderboardNameTooLong() {
    assertFalse(InputChecker.checkLeaderboardName("abcdef"));
  }

  /**
   * Tests that a leaderboard name is alpha
   * @author Jack Vickers
   * @satisfies UR_LEADERBOARD NFR_LEADERBOARD
   */
  @Test
  public void testLeaderboardNameNotLetters() {
    assertFalse(InputChecker.checkLeaderboardName("12345"));
  }
  /**
   * Tests that a valid name works
   * @author Jack Vickers
   * @satisfies UR_LEADERBOARD NFR_LEADERBOARD
   */
  @Test
  public void testLeaderboardNameValid() {
    assertTrue(InputChecker.checkLeaderboardName("abcde"));
  }
  /**
   * Tests that a single character works
   * @author Jack Vickers
   * @satisfies UR_LEADERBOARD NFR_LEADERBOARD
   */
  @Test
  public void testLeaderboardNameMinimum() {
    assertTrue(InputChecker.checkLeaderboardName("a"));
  }
  /**
   * Tests that symbols dont work
   * @author Jack Vickers
   * @satisfies UR_LEADERBOARD NFR_LEADERBOARD
   */
  @Test
  public void testLeaderboardNameSymbols() {
    assertFalse(InputChecker.checkLeaderboardName("a!@#$"));
  }
  /**
   * Tests that a leaderboard name cannot include spaces
   * @author Jack Vickers
   * @satisfies UR_LEADERBOARD NFR_LEADERBOARD
   */
  @Test
  public void testLeaderboardNameSpaces() {
    assertFalse(InputChecker.checkLeaderboardName("a b c"));
  }

}
