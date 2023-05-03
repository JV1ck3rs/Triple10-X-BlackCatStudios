package com.mygdx.game.Core.Chef;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Core.GameState.ChefParams;
import com.mygdx.game.Core.GameState.CookingParams;
import com.mygdx.game.Core.GameState.GameState;
import com.mygdx.game.Core.GameState.ItemState;
import com.mygdx.game.Core.Inputs;
import com.mygdx.game.Core.Interactions.Interactable;
import com.mygdx.game.Core.Interactions.Interaction;
import com.mygdx.game.Core.Interactions.Interaction.InteractionType;
import com.mygdx.game.Core.PathFinder.DistanceTest;
import com.mygdx.game.Core.PathFinder.Pathfinding;
import com.mygdx.game.Core.Rendering.BlackSprite;
import com.mygdx.game.Core.Rendering.BlackTexture;
import com.mygdx.game.Core.Rendering.GameObject;
import com.mygdx.game.Core.Scriptable;
import com.mygdx.game.Items.Item;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * This class manages the chefs and their inputs. Letting the player control them. This class is the
 * players main access point into the game outside of UI elements. BlackCatStudio's Code and a few
 * functions from team triple 10s
 *
 * @author Felix Seanor
 * @author Jack Vickers
 * @author Jack Hinton
 * @author Sam Toner
 * @date 01/05/23
 */
public class MasterChef extends Scriptable {

  public static final int MaxChefs = 5;
  public float maxRange = 18;
  public int currentControlledChef = 0;
  private static ArrayList<TextureAtlas> chefAtlasArray;
  private Camera camera;
  List<Chef> chefs;

  ChefParams chefParams;
  CookingParams cookingParams;
  private final Pathfinding pathfinder;

  private GameObject SelectionArrow;

  public int returnChefCount() {
    return chefs.size();
  }

  public void upgradeSpeed() {
    for (int i = 0; i < chefs.size(); i++) {
      chefs.get(i).changeSpeed();
    }
  }

  public void downgradeSpeed() {
    for (int i = 0; i < chefs.size(); i++) {
      chefs.get(i).decreaseSpeed();
    }
  }


  public Chef getChef(int i) {
    return chefs.get(i);
  }

  public Chef getCurrentChef() {

    return chefs.get(currentControlledChef);
  }

  /**
   * Generates a chef array which can be used to get random chef sprites from the chef class. Team
   * Triple 10s code
   *
   * @author Amy Cross
   */
  public void generateChefArray() {
    String filename;
    TextureAtlas chefAtlas;
    for (int i = 1; i < 6; i++) {
      filename = "Chefs/Chef" + i + "/chef" + i + ".txt";
      chefAtlas = new TextureAtlas(Gdx.files.internal(filename));
      chefAtlasArray.add(chefAtlas);
    }
  }

  /**
   * Returns the chef array that's been created
   *
   * @return ArrayList<TextureAtlas> chefAtlasArray;
   * @author Felix Seanor
   */
  public static ArrayList<TextureAtlas> getChefAtlasArray() {
    return chefAtlasArray;
  }

  /**
   * Creates a Chef controller class, handling inputs
   *
   * @param count
   * @param camera
   * @param pathfinding pathfinding module
   * @author Felix Seanor
   */
  public MasterChef(int count, Camera camera, Pathfinding pathfinding, ChefParams params,
      CookingParams cookParams) {

    chefParams = params;
    cookingParams = cookParams;
    chefs = new LinkedList<>();
    chefAtlasArray = new ArrayList<TextureAtlas>();
    this.pathfinder = pathfinding;
    generateChefArray();

    this.camera = camera;

    BlackTexture ArrowTex = new BlackTexture("Chefs/SelectionArrow.png");
    ArrowTex.setSize(20, 30);
    SelectionArrow = new GameObject(ArrowTex);

    for (int i = 0; i < count; i++) {
      Vector2 pos = new Vector2(0, 0);
      pos.x = 576 + 32 * i;
      pos.y = 232;
      CreateNewChef(pos, i);
    }

  }

