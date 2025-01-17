package com.mygdx.game.Stations;

import com.mygdx.game.Core.GameState.CookingParams;
import com.mygdx.game.Core.GameState.ItemState;
import com.mygdx.game.Core.Rendering.BlackTexture;
import com.mygdx.game.Core.Rendering.GameObject;
import com.mygdx.game.Core.SFX.SoundFrame;
import com.mygdx.game.Core.SFX.SoundFrame.soundsEnum;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Assembly station for assembling our ingredients into a final dish and storing items
 * BlackCatStudio's Code and
 *
 * @author Robin Graham
 * @author Jack Hinton
 * @date 01 /05/23
 */
public class AssemblyStation extends Station {

  private ArrayList<Item> ingredients;
  private ArrayList<ItemEnum> tempIngredients; // A temporary list to hold Item Enums to not disturb the Item list in case of failure to combine
  /**
   * The Held items.
   */
  public ArrayList<GameObject> heldItems = new ArrayList<>(); // Pictures of items currently on the station
  private ItemEnum temp;
  private boolean assembled;
  private Item dish;
  private Item tempDish;
  /**
   * The Ingredient size.
   */
  public int ingredientSize = 12;


  /**
   * Creates an assembly station
   *
   * @param params The parameters for cooking speed, burning speed etc.
   * @author Jack Hinton
   * @author Felix Seanor
   * @author Jack Vickers
   */
  public AssemblyStation(CookingParams params) {

    super(params);
    if (ingredients == null) {
      ingredients = new ArrayList<Item>();
    }

    tempIngredients = new ArrayList<ItemEnum>();
    assembled = false;
  }


  /**
   * Gives the assembly station an item, storing up to a maximum of 4 items
   *
   * @param item The item you want to give to the assembly station
   * @return boolean - If the method was successful giving an item
   * @author Jack Hinton
   * @author Jack Vickers
   */
  @Override
  public boolean giveItem(Item item) {
    if (canGive()) {
      if (assembled) {
//        ingredients.add(getDish());
        ingredients.add(item);
        assembled = false;
        updatePictures();
        return true;
      }
      ingredients.add(item);
      updatePictures();
      return true;
    }
    return false;
  }


  /**
   * Returns the last added item
   *
   * @return Item
   * @author Jack Hinton
   * @author Jack Vickers
   */
  @Override
  public Item retrieveItem() {
    if (assembled) {
      tempDish = ingredients.get(ingredients.size() - 1);
      assembled = false;
      heldItem.destroy();
      heldItem = null;
      heldItems.remove(heldItems.size() - 1);
      ingredients.remove(ingredients.size() - 1);
      return tempDish;
    }
    if (ingredients.isEmpty()) {
      return null;
    }
    int index = ingredients.size() - 1;
    if (heldItem != null) {
      heldItem.destroy();
    }
    heldItem = null;
    heldItems.get(index).destroy();
    heldItems.remove(index);
    return ingredients.remove(index);
  }


  /**
   * Checks if you can retrieve an item from the assembly station
   *
   * @return boolean
   * @author Jack Hinton
   * @author Felix Seanor
   */
  @Override
  public boolean canRetrieve() {
    return ingredients.size() > 0;
  }


  /**
   * Checks if you can give an item to the assembly station
   *
   * @return boolean
   * @author Jack Hinton
   */
  @Override
  public boolean canGive() {
    return ingredients.size() < 4;
  }

  /**
   * Checks if the user can interact with the assembly station
   *
   * @return boolean
   * @author Jack Hinton
   */
  @Override
  public boolean canInteract() {
    return !(ingredients.size() < 2);
  }


  /**
   * Interact with the assembly station to combine items
   *
   * @return float
   * @author Jack Hinton
   */
  @Override
  public float interact() {
    combine();
    return 0;
  }


  /**
   * Returns the list of ingredients in our arraylist form
   *
   * @return ArrayList ingredients
   * @author Jack Hinton
   * @author Jack Vickers
   */
  public ArrayList<Item> getIngredients() {
    return ingredients;
  }

  /**
   * Returns the gameobjects which are on the assembly station.
   *
   * @return ArrayList of gameobjects
   * @author Jack Vickers
   */
  public ArrayList<GameObject> getHeldItems() {
    return this.heldItems;
  }

  /**
   * Returns the gameobject.
   *
   * @return GameObject heldItem.
   * @author Jack Vickers
   */
  public GameObject getHeldItem() {
    return this.heldItem;
  }


  /**
   * Removes all ingredients from the arraylist
   *
   * @author Jack Hinton
   */
  public void clearIngredients() {
    ingredients = new ArrayList<Item>();
  }

