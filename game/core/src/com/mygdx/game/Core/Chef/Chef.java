package com.mygdx.game.Core.Chef;

import static java.lang.Math.min;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Core.Inputs;
import com.mygdx.game.Core.PathFinder.PathfindingAgent;
import com.mygdx.game.Core.Rendering.BlackTexture;
import com.mygdx.game.Core.Rendering.GameObject;
import com.mygdx.game.Core.SFX.SoundFrame;
import com.mygdx.game.Core.SFX.SoundFrame.soundsEnum;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;
import com.mygdx.game.Person;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;


/**
 * Creates the chef object which will interact with every object on the map and assemble dishes to
 * be fed to the customer The class also handles all sprite animations and movement.
 * <p>
 * BlackCatStudio's Code with a few methods from Team Triple 10
 *
 * @author Robin Graham
 * @author Amy Cross
 * @author Labib Zabeneh
 * @author Riko Puusepp
 * @author Felix Seanor
 * @date 01 /05/23
 */
public class Chef extends PathfindingAgent implements Person {


  /**
   * The Held items.
   */
  Stack<Item> heldItems = new Stack<>();
  /**
   * The Held item game objects.
   */
  List<GameObject> HeldItemGameObjects = new LinkedList<>();
  /**
   * The constant CarryCapacity.
   */
  public static int CarryCapacity = 3;

  private float oldSpeed;
  private String spriteOrientation, spriteState;
  private int currentSpriteAnimation;
  private final int MAX_ANIMATION = 4;
  private float stateTime = 0;
  private TextureAtlas chefAtlas;
  /**
   * The Is frozen.
   */
  public boolean isFrozen;
  private String lastOrientation;
  private float lockTime;
  private float lockProgress = 0;
  private boolean ModifiedStack = false;
  /**
   * The Path.
   */
  List<Vector2> path;

  private final int id;

  /**
   * The Timer atlas.
   */
  TextureAtlas timerAtlas;
  /**
   * The Timer sprite.
   */
  Sprite timerSprite;


  /**
   * Initialise the chef object and sets its spawn position. Black cat studios code
   *
   * @param id        the individual id of each chef i.e 0,1,2....
   * @param chefAtlas the chef atlas
   * @author Felix Seanor
   */
  public Chef(int id, TextureAtlas chefAtlas) {
    super();
    this.id = id;
    this.chefAtlas = chefAtlas; // chef now takes a texture atlas so
    // that the chefs can be created in the test files. Originally,
    // chefs were given a texture atlas from the getChefAtlasArray function in the GameScreen class.
    // Gamescreen could not be directly used in the test files as it caused an error.
    this.path = new LinkedList<>();
  }

  /**
   * Initialised the chefs data Mixture of Triple 10s and BlackCatStudios
   *
   * @author Felix Seanor
   */
  @Override
  public void start() {
    //Reorganised to fit work flow and requires access to data not yet created
    //Triple 10s
    gameObject.getSprite().setSprite(chefAtlas.createSprite("south1"));
    currentSpriteAnimation = 1;
    spriteOrientation = "south";

    isFrozen = false;
    //sprite.setPosition(posX, posY); unnessary now
    //MyGdxGame.buildObject(world, posX, posY, sprite.getWidth(), sprite.getHeight(), "Dynamic");
    this.lastOrientation = "south";

    timerAtlas = new TextureAtlas(Gdx.files.internal("Timer/timer.txt"));
    timerSprite = timerAtlas.createSprite("01");
//Black Cat studios
    for (int i = 0; i < CarryCapacity; i++) {
      HeldItemGameObjects.add(new GameObject(new BlackTexture(Item.getItemPath(ItemEnum.Buns))));
      HeldItemGameObjects.get(i).isVisible = false;

    }

  }

  /**
   * Defines all box2d associated variables for the chef and sets its hitbox to be used for
   * collisions.
   * This is Team Triple 10s code
   */


  /**
   * Makes items in the stack visble and hides stack items that do not have an item
   *
   * @author Felix Seanor
   */
  void changeItemVisibilities() {

    int i = -1;
    for (Item item : heldItems
    ) {
      i++;

      GameObject obj = HeldItemGameObjects.get(i);
      if (!obj.isVisible || ModifiedStack) {
        obj.image = item.tex;
      }

      obj.isVisible = true;
    }

    for (int j = i + 1; j < CarryCapacity; j++) {
      GameObject obj = HeldItemGameObjects.get(j);
      obj.isVisible = false;
    }

    for (int j = 0; j < CarryCapacity; j++) {
      GameObject obj = HeldItemGameObjects.get(j);
      obj.position.x = gameObject.position.x;
      obj.position.y = gameObject.position.y + j * 5;
      obj.image.layer = 1 + j;

      if (spriteOrientation.contains("north")) {
        obj.position.y += obj.image.getHeight() / 2;
        obj.image.layer -= CarryCapacity;
      } else if (spriteOrientation.contains("south")) {
        obj.position.y -= obj.image.getHeight() / 2;
      } else if (spriteOrientation.contains("east")) {
        obj.position.x += obj.image.getWidth() / 2;
      } else if (spriteOrientation.contains("west")) {
        obj.position.x -= obj.image.getWidth() / 2 + 5;
      }


    }
    ModifiedStack = false;
  }

