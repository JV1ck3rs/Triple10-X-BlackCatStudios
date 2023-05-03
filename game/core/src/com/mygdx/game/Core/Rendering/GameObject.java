package com.mygdx.game.Core.Rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.CameraFunctions;
import com.mygdx.game.Core.Scriptable;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstracts rendering, position and script execution to one instantiatable object BlackCatStudio's
 * Code
 *
 * @author Felix Seanor
 * @author Jack Hinton
 * @author Jack Vickers
 * @author Sam Toner
 * @date 23 /04/23
 */
public class GameObject {

  /**
   * This is the image used to render the game object
   */
  public Renderable image;

  /**
   * position of the gameobject
   */
  public Vector2 position;
  /**
   * The Destroyed.
   */
  public Boolean destroyed = false;

  /**
   * Object with no image width's width e.g. stations
   */
  public float physicalWidth;

  /**
   * Objects with no image height's height e.g. stations
   */
  public float physicalHeight;

  /**
   * is the gameobject visible currently
   */
  public Boolean isVisible = true;

  /**
   * Unique identifying ID unchangable
   */
  private Integer UID;

  /**
   * list of scriptables to run on the update loop that are attached to this gameobject
   */
  List<Scriptable> scripts = new LinkedList<>();

  /**
   * Create a new gameobject with a renderable
   *
   * @param renderable the renderable
   * @author Felix Seanor
   * @author Jack Hinton
   */
  public GameObject(Renderable renderable) {

    image = renderable;
    if (GameObjectManager.objManager != null) {
      GameObjectManager.objManager.submitGameObject(this);
    }
    position = new Vector2();
    physicalWidth = -1;
    physicalHeight = -1;

  }

  /**
   * Attach a scriptable to be executed on this gameobject
   *
   * @param script the script
   * @author Felix Seanor
   */
  public void attachScript(Scriptable script) {
    scripts.add(script);
    script.setGameObject(this);
    script.start();
  }

  /**
   * returns scriptable at i
   *
   * @param i index
   * @return script
   * @author Felix Seanor
   */
  public Scriptable getScript(int i) {
    return scripts.get(i);
  }

  /**
   * returns UID
   *
   * @return int so cannot accidentally modify the UID of gameObject
   * @author Felix Seanor
   */
  public int getUID() {
    return UID;
  }

  /**
   * Sets the Unique ID of this gameobject cannot be called more than once per gameobject
   *
   * @param ID the id
   * @author Felix Seanor
   */
  public void setUID(int ID) {
    if (UID != null) {
      throw new IllegalArgumentException("You cannot change the UID");
    }

    UID = ID;
  }

  /**
   * Runs update method on all scriptables
   *
   * @param dt the dt
   */
  public void doUpdate(float dt) {
    for (Scriptable script : scripts
    ) {
      script.update(dt);
    }
  }

  /**
   * run fixed update on each scipt
   *
   * @param dt the dt
   * @author Felix Seanor
   */
  public void doFixedUpdate(float dt) {
    for (Scriptable script : scripts
    ) {
      script.fixedUpdate(dt);
    }
  }

  /**
   * Runs just before rendering
   *
   * @author Felix Seanor
   */
  public void doOnRenderUpdate() {
    for (Scriptable script : scripts
    ) {
      script.onRender();
    }
  }

  /**
   * Render the gameobject to screen
   *
   * @param batch batch to render with
   * @author Felix Seanor
   * @date 18 /04/23
   */
  public void render(SpriteBatch batch) {

    if (!isVisible || destroyed) {
      return;
    }
    if (image == null) {
      return;
    }

    doOnRenderUpdate();

    image.render(batch, position.x, position.y);

  }

  /**
   * Returns the sprite if there is one
   *
   * @return sprite
   * @author Felix Seanor
   */
  public BlackSprite getSprite() {
    return ((BlackSprite) image);
  }

  /**
   * get the black texture if there is one
   *
   * @return black texture
   * @author Felix Seanor
   */
  public BlackTexture getBlackTexture() {
    return (BlackTexture) image;
  }

  /**
   * sets the current position
   *
   * @param x the x
   * @param y the y
   * @author Felix Seanor
   */
  public void setPosition(float x, float y) {
    position.set(x, y);
  }

  /**
   * this checks if the GameObject has been clicked on the current frame
   *
   * @return True if clicked, false otherwise
   * @author Sam Toner
   */
  public boolean isClicked() {
    CameraFunctions camera1 = CameraFunctions.camera;
    Vector3 touchPos = new Vector3();
    if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
      touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
      touchPos = camera1.unprojectCamera(touchPos);
      if (touchPos.x > position.x && touchPos.x < position.x + image.getWidth()) {
        if (touchPos.y > position.y && touchPos.y < position.y + image.getHeight()) {
//          System.out.println("Clicked");
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Sets the Physical width and height of the gameobject, for object with no actual image size
   *
   * @param width  the width
   * @param height the height
   * @author Jack Hinton
   */
  public void setWidthAndHeight(float width, float height) {
    physicalWidth = width;
    physicalHeight = height;
  }

  /**
   * Get the width, default to imageWidth if no physical width
   *
   * @return width
   * @author Jack Hinton
   */
  public float getWidth() {
    if (physicalWidth == -1) {
      return image.getWidth();
    }
    return physicalWidth;
  }

  /**
   * Get the height, default to imageHeight if no physical height
   *
   * @return height
   * @author Jack Hinton
   */
  public float getHeight() {
    if (physicalHeight == -1) {
      return image.getHeight();
    }
    return physicalHeight;
  }

  /**
   * Destroy this GameObject, Doesnt call destroy on individual scripts
   *
   * @author Felix Seanor
   */
  public void destroy() {
    GameObjectManager.objManager.destroyGameObject(this);
    image.destroy();
  }
}
