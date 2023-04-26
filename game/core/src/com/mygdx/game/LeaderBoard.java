package com.mygdx.game;

import com.badlogic.gdx.Gdx;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LeaderBoard {

  static String filepath = Gdx.files.internal("leadboard.Fson").path();
  static int MaxHighscorers =5;
  public void LeaderBoard() {

  }

  public void createFSONFile() {
    File jsonFile = new File(filepath);
    try {
      if (jsonFile.createNewFile()) {
      } else {
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public LeaderboardData getDataFromRegexMatch(String string, Pattern pattern){
    Matcher matcher = pattern.matcher(string);
    boolean match = matcher.find();
    int score = Integer.parseInt(matcher.group(2));
    String name = matcher.group(1);

    LeaderboardData data = new LeaderboardData();
    data.name = name;
    data.score = score;

    return data;
  }

  public List<LeaderboardData> readFSONData() throws IOException {
    FileReader reader = new FileReader(filepath);
    String data = "";
    int j;
    while ((j = reader.read()) != -1) {
      data = data
          + (char) j; //Iterates through every character in the json file adding it to the string
    }
    reader.close();

//\<([a-z]+)?,([\d]+)\>

    Pattern pattern = Pattern.compile("<([a-zA-Z]+)?,([\\d]+)>",Pattern.CASE_INSENSITIVE);
    Pattern splitPattern = Pattern.compile(" ");
   String[] matches =  splitPattern.split(data);

    List<LeaderboardData> leaderboardDataList = new LinkedList<>();


    for (int i = 0; i < matches.length; i++) {
      if(matches[i] == "")
        continue;
      leaderboardDataList.add(getDataFromRegexMatch(matches[i],pattern));
    }
    return leaderboardDataList;



  }


  public void WriteHighscores(LeaderboardData data) {
    List<LeaderboardData> highscores = new LinkedList<>();
    try {
      highscores = AppendData(data);
    }
    catch (IOException e) {

    }

    String writing = "";

    for (LeaderboardData ld: highscores
    ) {
      writing += String.format("<%s,%d> ", ld.name,ld.score);
    }

    createFSONFile();

    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));
      writer.write(writing);
      writer.close();

    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  public List<LeaderboardData> AppendData(LeaderboardData data) throws IOException{
    List<LeaderboardData> highscores = readFSONData();

    highscores.add(data);

    highscores.sort(Comparator.naturalOrder());

    if(highscores.size()<=MaxHighscorers)
      return highscores;

    return highscores.subList(0,MaxHighscorers);
  }


}