  /**
   * Gets top item.
   *
   * @return the top item
   */
  public Item getTopItem() {
    if (heldItems.size() == 0) {
      return null;
    }

    return heldItems.peek();
  }

  @Override
  public void onRender() {

    changeItemVisibilities();
  }


  /**
   * Updates the chef position and shows the animation depending on its direction and speed. This is
   * a mixture of BlackCatStudios and Team Triple10
   */
  @Override
  public void updateSpriteFromInput(String newOrientation) {

    Vector2 dir = getMoveDir().nor();

    //BlackCatStudios
    if (dir.dot(dir) <= 0) {
      newOrientation = "idle" + spriteOrientation.replace("idle", "");
    } else {
      if (Math.abs(dir.dot(new Vector2(1, 0))) < Math.abs(dir.dot(new Vector2(0, 1)))) {
        //North prefered

        if (dir.dot(new Vector2(0, 1)) > 0) {
          newOrientation = "north";
        } else {
          newOrientation = "south";
        }


      } else {
        if (dir.dot(new Vector2(1, 0)) > 0) {
          newOrientation = "east";
        } else {
          newOrientation = "west";
        }
      }
    }

    //Team Triple 10
    if (newOrientation.contains("idle")) {
      spriteState = newOrientation;
    } else {
      if (spriteOrientation != newOrientation) {
        currentSpriteAnimation = 1;
        stateTime = 0;
      } else {
        if (stateTime > 1 / 15.0) { // sprite is updated every 15th of a second
          currentSpriteAnimation++;
          if (currentSpriteAnimation > MAX_ANIMATION) { // a chef has 4 different animations
            currentSpriteAnimation = 1;
          }
          stateTime = 0;
        } else {
          stateTime += 0.01;
        }
      }
      spriteState = newOrientation + currentSpriteAnimation;
    }

    //Black Cat studios
    setTexture(spriteState);
    //Team Triple 10s
    spriteOrientation = newOrientation;
  }

  /**
   * Gets sprite orientation.
   *
   * @return the sprite orientation
   */
  public String getSpriteOrientation() {
    return spriteOrientation;
  }

  /**
   * Sets the texture of the chef. Black Studios and Triple 10s
   *
   * @author Amy Cross
   * @author Felix Seanor
   */
  @Override
  public void setTexture(String texture) {
    //System.out.println(texture);
    gameObject.getSprite().sprite.setRegion(chefAtlas.findRegion(texture));
  }

  /**
   * Returns the x position of the chef. Black Studios and Triple 10s
   *
   * @return int posX
   * @author Felix Seanor
   * @author Amy Cross
   */
  public float getX() {
    return gameObject.position.x;
  }

  /**
   * Returns the y position of the chef. Black Studios and Triple 10s
   *
   * @return int posY
   * @author Felix Seanor
   * @author Amy Cross
   */
  public float getY() {
    return gameObject.position.y;
  }

  /**
   * Returns the width of the chef. Black Studios and Triple 10s
   *
   * @return int width
   * @author Felix Seanor
   * @author Amy Cross
   */
  public float getWidth() {
    return gameObject.getSprite().sprite.getWidth();
  }

  /**
   * Returns the height of the chef. Black Studios and Triple 10s
   *
   * @return int height
   * @author Felix Seanor
   * @author Amy Cross
   */
  public float getHeight() {
    return gameObject.getSprite().sprite.getHeight();
  }

  /**
   * Gets the input from the user and orientates the chef accordingly. Black Studios and Triple 10s
   *
   * @author Felix Seanor
   * @author Amy Cross
   */
  @Override
  public String getMove() {
    String newOrientation = this.lastOrientation;
    if (isFrozen) {
      //System.out.println("Frozen");
      return "idle" + this.lastOrientation;
    } else {
      if (Gdx.input.isKeyPressed(Inputs.MOVE_CHEF_LEFT)) {
        newOrientation = "west";
      } else if (Gdx.input.isKeyPressed(Inputs.MOVE_CHEF_RIGHT)) {
        newOrientation = "east";
      } else if (Gdx.input.isKeyPressed(Inputs.MOVE_CHEF_UP)) {
        newOrientation = "north";
      } else if (Gdx.input.isKeyPressed(Inputs.MOVE_CHEF_DOWN)) {
        newOrientation = "south";
      } else {
        return "idle" + lastOrientation;
      }
      this.lastOrientation = newOrientation;
      return newOrientation;
    }
  }

