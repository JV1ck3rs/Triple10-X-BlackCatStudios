package com.mygdx.game.Core.Customers;


import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Core.Rendering.BlackTexture;
import com.mygdx.game.Core.Rendering.GameObject;
import com.mygdx.game.Core.PathFinder.PathfindingAgent;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;
import com.mygdx.game.Person;
import java.util.ArrayList;

/**
 * Assigns all attributes and animation and interactions that the customer will go through in the
 * game including which dish they will pick. Mixture of BlackCatStudios and Team Triple 10s
 *
 * @author Felix Seanor
 * @author Jack Vickers
 * @author Amy Cross
 * @author Robin Graham
 * @date 01/05/23
 */
public class Customer extends PathfindingAgent implements Person {


  private int currentSpriteAnimation;
  private final int MAX_ANIMATION = 4;
  private TextureAtlas customerAtlas;
  private float stateTime = 0;

  private String spriteOrientation, spriteState;
  public final int customerNumber;

  private GameObject HeldItem;

  public boolean waitingAtCounter;   // customer will be waiting at the counter for their dish
  public boolean eaten;

  public ItemEnum dish;

  public GameObject foodIcon;
  public boolean foodRecipeOpen = false;
  public GameObject foodRecipe;
  public GameObject recipeCloseButton;

  /**
   * Initialises the customer and certain variables that we are going to use to interact with the
   * game. We also set the spawn randomiser and the interval between when each customer arrives
   * Mixture of BlackCatStudios and Team Triple 10s
   *
   * @param customerNumber the ID of each individual customer which will be interacted with
   */
  public Customer(int customerNumber, ItemEnum Order, TextureAtlas texture) {

    //Triple 10s
    dish = Order;
    currentSpriteAnimation = 1;
    spriteOrientation = "north";

    // sprite.setPosition(posX, posY);
    //Black Cat studios
    this.waitingAtCounter = false;
    this.customerNumber = customerNumber;
    this.eaten = false;
    this.customerAtlas = texture;

//    System.out.println("customer " + customerNumber + ": " + dish);

    BlackTexture iconTex = new BlackTexture(Item.GetItemPath(this.dish));
    iconTex.setSize(20, 20);
    foodIcon = new GameObject(iconTex);

    BlackTexture tex = new BlackTexture(Item.GetItemPath(dish));
    tex.setSize(20, 20);
    HeldItem = new GameObject(tex);
    HeldItem.isVisible = false;

    BlackTexture closeButtonTex = new BlackTexture("Items/CloseButton.png");
    closeButtonTex.setSize(20, 20);
    closeButtonTex.layer = 10;
    recipeCloseButton = new GameObject(closeButtonTex);
    recipeCloseButton.setPosition(470, 470);
    recipeCloseButton.isVisible = false;

    foodIcon.isVisible = false;
    try {
      foodRecipe = new GameObject(new BlackTexture(Item.GetRecipePath(this.dish)));
    } catch (Exception e) {
      foodRecipe = new GameObject(new BlackTexture(Item.GetItemPath(this.dish)));
    }

    foodRecipe.isVisible = false;
  }

  /**
   * Returns animation frame Team Triple 10s code
   *
   * @return
   * @author Team Triple 10
   */
  public String getCurrentOrientation() {
    return spriteOrientation;
  }

  /**
   * Black Cat studios code
   */
  @Override
  public void Start() {
    gameObject.getSprite().setSprite(customerAtlas.createSprite("north1"));
    gameObject.getSprite().layer = 2;
    gameObject.image.setSize(25, 45);

  }

  /**
   * Updates the sprite to follow the correct animation. Mainly Team Triple 10s code
   *
   * @author Team Triple 10
   */
  @Override
  public void updateSpriteFromInput(String newOrientation) {
    if (newOrientation.contains("idle")) {
      spriteState = newOrientation;
    } else {
      if (spriteOrientation != newOrientation) {
        currentSpriteAnimation = 1;
        stateTime = 0;
      } else {
        if (stateTime > 0.06666) {
          currentSpriteAnimation++;
          if (currentSpriteAnimation > MAX_ANIMATION) {
            currentSpriteAnimation = 1;
          }
          stateTime = 0;
        } else {
          stateTime += 0.01;
        }
      }
      spriteState = newOrientation + currentSpriteAnimation;
    }
    setTexture(spriteState);
    spriteOrientation = newOrientation;
  }