  /**
   * Create a new chef given a position and iD
   *
   * @param position
   * @param i
   * @author Felix Seanor
   */
  void CreateNewChef(Vector2 position, int i) {
    GameObject chefsGameObject = new GameObject(
        new BlackSprite());//passing in null since chef will define it later
    chefs.add(new Chef(i, chefAtlasArray.get(i)));
    chefsGameObject.attachScript(chefs.get(i));
    chefsGameObject.image.setSize(18, 40);
    chefsGameObject.position.set(position);
    chefs.get(chefs.size() - 1).speed = chefParams.moveSpeed;
    chefs.get(i).updateSpriteFromInput("idlesouth");
  }


  /**
   * Select a chef
   *
   * @param i
   * @author Felix Seanor
   */
  void SelectChef(int i) {
    currentControlledChef = i;

  }

  void MoveArrow() {
    SelectionArrow.position.set(getCurrentChef().gameObject.position).add(new Vector2(0, 45));
  }

  /**
   * The chef tries to put down  an item onto a nearby surface
   *
   * @author Felix Seanor, Jack Vickers, Jack Hinton
   */
  public void GiveItem() {

    if (!chefs.get(currentControlledChef).CanFetchItem()) {
      return;
    }

    Vector2 chefPos = new Vector2(chefs.get(currentControlledChef).gameObject.position);

    chefPos.add(chefs.get(currentControlledChef).gameObject.getWidth() / 2f, 0);

    Scriptable script = Interaction.findClosetInteractable(
        chefPos, InteractionType.Give, maxRange);

    if (script == null) {
      return;
    }

    Optional<Item> itemToGive = chefs.get(currentControlledChef).FetchItem();

    if (!itemToGive.isPresent()) {
      return;
    }

    if (((Interactable) script).giveItem(itemToGive.get())) {
      chefs.get(currentControlledChef).popItem();
    }
  }

  /**
   * The chef tries to pick up an item from a nearby surface.
   *
   * @author Felix Seanor
   */
  public void FetchItem() {

    if (!chefs.get(currentControlledChef).CanGiveItem()) {
      return;
    }

    Vector2 chefPos = new Vector2(chefs.get(currentControlledChef).gameObject.position);

    chefPos.add(chefs.get(currentControlledChef).gameObject.getWidth() / 2f, 0);

    Scriptable script = Interaction.findClosetInteractable(
        chefPos, InteractionType.Fetch, maxRange);

    if (script == null) {
      return;
    }

    Item itemToGive = ((Interactable) script).retrieveItem();

    if (itemToGive == null) {
      return;
    }

    chefs.get(currentControlledChef).GiveItem(itemToGive);


  }

  void CycleItemStack() {
    getCurrentChef().CycleStack();
  }

  /**
   * The chef attempts to interact with a nearby surface
   *
   * @author Jack Hinton
   * @author Jack Vickers
   */
  public void Interact() {
    Vector2 chefPos = new Vector2(chefs.get(currentControlledChef).gameObject.position);

    chefPos.add(chefs.get(currentControlledChef).gameObject.getWidth() / 2f, 0);

    Scriptable script = Interaction.findClosetInteractable(
        chefPos, InteractionType.Interact, maxRange);

    if (script == null) {
      return;
    }

    float lockTime = ((Interactable) script).interact();
    if (lockTime > 0) {
      chefs.get(currentControlledChef).freeze(lockTime);
    }
  }

  /**
   * Select a chef from the number keys
   *
   * @author Felix Seanor
   */
  void selectChef() {
    for (int i = 0; i < chefs.size(); i++) {
      if (Gdx.input.isKeyPressed(Input.Keys.NUM_1
          + i)) // increments to next number for each chef 1,2,3 ect (dont go above 9) {
      {
        if (!chefs.get(i).isFrozen) {
          SelectChef(i);
        }
      }
      for (Chef c : chefs
      ) {
        // c.stop();
      }
    }
  }

  void checkFrozen(float dt) {
    for (Chef chef : chefs) {
      if (chef.isFrozen) {
        boolean ready = chef.freezeTimer(dt * cookingParams.chopSpeed);
        if (ready) {
          chef.unfreeze();
        }
      }
    }
  }

  boolean KeyPressedNow(int key) {
    return Gdx.input.isKeyJustPressed(key);
  }