  /**
   * Returns a boolean value if the user is pressing the ctrl key.
   * Triple 10s code
   * @return boolean
   * @author Amy Cross
   */
//  public boolean isCtrl() {
//    return Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT);
//  }

  /**
   * Freezes the chef for a set period of time. Triple 10 & BlackCatStudios code
   *
   * @param lockTime time the chef will be frozen
   * @author Amy Cross
   * @author Jack Hinton
   */
  public void freeze(float lockTime) {
    isFrozen = true;
    this.lockTime = lockTime;
  }

  /**
   * Unfreezes the chef. Triple 10 & BlackCatStudios code
   *
   * @author Amy Cross
   * @author Jack Hinton
   */
  public void unfreeze() {
    isFrozen = false;
  }

  /**
   * Freeze timer boolean.
   *
   * @param dt the dt
   * @return the boolean
   */
  public boolean freezeTimer(float dt) {
    lockProgress = min(lockProgress + dt, lockTime);
    if (lockProgress == lockTime) {
      lockProgress = 0;
      return true;
    }
    return false;
  }

  /**
   * Gets the inventory of the chef, so the item they are currently holding.
   *
   * @return Ingredient ingredient
   * @author Jack Vickers
   */
  public Stack<Item> getInventory() {
    return heldItems;
  }

  /**
   * Gets the inventory count of the chef, so the number of items they are currently holding.
   * BlackCatStudios Code
   *
   * @return The number of items the chef is holding.
   * @author Jack Vickers
   */
  public int getInventoryCount() {
    return heldItems.size();
  }

  /**
   * Gets the inventory of the chef, so the item they are currently holding. BlackCatStudios Code
   *
   * @return Ingredient ingredient
   * @author Jack Vickers
   */
  public int getCarryCapacity() {
    return CarryCapacity;
  }

  /**
   * Can fetch (take item from chef) BlackCatStudios Code
   *
   * @return heldItems > 0
   */
  public boolean CanFetchItem() {
    if (heldItems.size() == 0) {
      return false;
    }

    return true;

  }

  /**
   * Can an item be given to the chef BlackCatStudios Code
   *
   * @return boolean
   * @author Felix Seanor
   */
  public boolean CanGiveItem() {
    return heldItems.size() < CarryCapacity;

  }

  /**
   * Take item from chef
   *
   * @return optional
   * @author Felix Seanor
   */
  public Optional<Item> FetchItem() {

    if (!CanFetchItem()) {
      return Optional.empty();
    }

    SoundFrame.SoundEngine.playSound(soundsEnum.DropItem);
    ModifiedStack = true;

    return Optional.ofNullable(heldItems.peek());
  }

  /**
   * Pop item.
   */
  public void popItem() {
    heldItems.pop();
  }

  /**
   * Give item to chef BlackCatStudios Code
   *
   * @param item the item
   * @return boolean
   * @author Felix Seanor
   */
  public Boolean GiveItem(Item item) {
    if (CanGiveItem()) {
      heldItems.add(item);
      SoundFrame.SoundEngine.playSound(soundsEnum.EquipItem);
      ModifiedStack = true;

      return true;
    }

    return false;
  }

  /**
   * Give chef item boolean.
   *
   * @param item the item
   * @return the boolean
   */
  public Boolean GiveChefItem(Item item) {
    if (CanGiveItem()) {
      heldItems.add(item);
      return true;
    }

    return false;
  }

  /**
   * Drops the item from the top of the chef's stack. BlackCatStudios Code
   *
   * @author Felix Seanor
   */
  public void DropItem() {
    if (heldItems.size() != 0) {
      heldItems.pop();
    }
    SoundFrame.SoundEngine.playSound(soundsEnum.DropItem);
    ModifiedStack = true;

  }

  /**
   * Brings the item at the bottom of the stack to the top BlackCatStudios Code
   *
   * @author Felix Seanor
   */
  public void CycleStack() {
    if (heldItems.size() == 0) {
      return;
    }

    Item bottomItem = heldItems.elementAt(0);
    heldItems.removeElementAt(0);

    heldItems.push(bottomItem);

    SoundFrame.SoundEngine.playSound(soundsEnum.EquipItem);
    ModifiedStack = true;
  }

  /**
   * Clears the inventory of the chef. BlackCatStudios Code
   *
   * @author Hubert Solecki
   * @date 21 /04/2023
   */
  public void ClearInventory() {
    heldItems.clear();
  }

  /**
   * BlackCatStudios Code
   */
  public void changeSpeed() {
    oldSpeed = speed;
    speed = 2600;
  }

  /**
   * BlackCatStudios Code
   */
  public void decreaseSpeed() {
    speed = oldSpeed;

  }

}
