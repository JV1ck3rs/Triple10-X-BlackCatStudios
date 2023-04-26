package com.mygdx.game.Items;

import com.mygdx.game.Core.BlackTexture;
import com.mygdx.game.Core.GameState.ItemState;

/**
 * Item class, instatiates item Enum into a physical item in the game.
 * stores cooking progress ect.
 *
 * Last modified 09/04/23
 */
public class Item {

  public ItemEnum name;
  public BlackTexture tex;
  public float progress;
  public int step;

  int width = 20;
  int height = 20;

  /**
   * Creates a new item.
   *
   * @param item The item to create.
   * @author Felix Seanor
   * @author Jack Hinton
   * @author Jack Vickers
   */
  public Item(ItemEnum item) {
    name = item;
    step = 0;
    tex = new BlackTexture(GetItemPath(item));

    tex.setSize(width, height);
  }

  public Item(ItemState itemState){

    name = itemState.item;
    step = itemState.step;
    progress =itemState.progress;
    tex = new BlackTexture(GetItemPath(name));
    tex.setSize(width, height);

  }


  public static String GetItemPath(ItemEnum name) {
    return "Items/" + name.name() + ".png";
  }

  /**
   * Overrides the equals function to check whether two items are the same.
   *
   * @param obj The object to compare to.
   * @return Whether the two items are the same.
   * @author Jack Vickers
   */
  @Override
  public boolean equals(Object obj) {
    return obj instanceof Item && ((Item) obj).name == name;
  }

  public static String GetRecipePath(ItemEnum name){
    return "Recipe/"+name.name()+"Recipe.png";

  }
}
