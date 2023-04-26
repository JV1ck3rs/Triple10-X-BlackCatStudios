package com.mygdx.game;

public class LeaderboardData implements
    Comparable<LeaderboardData> {

  public int score;
  public String name;

  public LeaderboardData() {
    score = 0;
    name = "";
  }
  public LeaderboardData(int score, String name) {
    this.score = score;
    this.name = name;
  }

  @Override
  public int compareTo(LeaderboardData o) {
    if(score > o.score)
      return 1;
    if(score < o.score)
      return -1;

    return name.compareTo(o.name);
  }

  /**
   * Returns a string representation of the leaderboard data.
   *
   * @return a string representation of the leaderboard data.
   */
  @Override
  public String toString() {
    return name + " ".repeat(5 - name.length()) + "    " + score;
  }
}