  /**
   * Clears the temporary arraylist
   *
   * @author Jack Hinton
   */
  private void clearTempIngredients() {
    tempIngredients = new ArrayList<ItemEnum>();
  }


  /**
   * Assembles the dish into the final one when we have all the correct ingredients
   *
   * @return boolean boolean
   * @author Jack Hinton
   * @author Jack Vickers
   * @author Felix Seanor
   */
  public boolean combine() {
    for (Item ingredient : ingredients) {
      tempIngredients.add(ingredient.name);
    }
    Collections.sort(tempIngredients);
    for (int x = 0; x < tempIngredients.size() - 1; x++) {
      temp = combinations.combinationMap.get(
          tempIngredients.get(x).name() + " " + tempIngredients.get(x + 1).name());
      if (temp == null) {
        clearTempIngredients();
        return false;
      }
      tempIngredients.set(x + 1, temp);
      Collections.sort(tempIngredients);
    }
    SoundFrame.SoundEngine.playSound(soundsEnum.FoodReadyBell);
    setDish(tempIngredients.get(tempIngredients.size() - 1));
    clearIngredients();
    clearTempIngredients();
    assembled = true;
    updatePictures();
    ingredients.add(getDish());
    return assembled;
  }

  /**
   * Gets the current dish
   *
   * @return Item dish
   * @author Jack Hinton
   * @author Jack Vickers
   */
  public Item getDish() {
//    assembled = false;
    Item tempDish = dish;
    dish = null;
    return tempDish;
  }

  /**
   * Creates a new item and stores in dish using enum passed in the parameter
   *
   * @param item the enum passed in
   * @author Jack Hinton
   */
  public void setDish(ItemEnum item) {
    this.dish = new Item(item);
  }

  /**
   * Updates the pictures currently shown on the station
   *
   * @author Jack Hinton
   * @author Felix Seanor
   * @author Jack Vickers
   */
  @Override
  public void updatePictures() {

    if (ingredients.isEmpty()) {
      for (GameObject object : heldItems) {
        object.destroy();
      }
      heldItems = new ArrayList<>();

    }

    if (assembled) {
      heldItem = new GameObject(new BlackTexture(Item.getItemPath(dish.name)));
      heldItem.image.setSize(imageSize, imageSize);
      heldItem.setPosition(gameObject.position.x + (gameObject.getWidth() / 2) - 9,
          gameObject.position.y + gameObject.getHeight() - imageSize - 2);
      heldItems.add(heldItem);
      return;
    }

    if (ingredients.isEmpty()) {
      return;
    }

    int index = ingredients.size();

    heldItem = new GameObject(new BlackTexture(Item.getItemPath(ingredients.get(index - 1).name)));
    heldItem.image.setSize(ingredientSize, ingredientSize);
    if (index == 1) {
      heldItem.setPosition(gameObject.position.x + 2,
          gameObject.position.y + gameObject.getHeight() - ingredientSize - 2);
    } else if (index == 2) {
      heldItem.setPosition(gameObject.position.x + gameObject.getWidth() - ingredientSize - 2,
          gameObject.position.y + gameObject.getHeight() - ingredientSize - 2);
    } else if (index == 3) {
      heldItem.setPosition(gameObject.position.x + 2,
          gameObject.position.y + gameObject.getHeight() - (2 * ingredientSize) - 4);
    } else {
      heldItem.setPosition(gameObject.position.x + gameObject.getWidth() - ingredientSize - 2,
          gameObject.position.y + gameObject.getHeight() - (2 * ingredientSize) - 4);
    }
    heldItems.add(heldItem);

  }


  /**
   * Move the animation
   *
   * @author Jack Hinton
   */
  @Override
  public void moveAnimation() {
    return;
  }


  /**
   * Updates the assembly station
   *
   * @param dt delta time
   * @author Jack Hinton
   */
  @Override
  public void update(float dt) {
  }


  /**
   * Loads the items and lock state of the station from a save file
   *
   * @param state  items stored in save file
   * @param locked lock state
   * @author Felix Seanor
   */
  @Override
  public void loadState(List<ItemState> state, Boolean locked) {

    ingredients.clear();
    updatePictures();

    for (int i = 0; i < state.size(); i++) {
      if (state.get(i) == null) {
        continue;
      }
      ingredients.add(new Item(state.get(i)));
      updatePictures();
    }

  }

  /**
   * Saves the current state of the station
   *
   * @return List<ItemState>
   * @author Felix Seanor
   */
  @Override
  public List<ItemState> saveState() {
    LinkedList<ItemState> states = new LinkedList<>();

    for (Item item : ingredients
    ) {
      states.add(new ItemState(item));
    }

    return states;
  }
}