  /**
   * Sets the customer texture for each customer. BlackCatStudio's Code
   *
   * @author Felix Seanor
   */
  @Override
  public void setTexture(String texture) {

    if (texture.contains("idle")) {
      texture = texture.replace("idle", "");
      texture += "1";
    }
    gameObject.getSprite().sprite.setRegion(customerAtlas.findRegion(texture));
  }

  /**
   * Gets the move of the customer and direction and sets the animations accordingly.
   * BlackCatStudios Code
   *
   * @return currentDirection direction of the customer
   * @author Felix Seanor
   * @author Amy Cross
   */
  @Override
  public String getMove() {
    Vector2 dir = GetMoveDir().nor();
    String newOrientation;
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
    return newOrientation;
  }

  /**
   * Black Cat studios Code
   *
   * @return
   */
  public GameObject returnHeldItem() {
    return HeldItem;
  }

  /**
   * Returns the x of the customer. Black Cat studios Code
   *
   * @return int posX the x position of the customer
   * @author Felix Seanor
   */
  public float getX() {
    return gameObject.position.x;
  }

  /**
   * Returns the y of the customer. Black Cat studios Code
   *
   * @return int posY the y position of the customer
   * @author Felix Seanor
   * @author Amy Cross
   */
  public float getY() {
    return gameObject.position.y;
  }

  /**
   * Assigns the sprite for the customer at random. Team Triple 10s code
   *
   * @param customerAtlasArray array of customer sprites
   * @return atlas of the customer object
   * @author Amy Cross
   */
  public static TextureAtlas getCustomerAtlas(ArrayList<TextureAtlas> customerAtlasArray) {
    /*
          Currently can remove texture from array after each .get() to avoid repeats.
          If customers > number of sprites, then there will not be enough sprites available.
    */
    int randomIndex = (int) (Math.random() * customerAtlasArray.size());
    TextureAtlas atlas = customerAtlasArray.get(randomIndex);
    return atlas;
  }

//  /**
//   * Checks if the customer is waiting or not.
//   * Black Cat studios Code
//   * @return Boolean value for result
//   * @author Felix Seanor
//   */
//  public boolean isWaiting() {
//    return waitingAtCounter;
//  }

  /**
   * Gets the dish that the customer has. Team Triple 10s code
   *
   * @return Dish dish which is the object the customer has
   * @author Amy Cross
   */
  public ItemEnum getDish() {
    return dish;
  }

  /**
   * Checks if the customer has successfully been fed. Team Triple 10s code
   *
   * @return Boolean value of if the customer has been fed
   * @author Amy Cross
   */
  public boolean getFed() {
    return eaten;
  }


  /**
   * Display the item in the world and update its coordinates. BlackCatStudios Code
   *
   * @author Felix Seanor
   */
  public void displayItem() {

    HeldItem.isVisible = true;

    HeldItem.position.x = gameObject.position.x;
    HeldItem.position.y = gameObject.position.y;
    HeldItem.image.layer = gameObject.getSprite().layer + 1;

    if (spriteOrientation.contains("north")) {
      HeldItem.position.y += HeldItem.image.GetHeight();
      HeldItem.position.x += 2;
      HeldItem.image.layer -= 2;
    } else if (spriteOrientation.contains("south")) {
      HeldItem.position.y -= HeldItem.image.GetHeight();
    } else if (spriteOrientation.contains("east")) {
      HeldItem.position.x += HeldItem.image.GetWidth();
    } else if (spriteOrientation.contains("west")) {
      HeldItem.position.x -= HeldItem.image.GetWidth();
    }


  }

  /**
   * hide the held item. BlackCatStudios Code
   *
   * @author Felix Seanor
   */
  public void hideItem() {
    if (HeldItem != null) {
      HeldItem.isVisible = false;
    }
  }

  /**
   * BlackCatStudios Code
   *
   * @param dt
   * @author Felix Seanor
   */
  @Override
  public void Update(float dt) {
    super.Update(dt);

    if (!eaten && !waitingAtCounter) {
      displayItem();
    } else {
      hideItem();
    }

  }

  /**
   * Destroy the customer. BlackCatStudios Code
   *
   * @author Felix Seanor
   */
  public void Destroy() {
    HeldItem.Destroy();
    foodIcon.Destroy();
    gameObject.Destroy();

  }
}