  /**
   * Update method for this class
   *
   * @param dt
   * @author Felix Seanor
   * @author Jack Hinton
   * @author Jack Vickers
   */
  @Override
  public void update(float dt) {
    checkFrozen(dt);
    selectChef();
    if (chefs.get(currentControlledChef).isFrozen) {
      return;
    }
    chefs.get(currentControlledChef).updateSpriteFromInput("");
    if (KeyPressedNow(Inputs.CYCLE_STACK)) {
      CycleItemStack();
    }
    if (Gdx.input.isKeyJustPressed(Inputs.GIVE_ITEM)) {
      GiveItem();
    }
    if (Gdx.input.isKeyJustPressed(Inputs.FETCH_ITEM)) {
      FetchItem();
    }

    if (Gdx.input.isKeyJustPressed(Inputs.INTERACT)) {
      Interact();
    }

    if (Gdx.input.isKeyJustPressed((Inputs.DROP_ITEM))) {
      chefs.get(currentControlledChef).DropItem();
    }

    if (Gdx.input.isKeyJustPressed(Inputs.SPAWN_NEW_CHEF)) {
      AddNewChefIn();
    }

    if (Gdx.input.isButtonJustPressed(0)) {
      Vector3 touchpos = new Vector3();
      touchpos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
      touchpos = camera.unproject(touchpos);
      if (touchpos.y < 520
          && touchpos.x < 940) { // if the ui at the top of the screen is not clicked
        List<Vector2> path = pathfinder.findPath((int) getCurrentChef().gameObject.position.x,
            (int) getCurrentChef().gameObject.position.y, (int) touchpos.x, (int) touchpos.y,
            DistanceTest.Euclidean);
        getCurrentChef().givePath(path);
      }
    }
    MoveArrow();
  }


  /**
   * Adds in a new chef up to max
   *
   * @author Felix Seanor
   */
  public boolean AddNewChefIn() {
    if (chefs.size() < MaxChefs) {
      if (chefs.size() == 4) { // ensures the chefs are spaced out
        CreateNewChef(new Vector2(550, 232), chefs.size());
        return true;
      }
      CreateNewChef(new Vector2(576, 232), chefs.size());
      return true;
    } else {
      return false;
    }
  }

  public List<Chef> getChefList() {
    return chefs;
  }

  public void LoadState(GameState state) {
    for (int i = 0; i < state.chefPositions.length; i++) {
      if (i < chefs.size()) {
        chefs.get(i).gameObject.position = state.chefPositions[i];
      } else {
        CreateNewChef(state.chefPositions[i], i);
      }
    }

    for (Chef chef : chefs
    ) {
      for (int i = 0; i < Chef.CarryCapacity; i++) {
        chef.FetchItem();
      }
    }

    int i = 0;
    GiveBackFromState(state);
  }

  /**
   * Creates or modifies chefs from a save state.
   *
   * @param state
   * @author Felix Seanor
   */
  void GiveBackFromState(GameState state) {
    int i = 0;
    for (Chef chef : chefs
    ) {

      for (int j = 0; j < Chef.CarryCapacity; j++) {

        ItemState itemState = state.chefHoldingStacks[i * Chef.CarryCapacity + j];

        if (itemState == null || itemState.item == null) {
          continue;
        }
        Item item = new Item(itemState);
        chef.GiveItem(item);
      }

      i++;
    }
  }

  /**
   * Save the current state of the chefs into GameState
   *
   * @param state
   * @author Felix Seanor
   */
  public void SaveState(GameState state) {
    state.chefPositions = new Vector2[chefs.size()];
    state.chefHoldingStacks = new ItemState[chefs.size() * Chef.CarryCapacity];

    for (int i = 0; i < chefs.size(); i++) {
      state.chefPositions[i] = chefs.get(i).gameObject.position;

      for (int j = Chef.CarryCapacity - 1; j >= 0; j--) {
        Optional<Item> item = chefs.get(i).FetchItem();

        if (!item.isPresent()) {
          state.chefHoldingStacks[i * Chef.CarryCapacity + j] = null;
        } else {
          state.chefHoldingStacks[i * Chef.CarryCapacity + j] = new ItemState(item.get());
        }


      }
    }

    GiveBackFromState(state);//This exists to make quick saves look nicer

  }


}
