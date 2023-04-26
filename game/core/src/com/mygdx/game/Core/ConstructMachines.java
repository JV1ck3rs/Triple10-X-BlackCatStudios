package com.mygdx.game.Core;

import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Core.GameState.DifficultyState;
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

public class ConstructMachines
{
  public List<GameObject> Stations = new LinkedList();
  public List<GameObject> customerCounters = new LinkedList();
  public List<GameObject> assemblyStations = new LinkedList();
  DifficultyState difficultyState;

  CustomerController customerController;

  Pathfinding pathfinding;
  public ConstructMachines(CustomerController customerController, DifficultyState state, Pathfinding pathfinding){
    difficultyState = state;
    this.customerController = customerController;
    this.pathfinding = pathfinding;
  }

  /**
   * Create a bin given a rectangle
   * @param rect
   * @author Jack Vickers
   */
 public void CreateBin(Rectangle rect) {
    GameObject Bin = new GameObject(null);
    Bin.setPosition(rect.getX(), rect.getY());
    Bin.setWidthAndHeight(rect.getWidth(), rect.getHeight());
    TrashCan TC = new TrashCan();
    Bin.attachScript(TC);
    Stations.add(Bin);
  }

  /**
   * Create a hob
   * @param rect
   * @author Jack Vickers
   */
  public void CreateHobs(Rectangle rect) {
    GameObject Hob = new GameObject(null);
    Hob.setPosition(rect.getX(), rect.getY());
    Hob.setWidthAndHeight(rect.getWidth(), rect.getHeight());
    HobStation HS = new HobStation(difficultyState.cookingParams);
    Hob.attachScript(HS);
    Stations.add(Hob);
    HS.init();
  }

  /**
   * Create a toaster
   * @param rect
   * @author Jack Vickers
   */
  public void CreateToaster(Rectangle rect) {
    GameObject Toast = new GameObject(null);
    Toast.setPosition(rect.getX(), rect.getY());
    Toast.setWidthAndHeight(rect.getWidth(), rect.getHeight());
    ToasterStation TS = new ToasterStation(difficultyState.cookingParams);
    Toast.attachScript(TS);
    Stations.add(Toast);
    TS.init();
  }

  /**
   * Create chopping station
   * @param rect
   * @author Jack Vickers
   */
  public  void CreateChopping(Rectangle rect) {
    GameObject Chop = new GameObject(null);
    Chop.setPosition(rect.getX(), rect.getY());
    Chop.setWidthAndHeight(rect.getWidth(), rect.getHeight());
    ChopStation CS = new ChopStation(difficultyState.cookingParams);
    Chop.attachScript(CS);
    Stations.add(Chop);
    CS.init();
  }

  /**
   * Create an oven
   * @param rect
   * @author Jack Vickers
   */
  public void CreateOven(Rectangle rect) {
    GameObject Oven = new GameObject(null);
    Oven.setPosition(rect.getX(), rect.getY());
    Oven.setWidthAndHeight(rect.getWidth(), rect.getHeight());
    OvenStation OS = new OvenStation(difficultyState.cookingParams);
    Oven.attachScript(OS);
    Stations.add(Oven);
    OS.init();
  }

  /**
   * Create a food create with an item inside
   * @param rect
   * @param item
   * @author Jack Vickers
   */
  public void CreateFoodCrates(Rectangle rect, ItemEnum item) {
    GameObject Crate = new GameObject(null);
    Crate.setPosition(rect.getX(), rect.getY());
    Crate.setWidthAndHeight(rect.getWidth(), rect.getHeight());
    FoodCrate FC = new FoodCrate(item);
    Crate.attachScript(FC);
    Stations.add(Crate);
  }

  /**
   * Create an assembly station
   * @param rect
   * @author Jack Vickers
   */
  public void CreateAssembly(Rectangle rect) {
    GameObject Ass = new GameObject(null);
    Ass.setPosition(rect.getX(), rect.getY());
    Ass.setWidthAndHeight(rect.getWidth(), rect.getHeight());
    AssemblyStation AS = new AssemblyStation(difficultyState.cookingParams);
    Ass.attachScript(AS);
    assemblyStations.add(Ass);
    Stations.add(Ass);
  }

  /**
   * Create a customer counter
   * @param rect
   * @author Jack Vickers
   */
  public  void CreateCustomerCounters(Rectangle rect) {
    GameObject Cust = new GameObject(null);
    Cust.setPosition(rect.getX(), rect.getY());
    Cust.setWidthAndHeight(rect.getWidth(), rect.getHeight());
    CustomerCounters CC = new CustomerCounters((Item a) -> (customerController.tryGiveFood(a)),
        difficultyState.cookingParams);
    Cust.attachScript(CC);
    customerCounters.add(Cust);
    Stations.add(Cust);
  }


  /**
   * A function which builds the world box in Box2d which is used for all the hitboxes;
   *
   * @param world  the world it's being built in
   * @param x      the starting x of the world
   * @param y      the starting y of the world
   * @param width  the width of the world
   * @param height the height of the world
   * @param type   the type of the world
   * @param name   the name of the world
   *
   * @author Amy Cross
   * @author Felix Seanor
   */
  public void buildObject(World world, float x, float y, float width, float height,
      String type, String name) {
    BodyDef bdef = new BodyDef();
    bdef.position.set((x + width / 2), (y + height / 2));
    if (type == "Static") {
      bdef.type = BodyDef.BodyType.StaticBody;
      pathfinding.addStaticObject((int) x, (int) y, (int) width, (int) height);


    } else if (type == "Dynamic") {
      bdef.type = BodyDef.BodyType.DynamicBody;
    }
    Body body = world.createBody(bdef);
    body.getPosition();
    body.setUserData(name);
    PolygonShape shape = new PolygonShape();
    shape.setAsBox((width / 2), (height / 2));
    FixtureDef fdef = new FixtureDef();
    fdef.shape = shape;
    body.createFixture(fdef);
  }


}
