package piazzapanictests.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.mygdx.game.Core.Leaderboard.LeaderBoard;
import com.mygdx.game.Core.Leaderboard.LeaderboardData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Class containing all the leaderboard tests. Testing inserting a name ect
 */
@RunWith(GdxTestRunner.class)
public class LeaderBoardTests {

  /**
   * Tests that a name and score can be saved to and retrieved from the leaderboard.
   *
   * @satisfies UR_LEADERBOARD NFR_LEADERBOARD
   *
   * @author Jack Vickers
   * @date 28/04/2023
   */
  @Test
  public void testSaveDataToLeaderBoard() {
    File leaderboardFile = new File("leadboard.Fson");
    leaderboardFile.delete();
    LeaderboardData data = new LeaderboardData();
    data.score = 100;
    data.name = "TestName";
    LeaderBoard leaderBoard = new LeaderBoard();
    leaderBoard.createFSONFile();
    leaderBoard.writeHighscores(data);
    List<LeaderboardData> data2 = leaderBoard.readFSONData();
    assertEquals("The data in the leaderboard should be the same as the data written to it", data,
        data2.get(0));
    leaderboardFile.delete();
  }

  /**
   * Tests that 5 pairs of names and scores can be saved to the leaderbaord. This is the limit of
   * the leaderboard.
   * @satisifies  UR_LEADERBOARD NFR_LEADERBOARD
   * @author Jack Vickers
   * @date 28/04/2023
   */
  @Test
  public void testSave5RecordsToLeaderBoard() {
    File leaderboardFile = new File("leadboard.Fson");
    leaderboardFile.delete();
    LeaderboardData data = new LeaderboardData(100, "Test");
    LeaderBoard leaderBoard = new LeaderBoard();
    leaderBoard.createFSONFile();
    for (int i = 0; i < 5; i++) {
      leaderBoard.writeHighscores(data);
    }
    List<LeaderboardData> leaderBoardRecords = leaderBoard.readFSONData();
    for (int i = 0; i < 5; i++) {
      assertEquals("The data in the leaderboard should be the same as the data written to it", data,
          leaderBoardRecords.get(i));
    }
    leaderboardFile.delete();
  }

  /**
   * Test that scores are correctly updated when a new score is added to the leaderboard when it is
   * full.
   * @satisifies  UR_LEADERBOARD NFR_LEADERBOARD
   * @author Jack Vickers
   * @date 28/04/2023
   */
  @Test
  public void testSaveWhenLeaderBoardFull() {
    File leaderboardFile = new File("leadboard.Fson");
    leaderboardFile.delete();

    List<LeaderboardData> scoresList = new ArrayList<>();
    LeaderBoard leaderBoard = new LeaderBoard();
    leaderBoard.createFSONFile();

    // Write 5 scores to the leaderboard
    LeaderboardData score1 = new LeaderboardData();
    score1.score = 10;
    score1.name = "Jack";
    scoresList.add(score1);
    leaderBoard.writeHighscores(score1);
    LeaderboardData score2 = new LeaderboardData();
    score2.score = 8;
    score2.name = "Jim";
    leaderBoard.writeHighscores(score2);
    LeaderboardData score3 = new LeaderboardData();
    score3.score = 7;
    score3.name = "John";
    leaderBoard.writeHighscores(score3);
    LeaderboardData score4 = new LeaderboardData();
    score4.score = 6;
    score4.name = "James";
    leaderBoard.writeHighscores(score4);
    LeaderboardData score5 = new LeaderboardData();
    score5.score = 5;
    score5.name = "Jimbo";
    leaderBoard.writeHighscores(score5);

    // Write a score that is higher than the highest score in the leaderboard
    LeaderboardData score6 = new LeaderboardData();
    score6.score = 11;
    score6.name = "New";
    leaderBoard.writeHighscores(score6);

    // Check that the new score is in the leaderboard
    List<LeaderboardData> leaderBoardRecords = leaderBoard.readFSONData();

    assertEquals("The 1st score in the list should be score6", score6,
        leaderBoardRecords.get(0));

    assertEquals("The 2nd score in the list should be score1", score1,
        leaderBoardRecords.get(1));

    assertEquals("The 3rd score in the list should be score2", score2,
        leaderBoardRecords.get(2));

    assertEquals("The 4th score in the list should be score3", score3,
        leaderBoardRecords.get(3));

    assertEquals("The 5th score in the list should be score4", score4,
        leaderBoardRecords.get(4));

    LeaderboardData score7 = new LeaderboardData();
    score7.score = 9;
    score7.name = "NewP";
    leaderBoard.writeHighscores(score7);
    leaderBoardRecords = leaderBoard.readFSONData();

    assertEquals("The 1st score in the list should be score6", score6,
        leaderBoardRecords.get(0));
    assertEquals("The 2nd score in the list should be score1", score1,
        leaderBoardRecords.get(1));
    assertEquals("The 3rd score in the list should be score7", score7,
        leaderBoardRecords.get(2));
    assertEquals("The 4th score in the list should be score2", score2,
        leaderBoardRecords.get(3));
    assertEquals("The 5th score in the list should be score3", score3,
        leaderBoardRecords.get(4));

    LeaderboardData score8 = new LeaderboardData();
    score8.score = 1;
    leaderBoard.writeHighscores(score8);
    assertFalse("The leaderboard should not contain score8", leaderBoardRecords.contains(score8));
    leaderboardFile.delete();
  }

  /**
   * Asserts leaderboard data converts correctly to a string
   * @satisifies  UR_LEADERBOARD NFR_LEADERBOARD
   * @author Jack Vickers
   * @date 30/04/23
   */
  @Test
  public void testLeaderboardDataToString() {
    LeaderboardData data = new LeaderboardData(100, "Test");
    assertEquals("The string should be the same as the data", "100     Test ", data.toString());
    data.score = 10;
    data.name = "A";
    assertEquals("The string should be the same as the data", "10     A    ", data.toString());
  }

}
