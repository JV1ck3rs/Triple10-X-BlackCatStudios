package com.mygdx.game;

public class LeaderboardData implements
    Comparable<LeaderboardData> {

  public int score;
  public String name;

  @Override
  public int compareTo(LeaderboardData o) {
    if(score > o.score)
      return 1;
    if(score < o.score)
      return -1;

    return name.compareTo(o.name);
  }
}
