package com.mygdx.game.Stations;

import com.mygdx.game.Core.BlackTexture;
import com.mygdx.game.Core.CustomerController;
import com.mygdx.game.Core.GameObject;
import com.mygdx.game.Core.GameState.CookingParams;
import com.mygdx.game.Core.GameState.ItemState;
import com.mygdx.game.Core.Interactions.Interactable;
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
 * @date 01/05/23
 */
public abstract class Station extends Scriptable implements Interactable {

  public Item item;
  public Item returnItem;
  public RecipeDict recipes;
  public CombinationDict combinations;
  private boolean locked;
  public Recipe currentRecipe;
  GameObject heldItem;
  public int imageSize = 18;
  GameObject bubble, bubble2, bubble3, bubble4;
  GameObject animation;
  public float stationTimeDecrease;
  public int price = 100;
  private float BurnSpeed;
  float CookingSpeed;
  public static int numOvens;


  /**
   * Creates a station
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
    BurnSpeed = params.BurnSpeed;
    CookingSpeed = params.CookSpeed;
  }


  /**
   * Initialises the bubbles
   * @author Jack Hinton
   * @author Jack Vickers
   * @author Felix Seanor
   */
  public void init() {

    bubble = new GameObject(new BlackTexture("Timer/01.png"));
    bubble.setPosition(
        gameObject.position.x + (gameObject.getWidth() / 2) - (bubble.getWidth() / 2),
        gameObject.position.y + (gameObject.getHeight()) + 2);
    bubble.isVisible = false;
    bubble.image.layer=1;
    bubble2 = new GameObject(new BlackTexture("Timer/Warning.png"));
    bubble2.setPosition(bubble.position.x, bubble.position.y + bubble.getHeight());
    bubble2.isVisible = false;
    bubble2.image.layer=1;

    bubble3 = new GameObject(new BlackTexture("Timer/RepairBubble.png"));
    bubble3.setPosition(gameObject.position.x + gameObject.getWidth()/2 - bubble3.getWidth()/2,
            gameObject.position.y + gameObject.getHeight()/2 - bubble3.getHeight()/2);
    bubble3.isVisible = false;
    bubble3.image.layer=1;

    bubble4 = new GameObject(new BlackTexture("Timer/Ready.png"));
    bubble4.setPosition(bubble.position.x, bubble.position.y + bubble.getHeight()/2);
    bubble4.isVisible = false;
    bubble4.image.layer=1;

    if(animation != null)
      moveAnim();
  }


  /**
   * Gives the item to a station
   *
   * @param item The item you want to give
   * @return boolean
   * @author Jack Hinton
   */
  public abstract boolean GiveItem(Item item);


  /**
   * Returns the item to the chef
   *
   * @return Item
   * @author Jack Hinton
   */
  public abstract Item RetrieveItem();


  /**
   * Updates the pictures of the items currently in the station
   * @author Jack Hinton
   */
  public abstract void updatePictures();


  /**
   * Moves the animation
   * @author Jack Hinton
   */
  public abstract void moveAnim();

  /**
   * Sets the station to a "locked" state
   *
   * @param locked assigns variable to either true or false
   * @author Jack Hinton
   */
  public void setLocked(boolean locked) {
    if(bubble3 != null)
      bubble3.isVisible = locked;

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
   * Checks if the given item is a repair tool, if yes it unlocks the station and charges the player
   * @param item the item the player has given
   * @return boolean
   * @Author Jack Hinton
   */
  public boolean checkRepairTool(Item item) {
    if(item.name == ItemEnum.RepairTool && CustomerController.Money >= price) {
      setLocked(false);
      CustomerController.Money = CustomerController.Money - price;
      return true;
    }
    return false;
  }


  /**
   * Changes the current item to the new item
   * @param item item you want to give to the station
   * @Author Jack Hinton
   */
  public void changeItem(Item item) {
    this.item = item;
    updatePictures();
  }


  /**
   * Deletes the current held item
   * @Author Jack Hinton
   */
  public void deleteItem() {
    item = null;
    updatePictures();
    if(animation!=null) {
      animation.isVisible = false;
    }
  }


  /**
   * Loads the state of the station from a save file
   * @param state The items the station has when saved
   * @param locked The locked state of the station when saved
   * @Author Jack Hinton
   */
  public void LoadState(List<ItemState> state, Boolean locked) {

    setLocked(locked);
    if (state.get(0) == null || state.get(0).item == null) {
      return;
    }

    item = new Item(state.get(0));
    updatePictures();
  }


  /**
   * Decreases cook time
   * @Author Sam Toner
   */
  public void decreaseCookTime(){ stationTimeDecrease += 1;}


  /**
   * Saves the state of the station
   * @return List<ItemState>
   * @Author Felix Seanor
   */
  public List<ItemState> SaveState() {

    LinkedList<ItemState> states = new LinkedList<>();

    states.add(new ItemState(item));
    return states;
  }


}

