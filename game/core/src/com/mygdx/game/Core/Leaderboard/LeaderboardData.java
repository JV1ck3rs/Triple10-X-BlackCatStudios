package com.mygdx.game.Core.Leaderboard;

/**
 * This stores leaderboard data BlackCatStudio's code
 *
 * @author Felix Seanor
 * @author Jack Vickers
 * @date 28 /04/23
 */
public class LeaderboardData implements
    Comparable<LeaderboardData> {

  /**
   * The Score.
   */
  public int score;
  /**
   * The Name.
   */
  public String name;

  /**
   * Instantiates a new Leaderboard data.
   */
  public LeaderboardData() {
    score = 0;
    name = "";
  }

  /**
   * Instantiates a new Leaderboard data.
   *
   * @param score the score
   * @param name  the name
   */
  public LeaderboardData(int score, String name) {
    this.score = score;
    this.name = name;
  }

  @Override
  public int compareTo(LeaderboardData o) {
    if (score > o.score) {
      return 1;
    }
    if (score < o.score) {
      return -1;
    }

    return name.compareTo(o.name);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof LeaderboardData) {
      LeaderboardData data = (LeaderboardData) obj;
      return data.score == score && data.name.equals(name);
    }
    return false;
  }

  /**
   * Returns a string representation of the leaderboard data.
   *
   * @return a string representation of the leaderboard data.
   */
  @Override
  public String toString() {
    return score + "     " + name + " ".repeat(5 - name.length());
  }
}
