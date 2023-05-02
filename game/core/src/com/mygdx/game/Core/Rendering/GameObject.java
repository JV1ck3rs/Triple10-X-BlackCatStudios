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
 * Abstracts rendering, position and script execution to one instantiatable object
 *   BlackCatStudio's Code
 * @author Felix Seanor
 * @author Jack Hinton
 * @author Jack Vickers
 * @author Sam Toner
 * @date 23/04/23
 */
public class GameObject {

  /** This is the image used to render the game object*/
  public Renderable image;

  /** position of the gameobject */
  public Vector2 position;
  public Boolean destroyed = false;

  /** Object with no image width's width e.g. stations */
  public float PhysicalWidth;

  /** Objects with no image height's height e.g. stations */
  public float PhysicalHeight;

  /** is the gameobject visible currently */

  public Boolean isVisible = true;

  /** Unique identifying ID unchangable */
  private Integer UID;

  /** list of scriptables to run on the update loop that are attached to this gameobject */
  List<Scriptable> Scripts = new LinkedList<>();

  /**
   * Create a new gameobject with a renderable
   * @param renderable
   * @author Felix Seanor
   * @author Jack Hinton
   */
  public GameObject(Renderable renderable) {

    image = renderable;
    if (GameObjectManager.objManager != null) {
      GameObjectManager.objManager.SubmitGameObject(this);
    }
    position = new Vector2();
    PhysicalWidth = -1;
    PhysicalHeight = -1;

  }

  /**
   * Attach a scriptable to be executed on this gameobject
   * @param script
   * @author Felix Seanor
   */
  public void attachScript(Scriptable script) {
    Scripts.add(script);
    script.setGameObject(this);
    script.Start();
  }

  /**
   * returns scriptable at i
   * @param i index
   * @return
   * @author Felix Seanor
   */
  public Scriptable GetScript(int i){
   return Scripts.get(i);
  }

  /**
   * returns UID
   * @return int so cannot accidentally modify the UID of gameObject
   * @author Felix Seanor
   */
  public int getUID() {
    return UID;
  }

  /**
   * Sets the Unique ID of this gameobject cannot be called more than once per gameobject
   * @param ID
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
   * @param dt
   */
  public void doUpdate(float dt) {
    for (Scriptable script : Scripts
    ) {
      script.Update(dt);
    }
  }

  /**
   * run fixed update on each scipt
   * @param dt
   * @author Felix Seanor
   */
  public void doFixedUpdate(float dt) {
    for (Scriptable script : Scripts
    ) {
      script.FixedUpdate(dt);
    }
  }

  /**
   * Runs just before rendering
   * @author Felix Seanor
   */
  public void doOnRenderUpdate() {
    for (Scriptable script : Scripts
    ) {
      script.OnRender();
    }
  }

  /**
   * Render the gameobject to screen
   * @param batch batch to render with
   * @author Felix Seanor
   * @date 18/04/23
   */
  public void render(SpriteBatch batch) {

    if(!isVisible || destroyed)
      return;
    if(image == null)
      return;



    doOnRenderUpdate();

    image.Render(batch, position.x, position.y);

  }

  /**
   * Returns the sprite if there is one
   * @return
   * @author Felix Seanor
   */
  public BlackSprite getSprite() {
    return ((BlackSprite) image);
  }

  /**
   * get the black texture if there is one
   * @return
   * @author Felix Seanor
   */
  public BlackTexture getBlackTexture() {
    return (BlackTexture) image;
  }

  /**
   * sets the current position
   * @param x
   * @param y
   * @author Felix Seanor
   */
  public void setPosition(float x, float y) {
    position.set(x, y);
  }

  /**
   * this checks if the GameObject has been clicked on the current frame
   * @return True if clicked, false otherwise
   *
   * @author Sam Toner
   */
  public boolean isClicked(){
    CameraFunctions camera1 = CameraFunctions.camera;
    Vector3 touchPos = new Vector3();
    if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
      touchPos.set(Gdx.input.getX(), Gdx.input.getY(),0);
      touchPos = camera1.unprojectCamera(touchPos);
      if (touchPos.x > position.x && touchPos.x < position.x + image.GetWidth()) {
        if (touchPos.y > position.y && touchPos.y < position.y + image.GetHeight()) {
          System.out.println("Clicked");
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Sets the Physical width and height of the gameobject, for object with no actual image size
   * @param width
   * @param height
   * @author Jack Hinton
   */
  public void setWidthAndHeight(float width, float height){
    PhysicalWidth = width;
    PhysicalHeight = height;
  }

  /**
   * Get the width, default to imageWidth if no physical width
   * @return
   * @author  Jack Hinton
   */
  public float getWidth(){
    if(PhysicalWidth == -1)
      return image.GetWidth();
    return PhysicalWidth;
  }

  /**
   * Get the height, default to imageHeight if no physical height
   * @return
   * @author Jack Hinton
   */
  public float getHeight(){
    if(PhysicalHeight == -1)
      return image.GetHeight();
    return PhysicalHeight;
  }

  /**
   * Destroy this GameObject, Doesnt call destroy on individual scripts
   * @author Felix Seanor
   */
  public void Destroy(){
    GameObjectManager.objManager.DestroyGameObject(this);
    image.Destroy();
  }
}
