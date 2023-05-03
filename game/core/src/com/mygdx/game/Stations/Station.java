package com.mygdx.game.Stations;

import com.mygdx.game.Core.Customers.CustomerController;
import com.mygdx.game.Core.GameState.CookingParams;
import com.mygdx.game.Core.GameState.ItemState;
import com.mygdx.game.Core.Interactions.Interactable;
import com.mygdx.game.Core.Rendering.BlackTexture;
import com.mygdx.game.Core.Rendering.GameObject;
import com.mygdx.game.Core.Scriptable;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;
import com.mygdx.game.RecipeAndComb.CombinationDict;
import com.mygdx.game.RecipeAndComb.Recipe;
import com.mygdx.game.RecipeAndComb.RecipeDict;
import java.util.LinkedList;
import java.util.List;


/**
 * Defines a station object with its given name and attributes we also have the ability to "lock" a
 * station so it can only interact with one item at a time
 *
 * @author Jack Hinton
 * @author Robin Graham
 * @date 01 /05/23
 */
public abstract class Station extends Scriptable implements Interactable {

  /**
   * The Item.
   */
  public Item item;
  /**
   * The Return item.
   */
  public Item returnItem;
  /**
   * The Recipes.
   */
  public RecipeDict recipes;
  /**
   * The Combinations.
   */
  public CombinationDict combinations;
  private boolean locked;
  /**
   * The Current recipe.
   */
  public Recipe currentRecipe;
  /**
   * The Held item.
   */
  GameObject heldItem;
  /**
   * The Image size.
   */
  public int imageSize = 18;
  /**
   * The Timer.
   */
  GameObject timer, /**
   * The Warning icon.
   */
  warningIcon, /**
   * The Repair bubble.
   */
  repairBubble, /**
   * The Ready bubble.
   */
  readyBubble;
  /**
   * The Animation.
   */
  GameObject animation;
  /**
   * The Station time decrease.
   */
  public float stationTimeDecrease;
  /**
   * The Price.
   */
  public int price = 100;
  private float burnSpeed;
  /**
   * The Cooking speed.
   */
  float cookingSpeed;
  /**
   * The constant numOvens.
   */
  public static int numOvens;


  /**
   * Creates a station
   *
   * @param params Cooking parameters i.e. cooking speed, burning speed etc.
   * @author Jack Hinton
   * @author Sam Toner
   * @author Felix Seanor
   */
  public Station(CookingParams params) {
    item = null;
    locked = false;
    recipes = RecipeDict.recipes;
    combinations = CombinationDict.combinations;
    currentRecipe = null;
    stationTimeDecrease = 0;
    burnSpeed = params.burnSpeed;
    cookingSpeed = params.cookSpeed;
  }


  /**
   * Initialises the bubbles
   *
   * @author Jack Hinton
   * @author Jack Vickers
   * @author Felix Seanor
   */
  public void init() {

    timer = new GameObject(new BlackTexture("Timer/01.png"));
    timer.setPosition(
        gameObject.position.x + (gameObject.getWidth() / 2) - (timer.getWidth() / 2),
        gameObject.position.y + (gameObject.getHeight()) + 2);
    timer.isVisible = false;
    timer.image.layer = 1;
    warningIcon = new GameObject(new BlackTexture("Timer/Warning.png"));
    warningIcon.setPosition(timer.position.x, timer.position.y + timer.getHeight());
    warningIcon.isVisible = false;
    warningIcon.image.layer = 1;

    repairBubble = new GameObject(new BlackTexture("Timer/RepairBubble.png"));
    repairBubble.setPosition(
        gameObject.position.x + gameObject.getWidth() / 2 - repairBubble.getWidth() / 2,
        gameObject.position.y + gameObject.getHeight() / 2 - repairBubble.getHeight() / 2);
    repairBubble.isVisible = false;
    repairBubble.image.layer = 1;

    readyBubble = new GameObject(new BlackTexture("Timer/Ready.png"));
    readyBubble.setPosition(timer.position.x, timer.position.y + timer.getHeight() / 2);
    readyBubble.isVisible = false;
    readyBubble.image.layer = 1;

    if (animation != null) {
      moveAnimation();
    }
  }


  /**
   * Gives the item to a station
   *
   * @param item The item you want to give
   * @return boolean
   * @author Jack Hinton
   */
  public abstract boolean giveItem(Item item);


  /**
   * Returns the item to the chef
   *
   * @return Item
   * @author Jack Hinton
   */
  public abstract Item retrieveItem();


  /**
   * Updates the pictures of the items currently in the station
   *
   * @author Jack Hinton
   */
  public abstract void updatePictures();


  /**
   * Moves the animation
   *
   * @author Jack Hinton
   */
  public abstract void moveAnimation();

  /**
   * Sets the station to a "locked" state
   *
   * @param locked assigns variable to either true or false
   * @author Jack Hinton
   */
  public void setLocked(boolean locked) {
    if (repairBubble != null) {
      repairBubble.isVisible = locked;
    }

    this.locked = locked;
  }

  /**
   * return the boolean value of locked for the station
   *
   * @return boolean locked
   * @author Jack Hinton
   */
  public boolean getLocked() {
    return locked;
  }


  /**
   * Checks if the given item is a repair tool, if yes it unlocks the station and charges the
   * player
   *
   * @param item the item the player has given
   * @return boolean boolean
   * @author Jack Hinton
   */
  public boolean checkRepairTool(Item item) {
    if (item.name == ItemEnum.RepairTool && CustomerController.money >= price) {
      setLocked(false);
      CustomerController.money = CustomerController.money - price;
      return true;
    }
    return false;
  }


  /**
   * Changes the current item to the new item
   *
   * @param item item you want to give to the station
   * @author Jack Hinton
   */
  public void changeItem(Item item) {
    this.item = item;
    updatePictures();
  }


  /**
   * Deletes the current held item
   *
   * @author Jack Hinton
   */
  public void deleteItem() {
    item = null;
    updatePictures();
    if (animation != null) {
      animation.isVisible = false;
    }
  }


  /**
   * Loads the state of the station from a save file
   *
   * @param state  The items the station has when saved
   * @param locked The locked state of the station when saved
   * @author Jack Hinton
   */
  public void loadState(List<ItemState> state, Boolean locked) {

    setLocked(locked);
    if (state.get(0) == null || state.get(0).item == null) {
      return;
    }

    item = new Item(state.get(0));
    updatePictures();
  }


  /**
   * Decreases cook time
   *
   * @author Sam Toner
   */
  public void decreaseCookTime() {
    stationTimeDecrease += 1;
  }


  /**
   * Saves the state of the station
   *
   * @return List<ItemState> list
   * @author Felix Seanor
   */
  public List<ItemState> saveState() {

    LinkedList<ItemState> states = new LinkedList<>();

    states.add(new ItemState(item));
    return states;
  }


}

