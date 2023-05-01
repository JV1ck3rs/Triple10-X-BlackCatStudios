package com.mygdx.game.Stations;

import com.mygdx.game.Core.BlackTexture;
import com.mygdx.game.Core.GameObject;
import com.mygdx.game.Core.GameState.CookingParams;
import com.mygdx.game.Core.GameState.ItemState;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;
import com.mygdx.game.soundFrame;
import com.mygdx.game.soundFrame.soundsEnum;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Assembly station for assembling our ingredients into a final dish
 * BlackCatStudio's Code and
 * @author Robin Graham
 * @author Jack Hinton
 */
public class AssemblyStation extends Station {

  private ArrayList<Item> ingredients;
  private ArrayList<ItemEnum> tempIngredients;
  public ArrayList<GameObject> heldItems = new ArrayList<>();
  private ItemEnum temp;
  private boolean assembled;
  private Item dish;
  private Item tempDish;
  public int ingredientSize = 12;


  public AssemblyStation(CookingParams params) {

    super(params);
    if (ingredients == null) {
      ingredients = new ArrayList<Item>();
    }

    tempIngredients = new ArrayList<ItemEnum>();
    assembled = false;
  }


  @Override
  public boolean GiveItem(Item item) {
    if (CanGive()) {
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


  @Override
  public Item RetrieveItem() {
    if (assembled) {
      tempDish = ingredients.get(ingredients.size() - 1);
      assembled = false;
      heldItem.Destroy();
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
      heldItem.Destroy();
    }
    heldItem = null;
    heldItems.get(index).Destroy();
    heldItems.remove(index);
    return ingredients.remove(index);
  }


  @Override
  public boolean CanRetrieve() {
    return ingredients.size() > 0;
  }


  @Override
  public boolean CanGive() {
    return ingredients.size() < 4;
  }


  @Override
  public boolean CanInteract() {
    return !(ingredients.size() < 2);
  }


  @Override
  public float Interact() {
    combine();
    return 0;
  }


  /**
   * Returns the list of ingredients in our arraylist form
   *
   * @return ArrayList ingredients
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
   * removes all ingredients from the arraylist which means a successfull dish or simply ingredients
   * taken away
   */
  public void clearIngredients() {
    ingredients = new ArrayList<Item>();
  }

  private void clearTempIngredients() {
    tempIngredients = new ArrayList<ItemEnum>();
  }


  /**
   * Assembles the dish into the final one when we have all the correct ingredients
   */
  public boolean combine() {
    for (Item ingredient : ingredients) {
      tempIngredients.add(ingredient.name);
    }
    Collections.sort(tempIngredients);
    for (int x = 0; x < tempIngredients.size() - 1; x++) {
      temp = combinations.CombinationMap.get(
          tempIngredients.get(x).name() + " " + tempIngredients.get(x + 1).name());
      if (temp == null) {
        clearTempIngredients();
        return false;
      }
      tempIngredients.set(x + 1, temp);
      Collections.sort(tempIngredients);
    }
    soundFrame.SoundEngine.playSound(soundsEnum.FoodReadyBell);
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
   */
  public void setDish(ItemEnum item) {
    this.dish = new Item(item);
  }

  @Override
  public void updatePictures() {

    if (ingredients.isEmpty()) {
      for (int x = 0; x < heldItems.size(); x++) {
        heldItems.get(x).Destroy();
      }
      heldItems = new ArrayList<>();

    }

    if (assembled) {
      heldItem = new GameObject(new BlackTexture(Item.GetItemPath(dish.name)));
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

    heldItem = new GameObject(new BlackTexture(Item.GetItemPath(ingredients.get(index - 1).name)));
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


  @Override
  public void moveAnim(){
    return;
  }


  @Override
  public void Update(float dt) {
  }
  @Override
  public void LoadState(List<ItemState> state,Boolean locked) {

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

  @Override
  public List<ItemState> SaveState() {
    LinkedList<ItemState> states = new LinkedList<>();

    for (Item item : ingredients
    ) {
      states.add(new ItemState(item));
    }

    return states;
  }
}
