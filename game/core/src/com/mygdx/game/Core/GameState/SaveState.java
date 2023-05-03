package com.mygdx.game.Core.GameState;


import com.mygdx.game.Core.Chef.MasterChef;
import com.mygdx.game.Core.Customers.CustomerController;
import com.mygdx.game.Core.Rendering.GameObject;
import com.mygdx.game.Core.Scriptable;
import com.mygdx.game.Stations.Station;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Loads or saves states of the game to or from the disk BlackCatStudio's Code
 *
 * @author Felix Seanor
 * @author Jack Vickers
 * @date 25/04/23
 */
public class SaveState {


  /**
   * Save the state of the game without the need for a GameScreen.
   *
   * @param path               File path
   * @param masterChef         chef controller
   * @param customerController customer controller class
   * @param difficultyLevel    the games current difficulty level
   * @param timer              minute state of the timer
   * @param seconds            second state of the timer
   * @param Stations           List of station
   * @param customerCounters   List of customercounters
   * @param assemblyStations   List of assembly stations
   * @return The created game state
   * @author Jack Vickers
   * @author Felix Seanor
   */
  public GameState SaveState(String path, MasterChef masterChef,
      CustomerController customerController, Difficulty difficultyLevel, int timer, float seconds,
      List<GameObject> Stations, List<GameObject> customerCounters,
      List<GameObject> assemblyStations) {
    GameState state = new GameState();
    masterChef.SaveState(state);
    customerController.SaveState(state);
    saveGameScreen(state, difficultyLevel, timer, seconds, Stations, customerCounters,
        assemblyStations);

    SaveState(state, path);
    return state;
  }

  /**
   * Saves a game state to disk under the given path
   *
   * @param state GameState variables that the game has been saved under
   * @param path  File path
   * @author Felix Seanor
   * @author Jack Vickers
   */
  public void SaveState(GameState state, String path) {
    try {
      FileOutputStream fileOut = new FileOutputStream(path);
      ObjectOutputStream stream = new ObjectOutputStream(fileOut);
      stream.writeObject(state);
      stream.close();
      fileOut.close();
//    System.out.println("Game state printed to: " + path);

    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  /**
   * Saves the state of the game screen.
   *
   * @param state            The state to save to
   * @param difficultyLevel  The difficulty level of the game
   * @param timer            The timer of the game
   * @param seconds          The number of seconds the game has been running for (used for the
   *                         timer)
   * @param Stations         The stations in the game
   * @param customerCounters The customer counters in the game
   * @param assemblyStations The assembly stations in the game
   * @author Felix Seanor
   * @author Jack Vickers
   */
  public void saveGameScreen(GameState state, Difficulty difficultyLevel, int timer, float seconds,
      List<GameObject> Stations, List<GameObject> customerCounters,
      List<GameObject> assemblyStations) {
    List<List<ItemState>> itemsOnCounters = new LinkedList<>();
    List<Boolean> broken = new LinkedList<>();
    state.difficulty = difficultyLevel;
    state.timer = timer;
    state.seconds = seconds;
    for (GameObject station : Stations) {
      Scriptable scriptable = station.GetScript(0);

      if (scriptable instanceof Station) {
        itemsOnCounters.add(((Station) scriptable).SaveState());
        broken.add(((Station) scriptable).getLocked());
      }

    }

    for (GameObject station : customerCounters) {
      itemsOnCounters.add(((Station) station.GetScript(0)).SaveState());
    }

    for (GameObject station : assemblyStations) {
      itemsOnCounters.add(((Station) station.GetScript(0)).SaveState());
    }
    state.foodOnCounters = itemsOnCounters;
    state.repairState = broken;
  }

  /**
   * Load the current state into a GameState variable
   *
   * @param ID file path
   * @return GameState loaded in from disk
   */
  public GameState LoadState(String ID) {
    GameState state = null;

    FileInputStream fileIn = null;
    try {
      fileIn = new FileInputStream(ID);
      ObjectInputStream in = new ObjectInputStream(fileIn);
      state = (GameState) in.readObject();
      in.close();
      fileIn.close();


    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }

    return state;

  }
}
