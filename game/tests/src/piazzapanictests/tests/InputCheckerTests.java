package piazzapanictests.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.mygdx.game.InputChecker;
import org.junit.Test;

/**
 * Tests for the InputChecker class. This checks the user input on the scenario mode config screen
 * and the leaderboard screen.
 *
 * @author Jack Vickers
 * @date 28/04/2023
 */
public class InputCheckerTests {

  @Test
  public void testCustomerNumberEmpty() {
    assertFalse(InputChecker.checkCustomerNumberInput(""));
  }

  @Test
  public void testCustomerNumberNegative() {
    assertFalse(InputChecker.checkCustomerNumberInput("-1"));
  }

  @Test
  public void testCustomerNumberTooLarge() {
    assertFalse(InputChecker.checkCustomerNumberInput("101"));
  }

  @Test
  public void testCustomerNumberTooLong() {
    assertFalse(InputChecker.checkCustomerNumberInput("1000"));
  }

  @Test
  public void testCustomerNumberNotNumber() {
    assertFalse(InputChecker.checkCustomerNumberInput("abc"));
  }

  @Test
  public void testCustomerNumberValid() {
    assertTrue(InputChecker.checkCustomerNumberInput("50"));
  }

  @Test
  public void testCustomerNumberMinimum() {
    assertTrue(InputChecker.checkCustomerNumberInput("1"));
  }

  @Test
  public void testCustomerNumberMaximum() {
    assertTrue(InputChecker.checkCustomerNumberInput("100"));
  }

  @Test
  public void testCustomerNumberZero() {
    assertFalse(InputChecker.checkCustomerNumberInput("0"));
  }

  @Test
  public void testLeaderboardNameEmpty() {
    assertFalse(InputChecker.checkLeaderboardName(""));
  }

  @Test
  public void testLeaderboardNameTooLong() {
    assertFalse(InputChecker.checkLeaderboardName("abcdef"));
  }

  @Test
  public void testLeaderboardNameNotLetters() {
    assertFalse(InputChecker.checkLeaderboardName("12345"));
  }

  @Test
  public void testLeaderboardNameValid() {
    assertTrue(InputChecker.checkLeaderboardName("abcde"));
  }

  @Test
  public void testLeaderboardNameMinimum() {
    assertTrue(InputChecker.checkLeaderboardName("a"));
  }

  @Test
  public void testLeaderboardNameSymbols() {
    assertFalse(InputChecker.checkLeaderboardName("a!@#$"));
  }

  @Test
  public void testLeaderboardNameSpaces() {
    assertFalse(InputChecker.checkLeaderboardName("a b c"));
  }

}
