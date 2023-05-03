package com.mygdx.game.Core;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Core.Customers.CustomerController;
import com.mygdx.game.Core.GameState.DifficultyState;
import com.mygdx.game.Core.PathFinder.Pathfinding;
import com.mygdx.game.Core.Rendering.GameObject;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;
import com.mygdx.game.Stations.AssemblyStation;
import com.mygdx.game.Stations.ChopStation;
import com.mygdx.game.Stations.CustomerCounters;
import com.mygdx.game.Stations.FoodCrate;
import com.mygdx.game.Stations.HobStation;
import com.mygdx.game.Stations.OvenStation;
import com.mygdx.game.Stations.ToasterStation;
import com.mygdx.game.Stations.TrashCan;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Contains procedures for constructing machines and physics bodies Contains code from both
 * BlackCatStudios and Team Triple 10
 *
 * @author Jack Hinton
 * @author Amy Cross
 * @date 01/05/23
 */
public class ConstructMachines {

  public List<GameObject> stations = new LinkedList();
  public List<GameObject> customerCounters = new LinkedList();
  public List<GameObject> assemblyStations = new LinkedList();
  DifficultyState difficultyState;
  CustomerController customerController;
  int numHobs, numChopping, numOven;
  Pathfinding pathfinding;

  public ConstructMachines(CustomerController customerController, DifficultyState state,
      Pathfinding pathfinding) {
    difficultyState = state;
    this.customerController = customerController;
    this.pathfinding = pathfinding;
    numHobs = 0;
    numChopping = 0;
  }

  /**
   * Create a bin given a rectangle
   *
   * @param rect
   * @author Jack Hinton
   */
  public void CreateBin(Rectangle rect) {
    GameObject bin = new GameObject(null);
    bin.setPosition(rect.getX(), rect.getY());
    bin.setWidthAndHeight(rect.getWidth(), rect.getHeight());
    TrashCan TC = new TrashCan();
    bin.attachScript(TC);
    stations.add(bin);
  }

  /**
   * Create a hob
   *
   * @param rect
   * @author Jack Hinton
   */
  public void CreateHobs(Rectangle rect) {
    numHobs++;
    GameObject hob = new GameObject(null);
    hob.setPosition(rect.getX(), rect.getY());
    hob.setWidthAndHeight(rect.getWidth(), rect.getHeight());
    HobStation HS = new HobStation(difficultyState.cookingParams);
    hob.attachScript(HS);
    stations.add(hob);
    HS.init();
    if (numHobs > 1) {
      HS.setLocked(true);
    }
  }

  /**
   * Create a toaster
   *
   * @param rect
   * @author Jack Hinton
   */
  public void CreateToaster(Rectangle rect) {
    GameObject toast = new GameObject(null);
    toast.setPosition(rect.getX(), rect.getY());
    toast.setWidthAndHeight(rect.getWidth(), rect.getHeight());
    ToasterStation TS = new ToasterStation(difficultyState.cookingParams);
    toast.attachScript(TS);
    stations.add(toast);
    TS.init();
  }

  /**
   * Create chopping station
   *
   * @param rect
   * @author Jack Hinton
   */
  public void CreateChopping(Rectangle rect) {
    numChopping++;
    GameObject chop = new GameObject(null);
    chop.setPosition(rect.getX(), rect.getY());
    chop.setWidthAndHeight(rect.getWidth(), rect.getHeight());
    ChopStation CS = new ChopStation(difficultyState.cookingParams);
    chop.attachScript(CS);
    stations.add(chop);
    CS.init();
    if (numChopping > 1) {
      CS.setLocked(true);
    }
  }

  /**
   * Create an oven
   *
   * @param rect
   * @author Jack Hinton
   */
  public void CreateOven(Rectangle rect, CustomerController customerController) {
    Consumer<Boolean> updateMenu = (Boolean a) -> customerController.updateMenu(a);
    GameObject oven = new GameObject(null);
    oven.setPosition(rect.getX(), rect.getY());
    oven.setWidthAndHeight(rect.getWidth(), rect.getHeight());
    OvenStation OS = new OvenStation(difficultyState.cookingParams, updateMenu);
    oven.attachScript(OS);
    stations.add(oven);
    OS.init();
    if (numOven++ >= 1) {
      OS.setLocked(true);
    }
  }

  /**
   * Create a food create with an item inside
   *
   * @param rect
   * @param item
   * @author Jack Hinton
   */
  public void CreateFoodCrates(Rectangle rect, ItemEnum item) {
    GameObject crate = new GameObject(null);
    crate.setPosition(rect.getX(), rect.getY());
    crate.setWidthAndHeight(rect.getWidth(), rect.getHeight());
    FoodCrate foodCrate = new FoodCrate(item);
    crate.attachScript(foodCrate);
    stations.add(crate);
  }

  /**
   * Create an assembly station
   *
   * @param rect
   * @author Jack Hinton
   */
  public void CreateAssembly(Rectangle rect) {
    GameObject assembly = new GameObject(null);
    assembly.setPosition(rect.getX(), rect.getY());
    assembly.setWidthAndHeight(rect.getWidth(), rect.getHeight());
    AssemblyStation assemblyStation = new AssemblyStation(difficultyState.cookingParams);
    assembly.attachScript(assemblyStation);
    assemblyStations.add(assembly);
    stations.add(assembly);
  }

  /**
   * Create a customer counter
   *
   * @param rect
   * @author Jack Hinton
   */
  public void CreateCustomerCounters(Rectangle rect) {
    GameObject customer = new GameObject(null);
    customer.setPosition(rect.getX(), rect.getY());
    customer.setWidthAndHeight(rect.getWidth(), rect.getHeight());
    CustomerCounters CC = new CustomerCounters((Item a) -> (customerController.tryGiveFood(a)),
        difficultyState.cookingParams);
    customer.attachScript(CC);
    customerCounters.add(customer);
    stations.add(customer);
  }


  /**
   * This function puts the object onto the pathfinding grid
   *
   * @param x      the starting x of the world
   * @param y      the starting y of the world
   * @param width  the width of the world
   * @param height the height of the world
   * @param type   the type of the world
   * @param name   the name of the world Team Triple 10s code mainly
   * @author Amy Cross
   * @author Felix Seanor
   */
  public void buildObject(float x, float y, float width, float height,
      String type, String name) {
    if (type == "Static") {
      //BlackCatStudios Extension
      pathfinding.addStaticObject((int) x, (int) y, (int) width, (int) height);
    }
  }


}
